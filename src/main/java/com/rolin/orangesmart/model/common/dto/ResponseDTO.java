package com.rolin.orangesmart.model.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseDTO<T> {

    private T data;
    private String code;
    // 成功相应时，不需要提供此属性
    private String message;

    private static String SUCCESS_CODE = "200";
    private static String DEFAULT_FAILURE_CODE = "000";


    private ResponseDTO() {
        this(null, SUCCESS_CODE, null);
    }

    private ResponseDTO(T t) {
        this(t, SUCCESS_CODE, null);
    }

    private ResponseDTO(String message) {
        this(null, DEFAULT_FAILURE_CODE, message);
    }

    private ResponseDTO(String code, String message) {
        this(null, code, message);
    }

    private ResponseDTO(T t, String code, String message) {
        super();
        this.data = t;
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseDTO<T> ok(T t) {
        ResponseDTO<T> toResponseEntity = new ResponseDTO<T>(t);
        return toResponseEntity;
    }

    public static <T> ResponseDTO<T> ok() {
        ResponseDTO<T> toResponseEntity = new ResponseDTO<T>();
        return toResponseEntity;
    }

    public static <T> ResponseDTO<T> no(String message) {
        ResponseDTO<T> toResponseEntity = new ResponseDTO<T>(message);
        return toResponseEntity;
    }

    public static <T> ResponseDTO<T> no(String code, String message) {
        ResponseDTO<T> toResponseEntity = new ResponseDTO<T>(code, message);
        return toResponseEntity;
    }

//  public static <T> ResponseDTO<T> systemError(){
//    ResponseDTO<T> toResponseEntity = new ResponseDTO<T>(SYSTEM_ERROR_CODE, SYSTEM_ERROR_MESSAGE);
//    return toResponseEntity;
//  }

    public T getData() {
        return data;
    }

    public void setData(T t) {
        this.data = t;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public boolean isOk() {
        return SUCCESS_CODE.equals(this.getCode());
    }

}
