package com.rolin.orangesmart.model.visit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class VisitVO {

    @Schema(description = "附件访问次数")
    private Integer attachment;

    @Schema(description = "轮播图访问次数")
    private Integer carousel;

    @Schema(description = "爬虫访问次数")
    private Integer crawler;

    @Schema(description = "下载访问次数")
    private Integer download;

    @Schema(description = "专家访问次数")
    private Integer expert;

    @Schema(description = "智能访问次数")
    private Integer smart;

    @Schema(description = "上传访问次数")
    private Integer upload;

    @Schema(description = "用户访问次数")
    private Integer user;

    @Schema(description = "问答访问次数")
    private Integer problem;

    @Schema(description = "访问访问次数")
    private Integer visit;

    @Schema(description = "创建日期")
    private Date createDate;

}
