package com.blackeye.worth.core.params.extend;

import com.blackeye.worth.core.customer.BaseRepository;
import com.blackeye.worth.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * 示例
 */
public class ExampleService {

    @Autowired
    BaseRepository baseRepository;

    public List<SysUser> findListPerson(final Map<String, Object> params) {
        Specification<SysUser> spec = new Specification<SysUser>() {
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                SearchUtils.autoBuildQuery(root, query, cb, params);
                return null;
            }
        };
        List<SysUser> list = baseRepository.findAll(spec);
        return list;
    }

}

