package com.rolin.orangesmart.model.common.dto;

import jakarta.validation.constraints.Min;

public class PageDTO {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码不能小于1")
    private int pageNum = 1;

    /**
     * 页数
     */
    @Min(value = 1, message = "页数不能小于1")
    private int pageSize = defaultPageSize;

    private static final int maxPageSize = 5000;
    private static final int defaultPageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > maxPageSize) {
            this.pageSize = defaultPageSize;
        } else {
            this.pageSize = pageSize;
        }
    }

}