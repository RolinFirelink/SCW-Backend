package com.rolin.orangesmart.security.config;

import com.rolin.orangesmart.security.filter.AccountAuthenticationFilter;
import com.rolin.orangesmart.security.handler.FailureHandler;
import com.rolin.orangesmart.security.handler.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * 账号登录方式配置
 */
@Configuration
public class AccountSecurityConfigration implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    public AccountSecurityConfigration() {
    }

    public AccountSecurityConfigration(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    private List<AuthenticationProvider> authenticationProviders;

    @Autowired
    private SuccessHandler successHandler;

    @Autowired
    private FailureHandler failureHandler;

    @Override
    public void init(HttpSecurity http) throws Exception {

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AccountAuthenticationFilter filter = new AccountAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(authenticationProviders));
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationSuccessHandler(successHandler);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }


}