package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum UserErrorEnum implements ErrorEnum {

    USER_NOT_EXIST_ERROR("0000", "用戶不存在"),
    USER_EXIST_ERROR("0001", "用戶已存在"),

    USER_INVALID_ACCOUNT_ERROR("0002", "用戶賬號由6~30位字母、數字組成，不允許空格"),
    USER_INVALID_PASSWORD_ERROR("0003", "用戶密碼由{%s}~{%s}位字母、數字組成，不允許空格"),
    USER_INVALID_USERNAME_ERROR("0004", "用戶名稱由6~30位字符組成"),
    USER_INVALID_MOBILE_ERROR("0005", "電話號碼無效"),
    USER_INVALID_EMAIL_ERROR("0006", "郵箱無效"),

    USER_NO_PERMISSION_ERROR("0007", "此用戶無權限"),
    USER_SAME_PASSWORD_ERROR("0008", "新舊密碼不能相同"),
    USER_INPUT_OLDPASSWORD_ERROR("0009", "舊密碼輸入錯誤"),
    USER_INPUT_WRONGREPASSWORD_ERROR("0010", "請重新確認密碼"),

    USER_ENABLED_ERROR("0011", "用戶已啟用"),
    USER_DISABLED_ERROR("0012", "用戶已停用"),

    USER_DELETE_ERROR("0013", "該用戶不允許刪除"),
    USER_ACTIVE_DELETE_ERROR("0014", "用戶已激活,不可刪除"),

    USER_MOBILE_CAPTCHA_ERROR("0015", "驗證碼不正確"),
    USER_ACCOUNT_EXIST_ERROR("0016", "賬戶已存在"),
    USER_MOBILE_EXIST_ERROR("0017", "電話號碼已存在"),
    USER_EMAIL_EXIST_ERROR("0018", "郵箱已存在"),
    USER_INVALID_PASSWORD_REGEX_ERROR("0019", "密碼需要符合正則表達式{%s}"),
    USER_NO_SET_PASSWORD_ERROR("0020", "未分配用戶密碼"),
    USER_ROOT_CHANGE_ACTIVE_ERROR("0021", "root用戶不能禁用"),

    USER_SAME_HISTORY_PASSWORD_ERROR("0023", "新密碼不能是近期使用過的密碼"),

    USER_MOBILE_CAPTCHA_EXPIRE_ERROR("0101", "手机验证码超时"),
    USER_MOBILE_CAPTCHA_CHECK_ERROR("0102", "手机验证码错误"),
    USER_MOBILE_UUID_EXPIRE_ERROR("0105", "会话超时"),
    USER_MOBILE_UUID_CHECK_ERROR("0106", "会话错误"),
    USER_PASSWORD_PUBLISHKEY_PARAMETER_ERROR("0107", "公钥参数错误"),
    USER_PASSWORD_PUBLISHKEY_EXPIRED_ERROR("0108", "公钥验证错误"),
    ;

    private final String code;

    private final String message;

    private UserErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }

}
