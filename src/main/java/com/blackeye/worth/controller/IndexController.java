package com.blackeye.worth.controller;

import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.service.IMenuService;
import com.blackeye.worth.service.IUserService;
import com.blackeye.worth.tree.MenuTree;
import com.blackeye.worth.tree.TreeUtils;
import com.blackeye.worth.utils.StringX;
import com.blackeye.worth.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("/index")
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
    @RequestMapping("/user-menus")
    @ResponseBody
    public Result userMenus() {
        // 获取当前用户所拥有的菜单

        //Set<SysMenuPermission> menuSet = new LinkedHashSet<SysMenuPermission>(menuService.findByRoleIdAndType("1", MenuTypeEnum.MENU));
        Set<SysMenuPermission> menuSet = new LinkedHashSet(userService.findUserByName("admin").getSysRole().getSysMenuPermissions());
        List<MenuTree> menuTrees = TreeUtils.buildMenuTree(new ArrayList<SysMenuPermission>(menuSet));
        return new Result.Builder().data(menuTrees).build();
    }


    @RequestMapping("/getmenu")
    @ResponseBody
    public Result getmenu(String id){
        return new Result.Builder().data(menuService.findById(id)).build();
    }

    /**
     * 新增保存
     * @param sysMenuPermission
     * @return
     */
    @RequestMapping(value="/savemenu",method=RequestMethod.POST)
    @ResponseBody
    public Result savemenu(@RequestBody SysMenuPermission sysMenuPermission){
        if(StringX.isEmpty(sysMenuPermission.getId())){
            sysMenuPermission.setMenuOrder(menuService.getMaxMenuOrder()+1);//新增
        }
        return new Result.Builder().data(menuService.saveOrUpdate(sysMenuPermission.getId(),sysMenuPermission)).build();
    }

    /**
     * 获取所有菜单
     */
    @RequestMapping(value = "/menulist")
    @ResponseBody
    public Result menulist(){
        List<MenuTree> menuTrees = TreeUtils.buildMenuTree(menuService.findAll(new Sort(Sort.Direction.ASC,"menuOrder")));
        return new Result.Builder().data(menuTrees).build();
    }

    @RequestMapping(value = "/dragmenu")
    @ResponseBody
    public Result dragMenu(@RequestBody Map<String,String> param){
        String type=param.get("type");//类型
        String dropNode_id=param.get("dropNode");//最后进入节点ID
        String draggingNode_id=param.get("draggingNode");//当前节点ID
        SysMenuPermission dropNode=menuService.findById(dropNode_id);
        SysMenuPermission draggingNode= menuService.findById(draggingNode_id);
        //前面before、后面after、内部子节点inner
        switch(type){
            case "before":
                draggingNode.setParentId(dropNode.getParentId());
                //menuService
                draggingNode.setMenuOrder(dropNode.getMenuOrder());
                break;
            case "after":

                break;
            case "inner":

                break;
        }
        return new Result.Builder().build();
    }
}