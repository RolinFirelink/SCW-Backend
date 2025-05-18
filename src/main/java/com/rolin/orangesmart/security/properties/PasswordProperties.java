package com.rolin.orangesmart.security.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class PasswordProperties {

    private boolean enableDefault = false;

    private boolean enableEncrypt = false;

    private Integer publicKeyExpireSecond = 60;

    private String defaultRegister = "a123456";

    private String defaultReset = "a123456";

    private Integer expireDate = -1;

    private boolean enableRegex = false;

    private String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}";

    private String rootRegex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{12,16}";

//  private boolean enableLengthValid = false;
//
//  private Integer minLength = 6;
//
//  private Integer maxLength = 20;

    private boolean enableForce = false;

    private Integer forceCycle = 30;

    private Integer forceNotify = 3;

    private Integer historyCount = 6;

}
