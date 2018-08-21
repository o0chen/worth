package com.blackeye.worth.service;

import com.blackeye.worth.model.User;

public interface IUserService {
    public User findUserByName(String name);
}