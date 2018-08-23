package com.blackeye.worth.model;

import javax.persistence.MappedSuperclass;

/**
 */
@MappedSuperclass
public class TreeEntity extends IdEntity {

    /**
     * 层级，方便前端显示树形结构
     */
    private Integer level;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 显示的文本
     */
    private String label;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
