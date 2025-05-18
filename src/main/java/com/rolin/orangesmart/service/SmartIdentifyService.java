package com.rolin.orangesmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rolin.orangesmart.model.crawler.bo.AiBO;
import com.rolin.orangesmart.model.crawler.entity.Weather;
import com.rolin.orangesmart.model.smart.KimichatRequest;
import com.rolin.orangesmart.model.smart.Result;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 02:54
 */
@Service
public class SmartIdentifyService {

  @Resource
  private WeatherService weatherService;

  private static final String BASE_URL = "https://interview-test.nullht.com";

  private final RestTemplate restTemplate = new RestTemplate();

  private final ObjectMapper objectMapper = new ObjectMapper();

  public void start() {
    // 初始请求
    String code = "d3e8f4g6h1";
    String method = "DELETE";
    String path = "/api/settings";

    while (true) {
      try {
        // 拼接 URL
        String url = BASE_URL + path + "?code=" + code;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送请求并获取响应
        ResponseEntity<String> response = sendRequest(method, url, entity);

        if (response.getStatusCode().is2xxSuccessful()) {
          String body = response.getBody();
          if (body == null || body.isEmpty()) {
            System.out.println("响应为空，终止循环");
            break;
          }

          JsonNode json = objectMapper.readTree(body);
          if (json.has("code") && json.has("method") && json.has("path")) {
            // 提取下一次请求的信息
            code = json.get("code").asText();
            method = json.get("method").asText();
            path = json.get("path").asText();

            System.out.printf("下一个请求：method=%s, path=%s, code=%s%n", method, path, code);
          } else {
            System.out.println("响应格式不符合预期，终止循环");
            System.out.println("最后一次相应的值为：");
            System.out.println(json.toString());
            break;
          }
        } else {
          System.out.println("请求失败：" + response.getStatusCode());
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
  }

  private ResponseEntity<String> sendRequest(String method, String url, HttpEntity<String> entity) {
    return switch (method.toUpperCase()) {
      case "GET" -> restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
      case "POST" -> restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
      case "PUT" -> restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
      case "DELETE" -> restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
      default -> throw new IllegalArgumentException("Unsupported method: " + method);
    };
  }

  public String identifyImages(MultipartFile[] files) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    for (MultipartFile file : files) {
      try {
        body.add("images", new ByteArrayResource(file.getBytes()) {
          @Override
          public String getFilename() {
            return file.getOriginalFilename(); // 保持原始文件名
          }
        });
      } catch (IOException e) {
        throw new RuntimeException("Failed to read file content", e);
      }
    }

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    String serverUrl = "http://127.0.0.1:8000/api/detect";
    String response = restTemplate.postForObject(serverUrl, requestEntity, String.class);

    try {
      // 解析 JSON 数据
      JsonNode jsonNode = objectMapper.readTree(response);
      JsonNode resultsNode = jsonNode.get("results");

      // 将 results 解析为 List<Result>
      List<Result> results = objectMapper.readValue(
          resultsNode.toString(),
          new TypeReference<List<Result>>() {}
      );

      for (Result result : results) {
        System.out.println(result);
      }

      // 向 http://127.0.0.1:5000/kimichat 发送 POST 请求
      String kimichatUrl = "http://127.0.0.1:5000/kimichat";
      HttpHeaders kimichatHeaders = new HttpHeaders();
      kimichatHeaders.setContentType(MediaType.APPLICATION_JSON);

      String message =
          """
          请看下面的内容
          """
              + response.toString() +
          """
          这个是柑橘图像识别的结果,其中chineseClass指的是患病名称,如果值为健康,说明该植株健康,如果为黄龙病,说明该植株可能感染黄龙病,如果是其他疾病则说明该植株可能感染其他疾病
          ,confidence为可信度,每一个Result代表室一个图片的识别结果,一个图片可能有多个地方被识别,所以有多个Detection
          我要求你根据识别结果,给出柑橘种植的建议,注意你回答时不要提及具体的图片名称,说是第几章图片就好,给我提供合理的关于这个疾病识别结果的处理建议
          最后还要评价一下我的种植结果,你觉得我种植得好不好,要不要继续保持还是改变策略
          """
          ;

      // 构建 JSON 请求体
      String requestBody = objectMapper.writeValueAsString(new KimichatRequest(message));

      HttpEntity<String> kimichatRequestEntity = new HttpEntity<>(requestBody, kimichatHeaders);
      String kimichatResponse = restTemplate.postForObject(kimichatUrl, kimichatRequestEntity, String.class);
      return kimichatResponse; // 返回 kimichat 的响应

    } catch (IOException e) {
      throw new RuntimeException("Failed to parse response JSON", e);
    }

  }

  public String identifyVideoProcess(MultipartFile file) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    try {
      body.add("video", new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename(); // 保持原始文件名
        }
      });
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file content", e);
    }

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    String serverUrl = "http://127.0.0.1:8000/api/process/video";
    String response = restTemplate.postForObject(serverUrl, requestEntity, String.class);
    try {
      JsonNode jsonNode = objectMapper.readTree(response);
      JsonNode taskId = jsonNode.get("task_id");
      return taskId.asText();
    }catch (Exception e){
      throw new RuntimeException("进程出错");
    }
  }

  public String identifyVideo(String taskId) {
    // 构建请求 URL
    String serverUrl = "http://127.0.0.1:8000/api/tasks/" + taskId + "/results";

    // 发送 GET 请求并获取响应
    String response = restTemplate.getForObject(serverUrl, String.class);

    // 向 http://127.0.0.1:5000/kimichat 发送 POST 请求
    String kimichatUrl = "http://127.0.0.1:5000/kimichat";
    HttpHeaders kimichatHeaders = new HttpHeaders();
    kimichatHeaders.setContentType(MediaType.APPLICATION_JSON);

    String message =
        """
            下面是柑橘视频识别的结果,其中chineseClass指的是患病名称,如果值为健康,说明该植株健康,如果为黄龙病,说明该植株可能感染黄龙病,如果是其他疾病则说明该植株可能感染其他疾病
            ,confidence为可信度,每一个Result代表室一个图片的识别结果,一个图片可能有多个地方被识别,所以有多个Detection
            我要求你根据识别结果,给出柑橘种植的建议,注意你回答时不要提及具体的视频名称,就说个整体即可,给我提供合理的关于这个疾病识别结果的处理建议
            最后还要评价一下我的种植结果,你觉得我种植得好不好,要不要继续保持还是改编策略
        """
            + response.toString() +
            """

            """
        ;

    // 构建 JSON 请求体
    String requestBody = null;
    try {
      requestBody = objectMapper.writeValueAsString(new KimichatRequest(message));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    HttpEntity<String> kimichatRequestEntity = new HttpEntity<>(requestBody, kimichatHeaders);
    String kimichatResponse = restTemplate.postForObject(kimichatUrl, kimichatRequestEntity, String.class);
    return kimichatResponse; // 返回 kimichat 的响应

  }

  public String identifyWeather(Weather weather) {
    // 如果传入Null,则默认取最新的天气
    if(ObjectUtils.isEmpty(weather)){
      List<Weather> weathers = weatherService.list(weatherService.getLambdaQueryWrapper());
      weathers.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
      weather = weathers.get(0);
    }

    // 向 http://127.0.0.1:5000/kimichat 发送 POST 请求
    String kimichatUrl = "http://127.0.0.1:5000/kimichat";
    HttpHeaders kimichatHeaders = new HttpHeaders();
    kimichatHeaders.setContentType(MediaType.APPLICATION_JSON);

    String message =
        """
            下面是今日天气结果,请你根据今日的天气结果,告知我今日种植柑橘的建议,比如要做什么病虫害的预防等内容
        """
            + weather.toString()
        ;

    // 构建 JSON 请求体
    String requestBody = null;
    try {
      requestBody = objectMapper.writeValueAsString(new KimichatRequest(message));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    HttpEntity<String> kimichatRequestEntity = new HttpEntity<>(requestBody, kimichatHeaders);
    String kimichatResponse = restTemplate.postForObject(kimichatUrl, kimichatRequestEntity, String.class);
    return kimichatResponse; // 返回 kimichat 的响应
  }

  public String identifyVideo(MultipartFile file) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    try {
      body.add("video", new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename(); // 保持原始文件名
        }
      });
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file content", e);
    }

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    String serverUrl = "http://localhost:8000/api/process/video/sync";
    String response = restTemplate.postForObject(serverUrl, requestEntity, String.class);

    // 向 http://127.0.0.1:5000/kimichat 发送 POST 请求
    String kimichatUrl = "http://127.0.0.1:5000/kimichat";
    HttpHeaders kimichatHeaders = new HttpHeaders();
    kimichatHeaders.setContentType(MediaType.APPLICATION_JSON);

    String message =
        """
            下面是柑橘视频识别的结果,其中chineseClass指的是患病名称,如果值为健康,说明该植株健康,如果为黄龙病,说明该植株可能感染黄龙病,如果是其他疾病则说明该植株可能感染其他疾病
            ,confidence为可信度,每一个Result代表室一个图片的识别结果,一个图片可能有多个地方被识别,所以有多个Detection
            我要求你根据识别结果,给出柑橘种植的建议,注意你回答时不要提及具体的视频名称,就说个整体即可,给我提供合理的关于这个疾病识别结果的处理建议
            最后还要评价一下我的种植结果,你觉得我种植得好不好,要不要继续保持还是改编策略
        """
            + response.toString() +
            """

            """
        ;

    // 构建 JSON 请求体
    String requestBody = null;
    try {
      requestBody = objectMapper.writeValueAsString(new KimichatRequest(message));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    HttpEntity<String> kimichatRequestEntity = new HttpEntity<>(requestBody, kimichatHeaders);
    String kimichatResponse = restTemplate.postForObject(kimichatUrl, kimichatRequestEntity, String.class);
    return kimichatResponse; // 返回 kimichat 的响应
  }

  public String identifyAi(AiBO aiBO) {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("请你根据下面的给出的信息,给我提供相应的柑橘生长的注意事项");
    stringBuilder.append("我的柑橘的品种是"+aiBO.getVariety());
    stringBuilder.append("其生长的阶段是"+aiBO.getGrowth());
    stringBuilder.append("且其具有以下情况"+aiBO.getText());

    stringBuilder.append("注意: ");
    if(aiBO.isImage() && aiBO.isVideo()){
      stringBuilder.append("你应该在回复的答案前加入根据您上传的图片和视频给您提供以下建议:");
    }else if(aiBO.isImage()){
      stringBuilder.append("你应该在回复的答案前加入根据您上传的图片给您提供以下建议:");
    }else if(aiBO.isVideo()){
      stringBuilder.append("你应该在回复的答案前加入根据您上传的视频给您提供以下建议:");
    }

    try {
      // 向 http://127.0.0.1:5000/kimichat 发送 POST 请求
      String kimichatUrl = "http://127.0.0.1:5000/kimichat";
      HttpHeaders kimichatHeaders = new HttpHeaders();
      kimichatHeaders.setContentType(MediaType.APPLICATION_JSON);

      String message = stringBuilder.toString();

      // 构建 JSON 请求体
      String requestBody = objectMapper.writeValueAsString(new KimichatRequest(message));

      HttpEntity<String> kimichatRequestEntity = new HttpEntity<>(requestBody, kimichatHeaders);
      String kimichatResponse = restTemplate.postForObject(kimichatUrl, kimichatRequestEntity, String.class);
      return kimichatResponse; // 返回 kimichat 的响应
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
}
