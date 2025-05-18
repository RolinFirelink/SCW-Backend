package com.rolin.orangesmart.cache;

import com.rolin.orangesmart.cache.redis.RedisCacheService;
import com.rolin.orangesmart.cache.redis.RedisProducerService;
import com.rolin.orangesmart.constant.CacheDataConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis消息功能清除缓存
 * 消息发送方，参考CacheDataRedisConsumerService
 */
@Service
public class CacheDataRedisProducerService {

    @Autowired(required = false)
    private RedisProducerService redisProducerService;

    @Autowired(required = false)
    private RedisCacheService redisCacheService;


    public void send(String... prefixKeys) {
        if(redisProducerService != null) {
            for(String prefixKey : prefixKeys) {
                redisProducerService.send(CacheDataConstant.CACHA_DATA_CLEAR_TOPIC, prefixKey);
            }

            //生成新缓存版本信息
            String cacheDataVersion = System.currentTimeMillis() + "";
            redisCacheService.set(CacheDataConstant.CACHA_DATA_VERSION, cacheDataVersion, 100, TimeUnit.DAYS);
        }
    }

}