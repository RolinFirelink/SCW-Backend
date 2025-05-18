package com.rolin.orangesmart.service;

import com.google.common.collect.Lists;
import com.rolin.orangesmart.cache.ICacheService;
import com.rolin.orangesmart.cache.redis.RedisHashService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.security.properties.LoginProperties;
import com.rolin.orangesmart.security.service.IApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 存储系统所用用户会话
 */
@Service
public class DefaultApplicationUserService implements IApplicationUserService {

    @Autowired
    private RedisHashService redisHashService;

    @Autowired
    private LoginProperties loginProperties;

    @Autowired
    private ICacheService cacheService;

    public void add(String account, String userType, String usernameKey) {
        List<String> usernameKeys = this.get(account, userType);
        // 不允許同時登錄則踢出同名用戶
        if (!loginProperties.isEnableMultiLogin()) {
            for (Iterator<String> iterator = usernameKeys.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                User user = (User) cacheService.get(key);
                if (user != null) {
                    user.setKickFlag(true);
                    cacheService.set(key, user, 30, TimeUnit.MINUTES);
                }
            }
        } else {
            // 是否超出同一账号最大登录量
            while (usernameKeys.size() > loginProperties.getMaxMultiLoginCount()) {
                String key = usernameKeys.remove(0);
                User user = (User) cacheService.get(key);
                if (user != null) {
                    user.setKickFlag(true);
                    cacheService.set(key, user, 30, TimeUnit.MINUTES);
                }
            }
        }

        //加入
        usernameKeys.add(usernameKey);
        String accountKey = this.getRedisKey(account, userType);
        redisHashService.put(CoreConstant.CACHE_ALL_ACCOUNT, accountKey, usernameKeys);
    }

    public List<String> get(String account, String userType) {
        String accountKey = this.getRedisKey(account, userType);
        List<String> usernameKeys = (List<String>) redisHashService.get(CoreConstant.CACHE_ALL_ACCOUNT, accountKey, String.class);
        if (usernameKeys == null) {
            usernameKeys = Lists.newArrayList();
        }
        return usernameKeys;
    }

    /**
     * @param account
     * @param userType
     * @Title: delete
     * @Description:
     * @author: sunys
     */
    public void delete(String account, String userType) {
        List<String> usernameKeys = this.get(account, userType);
        if (usernameKeys.isEmpty()) {
            return;
        }
        //清除会话
        for (String usernameKey : usernameKeys) {
            cacheService.del(usernameKey);
        }
        //删除
        String accountKey = this.getRedisKey(account, userType);
        redisHashService.delete(CoreConstant.CACHE_ALL_ACCOUNT, accountKey);
    }

    private String getRedisKey(String account, String userType) {
        String accountKey = CoreConstant.CACHE_SAME_ACCOUNT_PREFIX
                + userType + CoreConstant.CACHE_KEY_SEPARATOR + account;
        return accountKey;
    }
}
