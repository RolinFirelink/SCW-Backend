package com.rolin.orangesmart.controller.dictionary;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.model.common.dto.PageDTO;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryTagSearchBo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import com.rolin.orangesmart.service.dictionary.DictionaryTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Tag(name = "字典项接口")
@RestController
@RequestMapping("/dictionaryTag")
public class DictionaryTagController {

    @Autowired
    private DictionaryTagService dictionaryTagService;

    @Operation(description = "根据categoryCode查询所有dictionaryTagName返回分页数据")
    @GetMapping("/page")
    public ResponseDTO<PageInfo<DictionaryTagVo>> getItemsPage(
            @Valid PageDTO pageDTO,
            DictionaryTagSearchBo dictionaryTagSearchBo) {
        IPage<?> page = new com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO(pageDTO.getPageNum(), pageDTO.getPageSize());
        return ResponseDTO.ok(new PageInfo<>(dictionaryTagService.find(page, dictionaryTagSearchBo)));
    }

    @Operation(description = "修改字典项")
    @PostMapping
    public ResponseDTO<DictionaryTagVo> save(
            @Parameter(description = "字典项对象")
            @RequestBody @Valid DictionaryTagVo dictionaryItemVo) {
        dictionaryItemVo = dictionaryTagService.save(dictionaryItemVo);
        return ResponseDTO.ok(dictionaryItemVo);
    }

    @Operation(description = "根据id删除字典项")
    @DeleteMapping("/{id}")
    public ResponseDTO<Integer> delete(
            @Parameter(description = "字典项id")
            @PathVariable("id") @Valid @NotNull(message = "字典項id不能為空") Long id) {
        return ResponseDTO.ok(dictionaryTagService.delete(id));
    }

}