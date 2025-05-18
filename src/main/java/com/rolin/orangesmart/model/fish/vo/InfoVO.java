package com.rolin.orangesmart.model.fish.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/12
 * @description
 */
@Data
public class InfoVO {

    @Schema(description = "新增用户数量")
    private Integer newUserCount;

    @Schema(description = "新增帖子数量")
    private Integer newPostCount;

    @Schema(description = "活跃用户数量")
    private Integer activeUserCount;

    @Schema(description = "日期")
    private String date;
}
