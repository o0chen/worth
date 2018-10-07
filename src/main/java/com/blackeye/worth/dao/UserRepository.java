package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.QSysUser;
import com.blackeye.worth.model.SysUser;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<SysUser,String>, QuerydslBinderCustomizer<QSysUser> {
    //@Query(value = "select * from sysUser t where t.name = :name", nativeQuery = true)
    SysUser findByName(@Param("name") String name);

    default void customize(QuerydslBindings bindings, QSysUser qSysUser) {
        bindings.bind(qSysUser.name).first(StringExpression::containsIgnoreCase); //模糊查询
    }



}