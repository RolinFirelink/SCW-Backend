package com.rolin.orangesmart.service;

import com.rolin.orangesmart.cache.ISessionService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.context.ReqObject;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.model.user.security.AuthenticationUserDetail;
import com.rolin.orangesmart.security.properties.LoginProperties;
import com.rolin.orangesmart.security.service.IApplicationUserService;
import com.rolin.orangesmart.service.IService.ISuccessService;
import com.rolin.orangesmart.util.JwtTokenUtil;
import com.rolin.orangesmart.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultSuccessService implements ISuccessService {

    @Autowired
    private LoginProperties loginProperties;

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private IApplicationUserService applicationUserService;

    @Override
    public void success(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        AuthenticationUserDetail principal = (AuthenticationUserDetail) authentication.getPrincipal();
        User user = new User();
        BeanUtils.copyProperties(principal, user);
        // 构建用户上下文属性,登录返回的menu/permission等接口需要appName,不可删除
        ReqObject reqObject = new ReqObject();
        ReqEnvContext.setReqObject(reqObject);
        // 获取权限用户
        user.setUserName(principal.getUsername());
        reqObject.setUser(user);
        // 写入缓存
        String usernameKey = user.getUserType() + CoreConstant.CACHE_KEY_SEPARATOR +
                user.getAccount() + CoreConstant.CACHE_KEY_SEPARATOR + System.currentTimeMillis();
        sessionService.set(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey, user, loginProperties.getSessionTime(), TimeUnit.SECONDS);
        // 加入系统用户会话集合
        applicationUserService.add(user.getAccount(), user.getUserType().toString(), CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
        // json方式通知前端
        User userDetail = new User();
        BeanUtils.copyProperties(principal, userDetail);
        // 生成authorization
        String authorization = JwtTokenUtil.generateAuthorization(usernameKey);
        response.addHeader(JwtTokenUtil.AUTHORIZATION_HEADER, authorization);
        userDetail.setExtendAttribute(authorization);
        ResponseUtil.out(response, ResponseDTO.ok(userDetail));
    }

}