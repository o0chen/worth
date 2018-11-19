package com.blackeye.worth.service;

import com.blackeye.worth.core.customer.BaseService;
import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.SysMenuPermission;

import java.util.List;

public interface IMenuService extends BaseService<SysMenuPermission,String> {
    List<SysMenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type);

    SysMenuPermission findById(String id);

    Integer getMaxMenuOrder();

    List<SysMenuPermission> findByParentIdOrderByMenuOrderAsc(String parentId);

}
