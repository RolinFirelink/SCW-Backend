package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误码规定为：0000000
 *
 */
@Getter
@AllArgsConstructor
public enum SecurityErrorEnum implements ErrorEnum {

    USER_NO_LOGIN_ERROR("00010001", "用戶沒有登入"),
    USER_ACCOUNT_OR_PASSWORD_ERROR("00010002", "用戶名或密碼錯誤"),
    USER_NO_PERMISSION_ERROR("00010003", "用戶無此權限請聯係管理員"),
    USER_NO_ROLE_ERROR("00010004", "用戶未設置權限,請聯係管理員"),
    USER_DISABLED_ERROR("00010005", "用戶已停用或未审核,請聯係管理員"),
    USER_LOCKED_ERROR("00010006", "用戶已鎖定,請{%s}後再試"),
    USER_PARAM_ERROR("00010007", "參數不能為空"),
    USER_PASSWORD_MODIFY_ERROR("00010008", "用戶密碼需要重置"),
    USER_CAPTCHA_ERROR("00010009", "驗證碼不正確"),
    USER_NOTIFY_LOCKED_ERROR("00010010", "用戶名或密碼錯誤,您還有{%s}次嘗試機會"),
    TOKEN_ANALYSIS_ERROR("00010011", "用戶會話無效,請重新登入"),
    TOKEN_EXPIRED_ERROR("00010012", "登入超時,請重新登入"),
    USER_EXPIRED_ERROR("00010021", "當前用戶已在其他地方登入"),
    USER_TYPE_NULL_ERROR("00010021", "用戶類型不能為空"),
    CLIENT_EXPIRED_ERROR("00010041", "会话超时"),
    USER_TYPE_ERROR("00010042", "实际用户与预期登录用户不一致"),
    ;

    private final String code;

    private final String message;

}