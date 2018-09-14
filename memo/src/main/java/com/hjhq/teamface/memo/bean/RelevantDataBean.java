package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.RowDataBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 * Describe：
 */

public class RelevantDataBean extends BaseBean {

    /**
     * data : {"moduleDataList":[{"module_id":815,"color":"","sub_id":null,"dataType":3,"taskInfoId":null,"beanName":"bean1534928165336","complete_status":0,"id":{"name":"id","label":"主键id","value":1},"row":[{"name":"text_1534928165336","label":"名称","value":"11111111"},{"name":"personnel_principal","label":"负责人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"personnel_create_by","label":"创建人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_create_time","label":"创建时间","value":1534928326715},{"name":"personnel_modify_by","label":"修改人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_modify_time","label":"修改时间","value":1534928326715},{"name":"del_status","label":"删除状态","value":"0"},{"name":"seas_pool_id","label":"公海池编号","value":0}],"module_name":"AAAAAAAA","lockedState":""},{"module_id":815,"color":"","sub_id":null,"dataType":3,"taskInfoId":null,"beanName":"bean1534928165336","complete_status":0,"id":{"name":"id","label":"主键id","value":2},"row":[{"name":"text_1534928165336","label":"名称","value":"2222222"},{"name":"personnel_principal","label":"负责人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"personnel_create_by","label":"创建人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_create_time","label":"创建时间","value":1534928330856},{"name":"personnel_modify_by","label":"修改人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_modify_time","label":"修改时间","value":1534928330856},{"name":"del_status","label":"删除状态","value":"0"},{"name":"seas_pool_id","label":"公海池编号","value":0}],"module_name":"AAAAAAAA","lockedState":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ModuleDataListBean> moduleDataList;

        public List<ModuleDataListBean> getModuleDataList() {
            return moduleDataList;
        }

        public void setModuleDataList(List<ModuleDataListBean> moduleDataList) {
            this.moduleDataList = moduleDataList;
        }

        public static class ModuleDataListBean {
            /**
             * module_id : 815
             * color :
             * sub_id : null
             * dataType : 3
             * taskInfoId : null
             * beanName : bean1534928165336
             * complete_status : 0
             * id : {"name":"id","label":"主键id","value":1}
             * row : [{"name":"text_1534928165336","label":"名称","value":"11111111"},{"name":"personnel_principal","label":"负责人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"personnel_create_by","label":"创建人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_create_time","label":"创建时间","value":1534928326715},{"name":"personnel_modify_by","label":"修改人","value":[{"post_name":"员工","name":"罗军","id":1,"picture":"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1534757029477\blob&fileSize=6226"}]},{"name":"datetime_modify_time","label":"修改时间","value":1534928326715},{"name":"del_status","label":"删除状态","value":"0"},{"name":"seas_pool_id","label":"公海池编号","value":0}]
             * module_name : AAAAAAAA
             * lockedState :
             */

            private String module_id;
            private String color;
            private String sub_id;
            private String dataType;
            private String taskInfoId;
            private String beanName;
            private String complete_status;
            private IdBean id;
            private String module_name;
            private String lockedState;
            private List<RowDataBean> row;
            private String icon_type;
            private String icon_color;
            private String icon_url;

            public String getIcon_type() {
                return icon_type;
            }

            public void setIcon_type(String icon_type) {
                this.icon_type = icon_type;
            }

            public String getIcon_color() {
                return icon_color;
            }

            public void setIcon_color(String icon_color) {
                this.icon_color = icon_color;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }

            public String getModule_id() {
                return module_id;
            }

            public void setModule_id(String module_id) {
                this.module_id = module_id;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getSub_id() {
                return sub_id;
            }

            public void setSub_id(String sub_id) {
                this.sub_id = sub_id;
            }

            public String getDataType() {
                return dataType;
            }

            public void setDataType(String dataType) {
                this.dataType = dataType;
            }

            public String getTaskInfoId() {
                return taskInfoId;
            }

            public void setTaskInfoId(String taskInfoId) {
                this.taskInfoId = taskInfoId;
            }

            public String getBeanName() {
                return beanName;
            }

            public void setBeanName(String beanName) {
                this.beanName = beanName;
            }

            public String getComplete_status() {
                return complete_status;
            }

            public void setComplete_status(String complete_status) {
                this.complete_status = complete_status;
            }

            public IdBean getId() {
                return id;
            }

            public void setId(IdBean id) {
                this.id = id;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public String getLockedState() {
                return lockedState;
            }

            public void setLockedState(String lockedState) {
                this.lockedState = lockedState;
            }

            public List<RowDataBean> getRow() {
                return row;
            }

            public void setRow(List<RowDataBean> row) {
                this.row = row;
            }

            public static class IdBean {
                /**
                 * name : id
                 * label : 主键id
                 * value : 1
                 */

                private String name;
                private String label;
                private String value;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
