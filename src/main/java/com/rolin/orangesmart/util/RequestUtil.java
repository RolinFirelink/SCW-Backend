package com.rolin.orangesmart.util;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {


    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = null;
        if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
                if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                    if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getRemoteAddr();
                    }
                }
            }
        }
        if (!ObjectUtils.isEmpty(ip)) {
            //使用代理，则获取第一个IP地址
            ip = ip.split(",")[0];
        }
        return ip;
    }
}