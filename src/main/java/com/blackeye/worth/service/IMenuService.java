package com.blackeye.worth.service;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.Menu;

import java.util.List;

public interface IMenuService {
    List<Menu> findByRoleIdsAndType(List<String> roleIds, MenuTypeEnum type);
}
