package com.blackeye.worth.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfiguration.class);


    //将自己的验证方式加入容器
    @Bean
    public Realm shiroRealm() {
        Realm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }

    //将自己的验证方式加入容器
    @Bean
    public FormAuthFilter formAuthValidFilter() {
        FormAuthFilter formAuthenticationFilter = new FormAuthFilter();
        return formAuthenticationFilter;
    }

    @Bean
    public SessionManager sessionManager(){
        SessionManager sessionManager=new SessionManager();
        SimpleCookie simpleCookie=new SimpleCookie();
        simpleCookie.setName("jeesite.session.id");
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }




    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }


    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     * https://blog.csdn.net/J_Bang/article/details/78233724
     * （4）拦截配置说明：
     * anon:例子/admins/**=anon 没有参数，表示可以匿名使用。
     * authc:例如/admins/user/**=authc表示需要认证(登录)才能使用，没有参数
     * roles：例子/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。
     * perms：例子/admins/user/**=perms[user:add:*],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/**=perms["user:add:*,user:modify:*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
     * rest：例子/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user:method] ,其中method为post，get，delete等。
     * port：例子/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数。
     * authcBasic：例如/admins/user/**=authcBasic没有参数表示httpBasic认证
     * ssl:例子/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
     * user:例如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
     * 注：anon，authcBasic，auchc，user是认证过滤器，
     * perms，roles，ssl，rest，port是授权过滤器
     *
     * @param
     * @return
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String,String> map = new HashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        map.put("/js/**", "anon");
        map.put("/css/**", "anon");
        map.put("/img/**", "anon");
        //登出
        map.put("/logout","logout");
        //对所有用户认证
        //登陆不需要认证
        map.put("/login","authc");
        map.put("/**","user");
        map.put("/**","anon");


        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        //shiroFilterFactoryBean.setUnauthorizedUrl("/error");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        Map<String,Filter> fileters=new LinkedHashMap<String,Filter> ();
        fileters.put("authc",formAuthValidFilter());
        shiroFilterFactoryBean.setFilters(fileters);

        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilterFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
}