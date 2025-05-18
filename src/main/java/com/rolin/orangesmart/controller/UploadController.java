package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.exception.errorEnum.AttachmentErrorEnum;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.attachment.vo.AttachmentVo;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.properties.ImageProperties;
import com.rolin.orangesmart.service.UploadService;
import com.rolin.orangesmart.util.FileIdJasyptUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "附件上传接口")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private ImageProperties imageProperties;

    @Autowired
    private UploadService uploadService;

    @Operation(description = "上传附件至临时目录")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTO<AttachmentVo> upload(
            @Parameter(description = "MultipartFile对象", required = true)
            @RequestPart("file") MultipartFile file) {
        Attachment attachment = uploadService.uploadStagingDir(file, false);
        AttachmentVo attachmentVo = attachment.toVo();
        attachmentVo.setEId(FileIdJasyptUtil.idEncrypt(attachment.getId(), 60 * 60));
        attachmentVo.setId(attachment.getId());
        return ResponseDTO.ok(attachmentVo);
    }

    @Operation(description = "上传图片至临时目录")
    @PostMapping(path="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTO<AttachmentVo> uploadImage(
            @Parameter(description = "MultipartFile对象", required = true)
            @RequestPart("file") MultipartFile file) {
        long uploadMaxSize = imageProperties.getMaxInputSize();
        AttachmentErrorEnum.BASE_ATTACHMENT_IMAGE_EXCESS_ERROR.isTrue(file.getSize() > uploadMaxSize);
        Attachment attachment = uploadService.uploadStagingDir(file, false);
        AttachmentVo attachmentVo = attachment.toVo();
        attachmentVo.setEId(FileIdJasyptUtil.idEncrypt(attachment.getId(), 60 * 60));
        return ResponseDTO.ok(attachmentVo);
    }

    @Operation(description = "上传确认")
    @PostMapping("/submit/{id}")
    public ResponseDTO<AttachmentVo> submit(
            @Parameter(description = "文件id")
            @PathVariable Long id) {
        Attachment attachment = uploadService.submit(id, null, false);
        AttachmentVo attachmentVo = attachment.toVo();
        attachmentVo.setEId(FileIdJasyptUtil.idEncrypt(attachment.getId(), 60 * 60));
        return ResponseDTO.ok(attachmentVo);
    }

    @Operation(description = "清理一个月前无效的附件")
    @PostMapping("/clear")
    public ResponseDTO<Integer> clear() {
        int size = uploadService.clear();
        return ResponseDTO.ok(size);
    }

}