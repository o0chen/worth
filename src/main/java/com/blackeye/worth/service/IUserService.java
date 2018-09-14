package com.blackeye.worth.service;

import com.blackeye.worth.core.customer.BaseService;
import com.blackeye.worth.model.SysUser;

public interface IUserService extends BaseService<SysUser,String> {
    public SysUser findUserByName(String name);

    public void test(String name);

//    Page<SysUser> listSysUserByPage(Predicate predicate, PageRequest pageRequest);

//    SysUser saveOrUpdateSysUser(SysUser sysUser);
}