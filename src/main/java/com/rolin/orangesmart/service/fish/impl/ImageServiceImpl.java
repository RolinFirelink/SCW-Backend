package com.rolin.orangesmart.service.fish.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rolin.orangesmart.mapper.fish.ImageMapper;
import com.rolin.orangesmart.model.fish.entity.Image;
import com.rolin.orangesmart.service.fish.ImageService;
import org.springframework.stereotype.Service;

/**
 * @author hzy
 * @description 针对表【image(帖子图片表)】的数据库操作Service实现
 * @createDate 2025-01-24 14:32:57
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
		implements ImageService {

}