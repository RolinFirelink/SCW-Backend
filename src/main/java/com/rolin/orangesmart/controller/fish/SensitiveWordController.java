package com.rolin.orangesmart.controller.fish;

import cn.hutool.core.collection.CollUtil;
import com.rolin.orangesmart.enums.BusinessFailCode;
import com.rolin.orangesmart.exception.GlobalException;
import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.fish.entity.SensitiveWord;
import com.rolin.orangesmart.service.fish.SensitiveWordService;
import com.rolin.orangesmart.util.DFAFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "敏感词管理")
@RestController
@CrossOrigin
@RequestMapping("/sensitive")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService wordService;
    @Autowired
    private DFAFilter dfaFilter;

    @Operation(description = "导入敏感词")
    @PostMapping("/import")
    public Result importSensitiveWord(
            @RequestPart
            MultipartFile file
    ) throws IOException {
        // 判断文件后缀为txt
        if (!file.getOriginalFilename().endsWith(".txt")) {
            return new Result<List<String>>().success(false).message("文件格式错误，请上传txt文件");
        }
        List<String> wordList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            wordList.add(line);
        }
        if (CollUtil.isEmpty(wordList)) {
            return new Result<List<String>>().success(false).message("文件解析为空");
        }
        List<SensitiveWord> list = wordList.stream().map(item -> {
            SensitiveWord word = new SensitiveWord();
            word.setWord(item);
            return word;
        }).collect(Collectors.toList());
        wordService.insertBatch(list);

        return new Result<List<String>>().success().message("导入完成");
    }

    @Operation(description = "下载敏感词导入模板")
    @GetMapping(value = "/downloadTemplate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate(HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("template/SpamWords_V1.0.0.txt");
        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=SpamWords.txt");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("文件下载失败"));
        }
    }

    @Operation(description = "测试敏感词")
    @GetMapping("test")
    public Result test(
            @RequestParam("value")
            String value
    ){
        String filter = dfaFilter.filter(value);
        return new Result<>().success().message("测试成功").data(filter);
    }
}
