package com.rolin.orangesmart.model.dictionary.entity;

import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "字典项名称")
public class DictionaryTagItem extends AbstractPO {

    /**
     * 字典类别Id
     */
    @Schema(description = "字典类别Id")
    @NotBlank(message = "字典类别ID不能為空")
    private Long categoryId;

    /**
     * 字典项Id
     */
    @Schema(description = "字典项Id")
    @NotBlank(message = "字典项ID不能為空")
    private Long tagId;

    private LanguageType languageType;

    private String itemName;

    private String itemName2;

}