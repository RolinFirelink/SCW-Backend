package com.rolin.orangesmart.exception;

/**
 * 业务异常接口
 */
public interface ErrorEnum {

    /**
     * 錯誤碼
     * 由四部分組成：
     * 1.項目段(2位) = 03
     * 2.模塊段(2位) = 00
     * 3.功能段(2位) = 00
     * 4.編號段(2位) = 初始值00,一直累加
     *
     * @return 錯誤碼(示例 : 03000000)
     */
    String getCode();

    /**
     * 錯誤描述
     *
     * @return 錯誤描述(示例 : 參數錯誤)
     */
    String getMessage();

    /**
     * 直接拋出異常
     *
     * @param args 格式化參數
     */
    default void fail(Object... args) {
        throw new BusinessException(getCode(), getMessage(), args);
    }

    /**
     * 如果obj值為null,則拋出異常
     * 例如: obj == null 拋出異常
     *
     * @param obj  判斷對象
     * @param args 格式化參數
     */
    default void isNull(Object obj, Object... args) {
        if (obj == null) {
            fail(args);
        }
    }

    /**
     * 如果obj值不為null,則拋出異常
     * 例如: obj != null 拋出異常
     *
     * @param obj  判斷對象
     * @param args 格式化參數
     */
    default void notNull(Object obj, Object... args) {
        if (obj != null) {
            fail(args);
        }
    }

    /**
     * 被判斷的字符串為空,則拋出異常
     *
     * @param cs   判斷字符串
     * @param args 格式化參數
     */
    default void isEmpty(CharSequence cs, Object... args) {
        if (cs == null || "".equals(cs)) {
            fail(args);
        }
    }

    /**
     * 被判斷的字符串不為空,則拋出異常
     *
     * @param cs   判斷字符串
     * @param args 格式化參數
     */
    default void notEmpty(CharSequence cs, Object... args) {
        if (cs != null && !"".equals(cs)) {
            fail(args);
        }
    }

    /**
     * 布爾結果為true,則拋出異常
     *
     * @param b    判斷布爾結果
     * @param args 格式化參數
     */
    default void isTrue(Boolean b, Object... args) {
        if (Boolean.TRUE.equals(b)) {
            fail(args);
        }
    }

    /**
     * 布爾結果為false,則拋出異常
     *
     * @param b    判斷布爾結果
     * @param args 格式化參數
     */
    default void isFalse(Boolean b, Object... args) {
        if (Boolean.FALSE.equals(b)) {
            fail(args);
        }
    }

}
