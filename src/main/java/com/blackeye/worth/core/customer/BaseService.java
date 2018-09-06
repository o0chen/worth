package com.blackeye.worth.core.customer;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T,ID extends Serializable> {

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

	T saveOne(T entity);

	List<T> findAll(Example<T> example);

	@Transactional
	T saveOrUpdate(ID id, T t);
}