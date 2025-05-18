package com.rolin.orangesmart.enums;

import lombok.Getter;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 21:17
 */
@Getter
public enum GanjuwEnum {

  NEWS("行业快讯", "NEWS"),
  LAWS("政策法规", "LAWS"),
  MARKET("行情咨询", "MARKET"),
  TECH("种植技术", "TECH"),
  HEAD("橘业头条", "HEAD")






  ;

  private String name;
  private String code;

  GanjuwEnum(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public GanjuwEnum getByCode(String code) {
    for (GanjuwEnum item : GanjuwEnum.values()) {
      if (item.getCode().equals(code)) {
        return item;
      }
    }
    return null;
  }
}
