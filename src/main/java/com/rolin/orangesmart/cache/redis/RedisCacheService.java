package com.rolin.orangesmart.cache.redis;

import com.rolin.orangesmart.cache.ICacheService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultedRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * RedisTemplate 封装类 提供常用操作redis方法
 */
@Component
public class RedisCacheService extends RedisService implements ICacheService {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        return valueops.get(getUkPrfex(key));
    }

    @Override
    public void set(String key, Object value, final long liveTime, TimeUnit unit) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.set(getUkPrfex(key), value, liveTime, unit);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(getUkPrfex(key));
    }

    @Override
    public void delAll(Collection<String> keys) {
        Collection<String> primaryKeys = keys.stream().map(
                k -> getUkPrfex(k)).collect(Collectors.toList());
        redisTemplate.delete(primaryKeys);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(getUkPrfex(key));
    }

    @Override
    public void rename(String oldKey, String newKey) {
        oldKey = getUkPrfex(oldKey);
        newKey = getUkPrfex(newKey);
        redisTemplate.rename(oldKey, newKey);
    }

    @Override
    public long size() {
        return (long) redisTemplate.execute((RedisCallback<Object>) DefaultedRedisConnection::dbSize);
    }

    @Override
    public void clear() {
    }

    public boolean expireAt(String key, Date endDate) {
        return redisTemplate.expireAt(getUkPrfex(key), endDate);
    }

    public Set<String> getAllKeys() {
        return null;
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(getUkPrfex(key));
    }

    public Long getExpire(String key, final TimeUnit timeUnit) {
        return redisTemplate.getExpire(getUkPrfex(key), timeUnit);
    }

    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(getUkPrfex(key), 1l);
    }

    public Long increment(String key, int time, TimeUnit unit) {
        key = getUkPrfex(key);
        Long value = redisTemplate.opsForValue().increment(key, 1l);
        redisTemplate.expire(key, time, unit);
        return value;
    }

    public boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(getUkPrfex(key), value);
    }

    public boolean setIfAbsent(String key, Object value, boolean mustFlag, int time, TimeUnit unit) {
        key = getUkPrfex(key);
        boolean absentFlag = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (absentFlag || mustFlag) {
            redisTemplate.expire(key, time, unit);
        }
        return absentFlag;
    }

    public boolean expire(String key, int time, TimeUnit unit) {
        return redisTemplate.expire(getUkPrfex(key), time, unit);
    }

    public boolean release(String key) {
        return redisTemplate.opsForValue().getOperations().delete(getUkPrfex(key));
    }

}