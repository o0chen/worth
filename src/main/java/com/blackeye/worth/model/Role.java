package com.blackeye.worth.model;

import javax.persistence.*;
import java.util.List;

@Entity(name="role")
public class Role extends IdEntity{
    public Role() {
    }

    private String roleName;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "menu_permission")
    @JoinTable(
            name = "role_menu_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_permission_id", referencedColumnName = "id")
    )
    private List<MenuPermission> menuPermissions;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MenuPermission> getMenuPermissions() {
        return menuPermissions;
    }

    public void setMenuPermissions(List<MenuPermission> menuPermissions) {
        this.menuPermissions = menuPermissions;
    }
}