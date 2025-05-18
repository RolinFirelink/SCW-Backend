package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum ExpertErrorEnum implements ErrorEnum {

    EXPERT_NOT_EXIST_ERROR("0000", "专家不存在"),
    EXPERT_EXIST_ERROR("0001", "专家已存在"),


    ;

    private final String code;

    private final String message;

    private ExpertErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }

}
