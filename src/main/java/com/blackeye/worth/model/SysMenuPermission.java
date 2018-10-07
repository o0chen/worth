package com.blackeye.worth.model;

import com.blackeye.worth.enums.MenuTypeEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class SysMenuPermission extends BaseDojo {
    public SysMenuPermission() {
    }

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 1, max = 20, message = "角色名称长度必须在{min}和{max}之间")
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 父级菜单ID
     */
    private String parentId;

    /**
     * 菜单路径
     * 唯一
     */
    @Column(length = 255)
    @NotBlank(message = "路径不能为空")
    //@Pattern(regexp = "^/.*?", message = "无效的路径地址")
    private String url;

    /**
     * 菜单顺序
     */
    @Min(value = 1, message = "顺序在1-1000之间")
    @Max(value = 1000, message = "顺序在1-1000之间")
    private Integer menuOrder;

    /**
     * 权限编码，权限说明常用：create，update，delete，view
     *
     * * button例子：role:create，role:update，role:delete，role:view
     */
    private String permission;

    /**
     * 用户状态
     * 使用枚举类型，数据库中只保存枚举对应的String
     */
    @Enumerated(EnumType.STRING)
    private MenuTypeEnum type;


    /**
     * 是否显示  0不显示  1显示
     */
    @NotNull(message="不能为null")
    private  Integer isShow;

//    @ManyToOne(fetch = FetchType.EAGER)
//    private SysRole role;
    /**
     * 角色-菜单关系
     * 多对多
     * 双向关联
     *//*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_menu",
            joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<SysRole> roles;*/

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public MenuTypeEnum getType() {
        return type;
    }

    public void setType(MenuTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}