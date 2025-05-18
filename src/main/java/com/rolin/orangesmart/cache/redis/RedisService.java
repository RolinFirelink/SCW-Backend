package com.rolin.orangesmart.cache.redis;

import jakarta.annotation.Resource;

public class RedisService {

    @Resource
    private RedisConfig redisConfig;

    protected String getUkPrfex(String key) {
        return redisConfig.getUkPrfex(key);
    }

}