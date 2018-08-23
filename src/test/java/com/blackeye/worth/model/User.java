package com.blackeye.worth.model;

import javax.persistence.Entity;

@Entity(name="user")
//@MappedSuperclass
public class User extends BaseDojo {

    public User() {
    }

    private String name;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
}