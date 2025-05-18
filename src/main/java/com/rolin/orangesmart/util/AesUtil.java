package com.rolin.orangesmart.util;

import com.rolin.orangesmart.exception.SystemException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class AesUtil {

    private AesUtil() {

    }

    private static final String AES_TYPE = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";

    public static String encrypt(String message, String key, String iv) {
        try {
            // 偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            // 加密
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] messageBase64Data = Base64.getEncoder().encode(message.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedData = cipher.doFinal(messageBase64Data);
            String encryptedBase64Data = Base64.getEncoder().encodeToString(encryptedData);
            return encryptedBase64Data;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }


    public static String decrypt(String encryptedBase64Data, String key, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            // 解密
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64Data.getBytes());
            byte[] messageBase64Data = cipher.doFinal(encryptedData);
            String message = new String(Base64.getDecoder().decode(messageBase64Data), StandardCharsets.UTF_8);
            return message;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}