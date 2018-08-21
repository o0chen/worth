package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {
@Query(value = "select * from User t where t.name = :name", nativeQuery = true)
User findByName(@Param("name") String name);

//User findOne(@Param("id") String id);

}