package com.rolin.orangesmart.util;

import com.rolin.orangesmart.model.common.po.AbstractPO;

/**
 * Author: Iverson
 * Date: 2025/1/24
 * Time: 11:51
 * 实体工具类
 */
public class EntityUtil {

    /**
     * 设置id为null
     */
    public static <T extends AbstractPO> T setIdNull(T t) {
        t.setId(null);
        return t;
    }

    /**
     * 设置status为1
     */
    public static <T extends AbstractPO> T setStatusActive(T t) {
        t.setStatus(1);
        return t;
    }

    /**
     * 设置ID为Null、status为1
     */
    public static <T extends AbstractPO> T convertToSave(T t) {
        setIdNull(t);
        setStatusActive(t);
        return t;
    }

}