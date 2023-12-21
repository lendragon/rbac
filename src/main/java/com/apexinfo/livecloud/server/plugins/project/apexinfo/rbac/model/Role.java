package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @ClassName: Role
 * @Description: 角色类, 对应角色表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class Role {
    // 角色标识
    private Long id;
    // 角色名称
    @JsonProperty(required = true)
    private String name;
    // 角色状态
    @JsonProperty(required = true)
    private Integer state;
    // 角色描述
    private String description;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;

    public Role() {
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Role(Long id, String name, Integer state, String description, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
