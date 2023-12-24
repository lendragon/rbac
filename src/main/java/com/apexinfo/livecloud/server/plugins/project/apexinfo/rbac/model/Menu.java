package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table("CT_Rbac_Menu")
public class Menu {
    // 菜单id
    @Column("ID")
    @JsonProperty("menuId")
    private Long id;
    // 菜单名称
    @Column("FName")
    private String name;
    // 菜单显示顺序
    @Column("FOrder")
    private Integer order;
    // 菜单层级
    @Column("FLevel")
    @JsonIgnore
    private Integer level;
    // 父菜单ID
    @Column("FParentId")
    private Long parentId;
    // 菜单路径
    @Column("FUrl")
    private String url;
    // 菜单状态
    @Column("FState")
    private Integer state;
    // 菜单描述
    @Column("FDescription")
    private String description;
    // 创建时间
    @Column("FCreateTime")
    private Date createTime;
    // 修改时间
    @Column("FUpdateTime")
    private Date updateTime;
    // 子菜单
    private List<Menu> children;
    // 角色是否拥有该菜单
    private Boolean checked;

    public Menu() {
    }

    public Menu(Long id, String name, Integer order, Integer level, Long parentId, String url, Integer state, String description, Date createTime, Date updateTime, List<Menu> children, Boolean checked) {
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
        this.checked = checked;
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

    @JsonProperty("children")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Menu> getChildren() {
        return children;
    }

    @JsonIgnore
    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @JsonProperty("checked")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getChecked() {
        return checked;
    }

    @JsonIgnore
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
