package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {
    User findByUserName(String userName);
}
