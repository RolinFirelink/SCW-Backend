package com.rolin.orangesmart.security.jwt;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rolin.orangesmart.cache.ISessionService;
import com.rolin.orangesmart.cache.redis.RedisCacheService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.context.ReqObject;
import com.rolin.orangesmart.exception.errorEnum.SecurityErrorEnum;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.security.properties.LoginProperties;
import com.rolin.orangesmart.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Token过滤,目前暂不真的开启过滤,方便测试
 */
@Configuration
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private LoginProperties loginProperties;

    @Resource
    private RedisCacheService redisCacheService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String usernameKey = (String) request.getAttribute("usernameKey");
        if (ObjectUtils.isEmpty(usernameKey)) {
            usernameKey = request.getHeader("usernameKey");
        }
        if (!ObjectUtils.isEmpty(usernameKey)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(JwtTokenUtil.AUTHORIZATION_HEADER);
        if (ObjectUtils.isEmpty(token)) {
            // TODO 注释掉这段代码,可以让登录验证稳定生效
            token = request.getParameter("accessToken");
            if (ObjectUtils.isEmpty(token)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        usernameKey = JwtTokenUtil.getUserNamekey(token);
        SecurityErrorEnum.TOKEN_ANALYSIS_ERROR.isEmpty(usernameKey);
        int start = usernameKey.indexOf(":");
        int end = usernameKey.lastIndexOf(":");
        usernameKey = usernameKey.substring(start+1,end);
//        User user = (User) sessionService.get(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
        User user = (User) redisCacheService.get(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
        SecurityErrorEnum.TOKEN_EXPIRED_ERROR.isNull(user);
        SecurityErrorEnum.USER_DISABLED_ERROR.isFalse(user.getActiveFlag());
        if (Boolean.TRUE.equals(user.getKickFlag())) {
            //踢出，user两分钟后从redis中失效
            sessionService.del(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
            SecurityErrorEnum.USER_EXPIRED_ERROR.fail();
        }
        //重置用户redis有效期并设置上下文
//        sessionService.set(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey, user, loginProperties.getSessionTime(), TimeUnit.SECONDS);
        redisCacheService.set(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey, user, loginProperties.getSessionTime(), TimeUnit.SECONDS);
        request.setAttribute("usernameKey", usernameKey);
        ReqObject reqObject = ReqEnvContext.getReqObject();
        if (reqObject == null) {
            reqObject = new ReqObject();
        }
        reqObject.setUser(user);
        ReqEnvContext.setReqObject(reqObject);

        filterChain.doFilter(request, response);
    }

}