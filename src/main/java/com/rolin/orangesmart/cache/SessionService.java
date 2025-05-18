package com.rolin.orangesmart.cache;

import com.rolin.orangesmart.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Service
public class SessionService implements ISessionService {

    @Override
    public void del(String key) {
        HttpSession session = getSession();
        session.removeAttribute(key);
    }

    @Override
    public void set(String key, Object value, long liveTime, TimeUnit timeUnit) {
        HttpSession session = getSession();
        SessionValue sessionValue = new SessionValue();
        sessionValue.setValue(value);
        long endTime = System.currentTimeMillis();
        if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
            endTime += liveTime;
        } else if (timeUnit.equals(TimeUnit.SECONDS)) {
            endTime += liveTime * 1000;
        } else if (timeUnit.equals(TimeUnit.MINUTES)) {
            endTime += liveTime * 1000 * 60;
        } else if (timeUnit.equals(TimeUnit.HOURS)) {
            endTime += liveTime * 1000 * 60 * 60;
        } else if (timeUnit.equals(TimeUnit.DAYS)) {
            endTime += liveTime * 1000 * 60 * 60 * 24;
        } else {
            throw new BusinessException("时长设置有误");
        }

        sessionValue.setEndTime(endTime);
        session.setAttribute(key, sessionValue);
    }

    @Override
    public Object get(String key) {
        HttpSession session = getSession();
        SessionValue sessionValue = (SessionValue) session.getAttribute(key);
        if (sessionValue != null) {
            long curTime = System.currentTimeMillis();
            if (sessionValue.getEndTime() >= curTime) {
                Object object = sessionValue.getValue();
                return object;
            } else {
                session.removeAttribute(key);
                return null;
            }
        } else {
            return null;
        }
    }

    private HttpSession getSession() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession(true);
        return session;
    }

    @Setter
    @Getter
    class SessionValue {

        Object value;

        long endTime;

    }

}
