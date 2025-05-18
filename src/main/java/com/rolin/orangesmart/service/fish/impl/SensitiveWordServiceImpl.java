package com.rolin.orangesmart.service.fish.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rolin.orangesmart.mapper.fish.SensitiveWordMapper;
import com.rolin.orangesmart.model.fish.entity.SensitiveWord;
import com.rolin.orangesmart.service.fish.SensitiveWordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【sensitive_word(敏感词表)】的数据库操作Service实现
* @createDate 2025-02-26 13:55:05
*/
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord>
    implements SensitiveWordService {
    @Override
    public void insertBatch(List<SensitiveWord> list) {
        baseMapper.insertBatch(list);
    }
}




