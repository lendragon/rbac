package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.Date;

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
    private String name;
    // 菜单显示顺序
    private Long order;
    // 菜单层级
    private Long level;
    // 父菜单ID
    private Long parentId;
    // 菜单路径
    private String url;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;
    // 菜单描述
    private String description;

    public Menu() {
    }

    public Menu(Long id, String name, Long order, Long level, Long parentId, String url, Date createTime, Date updateTime, String description) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.level = level;
        this.parentId = parentId;
        this.url = url;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.description = description;
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

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
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
}
