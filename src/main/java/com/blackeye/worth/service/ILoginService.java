package com.blackeye.worth.service;

import com.blackeye.worth.model.Role;
import com.blackeye.worth.model.User;

import java.util.Map;

public interface ILoginService  {
    //添加用户
    User addUser(Map<String, Object> map);

    //添加角色
    Role addRole(Map<String, Object> map);

    //查询用户通过用户名
    User findByName(String name);
}
