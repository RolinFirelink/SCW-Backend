package com.rolin.orangesmart.security.service;

public interface IAfterFailureService {

    void afterFailure(String account, String userType, String errorMessage);

}