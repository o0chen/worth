package com.blackeye.worth.core.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

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

	//
//    @Override
//    public List<Object[]> queryBySql(String sql) {
//        List<Object[]> list = entityManager
//                .createNativeQuery(sql)//eg："select address,count(*) from t_student group by address"
////	弃用			.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
//                .getResultList();
//
//        return list;
//    }
	List<Object[]> queryBySql(String sql, List values);

	//复杂sql动态条件查询需要传递动态参数--先用Map代替吧
	List<Map> findByNql(String nql, Map params);

	List<T> queryObjectBySql(String sql, Class<T> clazz);

	List<T> queryObjectBySql(String sql, List values, Class<T> clazz);

	List<T> queryObjectByNql(String nql, Map params, Class<T> clazz);

	//基于Hibernate的HQL进行查询
	List<Object[]> queryByHql(String hql);
	//基于Specification的方式进行查询，使用的是CriteriaQuery进行查询
	List<Object[]> queryBySpecification(CriteriaQuery<Object[]> query);

//    boolean exists(String id);
//
//	void delete(String id);
//
//	T getOne(String id);

}
