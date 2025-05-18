package com.rolin.orangesmart.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
public class JwtTokenUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    //Jwt Signature签名秘钥
    private static final String SECRET_KEY = "tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7tdrdadq59tbss5n7";

    //Jwt Payload 使用的Aes对称加密秘钥
    private static final String SECRET_KEY_SPEC = "0000000671595991";
    private static final String IV_PARAMETER_SPEC = "tdrdadq59tbss5n7";

    private static final String BEARER = "Bearer ";


    private JwtTokenUtil() {
    }

    public static String getUserNamekey(String authorization) {
        //1、获取token
        String token;
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER)) {
            token = authorization.substring(BEARER.length(), authorization.length()); // 返回Token字符串，去除Bearer
        }else{
            return null;
        }

        //2、Jwt解析，获取subject
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token).getPayload();
        }catch (Exception e){
            log.error("authorization：{} 无效", authorization);
        }
        String subject = Optional.ofNullable(claims).map(Claims::getSubject).orElse(null);

        //3、解密subject，获取userNameKey
        String userNameKey = null;
        if (StringUtils.hasText(subject)) {
            userNameKey = AesUtil.decrypt(subject, JwtTokenUtil.SECRET_KEY_SPEC, JwtTokenUtil.IV_PARAMETER_SPEC);
        }
        return userNameKey;
    }

    public static String generateAuthorization(String userNameKey) {
        //1、加密userNameKey，获取subject
        String subject = AesUtil.encrypt(userNameKey, JwtTokenUtil.SECRET_KEY_SPEC, JwtTokenUtil.IV_PARAMETER_SPEC);
        //2、Jwt生成token
        String token = Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        //3、产生authorization
        String authorization = BEARER + token;
        return authorization;
    }

    public static void main(String[] args) {
        String userkey = "aaaaaaaaaaa";
        String token = generateAuthorization(userkey);
        System.out.println(token);
        userkey = getUserNamekey(token);
        System.out.println(userkey);
    }

}
