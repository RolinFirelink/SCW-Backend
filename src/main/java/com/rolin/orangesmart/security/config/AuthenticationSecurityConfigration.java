package com.rolin.orangesmart.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

/**
 * 安全认证配置
 */
@EnableWebSecurity
@Configuration
public class AuthenticationSecurityConfigration {
    @Autowired
    private AccountSecurityConfigration accountSecurityConfigration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081","http://localhost:5173","http://localhost:5130"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    configuration.setAllowCredentials(true);

                    return configuration;
                }))
                .apply(accountSecurityConfigration);

        // 使用 HttpSecurity 的 authorizeHttpRequests 方法
      http.authorizeHttpRequests(authorize -> authorize
          .requestMatchers("/smtp/**").permitAll()  // 放行所有/smtp路径的请求
          .anyRequest().permitAll());

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.disable())
//                .apply(accountSecurityConfigration);
//
//        // 使用 HttpSecurity 的 authorizeHttpRequests 方法
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//
//        return http.build();
//    }

}
