package com.rolin.orangesmart.controller.fish;

import com.rolin.orangesmart.enums.ReportType;
import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.fish.bo.AddReportRequest;
import com.rolin.orangesmart.model.fish.bo.AuditReportRequest;
import com.rolin.orangesmart.model.fish.entity.PageResult;
import com.rolin.orangesmart.model.fish.vo.ReportVO;
import com.rolin.orangesmart.service.fish.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "举报管理")
@RestController
@CrossOrigin
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportsService reportsService;

	@Operation(description = "举报", tags = "举报管理")
	@PostMapping("reportObject")
	public Result reportObject(
			@RequestBody
			AddReportRequest addReportRequest,
			HttpServletRequest request
	){
		if (!ReportType.containsType(addReportRequest.getObjectType())){
			return new Result<>().fail().message("类型错误");
		}
		reportsService.reportObject(addReportRequest, request);
		return new Result<>().success().message("举报成功");
	}

	@Operation(description = "查看举报列表", tags = "举报管理")
	@GetMapping("getReport")
	public Result getReport(
			@RequestParam("current")
			@Parameter(description = "当前页")
			Integer current,
			@RequestParam("pageSize")
			@Parameter(description = "页容量")
			Integer pageSize,
			@RequestParam(value = "keyword", required = false)
			@Parameter(description = "搜索举报理由关键词")
			String keyword,
			@RequestParam(value = "status", required = false)
			@Parameter(description = "举报状态（1:待处理;2:已处理）不填则默认全部")
			Integer status,
			@RequestParam(value = "objectType", required = false)
			@Parameter(description = "举报对象类型（1:用户;2:评论;3:帖子）不填则默认全部")
			Integer objectType
	){
		PageResult<ReportVO> result = reportsService.getReport(current, pageSize, keyword, status, objectType);
		return new Result<>().success().message("查看举报列表").data(result);
	}

	@Operation(description = "审核", tags = "举报管理")
	@PostMapping("auditReport")
	public Result auditReport(
			@RequestBody
			AuditReportRequest auditReportRequest,
			HttpServletRequest request
	){
		reportsService.auditReport(auditReportRequest, request);
		return new Result<>().success().message("审核成功");
	}
}