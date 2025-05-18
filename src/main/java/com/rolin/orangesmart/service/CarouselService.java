package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.exception.errorEnum.CarouselErrorEnum;
import com.rolin.orangesmart.mapper.CarouselMapper;
import com.rolin.orangesmart.model.carousel.bo.CarouselBO;
import com.rolin.orangesmart.model.carousel.entity.Carousel;
import com.rolin.orangesmart.model.carousel.vo.CarouselVO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.ConvertUtil;
import com.rolin.orangesmart.util.EntityUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/7
 * Time: 13:38
 */
@Service
public class CarouselService extends BaseService<CarouselMapper, Carousel> {

  public PageInfo<CarouselVO> page(Integer pageNum, Integer pageSize, String keyWord) {
    return page(pageNum, pageSize, getLambdaQueryWrapper()).convert(Carousel::toVo);
  }

  public LambdaQueryWrapper<Carousel> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(Carousel.class);
  }

  public Boolean add(@Valid CarouselBO carouselBo) {
    return save(EntityUtil.setIdNull(carouselBo.toEntity()));
  }

  public CarouselVO get(Long id) {
    Carousel carousel = getById(id);
    CarouselErrorEnum.CAROUSEL_NOT_EXIST_ERROR.isNull(carousel);
    return carousel.toVo();
  }

  public Boolean update(CarouselBO carouselBo) {
    Carousel carousel = getById(carouselBo.getId());
    CarouselErrorEnum.CAROUSEL_NOT_EXIST_ERROR.isNull(carousel);
    return updateById(carouselBo.toEntity());
  }

  public Boolean delete(Long id) {
    Carousel carousel = getById(id);
    CarouselErrorEnum.CAROUSEL_NOT_EXIST_ERROR.isNull(carousel);
    return deleteById(id);
  }

  public List<CarouselVO> getList() {
    List<CarouselVO> carouselVOS = ConvertUtil.listToList(list(getLambdaQueryWrapper()), Carousel::toVo);
    carouselVOS.sort(Comparator.comparingInt(CarouselVO::getShowOrder));
    return carouselVOS;
  }
}
