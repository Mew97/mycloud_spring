package com.demo05.demo.service;

import com.demo05.demo.model.User;

public interface UserService {
    //查找用户
    User findUserById(Long id);

    User updateUuid(String uuid, User user);

    String isRegistered(User user);
}
