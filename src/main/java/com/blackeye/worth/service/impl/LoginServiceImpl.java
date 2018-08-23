package com.blackeye.worth.service.impl;

import com.blackeye.worth.dao.RoleRepository;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.MenuPermission;
import com.blackeye.worth.model.Role;
import com.blackeye.worth.model.User;
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
    public User addUser(Map<String, Object> map) {
        User user = new User();
        user.setName(map.get("username").toString());
        user.setPassword(map.get("password").toString());
        userRepository.save(user);
        return user;
    }

    //添加角色
    @Override
    public Role addRole(Map<String, Object> map) {
//        User user = userRepository.findOne(map.get("userId").toString());
        User user = userRepository.findById(map.get("userId").toString()).get();
        Role role = new Role();
        role.setRoleName(map.get("roleName").toString());
        role.setUser(user);
        MenuPermission menuPermission1 = new MenuPermission();
        menuPermission1.setPermission("create");
//        menuPermission1.setRole(role);
        MenuPermission menuPermission2 = new MenuPermission();
        menuPermission2.setPermission("update");
//        menuPermission2.setRole(role);
        List<MenuPermission> menuPermissions = new ArrayList<MenuPermission>();
        menuPermissions.add(menuPermission1);
        menuPermissions.add(menuPermission2);
        role.setMenuPermissions(menuPermissions);
        roleRepository.save(role);
        return role;
    }

    //查询用户通过用户名
    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}