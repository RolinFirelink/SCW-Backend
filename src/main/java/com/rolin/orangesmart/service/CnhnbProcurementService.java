package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.mapper.CnhnbProcurementMapper;
import com.rolin.orangesmart.model.crawler.entity.CnhnbProcurement;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.CheckUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 17:33
 */
@Service
public class CnhnbProcurementService extends BaseService<CnhnbProcurementMapper, CnhnbProcurement> {

  public LambdaQueryWrapper<CnhnbProcurement> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(CnhnbProcurement.class);
  }

  public void batchSaveOrUpdate(List<CnhnbProcurement> cnhnbProcurements) {
    for (CnhnbProcurement cnhnbProcurement : cnhnbProcurements) {
      LambdaQueryWrapper<CnhnbProcurement> queryWrapper = getLambdaQueryWrapper();
      queryWrapper.eq(CnhnbProcurement::getName, cnhnbProcurement.getName());
      queryWrapper.eq(CnhnbProcurement::getAmount, cnhnbProcurement.getAmount());
      queryWrapper.eq(CnhnbProcurement::getAddress, cnhnbProcurement.getAddress());
      queryWrapper.eq(CnhnbProcurement::getPurchaser, cnhnbProcurement.getPurchaser());
      queryWrapper.eq(CnhnbProcurement::getUpdateTime, cnhnbProcurement.getUpdateTime());
      List<CnhnbProcurement> list = list(queryWrapper);
      if(CheckUtil.isEmpty(list)){
        save(cnhnbProcurement);
      }else {
        CnhnbProcurement cnhnbProcurement1 = list.get(0);
        cnhnbProcurement1.setLevel(cnhnbProcurement.getLevel());
        cnhnbProcurement1.setDetailUrl(cnhnbProcurement.getDetailUrl());
        updateById(cnhnbProcurement1);
      }
    }
  }
}
