package com.rolin.orangesmart.model.dictionary.vo;

import com.rolin.orangesmart.model.common.po.AbstractPO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DictionaryWithTagsVO extends AbstractPO {
    /**
     * 字典类别编号
     */
    @Schema(description = "字典类别编号")
    private String categoryCode;

    /**
     * 字典类别中文简体名称
     */
    @Schema(description = "字典类别名称")
    private String categoryName;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String appName;

    private List<DictionaryTagVo> dictionaryTagVos;

}