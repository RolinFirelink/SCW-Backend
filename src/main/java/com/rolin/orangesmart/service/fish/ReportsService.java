package com.rolin.orangesmart.service.fish;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rolin.orangesmart.model.fish.bo.AddReportRequest;
import com.rolin.orangesmart.model.fish.bo.AuditReportRequest;
import com.rolin.orangesmart.model.fish.entity.PageResult;
import com.rolin.orangesmart.model.fish.entity.Reports;
import com.rolin.orangesmart.model.fish.vo.ReportVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author hzzzzzy
* @description 针对表【reports(举报表)】的数据库操作Service
* @createDate 2025-01-11 22:07:11
*/
public interface ReportsService extends IService<Reports> {

	void reportObject(AddReportRequest addReportRequest, HttpServletRequest request);

	PageResult<ReportVO> getReport(Integer current, Integer pageSize, String keyword, Integer status, Integer objectType);

	void auditReport(AuditReportRequest auditReportRequest, HttpServletRequest request);
}
