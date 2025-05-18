package com.rolin.orangesmart.security.handler;

import com.rolin.orangesmart.exception.BusinessException;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.logging.LoginLog;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.model.user.security.AuthenticationUserDetail;
import com.rolin.orangesmart.security.service.IAfterSuccessService;
import com.rolin.orangesmart.service.IService.ISuccessService;
import com.rolin.orangesmart.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录成功<
 */
@Slf4j
@Component
@Getter
@Setter
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ISuccessService successService;

    @Autowired
    private IAfterSuccessService afterSuccessService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        AuthenticationUserDetail principal = (AuthenticationUserDetail) authentication.getPrincipal();
        User user = new User();
        BeanUtils.copyProperties(principal, user);
        try {
			afterSuccessService.afterSuccess(user);
			successService.success(request, response, authentication);
            LoginLog.success(request, principal.getAccount());
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            ResponseUtil.out(response, ResponseDTO.no(e.getCode(), e.getMessage()));
            LoginLog.failure(request, user.getAccount(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ResponseUtil.out(response, ResponseDTO.no(SystemException.SYSTEM_ERROR_CODE, e.getMessage()));
            LoginLog.failure(request, user.getAccount(), e.getMessage());
        }
    }

}
