package com.blackeye.worth.dao;

import com.blackeye.worth.customer.BaseRepository;
import com.blackeye.worth.model.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoty extends BaseRepository<SysUser,Long> {
@Query(value = "select * from User t where t.name = :name", nativeQuery = true)
SysUser findByName(@Param("name") String name);
}