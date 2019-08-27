package com.demo05.demo.service.impl;

import com.demo05.demo.dao.UserRepo;
import com.demo05.demo.model.User;
import com.demo05.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Resource
    private UserRepo userRepo;

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    @Override
    public User updateUuid(String uuid, User user) {
        int i = userRepo.setUuidByName(uuid, user.getName());
        return userRepo.getFirstByNameAndPwd(user.getName(), user.getPwd());
    }

    @Override
    public String isRegistered(User user) {
        if (userRepo.existsByName(user.getName())){
            return "用户名已存在";
        } else if (userRepo.existsByPhoneNum(user.getPhoneNum())){
            return "手机号已注册";
        } else {
            return "可以注册";
        }
    }

}
