package com.rolin.orangesmart.context;

import com.rolin.orangesmart.cache.ISessionService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.model.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ReqEnvContextFilter extends OncePerRequestFilter {

    @Autowired
    private ISessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        User user = null;
        // 初始化 ReqEnvContext 从session取出user信息 放入上下文
        String usernameKey = (String) request.getAttribute("usernameKey");
        if (ObjectUtils.isEmpty(usernameKey)) {
            usernameKey = (String) request.getHeader("usernameKey");
        }
        if (!ObjectUtils.isEmpty(usernameKey)) {
            user = (User) sessionService.get(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
        } else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                user = (User) session.getAttribute("user");
            }
        }
        if (user == null) {
            user = new User(-1L, "mockUser");
        }

        ReqObject reqObject = ReqEnvContext.getReqObject();
        if (reqObject == null) {
            reqObject = new ReqObject();
        }
        reqObject.setUser(user);

        String clientId = (String) request.getAttribute("clientId");
        if (ObjectUtils.isEmpty(clientId)) {
            clientId = (String) request.getHeader("clientId");
        }

        clientId = (String) sessionService.get(CoreConstant.CACHE_CURRENT_CLIENT_PREFIX + clientId);
        if (StringUtils.hasText(clientId)) {
            reqObject.setClientId(clientId);
        }

        ReqEnvContext.setReqObject(reqObject);

        try {
            filterChain.doFilter(request, response);
        }finally {
            // 取完数据后clear掉 创建的线程
            ReqEnvContext.clear();
        }
    }


}
