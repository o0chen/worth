package com.blackeye.worth.model;

import com.blackeye.worth.enums.MenuTypeEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

/**
 * 菜单实体
 */
@Entity(name="menu")
public class Menu extends TreeEntity {

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
     * 角色-菜单关系
     * 多对多
     * 双向关联
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_menu",
            joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
