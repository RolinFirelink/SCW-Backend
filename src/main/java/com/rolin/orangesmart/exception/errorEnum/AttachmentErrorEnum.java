package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum AttachmentErrorEnum implements ErrorEnum {

    BASE_ATTACHMENT_IMAGE_EXCESS_ERROR("0001", "Image文件大小超過限制"),
    BASE_ATTACHMENT_NO_ATTACHMENT_ERROR("0002", "附件不存在"),
    BASE_ATTACHMENT_NULL_FTP_POOL_OBJ_ERROR("0003", "FTP線程池取出對象為空"),
    BASE_ATTACHMENT_UPLOAD_TYPE_ERROR("0004", "上傳類型不支持"),

    BASE_ATTACHMENT_FILE_NAME_MAX_LENGTH_ERROR("0005", "上傳文件名過長"),

    BASE_ATTACHMENT_FILE_NAME_REGEXP_ERROR("0006", "上傳文件名不符合正则表达式要求， ^([a-z0-9_\\-\\u4e00-\\u9fa5]+)(\\.)([a-z]{2,5})$"),
    ;

    private final String code;

    private final String message;

    private AttachmentErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }
}
