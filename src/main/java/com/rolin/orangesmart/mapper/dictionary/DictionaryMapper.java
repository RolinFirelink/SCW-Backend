package com.rolin.orangesmart.mapper.dictionary;

import java.util.List;

import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryVo;
import org.apache.ibatis.annotations.Param;

public interface DictionaryMapper {

    List<DictionaryVo> findByAppNameAndLanuageType(@Param("appName") String appName,
                                                   @Param("languageType") LanguageType languageType);

}