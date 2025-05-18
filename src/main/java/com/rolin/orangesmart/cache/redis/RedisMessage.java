package com.rolin.orangesmart.cache.redis;

import com.rolin.orangesmart.context.ReqObject;
import lombok.Getter;
import lombok.Setter;

/**
 * RedisMessage
 */
@Setter
@Getter
public class RedisMessage {

    private ReqObject reqObject;

    private Object message;
}
