package com.rolin.orangesmart.model.smart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 03:36
 */
@Data
public class Statistics {
  @JsonProperty("total_objects")
  private int totalObjects;
  @JsonProperty("violence_detected")
  private boolean violenceDetected;
}
