package com.blackeye.worth.controller;

import com.blackeye.worth.model.QSysMenuPermission;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.service.IMenuService;
import com.blackeye.worth.service.IRoleService;
import com.blackeye.worth.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController extends  BaseController{

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    //退出的时候是get请求，主要是用于退出
    @RequestMapping(value = "/getRoleMenuPermission",method = RequestMethod.GET)
    @ResponseBody
    public Result getRoleMenuPermission(@RequestParam String id){
        //System.out.println( baseService.getOne(SysRole.class,id).getClass());
        return new Result.Builder().
                data(((SysRole)baseService.getOne(SysRole.class,id)).getSysMenuPermissions()).
                code(0).message("OK").isSuccess(true).build();
    }

    //退出的时候是get请求，主要是用于退出
    @RequestMapping(value = "/saveRoleMenuPermission",method = RequestMethod.POST)
    @ResponseBody
    public Result saveRoleMenuPermission(@RequestBody HashMap<String,Object> param){
        List<String> sysMenuPermissionsId=(List<String>)param.get("sysMenuPermissionsId");
        String roleId=(String) param.get("roleId");

        SysRole sysRole=(SysRole) baseService.getOne(SysRole.class,roleId);


        List<SysMenuPermission> list=(List<SysMenuPermission>)baseService.findAll(SysMenuPermission.class, QSysMenuPermission.sysMenuPermission.id.in(sysMenuPermissionsId));
        sysRole.setSysMenuPermissions(list);
        roleService.saveOrUpdate(sysRole.getId(),sysRole);
        return new Result.Builder().code(0).message("OK").isSuccess(false).build();
    }



}
