package com.rolin.orangesmart.model.carousel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/3/7
 * Time: 13:29
 */
@Data
public class CarouselVO {

  private Long id;

  @Schema(description = "轮播图id")
  private String images;

  @Schema(description = "顺序")
  private Integer showOrder;

  @Schema(description = "是否启用")
  private Integer active;

  @Schema(description = "轮播图名")
  private String name;

}
