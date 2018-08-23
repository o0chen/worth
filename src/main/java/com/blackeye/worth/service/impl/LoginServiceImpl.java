package com.blackeye.worth.service.impl;

import com.blackeye.worth.dao.RoleRepository;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    //添加用户
    @Override
    public SysUser addUser(Map<String, Object> map) {
        SysUser sysUser = new SysUser();
        sysUser.setName(map.get("username").toString());
        sysUser.setPassword(map.get("password").toString());
        userRepository.save(sysUser);
        return sysUser;
    }

    //添加角色
    @Override
    public SysRole addRole(Map<String, Object> map) {
//        SysUser sysUser = userRepository.findOne(map.get("userId").toString());
        SysUser sysUser = userRepository.findById(map.get("userId").toString()).get();
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(map.get("roleName").toString());
        //sysRole.setSysUser(sysUser);
        SysMenuPermission sysMenuPermission1 = new SysMenuPermission();
        sysMenuPermission1.setPermission("create");
//        sysMenuPermission1.setSysRole(sysRole);
        SysMenuPermission sysMenuPermission2 = new SysMenuPermission();
        sysMenuPermission2.setPermission("update");
//        sysMenuPermission2.setSysRole(sysRole);
        List<SysMenuPermission> sysMenuPermissions = new ArrayList<SysMenuPermission>();
        sysMenuPermissions.add(sysMenuPermission1);
        sysMenuPermissions.add(sysMenuPermission2);
        sysRole.setSysMenuPermissions(sysMenuPermissions);
        roleRepository.save(sysRole);
        return sysRole;
    }

    //查询用户通过用户名
    @Override
    public SysUser findByName(String name) {
        return userRepository.findByName(name);
    }
}