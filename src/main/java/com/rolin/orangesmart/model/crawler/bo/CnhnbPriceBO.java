package com.rolin.orangesmart.model.crawler.bo;

import com.rolin.orangesmart.model.crawler.entity.CnhnbPrice;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 18:35
 */
@Data
public class CnhnbPriceBO {

  private Long id;

  /**
   * 时间
   */
  private Date time;

  /**
   * 产品
   */
  private String type;

  /**
   * 产地
   */
  private String address;

  /**
   * 价格
   */
  private String price;

  public CnhnbPrice toEntity() {
    return BeanUtil.copyProperties(this, CnhnbPrice.class);
  }

}