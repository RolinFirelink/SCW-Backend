package com.rolin.orangesmart.cache;

import java.util.concurrent.TimeUnit;

public interface ISessionService {

    void del(String key);

    void set(String key, Object value, long i, TimeUnit seconds);

    Object get(String key);

}
