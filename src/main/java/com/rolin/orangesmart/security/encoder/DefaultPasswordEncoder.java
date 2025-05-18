package com.rolin.orangesmart.security.encoder;

import com.rolin.orangesmart.util.Md5Util;
import org.springframework.stereotype.Component;

/**
 * 默认密码加密编码器
 */
@Component
public class DefaultPasswordEncoder implements IPasswordEncoder {

    @Override
    public String frontendEncode(String rawPassword) {
        String plaintext = this.confuse(rawPassword);
        plaintext = Md5Util.getMD5Code(plaintext);
        return plaintext;
    }

    @Override
    public String backendEncode(String rawPassword) {
        return Md5Util.saltEncrypt(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // MD5加盐加密进行匹配
        return Md5Util.saltMatch(rawPassword, encodedPassword);
    }

    private String confuse(String rawPassword) {
        StringBuilder sb = new StringBuilder();
        if (rawPassword.length() < 2) {
            return rawPassword;
        }
        sb.append(rawPassword.subSequence(0, 2)).append("xx");
        if (rawPassword.length() < 4) {
            return rawPassword;
        }
        sb.append(rawPassword.subSequence(2, 4)).append("yy");
        if (rawPassword.length() < 6) {
            return rawPassword;
        }
        sb.append(rawPassword.subSequence(4, 6)).append("zz");
        sb.append(rawPassword.subSequence(6, rawPassword.length()));
        return sb.toString();
    }
}