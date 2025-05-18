package com.rolin.orangesmart.exception;

/**
 * 全局业务异常
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 7601426179537049976L;

    public static final String BUSINESS_ERROR_CODE = "00000001";

    public static final String BUSINESS_ERROR_MESSAGE = "业务异常";

    private final String code;

    private final String message;

    private final transient Object[] args;

    public BusinessException(String message, Object... args) {
        this(BUSINESS_ERROR_CODE, message, null, args);
    }

    public BusinessException(String code, String message, Object... args) {
        this(code, message, null, args);
    }

    public BusinessException(String code, String message, Throwable throwable, Object... args) {
        super(throwable);
        this.code = code;
        this.message = String.format(message, args);
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

}
