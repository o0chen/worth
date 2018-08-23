package com.blackeye.worth.service;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.SysMenuPermission;

import java.util.List;

public interface IMenuService {
    List<SysMenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type);
}
