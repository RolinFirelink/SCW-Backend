package com.rolin.orangesmart.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rolin.orangesmart.model.common.po.AbstractPO;

public class QueryWrapperUtil {

    public static <T extends AbstractPO> LambdaQueryWrapper<T> getWrapper(Class<T> clazz) {
        LambdaQueryWrapper<T> wrapper = Wrappers.lambdaQuery(clazz);
        wrapper.eq(AbstractPO::getStatus, 1);
        return wrapper;
    }

    public static <T extends AbstractPO> LambdaQueryWrapper<T> getWrapper(T t) {
        LambdaQueryWrapper<T> wrapper = Wrappers.lambdaQuery(t);
        wrapper.eq(AbstractPO::getStatus, 1);
        return wrapper;
    }
}
