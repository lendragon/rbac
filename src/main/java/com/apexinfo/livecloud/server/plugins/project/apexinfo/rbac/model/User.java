package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.annotation.Valid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户类, 对应用户表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Table("CT_Rbac_User")
public class User {
    /**
     * 用户id
     */
    @Column("ID")
    @Valid(skipCode = 1)
    private Long id;
    /**
     * 用户标识
     */
    @Column("FUserName")
    @Valid(skipCode = 2)
    private String userName;
    /**
     * 用户名
     */
    @Column("FName")
    @Valid
    private String name;
    /**
     * 密码
     */
    @Column("FPassword")
    private String password;
    /**
     * 性别
     */
    @Column("FSex")
    @Valid
    private Integer sex;
    /**
     * 生日
     */
    @Column("FBirthDay")
    private Date birthDay;
    /**
     * 电话号码
     */
    @Column("FPhoneNum")
    private String phoneNum;
    /**
     * 角色状态
     */
    @Column("FState")
    @Valid(skipCode = 1)
    private Integer state;
    /**
     * 创建时间
     */
    @Column("FCreateTime")
    private Date createTime;
    /**
     * 修改时间
     */
    @Column("FUpdateTime")
    private Date updateTime;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 确认密码
     */
    private String checkedPassword;

    public User() {
    }

    public User(Long id, String userName, String name, String password, Integer sex, Date birthDay, String phoneNum, Integer state, Date createTime, Date updateTime, String oldPassword, String checkedPassword) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthDay = birthDay;
        this.phoneNum = phoneNum;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.oldPassword = oldPassword;
        this.checkedPassword = checkedPassword;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
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

    @JsonProperty("createTime")
    public Date getCreateTime() {
        return createTime;
    }

    @JsonIgnore
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("updateTime")
    public Date getUpdateTime() {
        return updateTime;
    }

    @JsonIgnore
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonIgnore
    public String getOldPassword() {
        return oldPassword;
    }

    @JsonProperty("oldPassword")
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @JsonIgnore
    public String getCheckedPassword() {
        return checkedPassword;
    }

    @JsonProperty("checkedPassword")
    public void setCheckedPassword(String checkedPassword) {
        this.checkedPassword = checkedPassword;
    }
}
