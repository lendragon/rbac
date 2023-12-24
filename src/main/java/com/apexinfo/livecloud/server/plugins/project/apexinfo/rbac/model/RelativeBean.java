package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.List;

/**
 * @ClassName: RelaDTO
 * @Description: 关联DTO类, 对应修改关联关系时前端发送的数据
 * @Author linlongyue
 * @Date 2023/12/18
 * @Version 1.0
 */
public class RelativeBean {
    // 要修改的实体id
    private Long roleId;
    // 要添加的id列表
    private List<Long> addIds;
    // 要删除的id列表
    private List<Long> deleteIds;

    public RelativeBean() {
    }

    public RelativeBean(Long roleId, List<Long> addIds, List<Long> deleteIds) {
        this.roleId = roleId;
        this.addIds = addIds;
        this.deleteIds = deleteIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
