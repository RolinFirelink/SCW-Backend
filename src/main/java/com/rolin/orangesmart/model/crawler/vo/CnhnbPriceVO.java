package com.rolin.orangesmart.model.crawler.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 18:35
 */
@Data
public class CnhnbPriceVO {

  /**
   * 主键id
   */
  @Schema(description = "主键id")
  private Long id;

  /**
   * 时间
   */
  @Schema(description = "时间")
  private Date time;

  /**
   * 产品
   */
  @Schema(description = "产品")
  private String type;

  /**
   * 产地
   */
  @Schema(description = "产地")
  private String address;

  /**
   * 价格
   */
  @Schema(description = "价格")
  private String price;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  private Date createDate;

  /**
   * 更新时间
   */
  @Schema(description = "更新时间")
  private Date updateDate;

  /**
   * 开始时间
   */
  @Schema(description = "开始时间")
  private Date startDate;

  /**
   * 结束时间
   */
  @Schema(description = "结束时间")
  private Date endDate;
}
