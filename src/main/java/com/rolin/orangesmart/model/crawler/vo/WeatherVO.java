package com.rolin.orangesmart.model.crawler.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rolin
 * Date: 2025/2/24
 * Time: 20:40
 */
@Data
public class WeatherVO {

  @Schema(description = "唯一主键")
  private Long id;

  @Schema(description = "短时预报")
  private String shortReport;

  @Schema(description = "AQI")
  private String aqi;

  @Schema(description = "空气质量级别")
  private String level;

  @Schema(description = "首要污染物")
  private String pollutants;

  @Schema(description = "PM2.5日均值")
  private String pm;

  @Schema(description = "AI建议")
  private String advice;

  @Schema(description = "最高气温")
  private String maxTemperature;

  @Schema(description = "最小气温")
  private String minTemperature;

  @Schema(description = "最大雨量")
  private String maxRainfall;

  @Schema(description = "最小雨量")
  private String minRainfall;

  @Schema(description = "最大风速")
  private String maxSpeed;

  @Schema(description = "最小风速")
  private String minSpeed;

  @Schema(description = "温度")
  private String temperature;

  @Schema(description = "风级")
  private String windLevel;

  @Schema(description = "风向")
  private String windDirection;

  @Schema(description = "湿度")
  private String humidity;

  @Schema(description = "小时雨量")
  private String hourRainfall;

  @Schema(description = "创建时间")
  private Date createDate;

}
