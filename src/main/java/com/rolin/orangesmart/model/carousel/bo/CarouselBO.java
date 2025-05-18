package com.rolin.orangesmart.model.carousel.bo;

import com.rolin.orangesmart.model.carousel.entity.Carousel;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/3/7
 * Time: 13:29
 */
@Data
public class CarouselBO {

  private Long id;

  @Schema(description = "轮播图id")
  private String images;

  @Schema(description = "轮播图名")
  private String name;

  @Schema(description = "顺序")
  private Integer showOrder;

  @Schema(description = "是否启用")
  private Integer active;

  public Carousel toEntity() {
    return BeanUtil.copyProperties(this, Carousel.class);
  }
}
