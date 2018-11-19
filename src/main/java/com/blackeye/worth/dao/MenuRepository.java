package com.blackeye.worth.dao;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.SysMenuPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends BaseRepository<SysMenuPermission,String> {
    @Query(value = "select max(t.menuOrder) from SysMenuPermission t")
    Integer getMaxMenuOrder();

   List<SysMenuPermission> findByParentIdOrderByMenuOrderAsc(@Param("parentId") String parentId);
}
