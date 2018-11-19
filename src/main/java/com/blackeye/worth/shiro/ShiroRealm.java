package com.blackeye.worth.shiro;

import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.ILoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

//实现AuthorizingRealm接口用户用户认证
public class ShiroRealm extends AuthorizingRealm {

    //用于用户查询
    @Autowired
    private ILoginService loginService;


    @Autowired
    private  SessionManager sessionManager;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name= (String) principalCollection.getPrimaryPrincipal();
        //查询用户名称
        SysUser sysUser = loginService.findByName(name);

        /*Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        if (sessions.size() > 0){
            // 如果是登录进来的，则踢出已在线用户
            if (SecurityUtils.getSubject().isAuthenticated()){
                for (Session session : sessions){
                    sessionManager.getSessionDAO().delete(session);
                }
            }
            // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
            else{
                UserUtils.getSubject().logout();
                throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
            }
        }*/

        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        for (SysRole sysRole:sysUser.getSysRole()) {
            SysRole sysRole = sysUser.getSysRole();
            //添加角色
            simpleAuthorizationInfo.addRole(sysRole.getRoleName());
            for (SysMenuPermission sysMenuPermission : sysRole.getSysMenuPermissions()) {
                //添加权限
                simpleAuthorizationInfo.addStringPermission(sysMenuPermission.getPermission());
            }
//        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        SysUser sysUser = loginService.findByName(name);
        if (sysUser == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(sysUser, sysUser.getPassword().toString(), getName());

            return simpleAuthenticationInfo;
        }
    }
}