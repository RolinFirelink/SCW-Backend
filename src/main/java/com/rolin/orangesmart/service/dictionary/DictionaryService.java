package com.rolin.orangesmart.service.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rolin.orangesmart.cache.CacheDataHelper;
import com.rolin.orangesmart.cache.ICacheService;
import com.rolin.orangesmart.constant.CacheDataConstant;
import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.mapper.dictionary.DictionaryMapper;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryItemVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryVo;
import com.rolin.orangesmart.properties.LanguageProperties;
import com.rolin.orangesmart.service.IService.ICacheDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DictionaryService implements ICacheDataService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private ICacheService cacheService;

    @Autowired
    private LanguageProperties languageProperties;

    @Autowired
    private CacheDataHelper cacheDataHelper;

    public Map<String, List<DictionaryItemVo>> getMapByAppName(String appName) {
        return this.getMapByAppName(appName, null);
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, List<DictionaryItemVo>> getMapByAppName(String appName, String language) {
        if (!StringUtils.hasText(language)) {
//            language = ReqEnvContext.getLanguageType().toString();
            language = "zh-CN";
        }
        String key = null;
        if (StringUtils.hasText(appName)) {
            key = CacheDataConstant.DICTONARY_PREFIX + appName + ICacheService.KEY_SEPARATOR + language;
        } else {
            key = CacheDataConstant.DICTONARY_PREFIX + language;
        }
        Map<String, List<DictionaryItemVo>> map = (Map<String, List<DictionaryItemVo>>) cacheService.get(key);
        if (map != null && !map.isEmpty()) {
            return map;
        }
        List<DictionaryVo> dictionaryVos = dictionaryMapper.findByAppNameAndLanuageType(appName, LanguageType.valueOf(language));

        map = new HashMap<String, List<DictionaryItemVo>>();
        for (DictionaryVo dictionaryVo : dictionaryVos) {
            List<DictionaryItemVo> list = map.get(dictionaryVo.getCategoryCode());
            if (list == null) {
                list = new ArrayList<DictionaryItemVo>();
                map.put(dictionaryVo.getCategoryCode(), list);
            }
            if (StringUtils.hasText(dictionaryVo.getTagCode())) {
                DictionaryItemVo dictionaryItemVo = new DictionaryItemVo();
                dictionaryItemVo.setCode(dictionaryVo.getTagCode());
                dictionaryItemVo.setName(dictionaryVo.getItemName());
                dictionaryItemVo.setName2(dictionaryVo.getItemName2());
                list.add(dictionaryItemVo);
            }
        }
        cacheDataHelper.setCacheData(key, map);
        return map;
    }

    public List<DictionaryItemVo> getListByCategoryCode(String categoryCode) {
        return this.getListByCategoryCode(categoryCode, null);
    }

    public List<DictionaryItemVo> getListByCategoryCode(String categoryCode, String language) {
        Map<String, List<DictionaryItemVo>> map = this.getMapByAppName(null, language);
        List<DictionaryItemVo> dictionaryItemVos = map.get(categoryCode);
        return dictionaryItemVos;
    }

    public Map<String, DictionaryItemVo> getMapByCategoryCode(String categoryCode) {
        return this.getMapByCategoryCode(categoryCode, null);
    }

    public Map<String, DictionaryItemVo> getMapByCategoryCode(String categoryCode, String language) {
        Map<String, DictionaryItemVo> dictionaryItemVoMap = new HashMap<>();
        Map<String, List<DictionaryItemVo>> dictionaryItemVoListMap = this.getMapByAppName(null, language);

        List<DictionaryItemVo> dictionaryItemVos = dictionaryItemVoListMap.get(categoryCode);
        for (DictionaryItemVo dictionaryItemVo : dictionaryItemVos) {
            dictionaryItemVoMap.put(dictionaryItemVo.getCode(), dictionaryItemVo);
        }
        return dictionaryItemVoMap;
    }

    public Map<String, List<DictionaryItemVo>> getMapByAppNames(String[] appNames) {
        Map<String, List<DictionaryItemVo>> map = new HashMap<>();
        for (String appName : appNames) {
            if (StringUtils.hasText(appName)) {
                map.putAll(this.getMapByAppName(appName, null));
            }
        }
        return map;
    }

    public Map<String, List<DictionaryItemVo>> getMapByCategoryCodes(String[] categoryCodes) {
        Map<String, List<DictionaryItemVo>> map = new HashMap<>();
        for (String categoryCode : categoryCodes) {
            if (StringUtils.hasText(categoryCode)) {
                map.put(categoryCode, this.getListByCategoryCode(categoryCode));
            }
        }
        return map;
    }

    @Override
    public void clearCacheData(String key) {
        cacheDataHelper.clearCacheData(key);
    }

    @Deprecated
    public void clearCacheDataByAppName(String appName) {
        for (LanguageType languageType : languageProperties.getLanguageTypesWithOrder()) {
            String key = null;
            if (StringUtils.hasText(appName)) {
                key = CacheDataConstant.DICTONARY_PREFIX + appName + ICacheService.KEY_SEPARATOR + languageType.toString();
            } else {
                key = CacheDataConstant.DICTONARY_PREFIX + languageType.toString();
            }
            cacheService.del(key);
        }
    }

}
