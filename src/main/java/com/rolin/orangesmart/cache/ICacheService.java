package com.rolin.orangesmart.cache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface ICacheService {

    public static final String KEY_SEPARATOR = ":";

    void set(String key, Object value, final long liveTime, TimeUnit unit);

    Object get(String key);

    boolean hasKey(String key);

    void rename(String oldKey, String newKey);

    void del(String key);

    void delAll(Collection<String> keys);

    void clear();

    long size();
}