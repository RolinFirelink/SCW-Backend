package com.rolin.orangesmart.model.attachment.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Storage {

    @NotBlank(message = "路径名称不能為空")
    public String pathName;

    @NotBlank(message = "文件名称不能為空")
    public String fileName;

    public Long fileSize;

}