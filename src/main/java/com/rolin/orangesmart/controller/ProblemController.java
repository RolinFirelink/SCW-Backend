package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.expert.bo.ExpertBO;
import com.rolin.orangesmart.model.expert.vo.ExpertVO;
import com.rolin.orangesmart.model.problem.bo.ProblemBO;
import com.rolin.orangesmart.model.problem.vo.ProblemVO;
import com.rolin.orangesmart.model.visit.vo.VisitVO;
import com.rolin.orangesmart.service.ProblemService;
import com.rolin.orangesmart.service.VisitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/22
 * Time: 13:18
 */
@RestController
@RequestMapping("/problem")
@Tag(name = "问答管理")
public class ProblemController {

  @Resource
  private ProblemService problemService;

  @GetMapping("/page")
  public ResponseDTO<PageInfo<ProblemVO>> page(
      @RequestParam Integer pageNum,
      @RequestParam Integer pageSize,
      @RequestParam(required = false) Integer active,
      @RequestParam(required = false) String keyWord
  ) {
    return ResponseDTO.ok(problemService.page(pageNum,pageSize,active,keyWord));
  }

  @GetMapping("/list")
  public ResponseDTO<List<ProblemVO>> list() {
    return ResponseDTO.ok(problemService.getList());
  }

  @PostMapping("/save")
  public ResponseDTO<Boolean> save(@RequestBody @Valid ProblemBO problemBO) {
    return ResponseDTO.ok(problemService.add(problemBO));
  }

  @GetMapping("/{id}")
  public ResponseDTO<ProblemVO> get(@PathVariable Long id) {
    return ResponseDTO.ok(problemService.get(id));
  }

  @PutMapping("/update")
  public ResponseDTO<Boolean> update(@RequestBody ProblemBO problemBO) {
    return ResponseDTO.ok(problemService.update(problemBO));
  }

  @DeleteMapping("/{id}")
  public ResponseDTO<Boolean> delete(@PathVariable Long id) {
    return ResponseDTO.ok(problemService.delete(id));
  }

}
