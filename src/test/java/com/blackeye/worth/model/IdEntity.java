package com.blackeye.worth.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Objects;

/**
 * 基本entity，项目中所有entity都继承它
 * Created by ganguixiang on 2017/9/27.
 */
@MappedSuperclass
public abstract class IdEntity {

    /**
     * 主键
     * uuid格式
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 描述
     */
    private String description;

    /**
     * 逻辑删除标志
     * false 未删除
     * true 删除
     */
    private Boolean delflag = Boolean.FALSE;

    public IdEntity() {
        createDate = new Date();
        modifyDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDelflag() {
        return delflag;
    }

    public void setDelflag(Boolean delflag) {
        this.delflag = delflag;
    }

    @Override
    public String toString() {
        return "IdEntity{" +
                "id='" + id + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                ", modifier='" + modifier + '\'' +
                ", modifyDate=" + modifyDate +
                ", description='" + description + '\'' +
                ", delflag=" + delflag +
                '}';
    }

    @Override
    public int hashCode()
    {
        if ("".equals(getId())){
            return super.hashCode();
        }
        return Objects.hashCode(this.getId());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null) {
            return false;
        }

        if ( (obj.getClass() != getClass()) ) {
            return false;
        }

        IdEntity idEntity = (IdEntity) obj;

        return getId().equals(idEntity.getId());
    }

}
