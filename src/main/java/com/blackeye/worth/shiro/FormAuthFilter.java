package com.blackeye.worth.shiro;

import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.utils.JsonUtil;
import com.blackeye.worth.vo.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * 表单登陆拦截类
 */
public class FormAuthFilter extends FormAuthenticationFilter{
    public static final String DEFAULT_MESSAGE_PARAM = "message";
    private String messageParam = DEFAULT_MESSAGE_PARAM;
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username=getUsername(request);
        String password=getPassword(request);
        return super.createToken(username,password,request, response);
    }

    @Override
    protected String getUsername(ServletRequest request) {
        String username= WebUtils.getCleanParam(request,getUsernameParam());
        return username==null?"":username;
    }


    @Override
    protected String getPassword(ServletRequest request) {
        return super.getPassword(request)==null?"":super.getPassword(request);
    }



    public String getMessageParam() {
        return messageParam;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
       /* PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute("CURRENT_USER", token.getPrincipal());
        //String s = JsonUtil.objectToJson(new Result.Builder().code(0).message("登陆成功").isSuccess(true).build());
        //out.println(s);
        //out.flush();
        //out.close();
        return true;
    }


    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String s = JsonUtil.objectToJson(new Result.Builder().code(-1).message("用户名或密码错误").isSuccess(false).build());
        out.println(s);
        out.flush();
        out.close();
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {

                return this.executeLogin(request, response);
            } else {

                return true;
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            String s = JsonUtil.objectToJson(new Result.Builder().code(-999).message("未登录，无法访问该地址").isSuccess(false).build());
            out.println(s);
            out.flush();
            out.close();
            return false;
        }
    }



}
