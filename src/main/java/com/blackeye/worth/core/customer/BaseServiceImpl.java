package com.blackeye.worth.core.customer;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.io.Serializable;

//@Service("baseService")
public abstract class BaseServiceImpl<T,ID extends Serializable> {//用以实现jpa中得各种功能和添加公用功能
    /*@Autowired 还是放controller比较合适
    Validator globalValidator;*/
    protected BaseRepository<T,ID> baseRepository;
    @Autowired
    private EntityManager entityManager;

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


}