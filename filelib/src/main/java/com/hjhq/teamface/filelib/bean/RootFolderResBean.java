package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class RootFolderResBean extends BaseBean implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 公司文件
         * id : 1
         * status : 1
         */

        private String name;
        private String id;
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
