package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.exception.errorEnum.ProblemErrorEnum;
import com.rolin.orangesmart.mapper.ProblemMapper;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.problem.bo.ProblemBO;
import com.rolin.orangesmart.model.problem.entity.Problem;
import com.rolin.orangesmart.model.problem.vo.ProblemVO;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.BeanUtil;
import com.rolin.orangesmart.util.ConvertUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/3/22
 * Time: 13:30
 */
@Service
public class ProblemService extends BaseService<ProblemMapper, Problem> {

  public LambdaQueryWrapper<Problem> getLambdaQueryWrapper(){
    return QueryWrapperUtil.getWrapper(Problem.class);
  }

  public PageInfo<ProblemVO> page(Integer pageNum, Integer pageSize, Integer active, String keyWord) {
    LambdaQueryWrapper<Problem> queryWrapper = getLambdaQueryWrapper();
    queryWrapper.eq(active!=null,Problem::getActive, active);
    queryWrapper.like(keyWord!=null,Problem::getQuestion, keyWord);
    queryWrapper.orderByDesc(Problem::getShowOrder);
    PageInfo<Problem> page = page(pageNum, pageSize, queryWrapper);
    return page.convert(Problem::toVo);
  }

  public Boolean add(ProblemBO problemBO) {
    LambdaQueryWrapper<Problem> queryWrapper = getLambdaQueryWrapper();
    queryWrapper.eq(Problem::getQuestion, problemBO.getQuestion());
    ProblemErrorEnum.PROBLEM_EXIST_ERROR.isTrue(exists(queryWrapper));
    return save(problemBO.toEntity());
  }

  public ProblemVO get(Long id) {
    Problem problem = getById(id);
    ProblemErrorEnum.PROBLEM_NOT_EXIST_ERROR.isNull(problem);
    return problem.toVo();
  }

  public Boolean update(ProblemBO problemBO) {
    Problem problem = getById(problemBO.getId());
    ProblemErrorEnum.PROBLEM_NOT_EXIST_ERROR.isNull(problem);
    return updateById(problemBO.toEntity());
  }

  public Boolean delete(Long id) {
    Problem problem = getById(id);
    ProblemErrorEnum.PROBLEM_NOT_EXIST_ERROR.isNull(problem);
    return deleteById(id);
  }

  public List<ProblemVO> getList() {
     return ConvertUtil.listToList(list(getLambdaQueryWrapper().orderByDesc(Problem::getShowOrder)), Problem::toVo);
  }
}
