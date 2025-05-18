package com.rolin.orangesmart.model.smart;

import lombok.Data;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 03:34
 */
@Data
public class Result {
  private List<Detection> detections;
  private String filename;
  private Statistics statistics;
  private String status;
  private String message;
}
