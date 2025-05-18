package com.rolin.orangesmart.model.crawler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.crawler.vo.CnhnbPriceVO;
import com.rolin.orangesmart.util.BeanUtil;
import com.rolin.orangesmart.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@TableName("cnhnb_price")
@AllArgsConstructor
@NoArgsConstructor
public class CnhnbPrice extends AbstractPO {

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

  /**
   * url
   */
  private String detailUrl;

  public CnhnbPriceVO toVo() {
    CnhnbPriceVO cnhnbPriceVO = BeanUtil.copyProperties(this, CnhnbPriceVO.class);
    cnhnbPriceVO.setStartDate(time);
    cnhnbPriceVO.setEndDate(DateUtil.getNextDay(time));
    return cnhnbPriceVO;
  }

  @Override
  public CnhnbPrice clone() {
    CnhnbPrice newPrice = BeanUtil.copyProperties(this, CnhnbPrice.class);
    return newPrice;
  }
}
