package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.MenuRepository;
import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.MenuPermission;
import com.blackeye.worth.model.User;
import com.blackeye.worth.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends BaseServiceImpl<User,String> implements IMenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List<MenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type) {
        return null;
    }
}