package com.blackeye.worth.controller;

import com.blackeye.worth.model.SysUser;
import com.blackeye.worth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {
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
}