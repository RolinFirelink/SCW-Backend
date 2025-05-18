package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.mapper.WeatherMapper;
import com.rolin.orangesmart.model.crawler.entity.Weather;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import org.springframework.stereotype.Service;

/**
 * Author: Rolin
 * Date: 2025/2/23
 * Time: 22:54
 */
@Service
public class WeatherService extends BaseService<WeatherMapper, Weather> {

  public LambdaQueryWrapper<Weather> getLambdaQueryWrapper() {
    return QueryWrapperUtil.getWrapper(Weather.class);
  }

}
