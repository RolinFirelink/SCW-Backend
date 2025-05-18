package com.rolin.orangesmart.model.expert.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 21:12
 */
@Data
public class ExpertVO {

  @Schema(description = "唯一主键")
  private Long id;

  @Schema(description = "著作id集合")
  private List<String> products;

  @Schema(description = "真实姓名")
  private String name;

  @Schema(description = "申请失败原因")
  private String reason;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "简介")
  private String profile;

  @Schema(description = "手机")
  private String phone;

  @Schema(description = "邮箱")
  private String email;

  @Schema(description = "qq号")
  private String qq;

  @Schema(description = "微信号")
  private String weChat;

  @Schema(description = "证明材料id")
  private List<String> certification;

  @Schema(description = "其他链接(JSON格式存储)")
  private String other;

  @Schema(description = "头像")
  private String avatar;

  @Schema(description = "履历")
  private String positions;

  @Schema(description = "擅长领域")
  private String field;

  @Schema(description = "申请状态(0代表申请中,1代表申请成功,2代表申请失败，3代表申请作废)")
  private Integer applyStatus;
}
