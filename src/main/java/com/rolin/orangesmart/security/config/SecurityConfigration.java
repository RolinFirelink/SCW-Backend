package com.rolin.orangesmart.security.config;

import com.rolin.orangesmart.cache.ISessionService;
import com.rolin.orangesmart.cache.SessionService;
import com.rolin.orangesmart.security.encoder.DefaultPasswordEncoder;
import com.rolin.orangesmart.security.encoder.IPasswordEncoder;
import com.rolin.orangesmart.security.handler.FailureHandler;
import com.rolin.orangesmart.security.handler.SuccessHandler;
import com.rolin.orangesmart.security.jwt.TokenFilter;
import com.rolin.orangesmart.security.properties.LoginProperties;
import com.rolin.orangesmart.security.properties.PasswordProperties;
import com.rolin.orangesmart.security.provider.AccountAuthenticationProvider;
import com.rolin.orangesmart.security.service.IAfterFailureService;
import com.rolin.orangesmart.security.service.IAfterSuccessService;
import com.rolin.orangesmart.security.service.IApplicationUserService;
import com.rolin.orangesmart.security.service.impl.DefaultAfterFailureService;
import com.rolin.orangesmart.security.service.impl.DefaultAfterSuccessService;
import com.rolin.orangesmart.service.DefaultApplicationUserService;
import com.rolin.orangesmart.service.DefaultSuccessService;
import com.rolin.orangesmart.service.IService.ISuccessService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;

import java.util.ArrayList;
import java.util.List;


@Import({
        AuthenticationSecurityConfigration.class,
        SuccessHandler.class,
        FailureHandler.class
})
@Configuration
public class SecurityConfigration {

    @Bean
    @ConfigurationProperties(prefix = "security.login")
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.password")
    public PasswordProperties passwordProperties() {
        return new PasswordProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISessionService sessionService() {
        return new SessionService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISuccessService successService() {
        return new DefaultSuccessService();
    }

    @Bean
    @ConditionalOnMissingBean
    public IAfterSuccessService afterSuccessService() {
        return new DefaultAfterSuccessService();
    }

    @Bean
    @ConditionalOnMissingBean
    public IAfterFailureService afterFailureService() {
        return new DefaultAfterFailureService();
    }

    @Bean
    @ConditionalOnMissingBean
    public IApplicationUserService applicationUserService() {
        return new DefaultApplicationUserService();
    }

    @Bean
    @ConditionalOnProperty(name = "token.filter.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<TokenFilter> tokenFilterRegistrationBean(TokenFilter tokenFilter) {
        FilterRegistrationBean<TokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(tokenFilter);// 添加过滤器
        registration.addUrlPatterns("/*");// 设置过滤路径，/*所有路径
        registration.setName("tokenFilter");
        registration.setOrder(9);// 设置优先级
        return registration;
    }

    @Bean
    @ConditionalOnProperty(name = "token.filter.enable", havingValue = "true", matchIfMissing = true)
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public IPasswordEncoder passwordEncoder() {
        return new DefaultPasswordEncoder();
    }

    @Bean
    public AccountSecurityConfigration accountSecurityConfigration(AccountAuthenticationProvider accountAuthenticationProvider) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(accountAuthenticationProvider);
        return new AccountSecurityConfigration(authenticationProviders);
    }

    @ConditionalOnMissingBean
    @Bean
    public AccountAuthenticationProvider accountAuthenticationProvider() {
        return new AccountAuthenticationProvider();
    }


}