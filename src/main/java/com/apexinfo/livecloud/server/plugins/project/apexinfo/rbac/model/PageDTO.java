package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.List;

/**
 * @ClassName: PageDTO
 * @Description: 分页DTO类, 对应查询数据和查询总数
 * @Author linlongyue
 * @Date 2023/12/18
 * @Version 1.0
 */
public class PageDTO<R> {
    private List<R> records;
    private Integer total;
    private Integer pageSize;

    public List<R> getRecords() {
        return records;
    }

    public void setRecords(List<R> records) {
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
