package com.rolin.orangesmart.controller.dictionary;

import java.util.List;
import java.util.Map;

import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryItemVo;
import com.rolin.orangesmart.service.dictionary.DictionaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name ="字典公共接口")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/list/{categoryCode}")
    public ResponseDTO<List<DictionaryItemVo>> getListByCategoryCode(
            @PathVariable("categoryCode") String categoryCode) {
        return ResponseDTO.ok(dictionaryService.getListByCategoryCode(categoryCode));
    }

    @GetMapping("/map/{categoryCode}")
    public ResponseDTO<Map<String, DictionaryItemVo>> getMapByCategoryCode(
            @PathVariable("categoryCode") String categoryCode) {
        return ResponseDTO.ok(dictionaryService.getMapByCategoryCode(categoryCode));
    }

    //client
    @GetMapping("/list/{categoryCode}/{language}")
    public ResponseDTO<List<DictionaryItemVo>> getListByCategoryCode(
            @PathVariable("categoryCode") String categoryCode,
            @PathVariable("language") String language) {
        language = LanguageType.getByCode(language).toString();
        return ResponseDTO.ok(dictionaryService.getListByCategoryCode(categoryCode, language));
    }

    //client
    @GetMapping("/map/{categoryCode}/{language}")
    public ResponseDTO<Map<String, DictionaryItemVo>> getMapByCategoryCode(
            @PathVariable("categoryCode") String categoryCode,
            @PathVariable("language") String language) {
        language = LanguageType.getByCode(language).toString();
        return ResponseDTO.ok(dictionaryService.getMapByCategoryCode(categoryCode, language));
    }

    @GetMapping("/map/byAppName")
    public ResponseDTO<Map<String, List<DictionaryItemVo>>> getMapByAppName(
            @RequestParam("appName") String appName) {
        return ResponseDTO.ok(dictionaryService.getMapByAppName(appName));
    }

    @GetMapping("/map/byAppNames")
    public ResponseDTO<Map<String, List<DictionaryItemVo>>> getMapByAppNames(
            @RequestParam("appNames") String[] appNames) {
        return ResponseDTO.ok(dictionaryService.getMapByAppNames(appNames));
    }

    @GetMapping("/map/categoryCodes")
    public ResponseDTO<Map<String, List<DictionaryItemVo>>> getMapByCategoryCodes(
            @RequestParam("categoryCodes") String[] categoryCodes) {
        return ResponseDTO.ok(dictionaryService.getMapByCategoryCodes(categoryCodes));
    }

    @Deprecated
    @GetMapping("/clear/byAppName")
    public ResponseDTO<Object> clearCacheDataByAppName(@RequestParam("appName") String appName) {
        dictionaryService.clearCacheDataByAppName(appName);
        return ResponseDTO.ok();
    }

    @Deprecated
    @GetMapping("/clearCache")
    public ResponseDTO<Object> clearCache() {
        dictionaryService.clearCacheData(null);
        return ResponseDTO.ok();
    }
}
