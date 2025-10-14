package com.example.user_service.repository;

import com.example.user_service.entity.User;

public interface UserRepositoryCustom {
    User saveWithEnumCast(User user);
}
