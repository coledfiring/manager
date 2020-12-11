package com.whaty.products.service.flow.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 审批流程分页
 *
 * @author weipengsen
 */
@Data
public class CheckFlowPage implements Serializable {

    private static final long serialVersionUID = -7674166009641159099L;

    private String search;

    private Integer currentPage;

    private Integer pageSize;

    private Integer totalPage;

    /**
     * 设置总页数
     * @param totalNum
     */
    public void countTotalPage(Integer totalNum) {
        this. totalPage = totalNum % this.pageSize == 0 ? totalNum / this.pageSize : (totalNum / this.pageSize + 1);
    }

    /**
     * 获取当前页数
     * @return
     */
    public Integer getCurrentPage() {
        this.currentPage = this.currentPage > this.totalPage ? this.totalPage : this.currentPage;
        this.currentPage = this.currentPage <= 1 ? 1 : this.currentPage;
        return this.currentPage;
    }

    /**
     * 获取开始边界
     * @return
     */
    public Integer countStartLimit() {
        return (this.getCurrentPage() - 1) * pageSize;
    }
}
