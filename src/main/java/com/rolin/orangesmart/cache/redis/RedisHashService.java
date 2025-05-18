package com.rolin.orangesmart.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHashService extends RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(RedisHashService.class);


    public RedisHashService(RedisTemplate<String, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    public void putAll(String key, Map<String, Object> value, long time, TimeUnit timeUnit) {
        key = getUkPrfex(key);
        redisTemplate.opsForHash().putAll(key, value);
        redisTemplate.expire(key, time, timeUnit);
    }

    public void put(String key, String field, Object value) {
        key = getUkPrfex(key);
        redisTemplate.opsForHash().put(key, field, value);
    }

    public void put(String key, String field, Object value, long time, TimeUnit timeUnit) {
        key = getUkPrfex(key);
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, time, timeUnit);

    }

    public Object get(String key, String field) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().get(key, field);
    }

    public Long delete(String key, String field) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().delete(key, field);
    }

    public Long delete(String key, Object... fields) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().delete(key, fields);
    }

    public Map<String, Object> gets(String key, List<String> fields) {
        key = getUkPrfex(key);
        List<Object> result = redisTemplate.<String, Object>opsForHash().multiGet(key, fields);
        Map<String, Object> map = new HashMap<>(fields.size());
        int index = 0;
        for (String field : fields) {
            map.put(field, result.get(index++));
        }
        return map;
    }

    public void delete(String key) {
        key = getUkPrfex(key);
        redisTemplate.delete(key);
    }

    public Map<String, Object> getAll(String key) {
        key = getUkPrfex(key);
        Map<Object, Object> values = redisTemplate.opsForHash().entries(key);

        Map<String, Object> map = Maps.newHashMap();
        if (values != null) {
            values.entrySet().stream().forEach(e -> {
                map.put(String.valueOf(e.getKey()), e.getValue());
            });
        }
        return map;
    }

    public long increment(String key, String field, long value) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    public <T> void put(String key, String field, List<T> values) {
        key = getUkPrfex(key);
        try {
            redisTemplate.opsForHash().put(key, field, JsonUtil.toJsonString((values)));
        } catch (JsonProcessingException e) {
            throw new SystemException(e);
        }
    }

    public boolean hasKey(String key, String hasKey) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().hasKey(key, hasKey);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> get(String key, String field, Class<T> obj) {
        key = getUkPrfex(key);
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value != null) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, obj);
            try {
                return (List<T>) mapper.readValue(value.toString(), javaType);
            } catch (IOException e) {
                log.error("redis hash 转换异常 = {}", e.getMessage());
            }
        }
        return new ArrayList<>();
    }


    public Long size(String key) {
        key = getUkPrfex(key);
        return redisTemplate.opsForHash().size(key);
    }

}