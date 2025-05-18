package com.rolin.orangesmart.mapper.dictionary;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryCategorySearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import org.apache.ibatis.annotations.Param;

public interface DictionaryCategoryMapper {

    DictionaryCategory getById(Long id);

    List<DictionaryCategory> find(@Param("searchBo") DictionaryCategorySearchBo dictionaryCategorySearchBo);
    IPage<DictionaryCategory> find(IPage page, @Param("searchBo")DictionaryCategorySearchBo dictionaryCategorySearchBo);

    DictionaryCategory findWithCategoryCodeAndAppName(@Param("categoryCode") String categoryCode, @Param("appName") String appName);

    void insert(DictionaryCategory dictionaryCategory);

    int update(DictionaryCategory dictionaryCategory);

    int delete(Long id);

    boolean exist(DictionaryCategory dictionaryCategory);

}