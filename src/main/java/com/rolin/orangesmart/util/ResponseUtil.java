package com.rolin.orangesmart.util;

import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ResponseUtil {

    public static <T> void out(HttpServletResponse response, ResponseDTO<T> responseDTO) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter();) {
            out.println(JsonUtil.toJsonString(responseDTO));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void out(HttpServletResponse response, String code, String message) {
        ResponseUtil.out(response, ResponseDTO.no(code, message));
//        SysProperties sysProperties = (SysProperties) AppEnvContext.getBean("sysProperties");
//        if (sysProperties.isResponseJson()) {
//
//        } else {
//            if (!sysProperties.isResponseStatus200()) {
//                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            }
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=utf-8");
//            try (PrintWriter out = response.getWriter();) {
//                out.write(code + ": " + message);
//            } catch (IOException e) {
//            }
//        }
    }

}
