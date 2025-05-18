package com.rolin.orangesmart.constant;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public enum LanguageType {

    ZH_CN("zh-CN", "简体中文"),
    ZH_TW("zh-TW", "繁體中文"),
    EN_US("en-US", "English"),
    PT("pt", "Português"),
    ;

    private String code;
    private String name;

    LanguageType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LanguageType getByCode(String code) {
        if (ZH_CN.code.equals(code)) {
            return ZH_CN;
        } else if (ZH_TW.code.equals(code)) {
            return ZH_TW;
        } else if (EN_US.code.equals(code)) {
            return EN_US;
        } else if (PT.code.equals(code)) {
            return PT;
        } else {
            return LanguageType.ZH_TW;
        }
    }

    public static LanguageType getNoDefaultByCode(String code) {
        if (ZH_CN.code.equals(code)) {
            return ZH_CN;
        } else if (ZH_TW.code.equals(code)) {
            return ZH_TW;
        } else if (EN_US.code.equals(code)) {
            return EN_US;
        } else if (PT.code.equals(code)) {
            return PT;
        } else {
            return null;
        }
    }

    public static String getLanguage(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (ObjectUtils.isEmpty(language)) {
            language = request.getHeader("Accept-Language");
            if (StringUtils.hasText(language) && language.indexOf(",") != -1) {
                language = language.substring(0, language.indexOf(","));
                if (language.equalsIgnoreCase("en")) {
                    language = LanguageType.EN_US.getCode();
                }
            }
        }
        return language;
    }
}
