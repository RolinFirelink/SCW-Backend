package com.rolin.orangesmart.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 错误上下文
 */
@Slf4j
public class ErrorCodeContext {

    private static final String PROPERTIES = "error.properties";

    private static final String APPLICATION = "error.application";

    private static final String MODEL = "error.model.";

    private static Map<String, String> map = new ConcurrentHashMap<>();

    private ErrorCodeContext() {
    }

    static {
        Resource resource = new ClassPathResource(PROPERTIES);
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            log.error("加载{}文件异常", PROPERTIES);
        }
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            map.put(key, properties.getProperty(key));
        }
    }

    public static String of(String name, String code) {
        String application = map.get(APPLICATION);
        String model = Optional.ofNullable(map.get(MODEL + name)).orElse(name);
        return application + model + code;
    }
}
