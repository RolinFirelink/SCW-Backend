package com.rolin.orangesmart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rolin.orangesmart.model.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    public List<User> getAll();

}
