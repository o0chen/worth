package com.blackeye.worth.service;

import com.blackeye.worth.model.SysUser;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    public SysUser findUserByName(String name);

    public void test(String name);

    Page<SysUser> listSysUserByPage(Predicate predicate, PageRequest pageRequest);
}