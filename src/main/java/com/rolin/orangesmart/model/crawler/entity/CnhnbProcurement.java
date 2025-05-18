package com.rolin.orangesmart.model.crawler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.crawler.vo.CnhnbProcurementVO;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@TableName("cnhnb_procurement")
@AllArgsConstructor
@NoArgsConstructor
public class CnhnbProcurement extends AbstractPO {

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

  public CnhnbProcurementVO toVo() {
    return BeanUtil.copyProperties(this, CnhnbProcurementVO.class);
  }

}
