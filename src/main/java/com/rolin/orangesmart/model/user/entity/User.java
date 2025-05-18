package com.rolin.orangesmart.model.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.user.vo.UserVO;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractPO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    private String userName;

    private Integer userType;

    private String avatar;

    private String name;

    private String mobile;

    private String email;

    private String extendAttribute;

    private Boolean activeFlag;

    @TableField(exist = false)
    private Boolean kickFlag;

    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    public User(Long id, String userName) {
        this.setId(id);
        this.userName = userName;
    }

    public UserVO toVo() {
        return BeanUtil.copyProperties(this, UserVO.class);
    }

}
