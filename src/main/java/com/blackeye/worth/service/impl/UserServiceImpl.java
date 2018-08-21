package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.User;
import com.blackeye.worth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User,String> implements IUserService{
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private EntityManager entityManager;
    public User findUserByName(String name) {
        User user = null;
        try {
            user = userRepository.findByName(name);
            userRepository.sayHello(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}