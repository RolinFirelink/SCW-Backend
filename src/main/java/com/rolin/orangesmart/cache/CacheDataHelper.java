package com.rolin.orangesmart.cache;

import com.rolin.orangesmart.cache.redis.RedisSetService;
import com.rolin.orangesmart.constant.CacheDataConstant;
import com.rolin.orangesmart.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class CacheDataHelper {

    @Autowired
    private ICacheService cacheService;

    @Autowired
    private RedisSetService redisSetService;

    public void setCacheData(String key, Object value){
        redisSetService.add(CacheDataConstant.CACHA_DATA_SET, 100, TimeUnit.DAYS, key);
        cacheService.set(key, value, RandomUtil.obtainRandomNumber(12), TimeUnit.HOURS);
    }
    public void clearCacheData(String prefixKey) {
        Set<Object> keyObjs = redisSetService.members(CacheDataConstant.CACHA_DATA_SET);
        for(Object keyObj : keyObjs) {
            String key = (String)keyObj;
            if(key.startsWith(prefixKey)) {
                log.info("清除缓存：{} {}", prefixKey, key);
                cacheService.del(key);
            }
        }
    }

}