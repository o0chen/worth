package com.blackeye.worth.core.customer;

import com.blackeye.worth.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Service("baseService")
public abstract class BaseServiceImpl<T,ID extends Serializable>  {//用以实现jpa中得各种功能和添加公用功能
    /*@Autowired 还是放controller比较合适
    Validator globalValidator;*/
    protected BaseRepository<T,ID> baseRepository;
    @Autowired
    public EntityManager entityManager;

//    public void save(T t) {
//        baseRepository.save(t);
//    }

//    public void update(T t) {
//        baseRepository.update(t);
//    }




    public void myDbOperation(T t) {

        baseRepository.myDbOperation(t);
    }

    public void sayHello(String name) {
        baseRepository.sayHello(name);
    }




    /**
     * 代码动态sql示例
     * @param
     * @return
     */
    public List<T> selectDojo(T dojo) {
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<T> specification=new Specification<T>() {
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
        List<T> list= baseRepository.findAll(specification);
        System.out.println("查询返回的结果为"+list);
        return list;
    }

}