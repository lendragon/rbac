package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: Menu
 * @Description: 菜单类, 对应菜单表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
public class Menu {
    // 菜单标识
    private Long id;
    // 菜单名称
    @JsonProperty(required = true)
    private String name;
    // 菜单显示顺序
    @JsonProperty(required = true)
    private Integer order;
    // 菜单层级
    @JsonProperty(required = true)
    private Integer level;
    // 父菜单ID
    @JsonProperty(required = true)
    private Long parentId;
    // 菜单路径
    private String url;
    // 菜单状态
    @JsonProperty(required = true)
    private Integer state;
    // 菜单描述
    private String description;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;
    private List<Menu> children;

    public Menu() {
    }

    public Menu(Long id, String name, Integer order, Integer level, Long parentId, String url, Integer state, String description, Date createTime, Date updateTime, List<Menu> children) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.level = level;
        this.parentId = parentId;
        this.url = url;
        this.state = state;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.children = children;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
