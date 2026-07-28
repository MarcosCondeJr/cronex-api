package com.chronex.cronex_api.service;

import java.util.UUID;

import com.chronex.cronex_api.entity.User;

public abstract class BaseService {
    protected CurrentUserService currentUserService;

    public BaseService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    protected User getCurrentUser() {
        return currentUserService.getCurrentUser();
    }

    protected UUID getCurrentUserId() {
        return currentUserService.getCurrentUserId();
    }
}
