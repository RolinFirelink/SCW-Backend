package com.rolin.orangesmart.model.fish.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "审核举报请求")
public class AuditReportRequest {


	@Schema(description = "举报id")
	private Integer reportId;

	@Schema(description = "举报成功:1;举报失败:0")
	private Integer flag;

	@Schema(description = "处理结果")
	private String result;
}
