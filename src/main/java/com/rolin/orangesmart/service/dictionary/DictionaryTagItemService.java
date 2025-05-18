package com.rolin.orangesmart.service.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.exception.errorEnum.DictionaryErrorEnum;
import com.rolin.orangesmart.mapper.dictionary.DictionaryTagItemMapper;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTagItem;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DictionaryTagItemService {

    @Autowired
    private DictionaryTagItemMapper dictionaryTagItemMapper;

    public List<DictionaryTagItem> findByTagIds(Collection<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        return dictionaryTagItemMapper.findByTagIds(tagIds);
    }

    public DictionaryTagItem findByTagIdAndLanguageType(Long tagId, String languageType) {
        return dictionaryTagItemMapper.findByTagIdAndLanguageType(tagId, languageType);
    }

    public int deleteByCategoryId(Long categoryId) {
        return dictionaryTagItemMapper.deleteByCategoryId(categoryId);
    }

    public int deleteByTagId(Long tagId) {
        return dictionaryTagItemMapper.deleteByTagId(tagId);
    }

    public List<DictionaryTagItem> save(List<DictionaryTagItemVo> dictionaryTagItemVos, Long categoryId, Long tagId) {
        List<DictionaryTagItem> dictionaryTagItems = new ArrayList<>();
        if (dictionaryTagItemVos != null) {
            for (DictionaryTagItemVo dictionaryTagItemVo : dictionaryTagItemVos) {
                DictionaryTagItem dictionaryTagItem = this.save(dictionaryTagItemVo, categoryId, tagId);
                dictionaryTagItems.add(dictionaryTagItem);
            }
        }
        return dictionaryTagItems;
    }

    private DictionaryTagItem save(DictionaryTagItemVo dictionaryTagItemVo, Long categoryId, Long tagId) {
        if (dictionaryTagItemVo == null) {
            return null;
        }
        DictionaryTagItem dictionaryTagItem = new DictionaryTagItem();
        BeanUtils.copyProperties(dictionaryTagItemVo, dictionaryTagItem);
        dictionaryTagItem.setCategoryId(categoryId);
        dictionaryTagItem.setTagId(tagId);
        dictionaryTagItem.setLanguageType(LanguageType.getByCode(dictionaryTagItemVo.getLanguageType()));
        this.check(dictionaryTagItem);
        if (dictionaryTagItem.getId() == null) {
            if (!StringUtils.hasText(dictionaryTagItem.getItemName())) {
                dictionaryTagItem = null;
            } else {
                this.insert(dictionaryTagItem);
            }
        } else {
            if (StringUtils.hasText(dictionaryTagItem.getItemName())) {
                this.update(dictionaryTagItem);
            } else {
                this.delete(dictionaryTagItem.getId());
                dictionaryTagItem = null;
            }
        }
        return dictionaryTagItem;
    }

    private int delete(Long id) {
        return dictionaryTagItemMapper.delete(id);
    }

    private void check(DictionaryTagItem dictionaryTagItem) {
        boolean flag = dictionaryTagItemMapper.exist(dictionaryTagItem);
        DictionaryErrorEnum.BASE_DICTIONARY_ITEM_EXIST_ERROR.isTrue(flag);
    }

    private DictionaryTagItem findById(Long id) {
        return dictionaryTagItemMapper.findById(id);
    }

    public void insert(DictionaryTagItem dictionaryTagItem) {
        dictionaryTagItem.setCreatedUserId(ReqEnvContext.getUser().getId());
        dictionaryTagItemMapper.insert(dictionaryTagItem);
    }

    public int update(DictionaryTagItem dictionaryTagItem) {
        DictionaryTagItem dbDictionaryTagItem = this.findById(dictionaryTagItem.getId());
//        SecurityErrorEnum.BASE_SECURITY_PERMISSION_ITEM_NOT_EXIST_ERROR.isNull(dbDictionaryTagItem);
        dictionaryTagItem.setUpdatedUserId(ReqEnvContext.getUser().getId());
        return dictionaryTagItemMapper.update(dictionaryTagItem);
    }

}