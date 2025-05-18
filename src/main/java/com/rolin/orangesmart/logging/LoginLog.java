package com.rolin.orangesmart.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.util.DateCommonUtil;
import com.rolin.orangesmart.util.DateTransformUtil;
import com.rolin.orangesmart.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j(topic = "LOGIN_LOG")
public class LoginLog {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private LoginLog() {
    }

    public static void success(HttpServletRequest request, String account) {
        writeLog(request, account, "success");
    }

    public static void failure(HttpServletRequest request, String account, String message) {
        writeLog(request, account, message);
    }

    private static void writeLog(HttpServletRequest request, String account, String message) {
        Map<String, Object> valueMap = new HashMap<>();
        String ip = RequestUtil.getIpAddr(request);
        valueMap.put("ip", Optional.ofNullable(ip).orElse(""));
        valueMap.put("url", "/login");
        valueMap.put("account", account);
        valueMap.put("http-method", request.getMethod());
        valueMap.put("class", "");
        valueMap.put("method", "");
        valueMap.put("args", "");
        valueMap.put("start-time", DateCommonUtil.now(DateTransformUtil.DATETIME_ZH_DEFAULT));
        valueMap.put("response", message);
        valueMap.put("end-time", DateCommonUtil.now(DateTransformUtil.DATETIME_ZH_DEFAULT));
        try {
            log.info(objectMapper.writeValueAsString(valueMap));
        } catch (JsonProcessingException e) {
            throw new SystemException(e);
        }
    }

}