package com.rolin.orangesmart.model.attachment.bo;

import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttachmentBo {

    @Schema(description = "附件id")
    @NotNull(message = "附件id不能为空")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "路径名")
    private String pathName;

    public Attachment toEntity() {
        return BeanUtil.copyProperties(this, Attachment.class);
    }
}