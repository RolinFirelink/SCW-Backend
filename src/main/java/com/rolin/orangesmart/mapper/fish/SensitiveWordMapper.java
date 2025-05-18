package com.rolin.orangesmart.mapper.fish;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rolin.orangesmart.model.fish.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

    void insertBatch(List<SensitiveWord> list);
}




