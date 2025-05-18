package com.rolin.orangesmart.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.rolin.orangesmart.context.ReqEnvContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        Long userId = ReqEnvContext.getUser().getId();
        this.setFieldValByName("createDate", date, metaObject);
        this.setFieldValByName("createdUserId", userId, metaObject);
        this.setFieldValByName("updateDate", date, metaObject);
        this.setFieldValByName("updatedUserId", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date = new Date();
        Long userId = ReqEnvContext.getUser().getId();
        this.setFieldValByName("updateDate", date, metaObject);
        this.setFieldValByName("updatedUserId", userId, metaObject);
    }

}
