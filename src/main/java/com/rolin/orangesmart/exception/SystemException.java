package com.rolin.orangesmart.exception;

import org.springframework.util.StringUtils;

/**
 * 系统异常
 */
public final class SystemException extends RuntimeException {

    public static final String SYSTEM_ERROR_CODE = "00000000";
    public static final String SYSTEM_ERROR_MESSAGE = "系统错误";

    private static final long serialVersionUID = -5489892850537945344L;

    private final String code;

    private final String message;

    public SystemException(Throwable throwable) {
        super(throwable);
        this.code = SYSTEM_ERROR_CODE;
        this.message = throwable.getMessage();
    }

    public SystemException(String message) {
        super();
        this.code = SYSTEM_ERROR_CODE;
        this.message = message;
    }

    public SystemException(String message, Object... args) {
        this.code = SYSTEM_ERROR_CODE;
        if (StringUtils.hasText(message)) {
            this.message = String.format(message, args);
        } else {
            this.message = null;
        }
    }

    public SystemException(String message, Throwable throwable, Object... args) {
        super(throwable);
        this.code = SYSTEM_ERROR_CODE;
        if (StringUtils.hasText(message)) {
            this.message = String.format(message, args);
        } else {
            this.message = null;
        }
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
