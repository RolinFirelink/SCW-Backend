package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.mapper.GanjuwMapper;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.crawler.entity.Ganjuw;
import com.rolin.orangesmart.model.crawler.vo.GanjuwVO;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.CheckUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 20:41
 */
@Service
public class GanjuwService extends BaseService<GanjuwMapper, Ganjuw> {

  public LambdaQueryWrapper<Ganjuw> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(Ganjuw.class);
  }

//  @Transactional
//  public void batchSaveOrUpdate(List<Ganjuw> ganjuws) {
//    // 提取所有标题
//    List<String> titles = ganjuws.stream().map(Ganjuw::getTitle).collect(Collectors.toList());
//
//    // 批量查询已存在的对象
//    LambdaQueryWrapper<Ganjuw> queryWrapper = getLambdaQueryWrapper();
//    queryWrapper.in(Ganjuw::getTitle, titles);
//    Map<String, Ganjuw> existingGanjuws = list(queryWrapper).stream()
//        .collect(Collectors.toMap(Ganjuw::getTitle, ganjuw -> ganjuw));
//
//    // 分离需要保存和更新的对象
//    List<Ganjuw> toSave = new ArrayList<>();
//    List<Ganjuw> toUpdate = new ArrayList<>();
//
//    for (Ganjuw ganjuw : ganjuws) {
//      Ganjuw existing = existingGanjuws.get(ganjuw.getTitle());
//      if (existing == null) {
//        toSave.add(ganjuw);
//      } else {
//        ganjuw.setId(existing.getId());
//        toUpdate.add(ganjuw);
//      }
//    }
//
//    // 批量保存和更新
//    saveBatch(toSave);
//    updateBatchById(toUpdate);
//  }

  @Transactional
  public void batchSaveOrUpdate(List<Ganjuw> ganjuws) {
    for (Ganjuw ganjuw : ganjuws) {
      LambdaQueryWrapper<Ganjuw> ganjuwLambdaQueryWrapper = getLambdaQueryWrapper();
      ganjuwLambdaQueryWrapper.eq(Ganjuw::getTitle, ganjuw.getTitle());
      List<Ganjuw> list = list(ganjuwLambdaQueryWrapper);
      if (CheckUtil.isEmpty(list)) {
        save(ganjuw);
      } else {
        ganjuw.setId(list.getFirst().getId());
        ganjuw.setClickNums(list.getFirst().getClickNums());
        updateById(ganjuw);
      }
    }
  }

  public PageInfo<GanjuwVO> page(Integer pageNum, Integer pageSize, String keyWord) {
    LambdaQueryWrapper<Ganjuw> queryWrapper = getLambdaQueryWrapper();
    if(!StringUtils.isEmpty(keyWord)){
      queryWrapper.like(Ganjuw::getTitle,keyWord);
    }
    return null;
  }
}
