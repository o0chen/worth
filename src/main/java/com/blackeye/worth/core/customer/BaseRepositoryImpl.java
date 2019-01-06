package com.blackeye.worth.core.customer;

import com.blackeye.worth.core.sql.SQLText;
import com.blackeye.worth.core.sql.SqlUtil;
import com.blackeye.worth.core.sql.expression.Expression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//必须使用该注解标明，此接口不是一个Repository Bean
@NoRepositoryBean
public  class BaseRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID>
        implements BaseRepository<T, ID>, QuerydslBinderCustomizer {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;


    public JPAQueryFactory queryFactory;


    public BaseRepositoryImpl(Class domainClass, EntityManager em) {
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
        Class clazz = root.getType();
        try {
            if (null != clazz.getField("createDate")) {
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

    //
    @Override
    public List<Object[]> queryBySql(String sql) {
        return this.queryBySql(sql, null);
    }

    @Override
    public List<Object[]> queryBySql(String sql, List values) {
        Query query = entityManager.createNativeQuery(sql);//转换结果
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                query.setParameter(i + 1, values.get(i));
            }
        }
        return query.getResultList();
    }


    //复杂sql动态条件查询需要传递动态参数--先用Map代替吧
    @Override
    public List<Map> findByNql(String nql, Map params) {
        Expression exp = SqlUtil.eval(nql, params);
        String sql = exp.getText();
        Query query = entityManager.createNativeQuery(sql, java.util.Map.class);//转换结果
        List values = exp.getValues();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                query.setParameter(i + 1, values.get(i));
            }
        }
        return query.getResultList();
    }

    @Override
    public List<Map> queryMapBySql(String sql) {
        return (List<Map>)this.queryObjectBySql(sql,null,Map.class);
    }

    @Override
    public List<T> queryObjectBySql(String sql, Class<T> clazz) {
        return this.queryObjectBySql(sql,null,clazz);
    }

    @Override
    public List<T> queryObjectBySql(String sql, List values, Class clazz) {
        Query query=null;
        if(Map.class.equals(clazz)){
            query= entityManager.createNativeQuery(sql);//转换结果
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }else{
            query= entityManager.createNativeQuery(sql,clazz);//转换结果--不知道为毛直接传MAP.class不行
        }

        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                query.setParameter(i + 1, values.get(i));
            }
        }

        return query.getResultList();
    }

    @Override
    public List<T> queryObjectByNql(String nql, Map params, Class<T> clazz) {
        Expression exp = SqlUtil.eval(nql, params);
        String sql = exp.getText();
        Query query = entityManager.createNativeQuery(sql, clazz);//转换结果
        List values = exp.getValues();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                query.setParameter(i + 1, values.get(i));
            }
        }
        return query.getResultList();
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


//    @Override
//    public T addOrUpdate(T entity) {
//        try {
//            Object obj=entity.getClass().getMethod("getId()").invoke(entity);
//            if (obj == null ) {
//                return null;
//            }
//            T saveTntity = (T)this.findOne((Example)obj);
//            BeanCopyUtil.beanCopyWithIngore(entity, saveTntity, "id");
//            return this.saveAndFlush(saveTntity);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return null;
//    }


//    @Override
//    public boolean exists(String id) {
//        return super.existsById((ID)id);
//    }
//
//    @Override
//    public void delete(String id) {
//        super.deleteById((ID)id);
//    }
//
//    @Override
//    public T getOne(String id) {
//        return super.getOne((ID)id);
//    }

    public MyPage page(String sql, Integer page, Integer size) {
        return page(sql, null, page, size);
    }

    public MyPage page(String sql, Object params, Integer page, Integer size){
        return page(sql,  params,  page,  size,Map.class);
    }
    public MyPage page(String sql, Object params, Integer page, Integer size,Class clazz) {
        if (sql.indexOf(" ") == -1) {
//            sql = SqlUtil.eval(SQLText.get(sql), params).getText();
            Expression exp = SqlUtil.eval(SQLText.get(sql), params);
            sql = exp.getText();
            params=exp.getValues();
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }
        int start = (page - 1) * size;
        sql = sql + "\r\n \r\n limit ?,?";
        ((List)params).add(start);
        ((List)params).add(size);
        List l = this.queryObjectBySql(sql,(List)params,clazz);
        return MyPage.of(page, size, l);
    }

    public Long countSql(String sql) {
        return countSql(sql, null);
    }

    public Long countSql(String sql, Object params) {

        if (sql.indexOf(" ") == -1) {
            Expression exp = SqlUtil.eval(SQLText.get(sql), params);
            sql = exp.getText();
            params=exp.getValues();
        }
        String count_sql = "select count(1) count from (" + sql + ") count";
        List<Object[]> countRes = this.queryBySql(count_sql,(List) params);
        Long total = Long.valueOf(countRes.get(0)[0].toString());
        return total;
//       Long pageCount = (long) Math.ceil((long) total / size);
    }

}

class MyPage {
    private long index;
    private long total;
    private long size;
    private List<Map> content;

    public static MyPage of(long index, long total, long size, List<Map> content) {
        return new MyPage(index, total, size, content);
    }

    public static MyPage of(long index, long size, List<Map> content) {
        return new MyPage(index, size, content);
    }


    private MyPage(long index, long total, long size, List<Map> content) {
        this.index = index;
        this.total = total;
        this.size = size;
        this.content = content;
    }

    private MyPage(long index, long size, List<Map> content) {
        this.index = index;
        this.size = size;
        this.content = content;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<Map> getContent() {
        return content;
    }

    public void setContent(List<Map> content) {
        this.content = content;
    }
}
