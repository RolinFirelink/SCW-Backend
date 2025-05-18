package com.rolin.orangesmart.cache.redis;

import com.rolin.orangesmart.context.ReqEnvContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis生产者服务
 */
@SuppressWarnings("rawtypes")
public class RedisProducerService {

    @Autowired
    protected RedisConfig redisConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    public void send(String topic, Object message) {
        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setReqObject(ReqEnvContext.getReqObject());
        redisMessage.setMessage(message);
        redisTemplate.convertAndSend(redisConfig.getUkPrfex(topic), redisMessage);
    }

}