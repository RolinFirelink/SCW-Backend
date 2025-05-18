package com.rolin.orangesmart.controller.dictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.rolin.orangesmart.constant.StorageConstant;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryCategorySearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagItemVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryWithTagsVO;
import com.rolin.orangesmart.properties.StoreProperties;
import com.rolin.orangesmart.service.UploadService;
import com.rolin.orangesmart.service.dictionary.DictionaryCategoryService;
import com.rolin.orangesmart.service.dictionary.DictionaryTagItemService;
import com.rolin.orangesmart.service.dictionary.DictionaryTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;


@Tag(name ="字典导出接口")
@RestController
@RequestMapping("/dictionaryExport")
public class DictionaryExportController {

    @Autowired
    private DictionaryCategoryService dictionaryCategoryService;

    @Autowired
    private DictionaryTagService dictionaryTagService;

    @Autowired
    private DictionaryTagItemService dictionaryTagItemService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private StoreProperties storeProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(description = "字典導出")
    @GetMapping("/{appName}")
    public ResponseDTO<Long> export(@PathVariable("appName") String appName) {
        File file = new File(MessageFormat.format(storeProperties.getTempPath() + "/dictionaryExport_{0}.txt", appName));

        try (FileWriter writer = new FileWriter(file)) {
            DictionaryCategorySearchBo dictionaryCategorySearchBo = new DictionaryCategorySearchBo();
            dictionaryCategorySearchBo.setAppName(appName);
            List<DictionaryCategory> dictionaryCategories = dictionaryCategoryService.find(dictionaryCategorySearchBo);
            dictionaryCategories.sort(new Comparator<DictionaryCategory>() {
                @Override
                public int compare(DictionaryCategory o1, DictionaryCategory o2) {
                    return o1.getCategoryCode().compareTo(o2.getCategoryCode());
                }
            });

            List<DictionaryWithTagsVO> resultList = dictionaryCategories.stream().map(dc -> {
                DictionaryWithTagsVO dictionaryWithTagsVO = new DictionaryWithTagsVO();
                List<DictionaryTagVo> dictionaryTagVos = dictionaryTagService.findByCategoryId(dc.getId());
                dictionaryTagVos.forEach(dtv -> {
                    List<DictionaryTagItemVo> itemVos = dictionaryTagItemService.findByTagIds(Collections.singletonList(dtv.getId())).stream().map(DictionaryTagItemVo::from).collect(Collectors.toList());
                    itemVos.sort(new Comparator<DictionaryTagItemVo>() {
                        @Override
                        public int compare(DictionaryTagItemVo o1, DictionaryTagItemVo o2) {
                            return (o1.getLanguageType() + ":" + o1.getItemName()).compareTo(o2.getLanguageType() + ":" + o2.getItemName());
                        }
                    });
                    dtv.setDictionaryTagItemVos(itemVos);
                });
                dictionaryTagVos.sort(new Comparator<DictionaryTagVo>() {
                    @Override
                    public int compare(DictionaryTagVo o1, DictionaryTagVo o2) {
                        return o1.getTagCode().compareTo(o2.getTagCode());
                    }
                });
                dictionaryWithTagsVO.setDictionaryTagVos(dictionaryTagVos);
                dc.setCreateDate(null);
                dc.setCreatedUserId(null);
                dc.setUpdateDate(null);
                dc.setUpdatedUserId(null);
                BeanUtils.copyProperties(dc, dictionaryWithTagsVO);
                return dictionaryWithTagsVO;
            }).collect(Collectors.toList());

            writer.write(objectMapper.writeValueAsString(resultList));
            writer.flush();

            //上傳導出文件
            Attachment attachment = uploadService.upload(file, file.getName(),
                    "/base/dict/export" + StorageConstant.YEAR_MONTH_DAY_PARAM_PATH);
            return ResponseDTO.ok(attachment.getId());
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            //刪除生成的導出文件
            try {
                Files.deleteIfExists(file.toPath());
            } catch (Exception e) {
            }
        }
    }
}