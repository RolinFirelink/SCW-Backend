package com.rolin.orangesmart.service.dictionary;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.exception.errorEnum.DictionaryErrorEnum;
import com.rolin.orangesmart.mapper.dictionary.DictionaryCategoryMapper;
import com.rolin.orangesmart.model.dictionary.bo.DictionaryCategorySearchBo;
import com.rolin.orangesmart.model.dictionary.entity.DictionaryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictionaryCategoryService {

    @Autowired
    private DictionaryCategoryMapper dictionaryCategoryMapper;

    @Autowired
    private DictionaryTagService dictionaryTagService;

    @Autowired
    private DictionaryTagItemService dictionaryTagItemService;

    /**
     * 获取全部DictionaryCategory记录
     *
     * @param dictionaryCategorySearchBo
     * @return List<DictionaryCategory>
     */
    public List<DictionaryCategory> find(DictionaryCategorySearchBo dictionaryCategorySearchBo) {
        return dictionaryCategoryMapper.find(dictionaryCategorySearchBo);
    }

    public IPage<DictionaryCategory> find(IPage page, DictionaryCategorySearchBo dictionaryCategorySearchBo) {
        return dictionaryCategoryMapper.find(page, dictionaryCategorySearchBo);
    }

    /**
     * 根据id获取DictionaryCategory记录
     *
     * @param id
     * @return DictionaryCategory
     */
    public DictionaryCategory getById(Long id) {
        return dictionaryCategoryMapper.getById(id);
    }


    public DictionaryCategory findWithCategoryCodeAndAppName(String categoryCode, String appName) {
        return dictionaryCategoryMapper.findWithCategoryCodeAndAppName(categoryCode, appName);
    }

    /**
     * 插入DictionaryCategory记录
     *
     * @param dictionaryCategory
     */
    public void insert(DictionaryCategory dictionaryCategory) {
        // 先检查是否存在相同分类，appName的数据
        // 如果已有数据则抛出异常，若无，继续增加字典类别数据
        check(dictionaryCategory);
        dictionaryCategory.setCreatedUserId(ReqEnvContext.getUser().getId());
        dictionaryCategory.setUpdatedUserId(ReqEnvContext.getUser().getId());
        dictionaryCategoryMapper.insert(dictionaryCategory);
    }

    /**
     * 检查是否已经存在同编号的分类，不同appName间也不允许出现相同编号的分类
     *
     * @param dictionaryCategory
     */
    private void check(DictionaryCategory dictionaryCategory) {
        boolean flag = dictionaryCategoryMapper.exist(dictionaryCategory);
        DictionaryErrorEnum.BASE_DICTIONARY_CATEGORY_EXIST_ERROR.isTrue(flag);
    }

    /**
     * 根据ID修改DictionaryCategory记录
     *
     * @param dictionaryCategory
     * @return int
     */
    public int update(DictionaryCategory dictionaryCategory) {
        check(dictionaryCategory);
        dictionaryCategory.setUpdatedUserId(ReqEnvContext.getUser().getId());
        return dictionaryCategoryMapper.update(dictionaryCategory);
    }

    /**
     * 根据ID删除DictionaryCategory记录
     *
     * @param id
     * @return int
     */
    @Transactional
    public int delete(Long id) {
        DictionaryCategory dictionaryCategory = dictionaryCategoryMapper.getById(id);
        DictionaryErrorEnum.BASE_DICTIONARY_CATEGORY_DELETE_ERROR.isNull(dictionaryCategory);
        dictionaryTagService.deleteByCategoryId(id);
        dictionaryTagItemService.deleteByCategoryId(id);
        return dictionaryCategoryMapper.delete(id);
    }

}