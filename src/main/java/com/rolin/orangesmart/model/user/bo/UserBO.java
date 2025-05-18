package com.rolin.orangesmart.model.user.bo;

import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.util.BeanUtil;
import lombok.Data;

@Data
public class UserBO {

    private Long id;

    private String account;

    private String password;

    private String userName;

    private Integer userType;

    private String avatar;

    private String name;

    private String mobile;

    private String email;

    private Boolean activeFlag;

    public User toEntity() {
        return BeanUtil.copyProperties(this, User.class);
    }
}
