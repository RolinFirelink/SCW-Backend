package com.rolin.orangesmart.security.filter;

import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.exception.BusinessException;
import com.rolin.orangesmart.exception.errorEnum.SecurityErrorEnum;
import com.rolin.orangesmart.security.token.AccountAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 账号登录
 */
public class AccountAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String EMAIL_LOGIN_URL = "/login";
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public AccountAuthenticationFilter() {
        super(new AntPathRequestMatcher(EMAIL_LOGIN_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        try {
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                SecurityErrorEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.fail();
            }
            username = username.trim();
            AbstractAuthenticationToken authRequest = new AccountAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (BusinessException be) {
            throw new InternalAuthenticationServiceException(be.getCode() +
                    CoreConstant.CODE_KEY_SEPARATOR + be.getMessage());
        }
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
    }

    protected void setDetails(HttpServletRequest request,
                              AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


}
