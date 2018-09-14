package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 * 应用列表
 *
 * @author Administrator
 * @date 2018/5/9
 */

public class AppListResultBean extends BaseBean {

    /**
     * data : {"commonApplication":[{"chinese_name":"打印设置","icon_url":"","icon_color":"","id":225,"english_name":"bean1522316577685","icon_type":""},{"chinese_name":"69","icon_url":"","icon_color":"","id":4,"english_name":"bean1520842509332","icon_type":""}],"myApplication":[{"chinese_name":"上传应用测试","icon_url":"","icon_color":"","id":67,"icon_type":""},{"chinese_name":"正式测试应用上传","icon_url":"","icon_color":"","id":55,"icon_type":""},{"chinese_name":"上传应用测试","icon_url":"","icon_color":"","id":65,"icon_type":""},{"chinese_name":"厉害的很","icon_url":"","icon_color":"","id":77,"icon_type":""},{"chinese_name":"非常强大的应用","icon_url":"","icon_color":"","id":76,"icon_type":""},{"chinese_name":"测试1","icon_url":"","icon_color":"","id":78,"icon_type":""},{"chinese_name":"未命名应用1","icon_url":"","icon_color":"","id":1,"icon_type":""},{"chinese_name":"测试","icon_url":"","icon_color":"","id":2,"icon_type":""},{"chinese_name":"未命名应用2","icon_url":"","icon_color":"","id":3,"icon_type":""},{"chinese_name":"未命名应用3","icon_url":"","icon_color":"","id":4,"icon_type":""},{"chinese_name":"未命名应用4","icon_url":"","icon_color":"","id":63,"icon_type":""},{"chinese_name":"测试人员可选范围","icon_url":"","icon_color":"","id":64,"icon_type":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<AppModuleBean> commonApplication;
        private List<AppModuleBean> myApplication;

        public List<AppModuleBean> getCommonApplication() {
            return commonApplication;
        }

        public void setCommonApplication(List<AppModuleBean> commonApplication) {
            this.commonApplication = commonApplication;
        }

        public List<AppModuleBean> getMyApplication() {
            return myApplication;
        }

        public void setMyApplication(List<AppModuleBean> myApplication) {
            this.myApplication = myApplication;
        }

    }
}
