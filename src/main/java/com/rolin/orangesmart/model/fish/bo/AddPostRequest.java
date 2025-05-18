package com.rolin.orangesmart.model.fish.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "添加帖子请求")
public class AddPostRequest {

    @Schema(description = "帖子标题")
    private String title;

    @Schema(description = "帖子内容")
    private String context;

    @Schema(description = "图片id列表")
    private List<Integer> imageIdList;
}