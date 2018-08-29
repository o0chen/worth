package com.blackeye.worth.service.impl;

import com.blackeye.worth.core.customer.BaseServiceImpl;
import com.blackeye.worth.dao.UserRepository;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<SysUser,String> implements IUserService{
    @Autowired
    private UserRepository userRepository;
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
            public Predicate toPredicate(Root<SysUser> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                //我理解为创建一个条件的集合
                List<Predicate> predicates = new ArrayList<Predicate>();
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
                Predicate[] pre = new Predicate[predicates.size()];
                //这句大概意思就是将上面拼接好的条件返回去
                return query.where(predicates.toArray(pre)).getRestriction();

            }
        };   //这里我们按照返回来的条件进行查询，就能得到我们想要的结果
        List<SysUser> list= baseRepository.findAll(specification);
        System.out.println("查询返回的结果为"+list);
        return list;
    }


}