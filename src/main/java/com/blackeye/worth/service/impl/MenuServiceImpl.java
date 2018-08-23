package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.MenuRepository;
import com.blackeye.worth.dao.RoleRepository;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.Menu;
import com.blackeye.worth.model.Permission;
import com.blackeye.worth.model.Role;
import com.blackeye.worth.model.User;
import com.blackeye.worth.service.ILoginService;
import com.blackeye.worth.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl extends BaseServiceImpl<User,String> implements IMenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List<Menu> findByRoleIdsAndType(List<String> roleIds, MenuTypeEnum type) {
        return null;
    }
}