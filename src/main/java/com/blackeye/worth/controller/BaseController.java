package com.blackeye.worth.controller;


import com.blackeye.worth.core.customer.BaseService;
import com.blackeye.worth.model.BaseDojo;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.utils.BeanCopyUtil;
import com.blackeye.worth.utils.DateX;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 */
@RestController
@RequestMapping(value = "/base")
public class BaseController {

    public PageRequest pageRequest = PageRequest.of(0, 10);
    @Autowired
    public BaseService baseService;

    //    @RequestMapping("/session")
    public @ResponseBody
    List<String> session(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        List<String> roleList = new ArrayList<String>();
        if (subject.hasRole("admin")) {
            roleList.add("admin");
        }
        if (subject.hasRole("teacher")) {
            roleList.add("teacher");
        }
        return roleList;
    }


    protected SysUser getLoginUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }


    public Predicate dealTimeRangeBinding(Predicate predicate, DateTimePath dateTimePath, MultiValueMap<String, String> paramsMap) {
        String name = dateTimePath.getMetadata().getName();
        if (paramsMap.get(name + "_begin") != null && paramsMap.get(name + "_end") != null) {
            String beginTime = paramsMap.get(name + "_begin").get(0);
            String endTime = paramsMap.get(name + "_begin").get(0);
            predicate = dateTimePath.between(DateX.parseDateTime(beginTime), DateX.parseDateTime(endTime))
                    .and(predicate);
        } else if (paramsMap.get(name + "_begin") != null && paramsMap.get(name + "_end") == null) {
            String beginTime = paramsMap.get(name + "_begin").get(0);
            predicate = dateTimePath.after(DateX.parseDateTime(beginTime))
                    .and(predicate);
        } else if (paramsMap.get(name + "_begin") == null && paramsMap.get(name + "_end") != null) {
            String endTime = paramsMap.get(name + "_end").get(0);
            predicate = dateTimePath.before(DateX.parseDateTime(endTime))
                    .and(predicate);
        }
        return predicate;
    }

    protected Class getClassByModelName(String entity) {
        Class clazz = null;
        try {
            clazz = Class.forName("com.blackeye.worth.model." + entity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }


    //通用的增删该查
    @ResponseBody
    @RequestMapping(value = "/add{entity}")
    public Object add(@PathVariable String entity, @RequestBody Object saveData) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).save(saveData);
    }

    @ResponseBody
    @RequestMapping(value = "/delete{entity}ById")
    public void delete(@PathVariable String entity, @RequestParam String id) {
        baseService.getBaseRepositoryByClass(getClassByModelName(entity)).deleteById(id);
    }

    @ResponseBody
    @RequestMapping(value = "/delete{entity}")
    public void delete(@PathVariable String entity, @RequestBody Object object) {
        baseService.getBaseRepositoryByClass(getClassByModelName(entity)).delete(object);
    }


    /**
     * 分页查询
     *
     * @param predicate   通过controller传过来-根据用户请求的参数自动生成 Predicate PageRequest
     * @param pageRequest
     * @return
     */

    public Page listByPage(@PathVariable String entity,
                           com.querydsl.core.types.Predicate predicate,
                           PageRequest pageRequest) {
        Class clazz = getClassByModelName(entity);
        if (predicate == null) {
            return this.baseService.getBaseRepositoryByClass(clazz).findAll(pageRequest);
        } else {
            return this.baseService.getBaseRepositoryByClass(clazz).findAll(predicate, pageRequest);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/list{entity}ByPage")
    public Page listByPage(@PathVariable String entity,
                           @QuerydslPredicate(root = BaseDojo.class) Predicate predicate,
                           @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Class clazz = getClassByModelName(entity);
        if (predicate == null) {
            return this.baseService.getBaseRepositoryByClass(clazz).findAll(pageable);
        } else {
            return this.baseService.getBaseRepositoryByClass(clazz).findAll(predicate, pageable);
        }
    }


    @ResponseBody
    @Transactional
    @RequestMapping(value = "/update{entity}")
    public Object update(@PathVariable String entity, @RequestBody Object data) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).saveAndFlush(data);
    }

    @ResponseBody
    @RequestMapping(value = "/get{entity}ById")
    public Object get(@PathVariable String entity, @RequestParam String id) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).getOne(id);
    }

    @ResponseBody
    @RequestMapping(value = "/isExists{entity}")
    public boolean exists(@PathVariable String entity, @RequestParam String id) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).existsById(id);
    }

    @ResponseBody
    @RequestMapping(value = "/count{entity}")
    public long count(@PathVariable String entity) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).count();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}")
    public List<Object> findAll(@PathVariable String entity) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}BySort")//待验证
    public List<Object> findAll(@PathVariable String entity, @SortDefault Sort sort) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).findAll(sort);
    }


    @ResponseBody
    @RequestMapping(value = "/find{entity}BySpecification")//待验证
    public List<Object> findAll(@PathVariable String entity,
                                Specification<Object> specification) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).findAll(specification);
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}ByPage")//待验证
    public Page<Object> findAll(@PathVariable String entity,
                                @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).findAll(pageable);
    }

    @ResponseBody
    @RequestMapping(value = "/findPage{entity}")//待验证
    public Page findPage(@PathVariable String entity,
                         @QuerydslPredicate(root = BaseDojo.class) Predicate predicate,
                         @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).findAll(predicate, pageable);
    }


    ///*********************************************************
    @ResponseBody
    @Transactional
    @RequestMapping(value = "/updateOne{entity}")//待验证
    public Object updateOne(@PathVariable String entity, String id, Object entityData) {
        Object tdb = baseService.getBaseRepositoryByClass(getClassByModelName(entity)).getOne(id);
        BeanCopyUtil.beanCopyWithIngore(entityData, tdb, "id");
        return tdb;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOne{entity}")//待验证
    public Object saveOne(@PathVariable String entity, Object entityData) {
        return baseService.getBaseRepositoryByClass(getClassByModelName(entity)).save(entityData);
    }


//    @RequestMapping(value="/saveOne{entity}")//待验证
//    public List<T> findAll(Example<T> example) {
//        return baseRepository.findAll(example);
//    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/saveOrUpdate{entity}")//待验证
    public Object saveOrUpdate(@PathVariable String entity, String id, Object object) {
        if (id != null) {
            Object db = get(entity, id);
            if (db != null) {
                return updateOne(entity, id, object);
            }
        }
        return saveOne(entity, object);
    }


}
