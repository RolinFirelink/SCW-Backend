package com.rolin.orangesmart.service.fish.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rolin.orangesmart.mapper.fish.PostMapper;
import com.rolin.orangesmart.model.fish.entity.Post;
import com.rolin.orangesmart.service.fish.PostService;
import org.springframework.stereotype.Service;

/**
* @author hzzzzzy
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-01-11 22:07:11
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}




