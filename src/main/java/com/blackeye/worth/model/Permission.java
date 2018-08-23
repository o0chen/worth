package com.blackeye.worth.model;

import com.blackeye.worth.enums.MenuTypeEnum;
import com.blackeye.worth.enums.UserStatusEnum;

import javax.persistence.*;

@Entity
public class Permission extends IdEntity{
    public Permission() {
    }


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

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public MenuTypeEnum getType() {
        return type;
    }

    public void setType(MenuTypeEnum type) {
        this.type = type;
    }

}