package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.QSysUser;
import com.blackeye.worth.model.SysUser;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserRepository extends BaseRepository<SysUser,String>, QuerydslBinderCustomizer<QSysUser> {



    @Query(value = "select * from sys_user t where t.name = ?", nativeQuery = true)
    SysUser findByName( String name);
//    SysUser findByName(@Param("name") String name);


    @Query(value = "getCustomerBuyLogByDB", nativeQuery = true)
    SysUser test(Map map);//暂时就支持这个吧

    default void customize(QuerydslBindings bindings, QSysUser qSysUser) {
        bindings.bind(qSysUser.name).first(StringExpression::containsIgnoreCase); //模糊查询
    }



}