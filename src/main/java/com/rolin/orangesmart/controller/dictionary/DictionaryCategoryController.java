package com.rolin.orangesmart.controller.dictionary;

import com.rolin.orangesmart.model.common.dto.PageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryCategorySearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import com.rolin.orangesmart.service.dictionary.DictionaryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "字典类别")
@RestController
@RequestMapping("/dictionaryCategory")
public class DictionaryCategoryController {

    @Autowired
    private DictionaryCategoryService dictionaryCategoryService;

    @Operation(description = "根据id查询字典类别")
    @GetMapping("/{id}")
    public ResponseDTO<DictionaryCategory> getById(@Parameter(description = "字典类别id")
                                                   @PathVariable("id") @Valid @NotNull(message = "字典類別id不能為空") Long id) {
        return ResponseDTO.ok(dictionaryCategoryService.getById(id));
    }

    @Operation(description = "根据分页属性或查询内容查询字典类别")
    @GetMapping("/page")
    public ResponseDTO<PageInfo<DictionaryCategory>> page(
            @Valid PageDTO pageDTO,
            @Parameter(description = "查询内容")
            DictionaryCategorySearchBo dictionaryCategorySearchBo) {
        IPage<?> page = new com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO(pageDTO.getPageNum(), pageDTO.getPageSize());
        return ResponseDTO.ok(new PageInfo<>(dictionaryCategoryService.find(page, dictionaryCategorySearchBo)));
    }

    @Operation(description = "增加字典类别")
    @PostMapping
    public ResponseDTO<Long> add(
            @Parameter(description = "字典类别对象")
            @RequestBody @Valid DictionaryCategory dictionaryCategory) {
        dictionaryCategoryService.insert(dictionaryCategory);
        return ResponseDTO.ok(dictionaryCategory.getId());
    }

    @Operation(description = "根据id删除字典类别")
    @DeleteMapping("/{id}")
    public ResponseDTO<Integer> delete(@Parameter(description = "字典类别id")
                                       @PathVariable("id") @Valid @NotNull(message = "字典類別id不能為空") Long id) {
        return ResponseDTO.ok(dictionaryCategoryService.delete(id));
    }

    @Operation(description = "根据id修改字典类别")
    @PutMapping
    public ResponseDTO<Integer> update(
            @Parameter(description = "字典类别对象")
            @RequestBody @Valid DictionaryCategory dictionaryCategory) {
        return ResponseDTO.ok(dictionaryCategoryService.update(dictionaryCategory));
    }

}