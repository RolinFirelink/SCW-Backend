package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum ProblemErrorEnum implements ErrorEnum {

    PROBLEM_NOT_EXIST_ERROR("0000", "问答不存在"),
    PROBLEM_EXIST_ERROR("0001", "问答已存在"),


    ;

    private final String code;

    private final String message;

    private ProblemErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }

}
