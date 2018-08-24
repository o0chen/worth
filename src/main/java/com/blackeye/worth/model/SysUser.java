package com.blackeye.worth.model;

import com.blackeye.worth.enums.UserStatusEnum;

import javax.persistence.*;

@Entity
public class SysUser extends BaseDojo {

    public SysUser() {
    }

    private String name;

    /**
     * 用户状态
     * 使用枚举类型，数据库中只保存枚举对应的String
     */
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private SysRole sysRole;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SysRole getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        this.sysRole = sysRole;
    }
}