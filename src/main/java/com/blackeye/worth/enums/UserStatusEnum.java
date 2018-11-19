package com.blackeye.worth.enums;

import java.io.Serializable;

/**
 * 菜单类型枚举
 */
public enum UserStatusEnum implements Serializable {
    ACTIVE("激活", 1),
    DISABLE("禁用", 2),
    ;

    private String label;
    private Integer index;

    UserStatusEnum(String label, Integer index) {
        this.label = label;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UserStatusEnum valueOf(Integer i) {
        for (UserStatusEnum menuTypeEnum : UserStatusEnum.values()) {
            if (menuTypeEnum.equals(i)) {
                return menuTypeEnum;
            }
        }
        return null;
    }




}
