package com.rolin.orangesmart.model.crawler.vo;

import lombok.Data;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/22
 * Time: 17:24
 */
@Data
public class ProcurementEchartsVO {

  private String province;

  private Integer value;

  private List<ProcurementCityVO> citys;

}
