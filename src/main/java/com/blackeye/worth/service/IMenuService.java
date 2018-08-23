package com.blackeye.worth.service;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.MenuPermission;

import java.util.List;

public interface IMenuService {
    List<MenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type);
}
