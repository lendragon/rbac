package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;

import java.util.Date;

/**
 * @ClassName: Menu
 * @Description: 菜单类, 对应菜单表
 * @Author linlongyue
 * @Date 2023/12/13
 * @Version 1.0
 */
@Table("CT_Rbac_Menu")
public class Menu {
    @Column("ID")
    private Long id;
    @Column("FName")
    private String name;
    @Column("FOrder")
    private Long order;
    @Column("FLevel")
    private Long level;
    @Column("FParentId")

    private Long parentId;
    @Column("FUrl")

    private String url;
    @Column("FCreateTime")

    private Date createTime;
    @Column("FUpdateTime")

    private Date updateTime;
    @Column("FDescription")

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
