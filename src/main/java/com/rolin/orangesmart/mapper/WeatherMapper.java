package com.rolin.orangesmart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rolin.orangesmart.model.crawler.entity.Weather;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeatherMapper extends BaseMapper<Weather> {

}
