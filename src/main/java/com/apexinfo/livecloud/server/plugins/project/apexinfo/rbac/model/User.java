package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户类, 对应用户表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class User {
    private Long id;
    private String no;
    private String name;
    private String password;
    private Long sex;
    private Date birthDay;
    private String phoneNum;
    private Date createTime;
    private Date updateTime;

    public User() {
    }

    public User(Long id, String no, String name, String password, Long sex, Date birthDay, String phoneNum, Date createTime, Date updateTime) {
        this.id = id;
        this.no = no;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthDay = birthDay;
        this.phoneNum = phoneNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
