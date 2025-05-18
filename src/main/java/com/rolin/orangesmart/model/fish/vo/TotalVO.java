package com.rolin.orangesmart.model.fish.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/12
 * @description
 */
@Data
public class TotalVO {


    @Schema(description = "累计用户数量")
    private Integer totalUser;

    @Schema(description = "累计帖子数量")
    private Integer totalPost;

    @Schema(description = "累计点赞数")
    private Long totalLike;
}
