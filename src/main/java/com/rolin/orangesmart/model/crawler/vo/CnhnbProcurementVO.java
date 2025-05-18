package com.rolin.orangesmart.model.crawler.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 17:31
 */
@Data
public class CnhnbProcurementVO {

  @Schema(description = "唯一主键")
  private Long id;

  @Schema(description = "采购品种")
  private String name;

  @Schema(description = "采购量")
  private String amount;

  @Schema(description = "收货地")
  private String address;

  @Schema(description = "采购方")
  private String purchaser;

  @Schema(description = "更新时间")
  private String updateTime;

  @Schema(description = "采购等级")
  private String level;

  @Schema(description = "详细url")
  private String detailUrl;
}
