package com.rolin.orangesmart.security.handler;

import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.exception.BusinessException;
import com.rolin.orangesmart.logging.LoginLog;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.security.service.IAfterFailureService;
import com.rolin.orangesmart.util.ResponseUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录失败
 */
@Component
@Slf4j
public class FailureHandler implements AuthenticationFailureHandler {

    @Resource
    private IAfterFailureService afterFailureService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String code = null;
        String message = exception.getMessage();
        if (exception instanceof InternalAuthenticationServiceException) {
            int separator = message.indexOf(CoreConstant.CODE_KEY_SEPARATOR);
            if (separator > 0) {
                code = message.substring(0, separator);
                message = message.substring(separator + 1);
            }
        }
        String userType = request.getHeader("userType");
        String account = request.getParameter("username");
        try {
            afterFailureService.afterFailure(account, userType, message);
            log.error(exception.getMessage(), exception);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            code = e.getCode();
            message = e.getMessage();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            message = e.getMessage();
        } finally {
            if (code != null) {
                ResponseUtil.out(response, ResponseDTO.no(code, message));
            } else {
                ResponseUtil.out(response, ResponseDTO.no(message));
            }
            LoginLog.failure(request, account, message);
        }
    }

}