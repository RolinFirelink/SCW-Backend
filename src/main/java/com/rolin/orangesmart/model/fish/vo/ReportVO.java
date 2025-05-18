package com.rolin.orangesmart.model.fish.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
@Data
public class ReportVO {

	@Schema(description = "举报主键id")
	private Integer id;

	@Schema(description = "举报用户id")
	private Integer reporterId;

	@Schema(description = "举报用户昵称")
	private String reporterNickname;

	@Schema(description = "举报用户头像")
	private String reporterAvatar;

	@Schema(description = "举报理由")
	private String reason;

	@Schema(description = "举报对象")
	private Object object;

	@Schema(description = "举报状态（1:待处理;2:已处理）")
	private Integer status;

	@Schema(description = "举报对象类型（1:用户;2:评论;3:帖子）")
	private Integer objectType;

	@Schema(description = "举报时间")
	@JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
	private Date createTime;
}
