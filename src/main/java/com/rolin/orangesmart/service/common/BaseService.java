package com.rolin.orangesmart.service.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rolin.orangesmart.model.common.info.PageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 分页查询 - 适配项目规范
     */
    public PageInfo<T> page(int pageNum, int pageSize) {
        IPage<T> pager = new Page<>(pageNum, pageSize);


        return new PageInfo<>(super.page(pager));
    }

    /**
     * 分页查询 - 适配项目规范
     */
    public PageInfo<T> page(int pageNum, int pageSize, Wrapper<T> wrapper) {
        IPage<T> pager = new Page<>(pageNum, pageSize);
        IPage<T> page = super.page(pager, wrapper);
        return new PageInfo<>(page);
    }

    /**
     * 分页查询 - 适配项目规范
     */
    public PageInfo<T> page(int pageNum, int pageSize, List<T> datas) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        if (null == datas) {
            return pageInfo;
        }
        int size = datas.size();
        pageInfo.setTotal(size);
        List<T> result = new ArrayList<>();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(pageNum * pageSize, size);
        for (int i = start; i < end; i++) {
            result.add(datas.get(i));
        }
        pageInfo.setList(result);
        return pageInfo;
    }

    /**
     * 新增 - 适配项目规范
     * insert -> save
     *
     * @param entity
     * @return
     */
    public boolean insert(T entity) {
        return super.save(entity);
    }

    /**
     * 删除 - 适配项目规范
     * deleteById -> removeById
     */
    public boolean deleteById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 删除 - 适配项目规范
     * delete -> remove
     *
     * @param wrapper
     * @return
     */
    public boolean delete(Wrapper<T> wrapper) {
        return super.remove(wrapper);
    }

    /**
     * 删除 - 适配项目规范
     * deleteByIds -> removeByIds
     *
     * @param ids
     * @return
     */
    public boolean deleteByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

}