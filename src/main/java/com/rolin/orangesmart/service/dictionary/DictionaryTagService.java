package com.rolin.orangesmart.service.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.cache.CacheDataRedisProducerService;
import com.rolin.orangesmart.constant.CacheDataConstant;
import com.rolin.orangesmart.constant.LanguageType;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.exception.errorEnum.DictionaryErrorEnum;
import com.rolin.orangesmart.mapper.dictionary.DictionaryCategoryMapper;
import com.rolin.orangesmart.mapper.dictionary.DictionaryTagMapper;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryTagSearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTag;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryTagItem;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagItemVo;
import com.rolin.orangesmart.model.dictionary.vo.DictionaryTagVo;
import com.rolin.orangesmart.properties.LanguageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictionaryTagService {

    @Autowired
    private DictionaryTagMapper dictionaryTagMapper;

    @Autowired
    private DictionaryTagItemService dictionaryTagItemService;

    @Autowired
    private LanguageProperties languageProperties;

    //为防止循环依赖DictionaryCategoryService，改为依赖dictionaryCategoryMapper，不推荐这么做
    @Autowired
    private DictionaryCategoryMapper dictionaryCategoryMapper;

    @Autowired(required = false)
    private CacheDataRedisProducerService cacheDataRedisProducerService;

    /**
     * 根据id获取DictionaryTag记录
     *
     * @param id
     * @return DictionaryTag
     */
    public DictionaryTag findById(Long id) {
        return dictionaryTagMapper.findById(id);
    }

    public DictionaryTag findByCategoryIdAndTagCode(Long categoryId, String tagCode) {
        return dictionaryTagMapper.findByCategoryIdAndTagCode(categoryId, tagCode);
    }

    /**
     * 插入DictionaryTag记录
     *
     * @param dictionaryTag
     */
    public void insert(DictionaryTag dictionaryTag) {
        dictionaryTag.setCreatedUserId(ReqEnvContext.getUser().getId());
        dictionaryTagMapper.insert(dictionaryTag);
    }

    public void update(DictionaryTag dictionaryTag) {
        dictionaryTag.setUpdatedUserId(ReqEnvContext.getUser().getId());
        dictionaryTagMapper.update(dictionaryTag);
    }

    /**
     * 根据ID修改DictionaryTag记录
     *
     * @param dictionaryTagVo
     * @return DictionaryTagVo
     */
    @Transactional
    public DictionaryTagVo save(DictionaryTagVo dictionaryTagVo) {
        DictionaryTag dictionaryTag = new DictionaryTag();
        BeanUtils.copyProperties(dictionaryTagVo, dictionaryTag);
        this.check(dictionaryTag);
        if (dictionaryTag.getId() != null) {
            this.update(dictionaryTag);
        } else {
            this.insert(dictionaryTag);
        }

        DictionaryCategory dictionaryCategory = dictionaryCategoryMapper.getById(dictionaryTag.getCategoryId());
        dictionaryCategoryMapper.update(dictionaryCategory);

        List<DictionaryTagItem> dictionaryTagItems = dictionaryTagItemService.save(dictionaryTagVo.getDictionaryTagItemVos(),
                dictionaryTag.getCategoryId(), dictionaryTag.getId());
        DictionaryTagVo dictionaryTagVo2 = this.toVo(dictionaryTag, dictionaryTagItems);
        this.productRedisMessage();
        return dictionaryTagVo2;
    }

    private DictionaryTagVo toVo(DictionaryTag dictionaryTag, List<DictionaryTagItem> dictionaryTagItems) {
        DictionaryTagVo dictionaryTagVo = DictionaryTagVo.from(dictionaryTag);
        List<DictionaryTagItemVo> dictionaryTagItemVos = new ArrayList<>();
        for (DictionaryTagItem dictionaryTagItem : dictionaryTagItems) {
            DictionaryTagItemVo dictionaryTagItemVo = DictionaryTagItemVo.from(dictionaryTagItem);
            dictionaryTagItemVos.add(dictionaryTagItemVo);
        }
        dictionaryTagVo.setDictionaryTagItemVos(dictionaryTagItemVos);
        return dictionaryTagVo;
    }

    private void check(DictionaryTag dictionaryTag) {
        DictionaryErrorEnum.BASE_DICTIONARY_TAG_NULL_ERROR.isNull(dictionaryTag.getCategoryId());
        DictionaryErrorEnum.BASE_DICTIONARY_TAG_NULL_ERROR.isNull(dictionaryTag.getTagCode());

        boolean flag = dictionaryTagMapper.exist(dictionaryTag);
        DictionaryErrorEnum.BASE_DICTIONARY_TAG_EXIST_ERROR.isTrue(flag);
    }

    /**
     * 根据ID删除DictionaryTag记录
     *
     * @param id
     * @return int
     */
    @Transactional
    public int delete(Long id) {
        DictionaryTag dictionaryTag = this.findById(id);
        DictionaryErrorEnum.BASE_DICTIONARY_TAG_DELETE_ERROR.isNull(dictionaryTag);
        dictionaryTagItemService.deleteByTagId(id);
        return dictionaryTagMapper.delete(id);
    }

    /**
     * <p>Title: 根据分类Code删除子项</p>
     * <p>Description: </p>
     *
     * @param categoryId
     */
    public int deleteByCategoryId(Long categoryId) {
        return dictionaryTagMapper.deleteByCategoryId(categoryId);
    }

    /**
     * 根据 类别编码category_code 和 项目名称app_name 获取DictionaryTagList记录
     *
     * @param dictionaryTagSearchBo
     * @return List<DictionaryTag>
     */
    public IPage<DictionaryTagVo> find(IPage<?> page, DictionaryTagSearchBo dictionaryTagSearchBo) {
        IPage<DictionaryTagVo> dictionaryTagVos = dictionaryTagMapper.find(page, dictionaryTagSearchBo);
        this.processDictionaryTagVos(dictionaryTagVos.getRecords());
        return dictionaryTagVos;
    }

    public List<DictionaryTagVo> find(DictionaryTagSearchBo dictionaryTagSearchBo) {
        List<DictionaryTagVo> dictionaryTagVos = dictionaryTagMapper.find(dictionaryTagSearchBo);
        this.processDictionaryTagVos(dictionaryTagVos);
        return dictionaryTagVos;
    }

    private void processDictionaryTagVos(List<DictionaryTagVo> dictionaryTagVos) {
        List<Long> tagIds = dictionaryTagVos.stream().map(d -> d.getId()).collect(Collectors.toList());
        List<DictionaryTagItem> dictionaryTagItems = dictionaryTagItemService.findByTagIds(tagIds);

        Map<Long, List<DictionaryTagItem>> dictionaryTagItemMap = dictionaryTagItems.stream()
                .collect(Collectors.groupingBy(d -> d.getTagId()));
        //获取多语言配置
        List<LanguageType> languageTypes = languageProperties.getLanguageTypesWithOrder();

        dictionaryTagVos.stream().forEach(d -> {
            List<DictionaryTagItem> dictionaryTagItems2 = dictionaryTagItemMap.get(d.getId());
            Map<LanguageType, DictionaryTagItem> dictionaryTagItemMap2 = null;
            if (dictionaryTagItems2 == null) {
                dictionaryTagItemMap2 = Collections.emptyMap();
            } else {
                dictionaryTagItemMap2 = dictionaryTagItems2.stream()
                        .collect(Collectors.toMap(c -> c.getLanguageType(), c -> c));
            }
            List<DictionaryTagItemVo> dictionaryTagItemVos3 = new ArrayList<>();
            for (LanguageType languageType : languageTypes) {
                DictionaryTagItem dictionaryTagItem = dictionaryTagItemMap2.get(languageType);
                if (dictionaryTagItem != null) {
                    dictionaryTagItemVos3.add(DictionaryTagItemVo.from(dictionaryTagItem));
                } else {
                    dictionaryTagItemVos3.add(null);
                }
            }
            d.setDictionaryTagItemVos(dictionaryTagItemVos3);
        });
    }

    public List<DictionaryTagVo> find(Long categoryId) {
        DictionaryTagSearchBo dictionaryTagSearchBo = new DictionaryTagSearchBo();
        dictionaryTagSearchBo.setCategoryId(categoryId);
        return this.find(dictionaryTagSearchBo);
    }

    public List<DictionaryTagVo> findByCategoryId(Long categoryId) {
        return dictionaryTagMapper.findByCategoryId(categoryId);
    }

    private void productRedisMessage() {
        if (cacheDataRedisProducerService != null) {
            cacheDataRedisProducerService.send(CacheDataConstant.DICTONARY_PREFIX);
        }
    }
}
