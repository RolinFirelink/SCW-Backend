package com.rolin.orangesmart.mapper.dictionary;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryTagSearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTag;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import org.apache.ibatis.annotations.Param;


public interface DictionaryTagMapper {

    List<DictionaryTagVo> findByCategoryId(@Param("categoryId")Long categoryId);

    List<DictionaryTagVo> find(@Param("searchBo") DictionaryTagSearchBo dictionaryTagSearchBo);

    IPage<DictionaryTagVo> find(IPage page, @Param("searchBo")DictionaryTagSearchBo dictionaryTagSearchBo);

    DictionaryTag findById(Long id);

    DictionaryTag findByCategoryIdAndTagCode(@Param("categoryId") Long categoryId, @Param("tagCode") String tagCode);

    int insert(DictionaryTag dictionaryItem);

    int update(DictionaryTag dictionaryItem);

    int delete(Long id);

    int deleteByCategoryId(Long categoryId);

    boolean exist(DictionaryTag dictionaryItem);


}