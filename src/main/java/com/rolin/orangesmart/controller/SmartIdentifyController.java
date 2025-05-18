package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.crawler.bo.AiBO;
import com.rolin.orangesmart.service.SmartIdentifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 02:53
 */
@Tag(name = "智能识别接口")
@RestController
@RequestMapping("/smart")
public class SmartIdentifyController {

  @Resource
  private SmartIdentifyService smartIdentifyService;

  @Operation(summary = "图片识别接口")
  @PostMapping(path = "/images", consumes = "multipart/form-data")
  public ResponseDTO<String> identifyImages(
      @RequestPart MultipartFile[] files
  ) {
    return ResponseDTO.ok(smartIdentifyService.identifyImages(files));
  }

  @Operation(summary = "面试测试接口")
  @GetMapping(path = "/interview")
  public ResponseDTO<Void> interview() {
    smartIdentifyService.start();
    return ResponseDTO.ok();
  }

  @Operation(summary = "视频识别接口")
  @PostMapping(path = "/video/process", consumes = "multipart/form-data")
  public ResponseDTO<String> identifyVideoProcess(
      @RequestPart MultipartFile file
  ) {
    return ResponseDTO.ok(smartIdentifyService.identifyVideo(file));
  }

  @Operation(summary = "智能建议接口")
  @PostMapping(path = "/ai")
  public ResponseDTO<String> aiProcess(@RequestBody AiBO aiBO) {
    return ResponseDTO.ok(smartIdentifyService.identifyAi(aiBO));
  }
}
