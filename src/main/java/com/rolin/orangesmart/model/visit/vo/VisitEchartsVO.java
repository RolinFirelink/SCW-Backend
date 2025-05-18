package com.rolin.orangesmart.model.visit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/4/4
 * Time: 16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitEchartsVO {

  private List<String> xAxisData;

  private List<VisitDataVO> seriesData;

}
