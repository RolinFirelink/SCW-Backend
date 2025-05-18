package com.rolin.orangesmart.model.dictionary.vo;

import com.rolin.orangesmart.model.dictionary.entity.DictionaryTagItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@Schema(description = "字典项名称")
public class DictionaryTagItemVo {

    private Long id;

    private String languageType;

    private String itemName;

    private String itemName2;

    public static DictionaryTagItemVo from(DictionaryTagItem dictionaryTagItem) {
        if (dictionaryTagItem == null) {
            return null;
        }
        DictionaryTagItemVo dictionaryTagItemVo = new DictionaryTagItemVo();
        BeanUtils.copyProperties(dictionaryTagItem, dictionaryTagItemVo);
        dictionaryTagItemVo.setLanguageType(dictionaryTagItem.getLanguageType().getCode());
        return dictionaryTagItemVo;
    }
}
