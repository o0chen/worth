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
public class MenuServiceImpl extends BaseServiceImpl<SysMenuPermission, String> implements IMenuService {

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.setBaseRepository(menuRepository);
        this.menuRepository = menuRepository;
    }

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<SysMenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type) {
       // return menuRepository.findByRoleIdAndType;
        return null;
    }

    @Override
    public SysMenuPermission findById(String id) {
        return  menuRepository.findById(id).get();
    }

    /** MenuOrder
     * 返回最大排序
     * @return
     */
    public Integer getMaxMenuOrder(){
        return menuRepository.getMaxMenuOrder();
    }

    @Override
    public List<SysMenuPermission> findByParentIdOrderByMenuOrderAsc(String parentId) {
        return menuRepository.findByParentIdOrderByMenuOrderAsc(parentId);
    }


}