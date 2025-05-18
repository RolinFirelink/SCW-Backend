package com.rolin.orangesmart.security.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rolin.orangesmart.cache.redis.RedisCacheService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.exception.errorEnum.SecurityErrorEnum;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.security.properties.LoginProperties;
import com.rolin.orangesmart.security.service.ILockUserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class DefaultLockUserService implements ILockUserService {

    private static final String LOCK_LOGIN = "lockLogin:";

    @Autowired
    private LoginProperties loginProperties;

    @Autowired(required = false)
    private RedisCacheService redisCacheService;


    /**
     * 是否加锁处理
     *
     * @param passFlag 成功登录标志
     * @param user     用户
     */
    @Override
    public void ifLockUser(boolean passFlag, User user) {
        this.ifLockUser(passFlag, user.getAccount(), user.getUserType().toString());
    }

    /**
     * Title: 是否加锁处理
     *
     * @param passFlag 成功登录标志
     * @param account  账号
     * @param userType 用户类型
     */
    @Override
    public void ifLockUser(boolean passFlag, String account, String userType) {
        if (loginProperties.isEnableLockLogin()) {
            Integer errorDuringTime = loginProperties.getErrorDuringTime();
            String lockKey = getRedisKey(account, userType);
            LockUser lockUser = (LockUser) redisCacheService.get(lockKey);
            if (passFlag) {
                //验证成功
                if (lockUser != null) {
                    if (LockUser.isLock(lockUser)) {
                        //已锁，解除时间未到
                        SecurityErrorEnum.USER_LOCKED_ERROR.fail(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lockUser.getLockedToDate()));
                    } else {
                        //未锁，但有失败记录，需清除
                        redisCacheService.del(lockKey);
                    }
                } else {
                    //无需任何处理
                }
            } else {
                //验证失败
                if (lockUser != null) {
                    if (LockUser.isLock(lockUser)) {
                        //已锁，解除时间未到
                        SecurityErrorEnum.USER_LOCKED_ERROR.fail(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lockUser.getLockedToDate()));
                    }
                } else {
                    lockUser = new LockUser();
                }
                //往次失败处理（过期的要删除）
                List<Date> failureDates = lockUser.getFailureDates();
                for (Iterator<Date> iterator = failureDates.iterator(); iterator.hasNext(); ) {
                    Date failureDate = iterator.next();
                    if ((System.currentTimeMillis() - failureDate.getTime()) / 1000 > errorDuringTime) {//秒比较
                        //过期
                        iterator.remove();
                    }
                }
                //本次失败添加
                failureDates.add(new Date());

                if (failureDates.size() >= loginProperties.getErrorCount()) {
                    lock(lockUser, lockKey, errorDuringTime);
                    SecurityErrorEnum.USER_LOCKED_ERROR.fail(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lockUser.getLockedToDate()));
                } else {
                    redisCacheService.set(lockKey, lockUser, errorDuringTime, TimeUnit.SECONDS);
                }
            }
        }
    }

    @Override
    public void unLockUser(String account, String userType) {
        String lockKey = getRedisKey(account, userType);
        redisCacheService.del(lockKey);
    }

    @Override
    public boolean checkLockUser(String account, String userType) {
        String lockKey = getRedisKey(account, userType);
        LockUser lockUser = (LockUser) redisCacheService.get(lockKey);
        return LockUser.isLock(lockUser);
    }

    private String getRedisKey(String account, String userType) {
        String lockKey = LOCK_LOGIN
                + userType + CoreConstant.CACHE_KEY_SEPARATOR + account;
        return lockKey;
    }

    private void lock(LockUser lockUser, String lockKey, Integer errorDuringTime) {
        lockUser.setLockedToDate(new Date(new Date().getTime() + loginProperties.getLockTime() * 1000));//毫秒
        //使用errorDuringTime时间，不使用loginProperties.getLockTime() 作为redis的存活时间，以便留存failureDates数据
        redisCacheService.set(lockKey, lockUser, errorDuringTime, TimeUnit.SECONDS);
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class LockUser {
        //错误验证时间
        private List<Date> failureDates = Lists.newArrayList();

        //锁定时间c
        private Date lockedToDate;

        private Map<String, Object> map = Maps.newHashMap();

        @JsonIgnore
        public static boolean isLock(LockUser lockUser) {
            if (lockUser != null) {
                Date lockedToDate = lockUser.getLockedToDate();
                if (lockedToDate != null && lockedToDate.compareTo(new Date()) == 1) {
                    //已锁，解除时间未到
                    return true;
                }
            }
            return false;
        }
    }
}