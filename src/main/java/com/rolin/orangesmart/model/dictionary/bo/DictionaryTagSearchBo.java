package com.rolin.orangesmart.model.dictionary.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DictionaryTagSearchBo {

    @NotBlank(message = "字典类别Id")
    @Schema(description = "字典类别Id", required = true)
    private Long categoryId;

    @Schema(description = "查询内容", required = false)
    private String searchWord;
}