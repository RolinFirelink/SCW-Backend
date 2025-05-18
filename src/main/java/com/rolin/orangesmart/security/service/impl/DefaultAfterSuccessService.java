package com.rolin.orangesmart.security.service.impl;

import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.security.service.IAfterSuccessService;
import com.rolin.orangesmart.security.service.ILockUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultAfterSuccessService implements IAfterSuccessService {

    @Autowired(required = false)
    private ILockUserService lockUserService;

    @Override
    public void afterSuccess(User user) {
        if (lockUserService != null) {
            lockUserService.ifLockUser(true, user);
        }
    }

}
