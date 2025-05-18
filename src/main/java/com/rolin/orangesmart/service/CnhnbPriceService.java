package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.mapper.CnhnbPriceMapper;
import com.rolin.orangesmart.model.crawler.entity.CnhnbPrice;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import org.springframework.stereotype.Service;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 18:36
 */
@Service
public class CnhnbPriceService extends BaseService<CnhnbPriceMapper, CnhnbPrice> {

  public LambdaQueryWrapper<CnhnbPrice> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(CnhnbPrice.class);
  }

}
