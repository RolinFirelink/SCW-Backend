package com.rolin.orangesmart.service.fish;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rolin.orangesmart.model.fish.entity.SensitiveWord;

import java.util.List;

/**
* @author hzzzzzy
* @description 针对表【sensitive_word(敏感词表)】的数据库操作Service
* @createDate 2025-02-26 13:55:05
*/
public interface SensitiveWordService extends IService<SensitiveWord> {

    void insertBatch(List<SensitiveWord> list);
}
