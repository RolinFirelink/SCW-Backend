package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.carousel.vo.CarouselVO;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.carousel.bo.CarouselBO;
import com.rolin.orangesmart.service.CarouselService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/7
 * Time: 13:38
 */
@Tag(name = "轮播图管理")
@RestController
@RequestMapping("/carousel")
public class CarouselController {

  @Resource
  private CarouselService carouselService;

  /**
   * 分页查询轮播图列表
   */
  @GetMapping("/page")
  public ResponseDTO<PageInfo<CarouselVO>> page(
      @RequestParam Integer pageNum,
      @RequestParam Integer pageSize) {
    return ResponseDTO.ok(carouselService.page(pageNum,pageSize,""));
  }

  @GetMapping("/list")
  public ResponseDTO<List<CarouselVO>> list() {
    return ResponseDTO.ok(carouselService.getList());
  }

  @PostMapping("/save")
  public ResponseDTO<Boolean> save(@RequestBody @Valid CarouselBO carouselBo) {
    return ResponseDTO.ok(carouselService.add(carouselBo));
  }

  @GetMapping("/{id}")
  public ResponseDTO<CarouselVO> get(@PathVariable Long id) {
    return ResponseDTO.ok(carouselService.get(id));
  }

  @PutMapping("/update")
  public ResponseDTO<Boolean> update(@RequestBody CarouselBO carouselBo) {
    return ResponseDTO.ok(carouselService.update(carouselBo));
  }

  @DeleteMapping("/{id}")
  public ResponseDTO<Boolean> delete(@PathVariable Long id) {
    return ResponseDTO.ok(carouselService.delete(id));
  }

  @DeleteMapping("/deleteByIds")
  public ResponseDTO<Boolean> deleteByIds(@RequestBody List<Long> ids) {
    return ResponseDTO.ok(carouselService.deleteByIds(ids));
  }

}
