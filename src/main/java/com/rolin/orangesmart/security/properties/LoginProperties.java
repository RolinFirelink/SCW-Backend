package com.rolin.orangesmart.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoginProperties {

    private String initAccount = "root";

    private boolean enableEmailLogin = true;

    private boolean enableMobileLogin = true;

    private boolean caseInsensitive = false;

//  private boolean enableFirstTimeChangePassword = true;

    private boolean enableMultiLogin = false;

    private int maxMultiLoginCount = 5;

    private Integer sessionTime = 60 * 60;

    private boolean enableLockLogin = true;

    private Integer errorDuringTime = 24 * 60 * 60;

    private Integer errorCount = 6;

    private Integer lockTime = 1 * 60 * 60;

}