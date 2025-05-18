package com.rolin.orangesmart.security.service.impl;

import com.rolin.orangesmart.security.service.IAfterFailureService;
import com.rolin.orangesmart.security.service.ILockUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultAfterFailureService implements IAfterFailureService {

    @Autowired(required = false)
    private ILockUserService lockUserService;

    @Override
    public void afterFailure(String account, String userType, String errorMessage) {
        if (lockUserService != null) {
            lockUserService.ifLockUser(false, account, userType);
        }
    }

}