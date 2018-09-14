package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.basis.bean.AssistantDataBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class AppAssistantListBean extends BaseBean {


    /**
     * data : {"dataList":[{"bean_name":"bean1515809747766","datetime_create_time":1515816689975,"assistant_id":81,"bean_name_chinese":"小助手推送1","read_status":"0","id":9,"push_content":"11111","field_info":[{"field_label":"名称","field_value":"u快快快","id":13,"type":"text"}]},{"bean_name":"bean1515809747766","datetime_create_time":1515816428969,"assistant_id":81,"bean_name_chinese":"小助手推送1","read_status":"0","id":8,"push_content":"这是推送信息","field_info":[{"field_label":"修改时间","field_value":"1515816420216","id":12,"type":"datetime"},{"field_label":"名称","field_value":"给黄金季节","id":11,"type":"text"},{"field_label":"修改时间","field_value":"1515816420216","id":10,"type":"datetime"}]}],"pageInfo":{"totalPages":1,"totalRows":2,"curPageSize":2}}
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
         * dataList : [{"bean_name":"bean1515809747766","datetime_create_time":1515816689975,"assistant_id":81,"bean_name_chinese":"小助手推送1","read_status":"0","id":9,"push_content":"11111","field_info":[{"field_label":"名称","field_value":"u快快快","id":13,"type":"text"}]},{"bean_name":"bean1515809747766","datetime_create_time":1515816428969,"assistant_id":81,"bean_name_chinese":"小助手推送1","read_status":"0","id":8,"push_content":"这是推送信息","field_info":[{"field_label":"修改时间","field_value":"1515816420216","id":12,"type":"datetime"},{"field_label":"名称","field_value":"给黄金季节","id":11,"type":"text"},{"field_label":"修改时间","field_value":"1515816420216","id":10,"type":"datetime"}]}]
         * pageInfo : {"totalPages":1,"totalRows":2,"curPageSize":2}
         */

        private PageInfo pageInfo;
        private List<AssistantDataBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<AssistantDataBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<AssistantDataBean> dataList) {
            this.dataList = dataList;
        }


    }
}