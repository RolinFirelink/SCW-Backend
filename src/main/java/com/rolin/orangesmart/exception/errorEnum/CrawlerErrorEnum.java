package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

/**
 * Author: Rolin
 * Date: 2025/3/6
 * Time: 04:43
 */
@Getter
public enum CrawlerErrorEnum implements ErrorEnum {

  CRAWLER_NOT_EXIST_ERROR("0000", "该爬虫信息不存在"),
  CRAWLER_EXIST_ERROR("0001", "该爬虫信息已存在"),


  ;

  private final String code;

  private final String message;

  private CrawlerErrorEnum(String code, String message) {
    this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
    this.message = message;
  }

}
