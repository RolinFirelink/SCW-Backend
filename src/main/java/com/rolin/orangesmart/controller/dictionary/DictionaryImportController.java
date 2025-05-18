package com.rolin.orangesmart.controller.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryWithTagsVO;
import com.rolin.orangesmart.service.dictionary.DictonaryImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag(name = "字典导入接口")
@RestController
@RequestMapping("/dictionaryImport")
public class DictionaryImportController {

    @Autowired
    private DictonaryImportService dictonaryImportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(description = "字典導入")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTO<Integer> importData(
            @Parameter(description = "MultipartFile对象", required = true)
            @RequestPart("file") MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            List<DictionaryWithTagsVO> componentWithTagsVOS = objectMapper.readValue(in, new TypeReference<List<DictionaryWithTagsVO>>() {
            });
            dictonaryImportService.importDataNew(componentWithTagsVOS);
        } catch (IOException e) {
            throw new SystemException(e);
        }
        return ResponseDTO.ok();
    }

}