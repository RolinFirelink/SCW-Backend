package com.rolin.orangesmart.util;

import com.rolin.orangesmart.exception.SystemException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class FileIdJasyptUtil {

    private FileIdJasyptUtil(){

    }

    private static final String KEY = "0000000671595991";
    private static final String IV = "fdgdsgu52424hjui";
    private static final String AES_TYPE = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";

    private static final String SEPARATOR = "||";
    private static final String REGEX_SEPARATOR = "\\|\\|";

    public static String idEncrypt(long id, long validitySeconds) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), AES);
            Cipher encryptCipher = Cipher.getInstance(AES_TYPE);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            String message = System.currentTimeMillis() + SEPARATOR + id + SEPARATOR + (System.currentTimeMillis() + validitySeconds * 1000);
            byte[] encryptedData = encryptCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encrypted = String.valueOf(Hex.encodeHex(encryptedData));
            return encrypted;
        } catch (Exception e) {
            throw new SystemException("无效参数", e);
        }
    }

    public static long idDecrypt(String encryptedId) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), AES);
            Cipher decryptCipher = Cipher.getInstance(AES_TYPE);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] outBytes = decryptCipher.doFinal(Hex.decodeHex(encryptedId.toCharArray()));
            String decrypted = new String(outBytes);
            String tmp[] = decrypted.split(REGEX_SEPARATOR);
            if (System.currentTimeMillis() > Long.valueOf(tmp[2])) {
                throw new SystemException("参数已过期");
            }
            long decryptedId = Long.parseLong(tmp[1]);
            return decryptedId;
        } catch (Exception e) {
            throw new SystemException("无效参数", e);
        }
    }

}
