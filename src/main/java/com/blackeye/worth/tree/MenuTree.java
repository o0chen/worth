package com.blackeye.worth.tree;


import com.blackeye.worth.model.SysMenuPermission;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 */
public class MenuTree extends Tree {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * url路径
     */
    private String url;

    /**
     * 类型
     */
    private String type;

    /**
     * VUE路径
     */
    private String index;

    /**
     * Vue使用菜单名称
     */
    private String title;


    private Set<Tree> subs = new LinkedHashSet<>();

    private String icon;

    /**
     * 权限字符串
     */
//    private String permission;

    public MenuTree (SysMenuPermission menu) {
        super(menu);
        this.name = menu.getName();
        this.url = menu.getUrl();
        this.type = menu.getType().getLabel();
        this.setParentId(menu.getParentId());
        this.title=menu.getName();
        this.index=menu.getUrl();
        this.icon=menu.getIcon();
        //this.permission = menu.getPermission();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Tree> getSubs() {
        return super.getChildren();
    }

    public void setSubs(Set<Tree> subs) {
        super.setChildren(subs);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    //    public String getPermission() {
//        return permission;
//    }
//
//    public void setPermission(String permission) {
//        this.permission = permission;
//    }

    @Override
    public String toString() {
        return "MenuTree{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
//                ", permission='" + permission + '\'' +
                '}';
    }
}
