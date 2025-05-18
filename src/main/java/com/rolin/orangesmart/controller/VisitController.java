package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.visit.vo.VisitEchartsVO;
import com.rolin.orangesmart.model.visit.vo.VisitVO;
import com.rolin.orangesmart.service.VisitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/22
 * Time: 13:18
 */
@RestController
@RequestMapping("/visit")
@Tag(name = "访问管理")
public class VisitController {

  @Resource
  private VisitService visitService;

  @GetMapping("/list")
  public ResponseDTO<List<VisitVO>> list() {
    return ResponseDTO.ok(visitService.getList());
  }

  @GetMapping("/getOne")
  public ResponseDTO<VisitVO> get() {
    return ResponseDTO.ok(visitService.get());
  }

  @GetMapping("/getSeven")
  public ResponseDTO<VisitEchartsVO> getSeven() {
    return ResponseDTO.ok(visitService.getSeven());
  }


}
