package com.rolin.orangesmart.model.crawler.bo;

import com.rolin.orangesmart.model.crawler.entity.Ganjuw;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 22:56
 */
@Data
public class GanjuwBO {

  private Long id;

  private String headImg;

  private String type;

  private String detailUrl;

  private String title;

  private String time;

  private String author;

  private String detail;

  private Long clickNums;

  public Ganjuw toEntity() {
    return BeanUtil.copyProperties(this, Ganjuw.class);
  }
}
