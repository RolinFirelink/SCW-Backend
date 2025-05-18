package com.rolin.orangesmart.model.crawler.bo;

import com.rolin.orangesmart.model.crawler.entity.Weather;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/3/6
 * Time: 05:19
 */
@Data
public class WeatherBO {

  private Long id;

  /**
   * 短时预报
   */
  private String shortReport;

  /**
   * AQI
   */
  private String aqi;

  /**
   * 空气质量级别
   */
  private String level;

  /**
   * 首要污染物
   */
  private String pollutants;

  /**
   * PM2.5日均值
   */
  private String pm;

  /**
   * AI建议
   */
  private String advice;

  /**
   * 最高气温
   */
  private String maxTemperature;

  /**
   * 最小气温
   */
  private String minTemperature;

  /**
   * 最大雨量
   */
  private String maxRainfall;

  /**
   * 最小雨量
   */
  private String minRainfall;

  /**
   * 最大风速
   */
  private String maxSpeed;

  /**
   * 最小风速
   */
  private String minSpeed;

  /**
   * 温度
   */
  private String temperature;

  /**
   * 风级
   */
  private String windLevel;

  /**
   * 风向
   */
  private String windDirection;

  /**
   * 湿度
   */
  private String humidity;

  /**
   * 小时雨量
   */
  private String hourRainfall;

  public Weather toEntity() {
    return BeanUtil.copyProperties(this, Weather.class);
  }

}
