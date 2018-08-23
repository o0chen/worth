package com.blackeye.worth.enums;

/**
 * 菜单类型枚举
 */
public enum MenuTypeEnum {
    MENU("菜单", 1),
    PERMISSION("权限", 2),
    ;

    private String label;
    private Integer index;

    MenuTypeEnum(String label, Integer index) {
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

    public MenuTypeEnum valueOf(Integer i) {
        for (MenuTypeEnum menuTypeEnum : MenuTypeEnum.values()) {
            if (menuTypeEnum.equals(i)) {
                return menuTypeEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MenuTypeEnum{" +
                "label='" + label + '\'' +
                ", index=" + index +
                '}';
    }
}
