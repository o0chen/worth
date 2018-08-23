package com.blackeye.worth.model;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.enums.UserStatusEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class MenuPermission extends IdEntity{
    public MenuPermission() {
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
     * 菜单路径
     * 唯一
     */
    @Column(unique = true)
    @NotBlank(message = "路径不能为空")
    @Pattern(regexp = "^/.*?", message = "无效的路径地址")
    private String url;

    /**
     * 菜单顺序
     */
    @Min(value = 1, message = "顺序在1-10之间")
    @Max(value = 10, message = "顺序在1-10之间")
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

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Role role;
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
    private Set<Role> roles;*/

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

}