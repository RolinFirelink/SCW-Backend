package com.rolin.orangesmart.security.service;

import com.rolin.orangesmart.model.user.entity.User;

public interface ILockUserService {

    /**
     * 是否加锁处理
     *
     * @param passFlag 成功登录标志
     * @param user     用户
     */
    public void ifLockUser(boolean passFlag, User user);

    /**
     * Title: 是否加锁处理
     *
     * @param passFlag 成功登录标志
     * @param account  账号
     * @param userType 用户类型
     */
    public void ifLockUser(boolean passFlag, String account, String userType);

    /**
     * Title: 解锁
     *
     * @param account  账号
     * @param userType 用户类型
     */
    public void unLockUser(String account, String userType);

    /**
     * Title: 检查是否锁定
     *
     * @param account  账号
     * @param userType 用户类型
     */
    public boolean checkLockUser(String account, String userType);
}