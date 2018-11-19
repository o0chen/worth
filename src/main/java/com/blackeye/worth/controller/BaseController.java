package com.blackeye.worth.controller;


import com.blackeye.worth.core.customer.BaseService;
import com.blackeye.worth.core.params.extend.QueryDslUtils;
import com.blackeye.worth.model.BaseDojo;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.utils.BeanCopyUtil;
import com.blackeye.worth.utils.DateX;
import com.blackeye.worth.utils.ObjectMapUtils;
import com.blackeye.worth.vo.Result;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Map;

import static com.blackeye.worth.core.params.extend.QueryDslUtils.getClassByModelName;
import static com.blackeye.worth.vo.Result.FAIL;
import static com.blackeye.worth.vo.Result.SUCCESS;

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


    @Deprecated
    public Predicate dealTimeRangeBinding(Predicate predicate, DateTimePath dateTimePath, MultiValueMap<String, String> paramsMap) {
        String name = dateTimePath.getMetadata().getName();
        if (paramsMap.get(name + "_begin") != null && paramsMap.get(name + "_end") != null) {
            String beginTime = paramsMap.get(name + "_begin").get(0);
            String endTime = paramsMap.get(name + "_end").get(0);
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
    @RequestMapping(value = "/l_delete{entity}ById")
    public Result logicalDelete(@PathVariable String entity, @RequestParam String id) {
        if (id == null) {
            return new Result.Builder().message("参数id不能为空").code(FAIL).isSuccess(false).build();
        }
        Class cls = getClassByModelName(entity);
        Object object = null;
        try {
            object = cls.newInstance();
            BaseDojo dojo = (BaseDojo) object;
            dojo.setDelFlag(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return new Result.Builder().message("系统异常").code(FAIL).isSuccess(false).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new Result.Builder().message("系统异常").code(FAIL).isSuccess(false).build();
        } finally {
        }
        return new Result.Builder().data(baseService.updateOne(cls, id, object)).message("删除成功").code(SUCCESS).isSuccess(true).build();
    }

    @ResponseBody
    @RequestMapping(value = "/unl_delete{entity}ById")
    public Result undoLogicalDelete(@PathVariable String entity, @RequestParam String id) {
        if (id == null) {
            return new Result.Builder().message("参数id不能为空").code(FAIL).isSuccess(false).build();
        }
        Class cls = getClassByModelName(entity);
        Object object = null;
        try {
            object = cls.newInstance();
            BaseDojo dojo = (BaseDojo) object;
            dojo.setDelFlag(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return new Result.Builder().message("系统异常").code(FAIL).isSuccess(false).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new Result.Builder().message("系统异常").code(FAIL).isSuccess(false).build();
        } finally {
        }
        return new Result.Builder().data(baseService.updateOne(cls, id, object)).message("恢复删除成功").code(SUCCESS).isSuccess(true).build();
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

    /**
     * page，第几页，从0开始，默认为第0页
     * size，每一页的大小，默认为10
     * sort，排序相关的信息，以property,property(ASC|DESC)的方式组织，
     * 例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列。
     */
    @ResponseBody
    @RequestMapping(value = "/list{entity}ByPage")
    public Result listByPage(@RequestParam MultiValueMap<String, String> paramsMap,
                             @PathVariable String entity,
                             @QuerydslPredicate(root = BaseDojo.class) Predicate predicate,//自动处理得只有BaseDojo里面得属相
                             @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page page = null;
        String message = "操作成功";
        Integer code = SUCCESS;
        boolean result = true;
        Class clazz = getClassByModelName(entity);
        Predicate inPredicate = QueryDslUtils.inQueryBuild(entity, paramsMap);
        Predicate keywordPredicate = QueryDslUtils.keywordQueryBuild(entity, paramsMap, "keyword");//整体是or
        Predicate dateRangePredicate = QueryDslUtils.dateRangeQueryBuild(entity, paramsMap);
        Predicate numberRangePredicate = QueryDslUtils.numberRangeQueryBuild(entity, paramsMap);
        predicate = QueryDslUtils.mergePredicatesByAnd(predicate, inPredicate, keywordPredicate, dateRangePredicate, numberRangePredicate);
        //过滤逻辑删除
//            Sort sort = new Sort(Sort.Direction.DESC, "createdate")
//                .and(new Sort(Sort.Direction.ASC, "id"));
//            pageable = PageRequest.of(1, 10, sort);
        if (predicate == null) {
            page = this.baseService.findAll(clazz, pageable);
        } else {
            page = this.baseService.findAll(clazz, predicate, pageable);
        }
        if (page == null) {
            result = false;
            code = FAIL;
            message = "操作失败";
        }
        return new Result.Builder().data(page).code(code).message(message).isSuccess(result).build();
    }

    @ResponseBody
    @RequestMapping(value = "/search{entity}ByPage")
    public Page searchByPage(@RequestParam MultiValueMap<String, String> paramsMap,
                             @PathVariable String entity,
                             @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Class clazz = getClassByModelName(entity);
        return this.baseService.searchAllByPage(clazz, paramsMap, pageable);
    }

    @ResponseBody
    @RequestMapping(value = "/searchAll{entity}")
    public List searchAll(@RequestParam MultiValueMap<String, String> paramsMap,
                          @PathVariable String entity) {
        Class clazz = getClassByModelName(entity);
//            this.baseService;
        return this.baseService.searchAll(clazz, paramsMap);
    }


    @ResponseBody
    @Transactional
    @RequestMapping(value = "/update{entity}")
    public Object update(@PathVariable String entity, @RequestBody Object data) {
        return baseService.saveAndFlush(getClassByModelName(entity), data);
    }

    @ResponseBody
    @RequestMapping(value = "/get{entity}ById")
    public Object get(@PathVariable String entity, @RequestParam String id) {
        return baseService.getOne(getClassByModelName(entity), id);
    }

    @ResponseBody
    @RequestMapping(value = "/isExists{entity}")
    public boolean exists(@PathVariable String entity, @RequestParam String id) {
        return baseService.existsById(getClassByModelName(entity), id);
    }

    @ResponseBody
    @RequestMapping(value = "/count{entity}")
    public long count(@PathVariable String entity) {
        return baseService.count(getClassByModelName(entity));
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}")
    public List<Object> findAll(@PathVariable String entity) {
        return baseService.findAll(getClassByModelName(entity));
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}BySort")//待验证
    public List<Object> findAll(@PathVariable String entity, @SortDefault Sort sort) {
        return baseService.findAll(getClassByModelName(entity), sort);
    }


    @ResponseBody
    @RequestMapping(value = "/find{entity}BySpecification")//待验证
    public List<Object> findAll(@PathVariable String entity,
                                Specification<Object> specification) {
        return baseService.findAll(getClassByModelName(entity), specification);
    }

    @ResponseBody
    @RequestMapping(value = "/findAll{entity}ByPage")//待验证
    public Page<Object> findAll(@PathVariable String entity,
                                @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return baseService.findAll(getClassByModelName(entity), pageable);
    }

    @ResponseBody
    @RequestMapping(value = "/findPage{entity}")//待验证
    public Page findPage(@PathVariable String entity,
                         @QuerydslPredicate(root = BaseDojo.class) Predicate predicate,
                         @PageableDefault(value = 10, sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return baseService.findAll(getClassByModelName(entity), predicate, pageable);
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
        Class cls = getClassByModelName(entity);
        try {
            Object o = ObjectMapUtils.mapToObject((Map) entityData, cls);
            return baseService.save(cls, o);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


//    @RequestMapping(value="/saveOne{entity}")//待验证
//    public List<T> findAll(Example<T> example) {
//        return baseRepository.findAll(example);
//    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate{entity}")//待验证
    public Object saveOrUpdate(@PathVariable String entity, String id, @RequestBody Object object) {
        Class cls = getClassByModelName(entity);
        return new Result.Builder().data(baseService.saveOrUpdate(cls, id, object)).code(SUCCESS).isSuccess(true).build();
    }


//    @InitBinder
//    private void initBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
//    }
}
