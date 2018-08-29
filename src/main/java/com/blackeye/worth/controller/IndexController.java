package com.blackeye.worth.controller;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.service.IMenuService;
import com.blackeye.worth.service.IUserService;
import com.blackeye.worth.tree.MenuTree;
import com.blackeye.worth.tree.TreeUtils;
import com.blackeye.worth.vo.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RestController
public class IndexController extends BaseController {

    @Autowired
    private IMenuService menuService;
    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public ModelAndView index(Model model) {
        return new ModelAndView("index");
    }

    /**
     * 获取当前用户所拥有的菜单
     * @return
     */
    @GetMapping("/user-menus")
    @ResponseBody
    public Result userMenus() {
        // 获取当前用户所拥有的菜单

        //Set<SysMenuPermission> menuSet = new LinkedHashSet<SysMenuPermission>(menuService.findByRoleIdAndType("1", MenuTypeEnum.MENU));
        Set<SysMenuPermission> menuSet = new LinkedHashSet(userService.findUserByName("admin").getSysRole().getSysMenuPermissions());
        List<MenuTree> menuTrees = TreeUtils.buildMenuTree(new ArrayList<SysMenuPermission>(menuSet));
        return new Result.Builder().data(menuTrees).build();
    }
}