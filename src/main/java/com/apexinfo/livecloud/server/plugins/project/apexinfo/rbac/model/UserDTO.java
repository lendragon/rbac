package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserDTO
 * @Description: 用户类, 对应用户表
 * @Author linlongyue
 * @Date 2023/12/15
 * @Version 1.0
 */
public class UserDTO {
   private List<User> records;
    private Integer total;
    private Integer pageSize;

    public List<User> getRecords() {
        return records;
    }

    public void setRecords(List<User> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
