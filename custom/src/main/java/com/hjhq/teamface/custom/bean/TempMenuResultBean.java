package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/11/3
 */

public class TempMenuResultBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public DataBean() {
        }

        public DataBean(String id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * id : 0
         * name : 全部市场活动
         */

        private String id;
        private String name;
        private String is_seas_pool;//1是公海池
        private String is_seas_admin;//1是管理员，0成员

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIs_seas_pool() {
            return is_seas_pool;
        }

        public void setIs_seas_pool(String is_seas_pool) {
            this.is_seas_pool = is_seas_pool;
        }

        public String getIs_seas_admin() {
            return is_seas_admin;
        }

        public void setIs_seas_admin(String is_seas_admin) {
            this.is_seas_admin = is_seas_admin;
        }
    }
}
