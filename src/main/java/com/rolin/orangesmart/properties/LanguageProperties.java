package com.rolin.orangesmart.properties;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.exception.SystemException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 为了不报错引入的文件,后续需要去除
 */
@Getter
@Setter
@SuppressWarnings("deprecation")
@Component
public class LanguageProperties {

    private List<LanguageType> types = Stream.of(LanguageType.ZH_TW).collect(Collectors.toList());

    //system.language.map.contentTypes = ZH_TW, ZH_CN, PT
    private Map<String, List<LanguageType>> map;

    public List<LanguageType> getLanguageTypesWithOrder() {
        String requestLanguageType = "zh-CN";
        if (StringUtils.hasText(requestLanguageType)) {
            List<LanguageType> languageTypes = new ArrayList<LanguageType>();
            for (LanguageType languageType : types) {
                if (languageType.toString().equals(requestLanguageType)) {
                    languageTypes.add(0, languageType);
                } else {
                    languageTypes.add(languageType);
                }
            }
            return languageTypes;
        } else {
            return types;
        }
    }

    public List<Map<String, String>> getLanguageTypes() {
        List<Map<String, String>> list = new ArrayList<>();
        List<LanguageType> languageTypes = this.getLanguageTypesWithOrder();
        languageTypes.stream().forEach(languageType -> {
            Map<String, String> map = new HashMap<>();
            list.add(map);
            map.put("code", languageType.getCode());
            map.put("name", languageType.getName());
        });
        return list;
    }

    public LanguageType getDefaultLanguage() {
        return types.get(0);
    }

    public LanguageType getRequestLanguageType(String requestLanguage) {
        LanguageType rtnLanguageType = null;
        for (LanguageType languageType : types) {
            if (languageType.getCode().equals(requestLanguage)) {
                rtnLanguageType = languageType;
                break;
            }
        }
        if (rtnLanguageType != null) {
            return rtnLanguageType;
        } else {
            return getDefaultLanguage();
        }
    }

    public LanguageType getRequestLanguageType(String requestLanguage, String languageCategory) {
        LanguageType rtnLanguageType = null;
        List<LanguageType> selectedTypes = map != null ? map.get(languageCategory) : null;
        if (selectedTypes == null) {
            selectedTypes = types;
        }
        for (LanguageType languageType : selectedTypes) {
            if (languageType.getCode().equals(requestLanguage)) {
                rtnLanguageType = languageType;
                break;
            }
        }
        if (rtnLanguageType != null) {
            return rtnLanguageType;
        } else {
            return getDefaultLanguage();
        }
    }

    public LanguageType getDefaultLanguage(String languageCategory) {
        List<LanguageType> selectedTypes = map != null ? map.get(languageCategory) : null;
        LanguageType languageType = null;
        if (selectedTypes != null) {
            languageType = selectedTypes.get(0);
        }
        if (languageType == null) {
            throw new SystemException("需指定" + languageCategory + " 多语言分类");
        }
        return languageType;
    }

}