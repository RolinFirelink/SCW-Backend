package com.rolin.orangesmart.model.expert.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.expert.vo.ExpertVO;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("expert")
public class Expert extends AbstractPO {

  @Schema(description = "著作id集合")
  private String products;

  @Schema(description = "用户id")
  private Long userId;

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

  @Schema(description = "其他链接(JSON格式存储)")
  private String other;

  @Schema(description = "申请状态(0代表申请中,1代表申请成功,2代表申请失败，3代表申请作废)")
  private Integer applyStatus;

  @Schema(description = "证明材料id")
  private String certification;

  @Schema(description = "头像")
  private String avatar;

  @Schema(description = "履历")
  private String positions;

  @Schema(description = "擅长领域")
  private String field;

  public ExpertVO toVo() {
    ExpertVO expertVO = BeanUtil.copyProperties(this, ExpertVO.class);
    expertVO.setProducts(Arrays.asList(products.split(",")));
    expertVO.setCertification(Arrays.asList(certification.split(",")));
    return expertVO;
  }
}
