package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.exception.errorEnum.ExpertErrorEnum;
import com.rolin.orangesmart.mapper.ExpertMapper;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.expert.bo.ExpertBO;
import com.rolin.orangesmart.model.expert.entity.Expert;
import com.rolin.orangesmart.model.expert.vo.ExpertVO;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.EntityUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Author: Rolin
 * Date: 2025/3/5
 * Time: 21:05
 */
@Service
public class ExpertService extends BaseService<ExpertMapper, Expert> {

  public PageInfo<ExpertVO> page(Integer pageNum, Integer pageSize, Integer status, String keyWord) {
    LambdaQueryWrapper<Expert> queryWrapper = getLambdaQueryWrapper();
    if (!StringUtils.isEmpty(keyWord)) {
      queryWrapper.like(Expert::getName, keyWord);
      queryWrapper.like(Expert::getWeChat, keyWord);
      queryWrapper.like(Expert::getQq, keyWord);
      queryWrapper.like(Expert::getPhone, keyWord);
    }
    if(!ObjectUtils.isEmpty(status)){
      queryWrapper.eq(Expert::getApplyStatus,status);
    }
    PageInfo<Expert> page = page(pageNum, pageSize, queryWrapper);
    return page.convert(Expert::toVo);
  }

  public PageInfo<ExpertVO> selfPage(Integer pageNum, Integer pageSize, Integer status, String keyWord) {
    LambdaQueryWrapper<Expert> queryWrapper = getLambdaQueryWrapper();
    if (!StringUtils.isEmpty(keyWord)) {
      queryWrapper.like(Expert::getName, keyWord);
      queryWrapper.like(Expert::getWeChat, keyWord);
      queryWrapper.like(Expert::getQq, keyWord);
      queryWrapper.like(Expert::getPhone, keyWord);
    }
    if(!ObjectUtils.isEmpty(status)){
      queryWrapper.eq(Expert::getApplyStatus,status);
    }
    queryWrapper.eq(Expert::getUserId,ReqEnvContext.getUser().getId());
    PageInfo<Expert> page = page(pageNum, pageSize, queryWrapper);
    return page.convert(Expert::toVo);
  }

  public LambdaQueryWrapper<Expert> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(Expert.class);
  }

  /**
   * 默认创建专家状态是申请中
   * @param expertBO
   * @return
   */
  public Boolean add(@Valid ExpertBO expertBO) {
    Expert entity = expertBO.toEntity();
    entity.setUserId(ReqEnvContext.getUser().getId());
    EntityUtil.setIdNull(entity);
    entity.setApplyStatus(0);
    LambdaQueryWrapper<Expert> queryWrapper = getLambdaQueryWrapper();
    queryWrapper.eq(Expert::getName,entity.getName());
    ExpertErrorEnum.EXPERT_EXIST_ERROR.isTrue(exists(queryWrapper));
    return save(entity);
  }

  public ExpertVO get(Long id) {
    Expert expert = getById(id);
    ExpertErrorEnum.EXPERT_NOT_EXIST_ERROR.isNull(expert);
    return expert.toVo();
  }

  public Boolean update(ExpertBO expertBO) {
    Expert expert = getById(expertBO.getId());
    ExpertErrorEnum.EXPERT_NOT_EXIST_ERROR.isNull(expert);
    Expert entity = expertBO.toEntity();
    entity.setApplyStatus(0);
    return updateById(entity);
  }

  public Boolean delete(Long id) {
    Expert expert = getById(id);
    ExpertErrorEnum.EXPERT_NOT_EXIST_ERROR.isNull(expert);
    return deleteById(id);
  }

  public Boolean approval(ExpertBO expertBO) {
    Expert expert = getById(expertBO.getId());
    ExpertErrorEnum.EXPERT_NOT_EXIST_ERROR.isNull(expert);
    Expert entity = expertBO.toEntity();
    return updateById(entity);
  }
}
