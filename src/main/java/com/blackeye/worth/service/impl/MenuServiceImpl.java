package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.MenuRepository;
import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends BaseServiceImpl<SysUser,String> implements IMenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List<SysMenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type) {
        return menuRepository.findByRoleIdAndType(roleId,type);
    }
}