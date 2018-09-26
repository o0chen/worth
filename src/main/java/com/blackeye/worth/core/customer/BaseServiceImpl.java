package com.blackeye.worth.core.customer;

import com.blackeye.worth.utils.BeanCopyUtil;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("baseService")
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {//用以实现jpa中得各种功能和添加公用功能
    /*@Autowired 还是放controller比较合适
    Validator globalValidator;*/
//    @Autowired
    protected BaseRepository<T, ID> baseRepository;


    private static Map<String, BaseRepository> registryMap = new HashMap<String, BaseRepository>();
    @Override
    public BaseRepositoryImpl getBaseRepositoryByClass(Class<T> clazz){
        String clazzName = clazz.getName();
        if(!registryMap.containsKey(clazzName)){
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    return new BaseRepositoryImpl(clazz,entityManager);
                }
            }
        }
        return (BaseRepositoryImpl) registryMap.get(clazzName);
    }

    public void setBaseRepository(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public EntityManager entityManager;


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
//        List<Map> list = entityManager
//                .createNativeQuery(sql, java.util.Map.class)//eg;"select address,count(*) from t_student group by address"
//                .getResultList();
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.getResultList();

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


    public void myDbOperation(T t) {

        baseRepository.myDbOperation(t);
    }

    public void sayHello(String name) {
        baseRepository.sayHello(name);
    }


    /**
     * 代码动态sql示例
     *
     * @param
     * @return
     */
    public List<T> selectDojo(T dojo) {
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                //我理解为创建一个条件的集合
                List<Predicate> predicates = new ArrayList<Predicate>();

                //TODO

                //判断传过来的name是否为null,如果不为null就加到条件中
//                if(sysUserParam.getName()!=null){
                /** cb.equal（）相当于判断后面两个参数是否一致
                 *root相当于我们的实体类的一个路径，使用get可以获取到我们的字段，因为我的cityid为Long类型
                 * 所以是as(Long.class)
                 *如果为Int,就是as(Integer.class) 第二个参数为前台传过来的参数，这句话就相当于
                 * 数据库字段的值name = 前台传过来的值sysUserParam.getName()
                 */
//                    predicates.add(cb.equal(root.<String>get("name").,dojo));
                //  predicates.add(cb.like(root.get("name"),"%"+sysUserParam.getName()+"%"));//like
//                }

                //创建一个条件的集合，长度为上面满足条件的个数
                Predicate[] pre = new Predicate[predicates.size()];
                //这句大概意思就是将上面拼接好的条件返回去
                return query.where(predicates.toArray(pre)).getRestriction();

            }
        };   //这里我们按照返回来的条件进行查询，就能得到我们想要的结果
        List<T> list = baseRepository.findAll(specification);
        System.out.println("查询返回的结果为" + list);
        return list;
    }


    /**
     * 分页查询
     *
     * @param predicate   通过controller传过来-根据用户请求的参数自动生成 Predicate PageRequest
     * @param pageRequest
     * @return
     */
    @Override
    public Page<T> listByPage(com.querydsl.core.types.Predicate predicate, PageRequest pageRequest) {
       if(predicate==null){
           return this.baseRepository.findAll(pageRequest);
       }else {
           return this.baseRepository.findAll(predicate, pageRequest);
       }
    }


    @Override
    public T save(T t) {
        return baseRepository.save(t);
    }

    @Transactional
    @Override
    public T update(T t) {
        return baseRepository.saveAndFlush(t);
    }

    @Override
    public T get(ID id) {
        return baseRepository.getOne(id);
    }

    @Override
    public void delete(ID id) {
        baseRepository.deleteById(id);
    }

    @Override
    public void delete(T t) {
        baseRepository.delete(t);
    }

    @Override
    public boolean exists(ID id) {
        return baseRepository.existsById(id);
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return baseRepository.findAll(specification);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return baseRepository.findAll(specification, pageable);
    }

    @Override
    public Page<T> findPage(Specification<T> specification, Pageable pageable) {
        return baseRepository.findAll(specification, pageable);
    }


///*********************************************************

    @Transactional
    public T updateOne(ID id, T entity) {
        T tdb = baseRepository.getOne(id);
        BeanCopyUtil.beanCopyWithIngore(entity, tdb,"id");
        return tdb;
    }

//    @Transactional
//    public <TT, TID extends Serializable> TT updateOne(BaseRepository<TT, TID> baseRepository, TID id, TT entity) {
//        TT tdb = baseRepository.getOne(id);
//        BeanCopyUtil.beanCopyWithIngore(entity, tdb,"id");
//        return tdb;
//    }

    @Override
    public T saveOne(T entity) {
        return baseRepository.save(entity);
    }

//    public <TT, TID extends Serializable> TT saveOne(BaseRepository<TT, TID> baseRepository, TT entity) {
//        return baseRepository.save(entity);
//    }

    @Override
    public List<T> findAll(Example<T> example) {
        return baseRepository.findAll(example);
    }

    @Transactional
    @Override
    public T saveOrUpdate(ID id, T t) {
        if (id != null) {
            T db = get(id);
            if (db != null) {
                return updateOne(id, t);
            }
        }
        return saveOne(t);
    }


}