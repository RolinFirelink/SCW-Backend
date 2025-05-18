package com.rolin.orangesmart.context;


import com.rolin.orangesmart.model.user.entity.User;

/**
 * 请求上下文
 */
public class ReqEnvContext {

    private ReqEnvContext() {

    }

    private static final ThreadLocal<ReqObject> threadLocal = new ThreadLocal<ReqObject>();

    public static User getUser() {
        ReqObject reqObject = threadLocal.get();
        if (reqObject != null) {
            User user = reqObject.getUser();
            return user;
        } else {
            return new User(-2L, "autoUser");
        }
    }

    public static String getClientId() {
        ReqObject reqObject = threadLocal.get();
        if (reqObject != null) {
            String clientId = reqObject.getClientId();
            return clientId;
        } else {
            return null;
        }
    }

    public static void setReqObject(ReqObject reqObject) {
        threadLocal.set(reqObject);
    }

    public static ReqObject getReqObject() {
        ReqObject reqObject = threadLocal.get();
        return reqObject;
    }

    static void clear() {
        threadLocal.remove();
    }

}
