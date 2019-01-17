package com.blackeye.worth.model;

import com.blackeye.worth.core.annotations.KeyWordSearch;
import com.blackeye.worth.enums.UserStatusEnum;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
public class SysUser extends BaseDojo {

    public SysUser() {
        super();
    }

    @KeyWordSearch
    private String name;
    @KeyWordSearch
    private String mobile;

    /**
     * 用户状态
     * 使用枚举类型，数据库中只保存枚举对应的String
     */
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    private String password;

    @ManyToOne(cascade = CascadeType.DETACH)//cascade-串联操作
    @JoinColumn(name="sys_role_id",updatable = true,insertable = true)//默认就是
//    @OnDelete(action = OnDeleteAction.CASCADE) 删除user时删除role
//    @ManyToOne(cascade=CascadeType.REMOVE) 删除role时删除user
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public String toString() {
        return "SysUser{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", password='" + password + '\'' +
                ", sysRole=" + sysRole +
                "} " + super.toString();
    }
}