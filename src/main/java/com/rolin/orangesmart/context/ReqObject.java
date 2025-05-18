package com.rolin.orangesmart.context;

import com.rolin.orangesmart.model.user.entity.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Data
public class ReqObject {

    //请求端唯一标识
    private String clientId;

    private User user = new User(-3L, "defaultUser");

    private Map extendDataMap = new HashMap();


    public Object removeExtendDataByKey(Object key) {
        return extendDataMap.remove(key);
    }

    public Object getExtendDataByKey(Object key) {
        return extendDataMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public void setExtendDataByKey(Object key, Object value) {
        extendDataMap.put(key, value);
    }

    public Map getExtendDataMap() {
        return extendDataMap;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
