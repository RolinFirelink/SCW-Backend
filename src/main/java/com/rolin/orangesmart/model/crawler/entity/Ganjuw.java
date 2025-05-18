package com.rolin.orangesmart.model.crawler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.crawler.vo.GanjuwVO;
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
 * @since 2025-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ganjuw")
@AllArgsConstructor
@NoArgsConstructor
public class Ganjuw extends AbstractPO {

  private String headImg;

  private String type;

  private String detailUrl;

  private String title;

  private String time;

  private String author;

  private String detail;

  private Long clickNums;

  public Ganjuw(String type, String detailUrl, String title) {
    this.type = type;
    this.detailUrl = detailUrl;
    this.title = title;
  }

  public GanjuwVO toVo() {
    return BeanUtil.copyProperties(this, GanjuwVO.class);
  }
}
