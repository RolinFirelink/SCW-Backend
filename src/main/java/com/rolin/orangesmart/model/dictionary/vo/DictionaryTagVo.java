package com.rolin.orangesmart.model.dictionary.vo;

import java.util.List;

import com.rolin.orangesmart.model.dictionary.entity.DictionaryTag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class DictionaryTagVo {

    private Long id;

    private Long categoryId;

    private String tagCode;

    private Integer seqNo;

    private List<DictionaryTagItemVo> dictionaryTagItemVos;

    public static DictionaryTagVo from(DictionaryTag dictionaryTag) {
        DictionaryTagVo dictionaryTagVo = new DictionaryTagVo();
        BeanUtils.copyProperties(dictionaryTag, dictionaryTagVo);
        return dictionaryTagVo;
    }


}