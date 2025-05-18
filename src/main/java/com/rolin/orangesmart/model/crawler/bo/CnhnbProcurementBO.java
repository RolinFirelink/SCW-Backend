package com.rolin.orangesmart.model.crawler.bo;

import com.rolin.orangesmart.model.crawler.entity.CnhnbProcurement;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-02-23
 */
@Data
public class CnhnbProcurementBO {

  private Long id;

  /**
   * 采购品种
   */
  private String name;

  /**
   * 采购量
   */
  private String amount;

  /**
   * 收货地
   */
  private String address;

  /**
   * 采购方
   */
  private String purchaser;

  /**
   * 更新时间
   */
  private String updateTime;

  /**
   * 采购等级
   */
  private String level;

  /**
   * 详细url
   */
  private String detailUrl;

  public CnhnbProcurement toEntity() {
    return BeanUtil.copyProperties(this, CnhnbProcurement.class);
  }

}
