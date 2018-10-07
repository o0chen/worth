package com.blackeye.worth.core.customer;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T,ID extends Serializable> {


	BaseRepositoryImpl getBaseRepositoryByClass(Class<T> clazz);

	//基于原生态的sql进行查询h
	List<Object[]> queryBySql(String sql);
	//基于原生态的sql进行查询,返回map,性能不如返回数组
	List<Map> queryMapBySql(String sql);
	//基于Hibernate的HQL进行查询
	List<Object[]> queryByHql(String hql);
	//基于Specification的方式进行查询，使用的是CriteriaQuery进行查询
	List<Object[]> queryBySpecification(CriteriaQuery<Object[]> query);


	Page<T> listByPage(com.querydsl.core.types.Predicate predicate, PageRequest pageRequest);

	T save(T t);
	
	T update(T t);

	T get(ID id);

	void delete(ID id);
	
	void delete(T t);
	
	boolean exists(ID id);

	long count();

	List<T> findAll();
	
	List<T> findAll(Sort sort);
	
	List<T> findAll(Specification<T> specification);
	
	Page<T> findAll(Pageable pageable);
	
	Page<T> findPage(Specification<T> specification, Pageable pageable);

	Page<T> findAll(Specification<T> specification, Pageable pageable);


	/**
	 * search开头方法，方便用于后台调用，也可在controller使用；
	 * 非mvc自动封装参数，手动配置参数名称（不需要过多之定义binding,仅用于and查询,范围查询方便），规范见
	 * @see com.blackeye.worth.core.params.extend.SearchUtils
	 * @param params
	 * @return
	 */
	List<T> searchAll(T t, Map<String, Object> params);

	Page<T> searchAllByPage(T t, Map<String, Object> params, Pageable pageable);

	T saveOne(T entity);

	List<T> findAll(Example<T> example);

	@Transactional
	T saveOrUpdate(ID id, T t);

}