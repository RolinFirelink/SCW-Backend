package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum CarouselErrorEnum implements ErrorEnum {

    CAROUSEL_NOT_EXIST_ERROR("0000", "轮播图不存在"),
    CAROUSEL_EXIST_ERROR("0001", "轮播图已存在"),


    ;

    private final String code;

    private final String message;

    private CarouselErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }

}
