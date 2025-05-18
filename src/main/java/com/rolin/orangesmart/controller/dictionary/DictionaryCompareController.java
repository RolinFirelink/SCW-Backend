package com.rolin.orangesmart.controller.dictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rolin.orangesmart.constant.StorageConstant;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagItemVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryWithTagsVO;
import com.rolin.orangesmart.properties.StoreProperties;
import com.rolin.orangesmart.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Tag(name = "字典比较接口")
@RestController
@RequestMapping("/dictionaryCompare")
public class DictionaryCompareController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private StoreProperties storeProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(description = "B相对A的差集")
    @PostMapping
    public ResponseDTO<Long> importData(MultipartFile fileA, MultipartFile fileB, @RequestHeader("appName") String appName) {
        List<DictionaryWithTagsVO> dictionaryWithTagsVOS = new ArrayList<DictionaryWithTagsVO>();
        try (InputStream ina = fileA.getInputStream();
             InputStream inb = fileB.getInputStream();) {
            List<DictionaryWithTagsVO> dictionaryWithTagsVOSA = objectMapper.readValue(ina, new TypeReference<List<DictionaryWithTagsVO>>() {
            });
            List<DictionaryWithTagsVO> dictionaryWithTagsVOSB = objectMapper.readValue(inb, new TypeReference<List<DictionaryWithTagsVO>>() {
            });

            Map<String, DictionaryWithTagsVO> dictionaryWithTagsVOMapA = dictionaryWithTagsVOListToMap(dictionaryWithTagsVOSA);
            Map<String, DictionaryWithTagsVO> dictionaryWithTagsVOMapB = dictionaryWithTagsVOListToMap(dictionaryWithTagsVOSB);

            for (String categoryCode : dictionaryWithTagsVOMapB.keySet()) {
                DictionaryWithTagsVO dictionaryWithTagsVOA = dictionaryWithTagsVOMapA.get(categoryCode);
                DictionaryWithTagsVO dictionaryWithTagsVOB = dictionaryWithTagsVOMapB.get(categoryCode);
                if (dictionaryWithTagsVOA == null) {
                    dictionaryWithTagsVOS.add(dictionaryWithTagsVOB);
                } else {
                    DictionaryWithTagsVO dictionaryWithTagsVO = new DictionaryWithTagsVO();
                    BeanUtils.copyProperties(dictionaryWithTagsVOB, dictionaryWithTagsVO);
                    dictionaryWithTagsVO.setDictionaryTagVos(null);

                    Map<String, DictionaryTagVo> dictionaryTagVoMapA = dictionaryTagVoListToMap(dictionaryWithTagsVOA.getDictionaryTagVos());
                    Map<String, DictionaryTagVo> dictionaryTagVoMapB = dictionaryTagVoListToMap(dictionaryWithTagsVOB.getDictionaryTagVos());
                    List<DictionaryTagVo> dictionaryTagVos = new ArrayList<>();
                    for (String tagCode : dictionaryTagVoMapB.keySet()) {
                        DictionaryTagVo dictionaryTagVoA = dictionaryTagVoMapA.get(tagCode);
                        DictionaryTagVo dictionaryTagVoB = dictionaryTagVoMapB.get(tagCode);
                        if (dictionaryTagVoA == null) {
                            dictionaryTagVos.add(dictionaryTagVoB);
                        } else {
                            DictionaryTagVo dictionaryTagVo = new DictionaryTagVo();
                            BeanUtils.copyProperties(dictionaryTagVoB, dictionaryTagVo);
                            dictionaryTagVo.setDictionaryTagItemVos(null);

                            Map<String, DictionaryTagItemVo> dictionaryTagItemVoMapA = dictionaryTagItemVoListToMap(dictionaryTagVoA.getDictionaryTagItemVos());
                            Map<String, DictionaryTagItemVo> dictionaryTagItemVoMapB = dictionaryTagItemVoListToMap(dictionaryTagVoB.getDictionaryTagItemVos());
                            List<DictionaryTagItemVo> dictionaryTagItemVos = new ArrayList<>();
                            for (String languageType : dictionaryTagItemVoMapB.keySet()) {
                                DictionaryTagItemVo dictionaryTagItemVoA = dictionaryTagItemVoMapA.get(languageType);
                                DictionaryTagItemVo dictionaryTagItemVoB = dictionaryTagItemVoMapB.get(languageType);
                                if (dictionaryTagItemVoA == null) {
                                    dictionaryTagItemVos.add(dictionaryTagItemVoB);
                                } else {
                                    if (!dictionaryTagItemVoA.getItemName().equals(dictionaryTagItemVoB.getItemName())) {
                                        dictionaryTagItemVos.add(dictionaryTagItemVoB);
                                    }
                                    if (StringUtils.hasText(dictionaryTagItemVoA.getItemName2()) &&
                                            StringUtils.hasText(dictionaryTagItemVoB.getItemName2()) &&
                                            !dictionaryTagItemVoA.getItemName2().equals(dictionaryTagItemVoB.getItemName2())) {
                                        dictionaryTagItemVos.add(dictionaryTagItemVoB);
                                    }
                                }
                            }
                            if (dictionaryTagItemVos.size() > 0) {
                                dictionaryTagVo.setDictionaryTagItemVos(dictionaryTagItemVos);
                                dictionaryTagVos.add(dictionaryTagVo);
                            }
                        }
                    }
                    if (dictionaryTagVos.size() > 0) {
                        dictionaryWithTagsVO.setDictionaryTagVos(dictionaryTagVos);
                        dictionaryWithTagsVOS.add(dictionaryWithTagsVO);
                    }
                }
            }
        } catch (IOException e) {
            throw new SystemException(e);
        }

        File file = new File(MessageFormat.format(storeProperties.getTempPath() + "/dictionaryCompare_{0}.txt", appName));
        try (FileWriter writer = new FileWriter(file)) {

            dictionaryWithTagsVOS.sort(new Comparator<DictionaryWithTagsVO>() {
                @Override
                public int compare(DictionaryWithTagsVO o1, DictionaryWithTagsVO o2) {
                    return o1.getCategoryCode().compareTo(o2.getCategoryCode());
                }
            });

            dictionaryWithTagsVOS.stream().forEach(c -> {
                        c.getDictionaryTagVos().sort(new Comparator<DictionaryTagVo>() {
                            @Override
                            public int compare(DictionaryTagVo o1, DictionaryTagVo o2) {
                                return o1.getTagCode().compareTo(o2.getTagCode());
                            }
                        });

                        c.getDictionaryTagVos().stream().forEach(d -> {
                            d.getDictionaryTagItemVos().sort(new Comparator<DictionaryTagItemVo>() {
                                @Override
                                public int compare(DictionaryTagItemVo o1, DictionaryTagItemVo o2) {
                                    return (o1.getLanguageType() + ":" + o1.getItemName()).compareTo(o2.getLanguageType() + ":" + o2.getItemName());
                                }
                            });
                        });
                    }
            );

            writer.write(objectMapper.writeValueAsString(dictionaryWithTagsVOS));
            writer.flush();
            //上傳導出文件
            Attachment attachment = uploadService.upload(file, file.getName(),
                    "/base/dict/import/" + StorageConstant.YEAR_MONTH_DAY_PARAM_PATH);
            return ResponseDTO.ok(attachment.getId());
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    private Map<String, DictionaryWithTagsVO> dictionaryWithTagsVOListToMap(List<DictionaryWithTagsVO> dictionaryWithTagsVOS) {
        Map<String, DictionaryWithTagsVO> dictionaryWithTagsVOMap = new HashMap<>();
        for (Iterator<DictionaryWithTagsVO> iterator = dictionaryWithTagsVOS.iterator(); iterator.hasNext(); ) {
            DictionaryWithTagsVO dictionaryWithTagsVO = iterator.next();
            dictionaryWithTagsVOMap.put(dictionaryWithTagsVO.getCategoryCode(), dictionaryWithTagsVO);
        }
        return dictionaryWithTagsVOMap;
    }

    private Map<String, DictionaryTagVo> dictionaryTagVoListToMap(List<DictionaryTagVo> dictionaryTagVos) {
        Map<String, DictionaryTagVo> dictionaryTagVoMap = new HashMap<>();
        for (Iterator<DictionaryTagVo> iterator = dictionaryTagVos.iterator(); iterator.hasNext(); ) {
            DictionaryTagVo dictionaryTagVo = iterator.next();
            dictionaryTagVoMap.put(dictionaryTagVo.getTagCode(), dictionaryTagVo);
        }
        return dictionaryTagVoMap;
    }

    private Map<String, DictionaryTagItemVo> dictionaryTagItemVoListToMap(List<DictionaryTagItemVo> dictionaryTagItemVos) {
        Map<String, DictionaryTagItemVo> dictionaryTagItemVoMap = new HashMap<>();
        for (Iterator<DictionaryTagItemVo> iterator = dictionaryTagItemVos.iterator(); iterator.hasNext(); ) {
            DictionaryTagItemVo dictionaryTagItemVo = iterator.next();
            dictionaryTagItemVoMap.put(dictionaryTagItemVo.getLanguageType(), dictionaryTagItemVo);
        }
        return dictionaryTagItemVoMap;
    }

}
