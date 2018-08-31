package com.blackeye.worth.service;

import com.blackeye.worth.model.SysUser;

public interface IUserService {
    public SysUser findUserByName(String name);

    public void test(String name);

}