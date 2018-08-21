package com.blackeye.worth.customer;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {

	private EntityManager entityManager;
 
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager em){
		super(entityInformation, em);
		this.entityManager = em;
	}

	@Override
	public void sayHello(String name) {
		System.out.println("entityManage:" + entityManager);
		System.out.println("hello, " + name);
	}

//	@Override
//	public T add(T entity) {
//		return null;
//	}

//	@Override
//	public T update(T entity) {
//		return null;
//	}

	@Override
	public Object myDbOperation(T entity) {
		System.out.println(entity.getClass());
		return null;
	}
}
