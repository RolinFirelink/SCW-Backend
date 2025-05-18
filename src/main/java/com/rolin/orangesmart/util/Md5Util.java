package com.rolin.orangesmart.util;

import com.rolin.orangesmart.exception.SystemException;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;

public final class Md5Util {

    private Md5Util() {
    }

    private static final String KEY_MD5 = "MD5";

    /**
     * 全局数组
     */
    private static final String[] STRING_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STRING_DIGITS[iD1] + STRING_DIGITS[iD2];
    }

    private static String byteToString(byte[] bByte) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < bByte.length; i++) {
            string.append(byteToArrayString(bByte[i]));
        }
        return string.toString();
    }

    public static String getMD5Code(String strObj) {
        try {
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            return byteToString(md.digest(strObj.getBytes()));
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static String saltEncrypt(String plaintext) {
        return saltEncrypt(plaintext, RandomUtil.fixedLengthRandomNumber(6));
    }

    public static String saltEncrypt(String plaintext, String salt) {
        return (getMD5Code(plaintext + salt) + salt).toUpperCase();
    }

    public static boolean saltMatch(String plaintext, String ciphertext) {
        if (StringUtils.hasText(plaintext) && StringUtils.hasText(ciphertext)) {
            if (ciphertext.length() == 38) {
                String salt = ciphertext.substring(32);
                return saltEncrypt(plaintext, salt).equalsIgnoreCase(ciphertext);
            }
        }
        return false;
    }

}
