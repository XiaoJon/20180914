package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 * Describe：
 */

public class MemoListBean extends BaseBean {


    /**
     * data : {"dataList":[],"pageInfo":{}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dataList : []
         * pageInfo : {}
         */

        private PageInfo pageInfo;
        private List<MemoListItemBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<MemoListItemBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<MemoListItemBean> dataList) {
            this.dataList = dataList;
        }
    }
}
