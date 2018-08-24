package com.blackeye.worth.controller;


import com.blackeye.worth.model.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 */
@RestController
public class BaseController {

//    @RequestMapping("/session")
    public @ResponseBody   List<String> session(HttpServletRequest request){
        Subject subject= SecurityUtils.getSubject();
        Session session=subject.getSession();
        List<String> roleList=new ArrayList<String>();
        if(subject.hasRole("admin")){
            roleList.add("admin");
        }
        if(subject.hasRole("teacher")){
            roleList.add("teacher");
        }
        return roleList;
    }


    protected SysUser getLoginUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

}
