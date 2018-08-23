package com.blackeye.worth.tree;


import com.blackeye.worth.model.SysMenuPermission;

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
     * 权限字符串
     */
//    private String permission;

    public MenuTree (SysMenuPermission menu) {
        super(menu);
//        this.name = menu.getName();
//        this.url = menu.getUrl();
//        this.type = menu.getType().getLabel();
//        this.permission = menu.getPermission();
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
