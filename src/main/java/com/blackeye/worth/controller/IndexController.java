package com.blackeye.worth.controller;

import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.model.SysRole;
import com.blackeye.worth.model.SysUser;
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
        SysUser sysUser = this.getLoginUser();
        SysRole sysRole=(SysRole)this.get("SysRole",sysUser.getSysRole().getId());
        Set<SysMenuPermission> menuSet = new LinkedHashSet(sysRole.getSysMenuPermissions());
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
        if (StringX.isEmpty(sysMenuPermission.getId())) {
            sysMenuPermission.setMenuOrder((menuService.getMaxMenuOrder() == null ? 0 : menuService.getMaxMenuOrder()) + 1);//新增
        }
//        if (StringX.isEmpty(sysMenuPermission.getUrl())) {
//            sysMenuPermission.setUrl("/");
//        }

        SysMenuPermission result = menuService.saveOrUpdate(sysMenuPermission.getId(), sysMenuPermission);
        return new Result.Builder().data(result).build();
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

        List<SysMenuPermission> list=null;
        int[] numbers;//菜单顺序数组
        //进入节点位置
        int dropNode_index=0;
        //前面before、后面after、内部子节点inner
        switch(type){
            case "before":
                draggingNode.setParentId(dropNode.getParentId());
                //menuService
                list=menuService.findByParentIdOrderByMenuOrderAsc(dropNode.getParentId());//进入节点的所有同级节点

                listRemoveNode(list,draggingNode);
                //菜单顺序数组
                numbers=new int[list.size()+1];

                for (int i=0;i<list.size();i++){
                    SysMenuPermission sm=list.get(i);
                    numbers[i]=sm.getMenuOrder();
                    if(dropNode.getId().equals(sm.getId())){
                        //当前节点需要进入的数组下标
                        dropNode_index=i;
                    }

                }

                numbers[list.size()]=draggingNode.getMenuOrder();
                insertSort(numbers);//排序
                setSort(numbers,dropNode_index,list,draggingNode);//写入顺序

                break;
            case "after":
                draggingNode.setParentId(dropNode.getParentId());
                //menuService
                list=menuService.findByParentIdOrderByMenuOrderAsc(dropNode.getParentId());//进入节点的所有同级节点
                listRemoveNode(list,draggingNode);

                //菜单顺序数组
                numbers=new int[list.size()+1];

                for (int i=0;i<list.size();i++){
                    SysMenuPermission sm=list.get(i);
                    numbers[i]=sm.getMenuOrder();
                    if(dropNode.getId().equals(sm.getId())){
                        //当前节点需要进入的数组下标
                        dropNode_index=i+1;
                    }
                }
                numbers[list.size()]=draggingNode.getMenuOrder();
                insertSort(numbers);//排序
                setSort(numbers,dropNode_index,list,draggingNode);//写入顺序


                break;
            case "inner":
                draggingNode.setParentId(dropNode.getId());
                //menuService
                list=menuService.findByParentIdOrderByMenuOrderAsc(dropNode.getId());//进入节点的子节点集合
                listRemoveNode(list,draggingNode);

                //菜单顺序数组
                numbers=new int[list.size()+1];

                for (int i=0;i<list.size();i++){
                    SysMenuPermission sm=list.get(i);
                    numbers[i]=sm.getMenuOrder();
                }
                dropNode_index=list.size();//放入最后

                numbers[list.size()]=draggingNode.getMenuOrder();
                insertSort(numbers);//排序
                setSort(numbers,dropNode_index,list,draggingNode);//写入顺序
                break;
        }
        return new Result.Builder().build();
    }

    public void insertSort(int[] numbers) {
        int size = numbers.length, temp, j;
        for (int i = 1; i < size; i++) {
            temp = numbers[i];
            for (j = i; j > 0 && temp < numbers[j - 1]; j--){
                numbers[j] = numbers[j - 1];
            }
            numbers[j] = temp;
        }
    }

    public void setSort(int[] numbers,int dropNode_index,List<SysMenuPermission> list,SysMenuPermission draggingNode){
        int size=list.size();
        list.add(new SysMenuPermission());//增加数组长度。。。
        //进入的节点的同级菜单内顺序在后面的菜单往后排序一位
        for(int i=size;i>dropNode_index;i--){
            list.set(i,list.get(i-1));
        }
        list.set(dropNode_index,draggingNode);//当前节点占据进入节点的位置
        //插入顺序号码
        for(int i=0;i<list.size();i++){
            list.get(i).setMenuOrder(numbers[i]);
            menuService.saveOrUpdate(list.get(i).getId(),list.get(i));
        }
    }


    public void listRemoveNode(List<SysMenuPermission> list,SysMenuPermission draggingNode){
        for (int i=0;i<list.size();i++){
            if(list.get(i).getId().equals(draggingNode.getId())){
                list.remove(i);
                break;//去除当前节点
            }
        }
    }
}