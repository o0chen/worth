package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<SysUser,String> implements IUserService{
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private EntityManager entityManager;
    public SysUser findUserByName(String name) {
        SysUser sysUser = null;
        try {
            sysUser = userRepository.findByName(name);
            userRepository.sayHello(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysUser;
    }
}