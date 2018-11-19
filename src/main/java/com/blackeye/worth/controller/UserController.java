package com.blackeye.worth.controller;

import com.blackeye.worth.core.annotations.KeyWordSearch;
import com.blackeye.worth.model.QSysUser;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import com.blackeye.worth.utils.AnnotationUtil;
import com.blackeye.worth.utils.ReflectUtil;
import com.blackeye.worth.vo.Result;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;

import static com.blackeye.worth.vo.Result.FAIL;
import static com.blackeye.worth.vo.Result.SUCCESS;

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




    @RequestMapping(value = "/listSysUserByPage")
    @ResponseBody
    public Result listSysUserByPage(@RequestParam MultiValueMap<String, String> paramsMap,
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

        Page page=null;
        String message="操作成功";
        Integer code=SUCCESS;
        boolean result=true;

        //需要特殊处理的字段
        String keyword=null;
        if(paramsMap.get("keyword")!=null)
            keyword=paramsMap.get("keyword").get(0);
//        page= userService.listSysUserByPage(predicate, PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()));
        if(!StringUtils.isEmpty(keyword))
        {

            // 获得字段注解
            List<Field> feilds = AnnotationUtil.getSpecifyAnnotationFileds(SysUser.class, KeyWordSearch.class);
//            Predicate keyWordPredicate
//            Arrays.stream(feilds).forEach();
            try {
                Object qobj=QSysUser.class.getField("sysUser").get(QSysUser.class);
                Predicate keyWordPredicate=null;
                for (Field f:feilds ) {
                    System.out.println(f.getName());
                    StringPath stringPath=(StringPath)ReflectUtil.get(qobj,QSysUser.class,f.getName());
//                    StringPath stringPath=(StringPath)QSysUser.class.getField(f.getName()).get(StringPath.class);
                    if(keyWordPredicate==null)
                    {
                        keyWordPredicate=stringPath.like(keyword);
                    }  else {
                        keyWordPredicate=stringPath.like(keyword).or(keyWordPredicate);
                    }
                }

//                Predicate keyWordPredicate = QSysUser.sysUser.name.like(keyword);
//                keyWordPredicate=QSysUser.sysUser.mobile.like(keyword).or(keyWordPredicate);
                predicate=ExpressionUtils.and(predicate,keyWordPredicate);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } finally {
            }
        }
        predicate = dealTimeRangeBinding(predicate, QSysUser.sysUser.createDate, paramsMap);
        page= userService.listByPage(predicate, PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()));

        if(page==null) {
            result=false;
            code=FAIL;
            message ="操作失败";
        }
        return new Result.Builder().data(page).code(code).message(message).isSuccess(result).build();

    }


}