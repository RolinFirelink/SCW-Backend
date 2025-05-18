package com.rolin.orangesmart.model.attachment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentVo {

    /**
     * 附件id
     */
    @Schema(description = "附件id")
    private Long id;

    @Schema(description = "加密后附件id")
    private String eId;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 路径名
     */
    @Schema(description = "路径名")
    private String pathName;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;


}
