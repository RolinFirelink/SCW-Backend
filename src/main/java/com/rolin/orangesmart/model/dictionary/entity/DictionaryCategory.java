package com.rolin.orangesmart.model.dictionary.entity;

import com.rolin.orangesmart.model.common.po.AbstractPO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "字典类别")
public class DictionaryCategory extends AbstractPO {

    /**
     * 字典类别编号
     */
    @Schema(description = "字典类别编号")
    @NotBlank(message = "字典類別編號不能為空")
    private String categoryCode;

    /**
     * 字典类别中文简体名称
     */
    @Schema(description = "字典类别名称")
    @NotBlank(message = "字典類別名稱不能為空")
    private String categoryName;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    @NotBlank(message = "appName不能為空")
    private String appName;

}
