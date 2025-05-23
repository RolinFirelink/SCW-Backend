package com.rolin.orangesmart.service.IService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface ISuccessService {

    void success(HttpServletRequest request, HttpServletResponse response,
                 Authentication authentication) throws IOException, ServletException;

}