package com.blackeye.worth.service;

import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.model.SysUser;

import java.util.Map;

public interface ILoginService  {
    //添加用户
    SysUser addUser(Map<String, Object> map);

    //添加角色
    SysRole addRole(Map<String, Object> map);

    //查询用户通过用户名
    SysUser findByName(String name);
}
