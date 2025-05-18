package com.rolin.orangesmart.exception;

public interface ResultCode {
    /**
     * 获取结果状态编号
     *
     * @return 结果状态编号
     */
    default Integer getCode() {
        return 2000;
    }

    /**
     * 获取结果状态简短描述信息
     *
     * @return 状态描述
     */
    default String getDesc() {
        return "success";
    }
}