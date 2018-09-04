package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.QSysUser;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

//import javax.persistence.criteria.Predicate;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<SysUser,String> implements IUserService{
    @Autowired
    private UserRepository userRepository;



    public  void test(String name){
//        userRepository.exi

        //方式1
        //http://www.querydsl.com/static/querydsl/latest/reference/html/index.html
//        http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html
        QSysUser qSysUser = QSysUser.sysUser;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        SysUser bob = query.select(qSysUser)
                .from(qSysUser)
                .where(qSysUser.name.eq(name))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
                .fetchOne();
        System.out.println(bob.getPassword());

        //方式2
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        SysUser bob1 = jpaQueryFactory.select(qSysUser).from(qSysUser)
                .where(qSysUser.name.eq(name))
                .fetchOne();
        System.out.println(bob.getPassword());

        //示例3
        String[] names=new String[]{name};
        JPAQuery<SysUser> query1 = jpaQueryFactory.selectFrom(qSysUser);
        BooleanBuilder builder = new BooleanBuilder();
        for (String name1 : names) {
            builder.or(qSysUser.name.eq(name1));
        }
        query1.where(builder);
        List<SysUser> res=query1.fetch();
        System.out.println(res.get(0).getPassword());


//        示例4 表达式条件
        BooleanExpression booleanExpression=qSysUser.name.startsWith("admi");
        //-----------------------
        Path<SysUser> person = Expressions.path(SysUser.class, "sys_user");
        Path<String> personFirstName = Expressions.path(String.class, person, "name");
        Constant<String> constant = (Constant<String>)Expressions.constant("admi");
        Expressions.predicate(Ops.STARTS_WITH, personFirstName, constant);


//        示例5 case when 语句表达式
        Expression<String> cases = new CaseBuilder()
                .when(qSysUser.password.eq("*")).then("Premier")
                .when(qSysUser.password.contains("*")).then("Gold")
                .otherwise("Bronze");

        ComparableExpressionBase<?> postTimePeriodsExp = qSysUser.createDate.year();

        query.select(postTimePeriodsExp,cases).fetchAll();



//        Expression<String> cases1 = customer.annualSpending
//                .when(10000).then("Premier")
//                .when(5000).then("Gold")
//                .when(2000).then("Silver")
//                .otherwise("Bronze");
        Predicate predicate =qSysUser.name.startsWith("admi");
        System.out.println(qSysUser.name.getMetadata().getName());
        System.out.println(predicate.getClass());
        System.out.println(predicate.getType());
        System.out.println(predicate.getType().getName());




    }


//    @Autowired
//    private EntityManager entityManager;
    public SysUser findUserByName(String name) {
        SysUser sysUser = null;
        try {
            sysUser = userRepository.findByName(name);
            userRepository.sayHello(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysUser;
    }

//    /**
//     * Details：多表连接查询
//     */
//    public List<SysMenuPermission> findAllBySysRoleId(String sysRoleId){
//        //添加查询条件
//        Predicate predicate = QSysRole.orderName.eq(orderName);
//        JPAQuery<SysMenuPermission> jpaQuery = queryFactory.select(QOrder.order, QOrderItem.orderItem)
//                .from(QOrder.order, QOrderItem.orderItem)
//                .rightJoin(QOrder.order)
//                .on(QOrderItem.orderItem.order.id.intValue().eq(QOrder.order.id.intValue()));
//        jpaQuery.where(predicate);
//        //拿到结果
//        return jpaQuery.fetch();
//    }

    /**
     * 代码动态sql示例
     * @param
     * @return
     */
    public List<SysUser> selectSysUser(SysUser sysUserParam) {
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<SysUser> specification=new Specification<SysUser>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<SysUser> root,
                                                                    CriteriaQuery<?> query,
                                                                    CriteriaBuilder cb) {
                //我理解为创建一个条件的集合
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<javax.persistence.criteria.Predicate>();
                //判断传过来的name是否为null,如果不为null就加到条件中
                if(sysUserParam.getName()!=null){
                    /** cb.equal（）相当于判断后面两个参数是否一致
                     *root相当于我们的实体类的一个路径，使用get可以获取到我们的字段，因为我的cityid为Long类型
                     * 所以是as(Long.class)
                     *如果为Int,就是as(Integer.class) 第二个参数为前台传过来的参数，这句话就相当于
                     * 数据库字段的值name = 前台传过来的值sysUserParam.getName()
                     */
                    predicates.add(cb.equal(root.get("name").as(String.class),sysUserParam.getName()));
                    //  predicates.add(cb.like(root.get("name"),"%"+sysUserParam.getName()+"%"));//like
                }

                if(sysUserParam.getPassword()!=null){
                    //这里相当于数据库字段 parent（也是Long类型） = 前台传过来的值schoolParam.getParent()
                    predicates.add(cb.equal(root.get("password").as(String.class),sysUserParam.getPassword()));
                }
                //创建一个条件的集合，长度为上面满足条件的个数
                javax.persistence.criteria.Predicate[] pre = new javax.persistence.criteria.Predicate[predicates.size()];
                //这句大概意思就是将上面拼接好的条件返回去
                return query.where(predicates.toArray(pre)).getRestriction();

            }
        };   //这里我们按照返回来的条件进行查询，就能得到我们想要的结果
        List<SysUser> list= baseRepository.findAll(specification);
        System.out.println("查询返回的结果为"+list);
        return list;
    }


    @Override
    public Page<SysUser> listSysUserByPage(Predicate predicate, PageRequest pageRequest) {

        return this.userRepository.findAll(predicate, pageRequest);
    }


}