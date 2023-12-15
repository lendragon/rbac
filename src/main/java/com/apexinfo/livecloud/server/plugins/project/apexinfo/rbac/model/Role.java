package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.Date;

/**
 * @ClassName: Role
 * @Description: 角色类, 对应角色表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class Role {
    private Long id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String description;

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
