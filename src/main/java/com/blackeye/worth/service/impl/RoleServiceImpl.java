package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.RoleRepository;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<SysRole, String> implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.setBaseRepository(roleRepository);
        this.roleRepository=roleRepository;
    }
}
