package com.rolin.orangesmart.exception;

import com.rolin.orangesmart.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 异常过滤器
 */
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException a = (BusinessException) e;
                logger.error(a.getCode() + " : " + a.getMessage(), e);
                ResponseUtil.out(response, a.getCode(), e.getMessage());
            } else if (e instanceof SystemException) {
                SystemException a = (SystemException) e;
                logger.error(a.getCode() + " : " + a.getMessage(), e);
                ResponseUtil.out(response, a.getCode(), e.getMessage());
            } else {
                logger.error(e.getMessage(), e);
                ResponseUtil.out(response, SystemException.SYSTEM_ERROR_CODE, SystemException.SYSTEM_ERROR_MESSAGE);
            }
        }
    }

}
