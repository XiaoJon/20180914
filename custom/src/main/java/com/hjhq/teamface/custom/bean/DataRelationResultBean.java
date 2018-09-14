package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.RowBean;

import java.io.Serializable;
import java.util.List;

/**
 * 数据关联模块和title
 *
 * @author lx
 * @date 2017/9/19
 */

public class DataRelationResultBean extends BaseBean implements Serializable {

    /**
     * data : {"customerAddress":"140000,140300,140303","customerLeve":"[{\"color\":\"#fff\",\"label\":\"中型客户\",\"value\":\"2\"}]","publicPool_startTime":"","companyTel":"354845345","remark":"倒萨发送到饭店符合法规和规范化斯蒂芬斯蒂芬","dealStatus":"[{\"color\":\"#fff\",\"label\":\"未成交\",\"value\":\"1\"}]","companyUrl":"http://www.dsfdsf.com","customerName":"帅剩余","principal":"[{\"employeeName\":\"李萌\",\"id\":3489871441805312,\"$$hashKey\":274}]","activityId":"","companyEmail":"35468154@qq.com","detailedAddress":"广东省但是法国","customerTrade":"[{\"color\":\"#fff\",\"label\":\"教育\",\"value\":\"1\"}]","commonCustomerPond":"[{\"color\":\"#fff\",\"label\":\"公用公海池\",\"value\":\"0\"}]","id":"3","customerSource":"[{\"color\":\"#fff\",\"label\":\"客户推荐\",\"value\":\"2\"}]"}
     */

    private DataRelation data;

    public static class DataRelation {
        private List<RefModule> refModules;
        private RowBean operationInfo;
        //0 代表没有开通 大于等于1：开通
        private String isOpenProcess;
        //0 代表没有开通 大于等于1：开通
        private String isEmailModule;

        public List<RefModule> getRefModules() {
            return refModules;
        }

        public void setRefModules(List<RefModule> refModules) {
            this.refModules = refModules;
        }

        public RowBean getOperationInfo() {
            return operationInfo;
        }

        public void setOperationInfo(RowBean operationInfo) {
            this.operationInfo = operationInfo;
        }

        public String getIsEmailModule() {
            return isEmailModule;
        }

        public void setIsEmailModule(String isEmailModule) {
            this.isEmailModule = isEmailModule;
        }

        public static class RefModule {
            private String moduleName;
            private String moduleLabel;
            private String totalRows;
            private String fieldName;
            private String fieldLabel;
            private String referenceField;
            private String show;

            public String getModuleName() {
                return moduleName;
            }

            public void setModuleName(String moduleName) {
                this.moduleName = moduleName;
            }

            public String getModuleLabel() {
                return moduleLabel;
            }

            public void setModuleLabel(String moduleLabel) {
                this.moduleLabel = moduleLabel;
            }

            public String getTotalRows() {
                return totalRows;
            }

            public void setTotalRows(String totalRows) {
                this.totalRows = totalRows;
            }

            public String getFieldName() {
                return fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }

            public String getFieldLabel() {
                return fieldLabel;
            }

            public void setFieldLabel(String fieldLabel) {
                this.fieldLabel = fieldLabel;
            }

            public String getShow() {
                return show;
            }

            public void setShow(String show) {
                this.show = show;
            }

            public String getReferenceField() {
                return referenceField;
            }

            public void setReferenceField(String referenceField) {
                this.referenceField = referenceField;
            }
        }

        public String getIsOpenProcess() {
            return isOpenProcess;
        }

        public void setIsOpenProcess(String isOpenProcess) {
            this.isOpenProcess = isOpenProcess;
        }
    }

    public DataRelation getData() {
        return data;
    }

    public void setData(DataRelation data) {
        this.data = data;
    }
}
