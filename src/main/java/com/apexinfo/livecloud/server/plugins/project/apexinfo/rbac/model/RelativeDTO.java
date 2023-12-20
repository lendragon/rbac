package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.List;

/**
 * @ClassName: RelaDTO
 * @Description: 关联DTO类, 对应修改关联关系时前端发送的数据
 * @Author linlongyue
 * @Date 2023/12/18
 * @Version 1.0
 */
public class RelativeDTO {
    // 要修改的实体id
    private Long id;
    // 要添加的id列表
    private List<Long> addList;
    // 要删除的id列表
    private List<Long> deleteList;

    public RelativeDTO() {
    }

    public RelativeDTO(Long id, List<Long> addList, List<Long> deleteList) {
        this.id = id;
        this.addList = addList;
        this.deleteList = deleteList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getAddList() {
        return addList;
    }

    public void setAddList(List<Long> addList) {
        this.addList = addList;
    }

    public List<Long> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<Long> deleteList) {
        this.deleteList = deleteList;
    }
}
