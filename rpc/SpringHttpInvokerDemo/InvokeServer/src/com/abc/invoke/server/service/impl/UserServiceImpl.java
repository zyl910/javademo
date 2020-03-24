package com.abc.invoke.server.service.impl;

import com.abc.invoke.bean.User;
import com.abc.invoke.server.service.UserService;

public class UserServiceImpl implements UserService {
    
    public User getUserbyName(String name) {
        User u = new User();
        u.setName(name);
        u.setEmail("abc@abc.com");
        u.setAge(20);
        return u;
    }
    
}