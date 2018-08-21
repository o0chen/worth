package com.blackeye.worth.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class Permission extends IdEntity{
    public Permission() {
    }

    private String permission;
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

}