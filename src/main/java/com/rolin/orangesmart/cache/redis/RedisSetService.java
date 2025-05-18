package com.rolin.orangesmart.cache.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisSetService extends RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisSetService(RedisTemplate<String, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    public void add(String key, long time, TimeUnit timeUnit, Object... values) {
        key = getUkPrfex(key);
        redisTemplate.opsForSet().add(key, values);
        redisTemplate.expire(key, time, timeUnit);
    }

    public void add(String key, Set<?> set, long time, TimeUnit timeUnit) {
        this.add(key, time, timeUnit, set.toArray());
    }

    public void remove(String key, Object... values) {
        key = getUkPrfex(key);
        redisTemplate.opsForSet().remove(key, values);
    }

    public void removes(String key, List<?> values) {
        this.remove(key, values.toArray());
    }

    public boolean isMember(String key, Object value) {
        key = getUkPrfex(key);
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Set<Object> members(String key) {
        key = getUkPrfex(key);
        return redisTemplate.opsForSet().members(key);
    }
}