package com.blackeye.worth.core.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T>,QuerydslPredicateExecutor<T>  {
 
	//自定义接口方法
	public void sayHello(String name);

//	public T add(T entity);
//	public T update(T entity);

	public Object myDbOperation(T entity);


	//基于原生态的sql进行查询h
	List<Object[]> queryBySql(String sql);
	//基于原生态的sql进行查询,返回map,性能不如返回数组
	List<Map> queryMapBySql(String sql);
	//基于Hibernate的HQL进行查询
	List<Object[]> queryByHql(String hql);
	//基于Specification的方式进行查询，使用的是CriteriaQuery进行查询
	List<Object[]> queryBySpecification(CriteriaQuery<Object[]> query);




}
