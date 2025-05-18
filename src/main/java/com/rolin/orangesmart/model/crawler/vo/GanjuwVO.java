package com.rolin.orangesmart.model.crawler.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 22:56
 */
@Data
public class GanjuwVO {

  @Schema(description = "主键id")
  private Long id;

  @Schema(description = "资讯封面")
  private String headImg;

  @Schema(description = "资讯类型")
  private String type;

  @Schema(description = "详细链接")
  private String detailUrl;

  @Schema(description = "资讯标题")
  private String title;

  @Schema(description = "资讯时间")
  private String time;

  @Schema(description = "资讯作者")
  private String author;

  @Schema(description = "资讯详情")
  private String detail;

  @Schema(description = "资讯点击次数")
  private Long clickNums;

}
