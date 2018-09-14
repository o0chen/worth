package com.blackeye.worth.controller;

import com.blackeye.worth.model.QSysUser;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import com.blackeye.worth.utils.DateX;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/show")
    @ResponseBody
    public String show(@RequestParam(value = "name") String name) {
        SysUser sysUser = userService.findUserByName(name);
        if (null != sysUser)
            return sysUser.getId() + " & " + sysUser.getName() + " & " + sysUser.getPassword();
        else return "null";
    }


    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(@RequestParam(value = "name") String name) {
        userService.test(name);
        return "null";
    }



    @RequestMapping(value = "/addOrUpdate")
    @ResponseBody
    public SysUser addOrUpdateSysUser(@RequestBody SysUser sysUser) {
        return userService.saveOrUpdate(sysUser.getId(),sysUser);
    }




    @RequestMapping(value = "/test1")
    @ResponseBody
    public Page<SysUser> listSysUserByPage(@RequestParam MultiValueMap<String, String> paramsMap,
                                            @QuerydslPredicate(root = SysUser.class) Predicate predicate,
                                            @PageableDefault(value = 15, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
//                                            @PageableDefault(size = 5) PageRequest pageRequest) {
//        PageRequest pageRequest = PageRequest.of(paramsMap.get("page")==null?0:Integer.parseInt(paramsMap.get("page").get(0)),
//                paramsMap.get("page")==null?0:Integer.parseInt(paramsMap.get("page").get(0)));

//        BooleanBuilder
        //默认自动生成得Predicate条件都是等于，可以有两种方式去修改或扩展某认方式
        //方式1:使用自定义绑定--在库中修改 {@link baseRespository}
//        @Repository
//        public interface NoticeRespository extends JpaRepository<Notice,Long>, QueryDslPredicateExecutor<Notice>,QuerydslBinderCustomizer<QNotice> {
//
//            default void customize(QuerydslBindings bindings,QNotice notice){
//                bindings.bind(notice.content).first((path,value) ->path.contains(value).or(notice.title.contains(value)));
//                bindings.bind(notice.firsttime).first((path,value) ->notice.pushdate.after(value));
//                bindings.bind(notice.secondtime).first((path,value) ->notice.pushdate.before(value));
//            }
//
//        }

        //方式2：直接追加条件--添加新条件 eg：
//        predicate = QSysUser.sysUser.createDate.between(new Date(), new Date()).and(predicate);
//        predicate = QSysUser.sysUser.status.eq(UserStatusEnum.ACTIVE).and(predicate);

        predicate = dealTimeRangeBinding(predicate, QSysUser.sysUser.createDate, paramsMap);
//        return userService.listSysUserByPage(predicate, PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()));
        return userService.listByPage(predicate, PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()));
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
}