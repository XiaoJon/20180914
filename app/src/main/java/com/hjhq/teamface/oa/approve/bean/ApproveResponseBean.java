package com.hjhq.teamface.oa.approve.bean;

import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/1/10
 */

public class ApproveResponseBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private PageInfo pageInfo;
        private List<ApproveListBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<ApproveListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<ApproveListBean> dataList) {
            this.dataList = dataList;
        }

    }
}
