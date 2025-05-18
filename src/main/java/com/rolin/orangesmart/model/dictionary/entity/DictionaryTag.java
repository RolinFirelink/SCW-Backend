package com.rolin.orangesmart.model.dictionary.entity;

import com.rolin.orangesmart.model.common.po.AbstractPO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "字典项")
public class DictionaryTag extends AbstractPO {

    /**
     * 字典类别Id
     */
    @Schema(description = "字典类别Id")
    @NotBlank(message = "字典类别Id不能為空")
    private Long categoryId;

    /**
     * 字典项编号
     */
    @Schema(description = "字典项编号")
    @NotBlank(message = "字典項編號不能為空")
    private String tagCode;

    /**
     * 字典项优先级
     */
    @Schema(description = "字典项优先级")
    private Integer seqNo;

}