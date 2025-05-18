package com.rolin.orangesmart.exception;


import com.rolin.orangesmart.exception.errorEnum.SecurityErrorEnum;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@Slf4j
@Hidden
@ControllerAdvice
@Order(100)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDTO<Object> businessExceptionHandler(BusinessException e, HttpServletResponse response) {
        String message = e.getMessage();
        String localeString = String.format(message, e.getArgs());
        log.error("{} : {}", e.getCode(), localeString);
        return creatResponseDTO(e.getCode(), e, response, localeString);
    }

    @ExceptionHandler(value = SystemException.class)
    @ResponseBody
    public ResponseDTO<Object> systemExceptionHandler(SystemException e, HttpServletResponse response) {
        log.error("{} : {}", e.getCode(), e.getMessage());
        String message = e.getMessage();
        return creatResponseDTO(e.getCode(), e, response, message);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseDTO<Object> exceptionHandler(Exception e, HttpServletResponse response) {
        String message = e.getMessage();
        return creatResponseDTO(SystemException.SYSTEM_ERROR_CODE, e, response, message);
    }

    private ResponseDTO<Object> creatResponseDTO(String errorCode, Exception e, HttpServletResponse response, String message) {
        if(SecurityErrorEnum.USER_NO_PERMISSION_ERROR.equals(errorCode)){
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        log.error(errorCode, e);
        return ResponseDTO.no(errorCode, message);
    }

}

