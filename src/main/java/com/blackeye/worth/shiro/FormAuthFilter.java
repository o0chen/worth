package com.blackeye.worth.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 表单登陆拦截类
 */
public class FormAuthFilter extends FormAuthenticationFilter{

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

}
