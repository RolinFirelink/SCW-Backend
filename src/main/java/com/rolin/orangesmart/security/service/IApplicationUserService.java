package com.rolin.orangesmart.security.service;

import java.util.List;

public interface IApplicationUserService {

    public void add(String account, String userType, String usernameKey);

    public List<String> get(String account, String userType);

    /**
     * @param account
     * @param userType
     * @Title: delete
     * @Description: 清除会话
     * @author: sunys
     */
    public void delete(String account, String userType);
}