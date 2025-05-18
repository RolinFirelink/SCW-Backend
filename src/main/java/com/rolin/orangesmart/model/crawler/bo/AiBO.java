package com.rolin.orangesmart.model.crawler.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Rolin
 * Date: 2025/4/1
 * Time: 03:23
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiBO {

  // 柑橘品种
  private String variety;

  // 生长情况
  private String growth;

  // 是否传入视频
  private boolean video = false;

  // 是否传入图片
  private boolean image = false;

  private String text;

}
