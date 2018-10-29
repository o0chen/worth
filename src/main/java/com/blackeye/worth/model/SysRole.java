package com.blackeye.worth.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class SysRole extends BaseDojo {
    public SysRole() {
    }

    private String roleName;
    //@OneToOne(fetch = FetchType.EAGER)
    //private SysUser sysUser;

    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_menu_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_permission_id", referencedColumnName = "id")
    )
    @OrderBy("menuOrder ASC")
    private List<SysMenuPermission> sysMenuPermissions;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<SysMenuPermission> getSysMenuPermissions() {
        return sysMenuPermissions;
    }

    public void setSysMenuPermissions(List<SysMenuPermission> sysMenuPermissions) {
        this.sysMenuPermissions = sysMenuPermissions;
    }
}