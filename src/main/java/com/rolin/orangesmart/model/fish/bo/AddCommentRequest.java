package com.rolin.orangesmart.model.fish.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddCommentRequest {

    @Schema(description = "帖子id")
    private Integer postId;

    @Schema(description = "评论内容")
    private String context;
}
