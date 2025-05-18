package com.rolin.orangesmart.model.carousel.entity;

import com.rolin.orangesmart.model.carousel.vo.CarouselVO;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Carousel extends AbstractPO {

  @Schema(description = "轮播图id")
  private String images;

  @Schema(description = "轮播图名")
  private String name;

  @Schema(description = "顺序")
  private Integer showOrder;

  @Schema(description = "是否启用")
  private Integer active;

  public CarouselVO toVo() {
    return BeanUtil.copyProperties(this, CarouselVO.class);
  }
}
