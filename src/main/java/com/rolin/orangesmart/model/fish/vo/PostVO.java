package com.rolin.orangesmart.model.fish.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/14
 * @description
 */
@Data
public class PostVO {

    @Schema(description = "帖子id")
    private Integer id;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "帖子标题")
    private String title;

    @Schema(description = "帖子内容")
    private String context;

    @Schema(description = "作者id")
    private Integer authorId;

    @Schema(description = "是否已关注")
    private Boolean isFollowed;

    @Schema(description = "帖子图片url列表")
    private List<String> urlList;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "评论数")
    private Long commentCount;

    @Schema(description = "是否已点赞")
    private Boolean isLiked;
}
