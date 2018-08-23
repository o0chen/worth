package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.SysRole;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<SysRole,String> {
//@Query(value = "select * from SysUser t where t.name = :name", nativeQuery = true)
//SysUser findByName(@Param("name") String name);
}