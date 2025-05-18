package com.rolin.orangesmart.model.visit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/4/4
 * Time: 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDataVO {

  private String name;

  private List<Integer> data;

}
