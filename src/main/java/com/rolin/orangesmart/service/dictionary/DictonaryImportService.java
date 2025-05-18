package com.rolin.orangesmart.service.dictionary;

import java.util.List;

import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTag;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTagItem;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagItemVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryWithTagsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class DictonaryImportService {

    @Autowired
    private DictionaryCategoryService dictionaryCategoryService;

    @Autowired
    private DictionaryTagService dictionaryTagService;

    @Autowired
    private DictionaryTagItemService dictionaryTagItemService;

    @Transactional
    public void importDataNew(List<DictionaryWithTagsVO> dictionaryWithTagsVOs) {
        if (CollectionUtils.isEmpty(dictionaryWithTagsVOs)) return;
        dictionaryWithTagsVOs.forEach(DictionaryWithTagsVO -> {
            this.importDictionaryWithTagsVO(DictionaryWithTagsVO);
        });
    }

    private void importDictionaryWithTagsVO(DictionaryWithTagsVO dictionaryWithTagsVO) {
        DictionaryCategory dictionaryCategory = dictionaryCategoryService
                .findWithCategoryCodeAndAppName(dictionaryWithTagsVO.getCategoryCode(), dictionaryWithTagsVO.getAppName());
        if (dictionaryCategory == null) {// component不存在导入的环境中
            dictionaryCategory = new DictionaryCategory();
            BeanUtils.copyProperties(dictionaryWithTagsVO, dictionaryCategory);
            dictionaryCategory.setId(null);
            dictionaryCategoryService.insert(dictionaryCategory);
        }
        Long dictionaryCategoryId = dictionaryCategory.getId();
        if (CollectionUtils.isEmpty(dictionaryWithTagsVO.getDictionaryTagVos())) return; // forEach中等价于continue
        dictionaryWithTagsVO.getDictionaryTagVos().forEach(dtv -> this.importDictionaryTagVo(dtv, dictionaryCategoryId));
    }

    private void importDictionaryTagVo(DictionaryTagVo dictionaryTagVo, Long categoryId) {
        DictionaryTag dictionaryTag = dictionaryTagService.findByCategoryIdAndTagCode(categoryId, dictionaryTagVo.getTagCode());
        if (dictionaryTag == null) {
            dictionaryTag = new DictionaryTag();
            BeanUtils.copyProperties(dictionaryTagVo, dictionaryTag);
            dictionaryTag.setCategoryId(categoryId);
            dictionaryTag.setId(null);
            dictionaryTagService.insert(dictionaryTag);
        }
        Long dictionaryTagId = dictionaryTag.getId();
        dictionaryTagVo.getDictionaryTagItemVos().forEach(dtiv -> this.importDictionaryTagVo(dtiv, categoryId, dictionaryTagId));
    }

    private void importDictionaryTagVo(DictionaryTagItemVo dictionaryTagItemVo, Long categoryId, Long tagId) {
        LanguageType languageType = LanguageType.getByCode(dictionaryTagItemVo.getLanguageType());
        DictionaryTagItem dictionaryTagItem = dictionaryTagItemService.findByTagIdAndLanguageType(tagId, languageType.toString());
        if (dictionaryTagItem == null) {
            dictionaryTagItem = new DictionaryTagItem();
            BeanUtils.copyProperties(dictionaryTagItemVo, dictionaryTagItem);
            dictionaryTagItem.setCategoryId(categoryId);
            dictionaryTagItem.setTagId(tagId);
            dictionaryTagItem.setId(null);
            dictionaryTagItem.setLanguageType(languageType);
            dictionaryTagItemService.insert(dictionaryTagItem);
        } else {
            dictionaryTagItem.setItemName(dictionaryTagItemVo.getItemName());
            dictionaryTagItem.setItemName2(dictionaryTagItemVo.getItemName2());
            dictionaryTagItemService.update(dictionaryTagItem);
        }
    }

}
