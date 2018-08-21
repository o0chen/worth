package com.blackeye.worth.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
 
	//自定义接口方法
	public void sayHello(String name);

//	public T add(T entity);
//	public T update(T entity);

	public Object myDbOperation(T entity);

}
