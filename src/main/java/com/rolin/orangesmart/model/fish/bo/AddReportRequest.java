package com.rolin.orangesmart.model.fish.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "添加举报请求")
public class AddReportRequest {

	@Schema(description = "类型（1:用户,2:评论,3:帖子）")
	private Integer objectType;

	@Schema(description = "对象id")
	private Integer objectId;

	@Schema(description = "举报理由")
	private String reason;

}
