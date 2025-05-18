package com.rolin.orangesmart.service.fish.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rolin.orangesmart.constant.RedisConstant;
import com.rolin.orangesmart.enums.ReportType;
import com.rolin.orangesmart.mapper.fish.ReportsMapper;
import com.rolin.orangesmart.model.fish.bo.AddReportRequest;
import com.rolin.orangesmart.model.fish.bo.AuditReportRequest;
import com.rolin.orangesmart.model.fish.entity.Comment;
import com.rolin.orangesmart.model.fish.entity.PageResult;
import com.rolin.orangesmart.model.fish.entity.Post;
import com.rolin.orangesmart.model.fish.entity.Reports;
import com.rolin.orangesmart.model.fish.vo.ReportVO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.service.UserService;
import com.rolin.orangesmart.service.fish.CommentService;
import com.rolin.orangesmart.service.fish.PostCommonService;
import com.rolin.orangesmart.service.fish.PostService;
import com.rolin.orangesmart.service.fish.ReportsService;
import com.rolin.orangesmart.util.BanUtils;
import com.rolin.orangesmart.util.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rolin.orangesmart.constant.CommonConstant.HEADER_TOKEN;
import static com.rolin.orangesmart.constant.CommonConstant.TRUE;

/**
* @author hzzzzzy
* @description 针对表【reports(举报表)】的数据库操作Service实现
* @createDate 2025-01-11 22:07:11
*/
@Service
public class ReportsServiceImpl extends ServiceImpl<ReportsMapper, Reports>
    implements ReportsService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private PostCommonService postCommonService;
	@Autowired
	private BanUtils banUtils;
	@Autowired
	private CommentService commentService;
	@Autowired
	private PostService postService;

	@Override
	public void reportObject(AddReportRequest addReportRequest, HttpServletRequest request) {
		Integer reporterId = getUser(request).getId().intValue();
		Reports item = new Reports();
		item.setReporterId(reporterId);
		item.setObjectId(addReportRequest.getObjectId());
		item.setReason(addReportRequest.getReason());
		item.setObjectType(addReportRequest.getObjectType());
		this.save(item);
	}

	@Override
	public PageResult<ReportVO> getReport(Integer current, Integer pageSize, String keyword, Integer status, Integer objectType) {
		LambdaQueryWrapper<Reports> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper
				.like(!StringUtils.isEmpty(keyword), Reports::getReason, keyword)
				.eq(status != null, Reports::getStatus, status)
				.eq(objectType != null, Reports::getObjectType, objectType);
		List<Reports> reportsList = this.list(queryWrapper);
		if (reportsList == null){
			return null;
		}
		List<ReportVO> voList = reportsList.stream().map(item -> {
			User reporter = userService.getById(item.getReporterId());
			ReportVO vo = BeanUtil.copyProperties(item, ReportVO.class);
			vo.setReporterNickname(reporter.getUserName());
			vo.setReporterAvatar(reporter.getAvatar());
			// 判断类型
			if (item.getObjectType() == ReportType.USER.getType()) {
				vo.setObject(userService.getUserInfo(item.getObjectId()));
			} else if (item.getObjectType() == ReportType.COMMENT.getType()) {
				vo.setObject(postCommonService.getCommentInfo(item.getObjectId()));
			} else if (item.getObjectType() == ReportType.POST.getType()) {
				vo.setObject(postCommonService.getPostInfo(item.getObjectId()));
			}
			return vo;
		}).collect(Collectors.toList());
		return PageUtil.getPage(voList, current, pageSize);
	}

	@Override
	public void auditReport(AuditReportRequest auditReportRequest, HttpServletRequest request) {
		Integer reportId = auditReportRequest.getReportId();
		Integer flag = auditReportRequest.getFlag();
		String result = auditReportRequest.getResult();

		Reports entity = this.getById(reportId);
		entity.setStatus(2);
		entity.setResult(result);
		this.updateById(entity);

		if (flag == TRUE){
			// 封禁
			banUtils.handleViolation(getUser(request).getId().intValue());
			// 更新状态
			if (entity.getObjectType() == ReportType.COMMENT.getType()){
				Comment comment = commentService.getById(entity.getObjectId());
				comment.setStatus(0);
				commentService.updateById(comment);
			}
			if (entity.getObjectType() == ReportType.POST.getType()){
				Post post = postService.getById(entity.getObjectId());
				post.setStatus(0);
				postService.updateById(post);
			}
		}
	}

	private User getUser(HttpServletRequest request) {
		String token = request.getHeader(HEADER_TOKEN);
		String jsonUser = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
		return JSONUtil.toBean(jsonUser, User.class);
	}
}




