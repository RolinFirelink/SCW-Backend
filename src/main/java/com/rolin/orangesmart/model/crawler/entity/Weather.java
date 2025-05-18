package com.rolin.orangesmart.model.crawler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.crawler.vo.WeatherVO;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("weather")
public class Weather extends AbstractPO {

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

  @Override
  public String toString() {
    return "本日天气情况为:{" +
        "短时预报='" + shortReport + '\'' +
        ", AQI='" + aqi + '\'' +
        ", 空气质量级别='" + level + '\'' +
        ", 首要污染物='" + pollutants + '\'' +
        ", PM2.5日均值='" + pm + '\'' +
        ", 最高气温='" + maxTemperature + '\'' +
        ", 最小气温='" + minTemperature + '\'' +
        ", 最大雨量='" + maxRainfall + '\'' +
        ", 最小雨量='" + minRainfall + '\'' +
        ", 最大风速='" + maxSpeed + '\'' +
        ", 最小风速='" + minSpeed + '\'' +
        ", 温度='" + temperature + '\'' +
        ", 风级='" + windLevel + '\'' +
        ", 风向='" + windDirection + '\'' +
        ", 湿度='" + humidity + '\'' +
        ", 小时雨量='" + hourRainfall + '\'' +
        '}';
  }

  public WeatherVO toVo() {
    return BeanUtil.copyProperties(this, WeatherVO.class);
  }
}
