package com.rolin.orangesmart.model.attachment.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.attachment.vo.AttachmentVo;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
@Schema(description = "附件")
@TableName(value = "attachment")
@KeySequence(value = "S_BASE_ATTACHMENT")
public class Attachment extends AbstractPO {

    /**
     * 路径名
     */
    @Schema(description = "路径名")
    private String pathName;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

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

    @Schema(description = "过期日期")
    private Date expireDate;

    /**
     * false 一个月后（当前日期与updateDate比较），还未确认，则删除文件
     */
    @Schema(description = "false 一个月后（当前日期与updateDate比较），还未确认，则删除文件")
    private boolean confirmationFlag = false;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String appName;

    public AttachmentVo toVo() {
        AttachmentVo attachmentVo = new AttachmentVo();
        BeanUtils.copyProperties(this, AttachmentVo.class);
        attachmentVo.setPathName(null);
        return attachmentVo;
    }

}