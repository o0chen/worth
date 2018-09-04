package com.blackeye.worth.core.customer;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID>
        implements BaseRepository<T, ID>,QuerydslBinderCustomizer {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public JPAQueryFactory queryFactory;


    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
        this.entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public void customize(QuerydslBindings bindings, EntityPath root) {
        Class clazz=root.getType();
        try {
            if(null!=clazz.getField("createDate")){
                //TODO

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
        }
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
    public List<Object[]> queryBySql(String sql) {
        List<Object[]> list = entityManager
                .createNativeQuery(sql)//eg："select address,count(*) from t_student group by address"
//	弃用			.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .getResultList();

        return list;
    }

    @Override
    public List<Map> queryMapBySql(String sql) {
        List<Map> list = entityManager
                .createNativeQuery(sql, java.util.Map.class)//eg;"select address,count(*) from t_student group by address"
                .getResultList();
        return list;
    }


    @Override
    public List<Object[]> queryByHql(String hql) {
        List<Object[]> list = entityManager
                .createQuery(hql)//"select address,count(*) from Student group by address"
                .getResultList();
        return list;
    }

    @Override
    public List<Object[]> queryBySpecification(CriteriaQuery<Object[]> query) {
//		//根据地址分组查询，并且学生数量大于3的所有地址
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
//		Root root = query.from(SysUser.class);
//		query.multiselect(root.get("address"),builder.count(root.get("id"))).
//				.groupBy(root.get("address")).having(builder.gt(builder.count(root.get("id")),3));

        return entityManager.createQuery(query).getResultList();
    }



}
