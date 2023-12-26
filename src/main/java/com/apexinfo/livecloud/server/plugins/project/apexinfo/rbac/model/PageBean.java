package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @ClassName: PageDTO
 * @Description: 分页Bean类
 * @Author linlongyue
 * @Date 2023/12/18
 * @Version 1.0
 */
public class PageBean<R> {
    /**
     * 获取到的记录
     */
    private List<R> records;
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 页长
     */
    private int pageSize;
    /**
     * 总数
     */
    private int total;
    /**
     * 查询关键字
     */
    private String keyword;

    public PageBean() {
    }

    public PageBean(List<R> records, int pageNo, int pageSize, int total, String keyword) {
        this.records = records;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.keyword = keyword;
    }


    @JsonProperty("records")
    public List<R> getRecords() {
        return records;
    }

    @JsonIgnore
    public void setRecords(List<R> records) {
        this.records = records;
    }

    @JsonProperty("total")
    public int getTotal() {
        return total;
    }

    @JsonIgnore
    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @JsonIgnore
    public String getKeyword() {
        return keyword;
    }

    @JsonProperty("keyword")
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
