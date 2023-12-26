package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;


import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.annotation.Valid;

import java.util.List;

/**
 * @ClassName: RelaDTO
 * @Description: 关联DTO类, 对应修改关联关系时前端发送的数据
 * @Author linlongyue
 * @Date 2023/12/18
 * @Version 1.0
 */
public class RelativeBean {
    /**
     * 要修改的实体主键
     */
    @Valid
    private Long id;
    /**
     * 要添加的主键列表
     */
    @Valid(empty = true)
    private List<Long> addIds;
    /**
     * 要删除的主键列表
     */
    @Valid(empty = true)
    private List<Long> deleteIds;

    public RelativeBean() {
    }

    public RelativeBean(Long id, List<Long> addIds, List<Long> deleteIds) {
        this.id = id;
        this.addIds = addIds;
        this.deleteIds = deleteIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getAddIds() {
        return addIds;
    }

    public void setAddIds(List<Long> addIds) {
        this.addIds = addIds;
    }

    public List<Long> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<Long> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
