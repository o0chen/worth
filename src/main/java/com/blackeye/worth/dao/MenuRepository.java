package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;

import java.util.List;

public interface MenuRepository extends BaseRepository<SysRole,String> {

    List<SysMenuPermission> findByRoleIdAndType(String roleId, MenuTypeEnum type);
}
