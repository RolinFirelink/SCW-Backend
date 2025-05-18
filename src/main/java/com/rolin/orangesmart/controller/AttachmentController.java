package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.attachment.bo.AttachmentBo;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "附件对象管理器")
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Operation(description = "根据附件id获取附件对象")
    @GetMapping("/{id}")
    public ResponseDTO<Attachment> getById(
            @Parameter(description = "通过文件id")
            @PathVariable Long id) {
        return ResponseDTO.ok(attachmentService.getById(id));
    }

    @Operation(description = "根据附件路径获取附件对象")
    @GetMapping
    public ResponseDTO<Attachment> getByPath(
            @Parameter(description = "通过文件path")
            @RequestParam("path") String path) {
        return ResponseDTO.ok(attachmentService.getByPath(path));
    }

    @PostMapping
    public ResponseDTO<Long> insert(@RequestBody Attachment attachment) {
        attachmentService.create(attachment);
        return ResponseDTO.ok(attachment.getId());
    }

    @PutMapping
    public ResponseDTO<Object> update(@RequestBody @Valid AttachmentBo attachmentBo) {
        attachmentService.update(attachmentBo);
        return ResponseDTO.ok();
    }
}