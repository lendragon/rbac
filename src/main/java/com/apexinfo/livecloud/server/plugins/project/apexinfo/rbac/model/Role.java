package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @ClassName: Role
 * @Description: 角色类, 对应角色表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Table("CT_Rbac_Role")
public class Role {
    // 角色id
    @Column("ID")
    @JsonProperty("roleId")
    private Long id;
    // 角色编码
    @Column("FRoleCode")
    private String roleCode;
    // 角色名称
    @Column("FName")
    private String name;
    // 角色状态
    @Column("FState")
    private Integer state;
    // 角色描述
    @Column("FDescription")
    private String description;
    // 创建时间
    @Column("FCreateTime")
    private Date createTime;
    // 修改时间
    @Column("FUpdateTime")
    private Date updateTime;
    public Role() {
    }

    public Role(Long id, String roleCode, String name, Integer state, String description, Date createTime, Date updateTime) {
        this.id = id;
        this.roleCode = roleCode;
        this.name = name;
        this.state = state;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
