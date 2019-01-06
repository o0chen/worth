package com.blackeye.worth.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

@Entity
@DynamicUpdate
@Data
public class VipCustomer extends BaseDojo {

    public VipCustomer() {
        super();
    }

    private String name;
    private String mobile;
    private Integer status;
    private Integer gradle;
    private Integer growthValue;
    private Integer source;
    private Integer thirdKeyId;
    private Integer unionid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGradle() {
        return gradle;
    }

    public void setGradle(Integer gradle) {
        this.gradle = gradle;
    }

    public Integer getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(Integer growthValue) {
        this.growthValue = growthValue;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getThirdKeyId() {
        return thirdKeyId;
    }

    public void setThirdKeyId(Integer thirdKeyId) {
        this.thirdKeyId = thirdKeyId;
    }

    public Integer getUnionid() {
        return unionid;
    }

    public void setUnionid(Integer unionid) {
        this.unionid = unionid;
    }
}