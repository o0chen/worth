package com.blackeye.worth.core.customer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.blackeye.worth.model.BaseDojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {
	@Autowired
	@PersistenceContext
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

	@Override
	public Object myDbOperation(T entity) {
		System.out.println(entity.getClass());
		return null;
	}


	@Override
	public List<Object[]> groupBySql(String sql) {
		List<Object[]> list = entityManager
				.createNativeQuery(sql)//eg;"select address,count(*) from t_student group by address"
				.getResultList();

		return list;
	}

	@Override
	public List<Object[]> groupByHql(String hql) {
		List<Object[]> list = entityManager
				.createQuery(hql)//"select address,count(*) from Student group by address"
				.getResultList();
		return list;
	}

	@Override
	public List<Object[]> groupBySpecification(CriteriaQuery<Object[]> query) {
		//根据地址分组查询，并且学生数量大于3的所有地址
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
//		Root root = query.from(entityClass);
//		query.multiselect(root.get("address"),builder.count(root.get("id")))
//				.groupBy(root.get("address")).having(builder.gt(builder.count(root.get("id")),3));

		return entityManager.createQuery(query).getResultList();
	}

}
