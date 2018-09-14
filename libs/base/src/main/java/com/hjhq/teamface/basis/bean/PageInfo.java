package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * 分页
 *
 * @author Administrator
 * @date 2017/12/27
 */

public class PageInfo implements Serializable {

    /**
     * totalPages : 1
     * pageSize : 10
     * totalRows : 1
     * pageNum : 1
     * curPageSize : 1
     */

    private int totalPages;
    private int pageSize;
    private int totalRows;
    private int pageNum;
    private int curPageSize;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getCurPageSize() {
        return curPageSize;
    }

    public void setCurPageSize(int curPageSize) {
        this.curPageSize = curPageSize;
    }
}
