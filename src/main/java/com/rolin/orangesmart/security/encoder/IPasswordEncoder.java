package com.rolin.orangesmart.security.encoder;

public interface IPasswordEncoder {

    String frontendEncode(String rawPassword);

    String backendEncode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);


}