package com.rolin.orangesmart.model.smart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 03:35
 */
@Data
public class Detection {
  private Bbox bbox;
  @JsonProperty("chinese_class")
  private String chineseClass;
  @JsonProperty("class")
  private String class_;
  private double confidence;
  private int id;
}
