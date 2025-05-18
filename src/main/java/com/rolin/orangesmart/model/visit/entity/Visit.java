package com.rolin.orangesmart.model.visit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.visit.vo.VisitVO;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("visit")
public class Visit extends AbstractPO {

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

    @Schema(description = "通用访问次数")
    private Integer common;

    @Schema(description = "帖子访问次数")
    private Integer post;

    @Schema(description = "举报访问次数")
    private Integer report;

    @Schema(description = "敏感词访问次数")
    private Integer sensitiveWord;

    @Schema(description = "问答访问次数")
    private Integer problem;

    @Schema(description = "访问访问次数")
    private Integer visit;

    public VisitVO toVo(){
        return BeanUtil.copyProperties(this, VisitVO.class);
    }
}
