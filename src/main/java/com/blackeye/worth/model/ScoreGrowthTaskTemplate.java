package com.blackeye.worth.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
public class ScoreGrowthTaskTemplate extends BaseDojo {

    public ScoreGrowthTaskTemplate() {
        super();
    }

    /**
     * 任务名称
     */
    private Integer taskName;
    /**
     * 任务描述
     */
    private Integer taskDescription;
    /**
     * 任务类型 1-被动分发  2-主动领取 3-活动参与
     */
    private Integer type;


    /**
     * 任务奖励（成长值）积分
     */
    private Integer score;

    /**
     * 任务状态
     */
    private Integer status;

     /**
     * 任务开展时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 是否是可以多次参与的任务
     */
    private Integer isCanRepeat;
    /**
     * 可以从重复参与的次数
     */
    private Integer repeatTimes;


    public Integer getTaskName() {
        return taskName;
    }

    public void setTaskName(Integer taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(Integer taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsCanRepeat() {
        return isCanRepeat;
    }

    public void setIsCanRepeat(Integer isCanRepeat) {
        this.isCanRepeat = isCanRepeat;
    }

    public Integer getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(Integer repeatTimes) {
        this.repeatTimes = repeatTimes;
    }
}