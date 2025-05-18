package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.expert.bo.ExpertBO;
import com.rolin.orangesmart.model.expert.vo.ExpertVO;
import com.rolin.orangesmart.service.ExpertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 21:05
 */
@Tag(name = "专家管理")
@RestController
@RequestMapping("/expert")
public class ExpertController {

  @Resource
  private ExpertService expertService;

  /**
   * 分页查询专家列表
   */
  @GetMapping("/page")
  public ResponseDTO<PageInfo<ExpertVO>> page(
      @RequestParam Integer pageNum,
      @RequestParam Integer pageSize,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String keyWord) {
    return ResponseDTO.ok(expertService.page(pageNum,pageSize,status,keyWord));
  }

  /**
   * 分页查询当前用户的专家列表
   */
  @GetMapping("/selfPage")
  public ResponseDTO<PageInfo<ExpertVO>> selfPage(
      @RequestParam Integer pageNum,
      @RequestParam Integer pageSize,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String keyWord) {
    return ResponseDTO.ok(expertService.selfPage(pageNum,pageSize,status,keyWord));
  }

  @PostMapping("/save")
  public ResponseDTO<Boolean> save(@RequestBody @Valid ExpertBO expertBO) {
    return ResponseDTO.ok(expertService.add(expertBO));
  }

  @GetMapping("/{id}")
  public ResponseDTO<ExpertVO> get(@PathVariable Long id) {
    return ResponseDTO.ok(expertService.get(id));
  }

  @PutMapping("/update")
  public ResponseDTO<Boolean> update(@RequestBody ExpertBO expertBO) {
    return ResponseDTO.ok(expertService.update(expertBO));
  }

  @PutMapping("/approval")
  public ResponseDTO<Boolean> approval(@RequestBody ExpertBO expertBO) {
    return ResponseDTO.ok(expertService.approval(expertBO));
  }

  @DeleteMapping("/{id}")
  public ResponseDTO<Boolean> delete(@PathVariable Long id) {
    return ResponseDTO.ok(expertService.delete(id));
  }

}
