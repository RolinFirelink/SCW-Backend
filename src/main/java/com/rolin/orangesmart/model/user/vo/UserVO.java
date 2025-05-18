package com.rolin.orangesmart.model.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserVO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "昵称")
    private String userName;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "手机")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "额外属性")
    private String extendAttribute;

    @Schema(description = "逻辑删除")
    private Integer isDelete;

    @Schema(description = "是否启用")
    private Boolean activeFlag;

    @Schema(description = "关注的人数")
    @JsonIgnore
    private Integer followCount;

    @Schema(description = "粉丝数")
    @JsonIgnore
    private Integer fansCount;

    @JsonIgnore
    @Schema(description = "帖子点赞的总数量")
    private long postLikeCount;

    @JsonIgnore
    @Schema(description = "禁言状态")
    private String banDesc;
}
