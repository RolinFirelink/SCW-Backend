package com.rolin.orangesmart.mapper.dictionary;

import java.util.Collection;
import java.util.List;

import com.rolin.orangesmart.model.dictionary.entity.DictionaryTagItem;
import org.apache.ibatis.annotations.Param;


public interface DictionaryTagItemMapper {

    List<DictionaryTagItem> findByTagIds(@Param("tagIds") Collection<Long> tagIds);

    DictionaryTagItem findById(@Param("id") Long id);

    DictionaryTagItem findByTagIdAndLanguageType(@Param("tagId") Long tagId,
                                                 @Param("languageType") String languageType);

    int insert(DictionaryTagItem dictionaryTagItem);

    int update(DictionaryTagItem dictionaryTagItem);

    int delete(@Param("id") Long id);

    int deleteByCategoryId(@Param("categoryId") Long categoryId);

    int deleteByTagId(@Param("tagId") Long tagId);

    boolean exist(DictionaryTagItem dictionaryItemName);


}