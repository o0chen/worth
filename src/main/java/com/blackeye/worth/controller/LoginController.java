package com.blackeye.worth.controller;


import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.ILoginService;
import com.blackeye.worth.shiro.FormAuthFilter;
import com.blackeye.worth.vo.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private ILoginService loginService;

    //退出的时候是get请求，主要是用于退出
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ResponseBody
    public Result login(){
        return new Result.Builder().code(-999).message("请登陆").isSuccess(false).build();
    }

    //post登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result loginFail(HttpServletRequest request){
       /* //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                map.get("username").toString(),
                map.get("password").toString());
        //进行验证，这里可以捕获异常，然后返回对应信息
        subject.login(usernamePasswordToken);*/
        SysUser principal=(SysUser)SecurityUtils.getSubject().getPrincipal();
        if(principal!=null){
            return new Result.Builder().code(0).message("登陆成功").isSuccess(true).build();
        }

        String message = (String)request.getAttribute(FormAuthFilter.DEFAULT_MESSAGE_PARAM);
        if(StringUtils.isEmpty(message)){
            message="用户或密码错误, 请重试.";
        }
        return new Result.Builder().code(-999).message(message).isSuccess(false).build();
    }

    @RequestMapping(value = "/index")
    @ResponseBody
    public Map index(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("href","index");
        return map;
    }

    //登出
    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "logout";
    }

    //错误页面展示
    @RequestMapping(value = "/error",method = RequestMethod.POST)
    @ResponseBody
    public String error(){
        return "error ok!";
    }

    //数据初始化
    @RequestMapping(value = "/addUser")
    @ResponseBody
    public String addUser(@RequestBody Map<String,Object> map){
        SysUser sysUser = loginService.addUser(map);
        return "addUser is ok! \n" + sysUser;
    }

    //角色初始化
    @RequestMapping(value = "/addRole")
    @ResponseBody
    public String addRole(@RequestBody Map<String,Object> map){
        SysRole sysRole = loginService.addRole(map);
        return "addRole is ok! \n" + sysRole;
    }

    //注解的使用
    @RequiresRoles("admin")
    @RequiresPermissions("create")
    @RequestMapping(value = "/create")
    public String create(){
        return "Create success!";
    }
}