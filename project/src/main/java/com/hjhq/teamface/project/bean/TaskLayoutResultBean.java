package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomBean;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/6/25
 */

public class TaskLayoutResultBean extends BaseBean {

    /**
     * data : {"disableLayout":{"rows":[{"field":{"fieldControl":"0","defaultEntrys":[],"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_difficulty","width":"50%","active":"0","label":"难易度","state":"1","type":"picklist","remove":"1","entrys":[{"color":"#FFFFFF","label":"1","value":"0"},{"color":"#FFFFFF","label":"2","value":"1"},{"color":"#FFFFFF","label":"3","value":"2"},{"color":"#FFFFFF","label":"4","value":"3"},{"color":"#FFFFFF","label":"5","value":"4"}],"componentList":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"subform_datetime_date","width":"50%","label":"日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","editView":"1","numberType":"0","terminalPc":"1","terminalApp":"1","pointOut":"","defaultValue":"","numberLenth":"2","betweenMax":"","addView":"1","betweenMin":"","structure":"1"},"typeText":"数字","name":"subform_number_worktime","width":"50%","label":"工时","state":"0","type":"number","remove":"1"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","addView":"1","structure":"1"},"typeText":"单行文本","name":"subform_text_content","width":"50%","label":"内容","state":"0","type":"text","remove":"1"}]},{"field":{"fieldControl":"0","defaultEntrys":[],"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_priority","width":"50%","active":"0","label":"优先级","state":"1","type":"picklist","remove":"1","entrys":[{"color":"#F40808","label":"高","value":"0"},{"color":"#F8F40A","label":"中","value":"1"},{"color":"#0623F9","label":"低","value":"2"}]},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"datetime_starttime","width":"50%","active":"0","label":"开始时间","state":"1","type":"datetime","remove":"1"},{"field":{"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1"},"typeText":"子表单","name":"subform_tasktime","width":"100%","componentList":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"subform_datetime_date","width":"50%","label":"日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","editView":"1","numberType":"0","terminalPc":"1","terminalApp":"1","pointOut":"","defaultValue":"","numberLenth":"2","betweenMax":"","addView":"1","betweenMin":"","structure":"1"},"typeText":"数字","name":"subform_number_worktime","width":"50%","label":"工时","state":"0","type":"number","remove":"1"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","addView":"1","structure":"1"},"typeText":"单行文本","name":"subform_text_content","width":"50%","label":"内容","state":"0","type":"text","remove":"1"}],"active":"1","label":"任务工期","state":"1","type":"subform","remove":"1"}]},"companyId":"2","terminalPc":1,"terminalApp":1,"componentLayout":{"rows":[{"field":{},"typeText":"单行文本","icon":"icon-danhangwenben","label":"单行文本","type":"text","isDrag":true,"selected":true},{"field":{},"typeText":"多行文本","icon":"icon-duohangwenben","label":"多行文本","type":"textarea","isDrag":true,"selected":true},{"field":{},"typeText":"下拉选项","icon":"icon-xialaxuanxiang","label":"下拉选项","type":"picklist","isDrag":true,"selected":true},{"field":{},"typeText":"数字","icon":"icon-shuzi","label":"数字","type":"number","isDrag":true,"selected":true},{"field":{},"typeText":"日期/时间","icon":"icon-riqishijian","label":"日期/时间","type":"datetime","isDrag":true,"selected":true},{"field":{},"typeText":"附件","icon":"icon-fujian","label":"附件","type":"attachment","isDrag":true,"selected":true}]},"enableLayout":{"rows":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","isShowCard":"0","isFillReason":"0","addView":"1","structure":"1"},"typeText":"单行文本","name":"text_name","width":"100%","active":"0","label":"名称","state":"0","type":"text","remove":"1","relevanceWhere":[{"fieldName":"","fieldLabel":"","operatorType":"","value":""}],"relevanceModule":{"moduleName":"","moduleLabel":""},"relevanceField":{"fieldName":"","fieldLabel":""},"seniorWhere":"1 AND 2","searchFields":[{"fieldName":"","fieldLabel":""}],"entrys":[{"color":"","label":"","value":""}]},{"relevanceWhere":[{"fieldName":"","fieldLabel":"","operatorType":"","value":""}],"active":"0","label":"关联","type":"reference","remove":"1","field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","pointOut":"","isShowCard":"1","isFillReason":"0","addView":"1","structure":"1"},"relevanceModule":{"moduleName":"","moduleLabel":""},"typeText":"关联关系","name":"reference_relation","width":"100%","relevanceField":{"fieldName":"","fieldLabel":""},"seniorWhere":"1 AND 2","searchFields":[{"fieldName":"","fieldLabel":""}],"state":"0"},{"field":{"fieldControl":"0","editView":"1","defaultEntrys":[],"terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_tag","width":"100%","active":"0","label":"标签","state":"0","type":"picklist","remove":"1","entrys":[{"color":"","label":"","value":""}]},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","isShowCard":"0","isFillReason":"0","addView":"1","structure":"1"},"typeText":"富文本","name":"multitext_desc","width":"100%","active":"0","label":"描述","state":"0","type":"multitext","remove":"1"},{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":1,"chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_execution","width":"50%","active":"0","label":"执行人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","datetimeType":"YYYY-MM-DD","terminalApp":1,"defaultValue":"","defaultValueId":"","addView":"1","structure":"1"},"typeText":"日期/时间","name":"datetime_deadline","width":"50%","active":"0","label":"截止日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","countLimit":"0","editView":"1","terminalPc":"1","terminalApp":1,"maxSize":"","addView":"1","maxCount":"","structure":"1"},"typeText":"附件","name":"attachment_1520248017179","width":"100%","active":"0","label":"附件","state":"0","type":"attachment","remove":"1"}]},"systemLayout":{"rows":[{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_create_by","width":"50%","label":"创建人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"","formatType":"yyyy-MM-dd HH:mm:ss","addView":"1","structure":"1"},"typeText":"日期时间","name":"datetime_create_time","width":"50%","label":"创建时间","state":"0","type":"datetime","remove":"1"},{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_modify_by","width":"50%","label":"修改人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"","formatType":"yyyy-MM-dd HH:mm:ss","addView":"1","structure":"1"},"typeText":"日期时间","name":"datetime_modify_time","width":"50%","label":"修改时间","state":"0","type":"datetime","remove":"1"}]},"_id":{"$oid":"5b2c723f410b862a6437da4d"},"version":"1.0.0","bean":"project_custom_51"}
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
         * disableLayout : {"rows":[{"field":{"fieldControl":"0","defaultEntrys":[],"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_difficulty","width":"50%","active":"0","label":"难易度","state":"1","type":"picklist","remove":"1","entrys":[{"color":"#FFFFFF","label":"1","value":"0"},{"color":"#FFFFFF","label":"2","value":"1"},{"color":"#FFFFFF","label":"3","value":"2"},{"color":"#FFFFFF","label":"4","value":"3"},{"color":"#FFFFFF","label":"5","value":"4"}],"componentList":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"subform_datetime_date","width":"50%","label":"日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","editView":"1","numberType":"0","terminalPc":"1","terminalApp":"1","pointOut":"","defaultValue":"","numberLenth":"2","betweenMax":"","addView":"1","betweenMin":"","structure":"1"},"typeText":"数字","name":"subform_number_worktime","width":"50%","label":"工时","state":"0","type":"number","remove":"1"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","addView":"1","structure":"1"},"typeText":"单行文本","name":"subform_text_content","width":"50%","label":"内容","state":"0","type":"text","remove":"1"}]},{"field":{"fieldControl":"0","defaultEntrys":[],"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_priority","width":"50%","active":"0","label":"优先级","state":"1","type":"picklist","remove":"1","entrys":[{"color":"#F40808","label":"高","value":"0"},{"color":"#F8F40A","label":"中","value":"1"},{"color":"#0623F9","label":"低","value":"2"}]},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"datetime_starttime","width":"50%","active":"0","label":"开始时间","state":"1","type":"datetime","remove":"1"},{"field":{"editView":"1","terminalPc":"1","terminalApp":"1","addView":"1","structure":"1"},"typeText":"子表单","name":"subform_tasktime","width":"100%","componentList":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"0","formatType":"yyyy-MM-dd","addView":"1","structure":"1"},"typeText":"日期/时间","name":"subform_datetime_date","width":"50%","label":"日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","editView":"1","numberType":"0","terminalPc":"1","terminalApp":"1","pointOut":"","defaultValue":"","numberLenth":"2","betweenMax":"","addView":"1","betweenMin":"","structure":"1"},"typeText":"数字","name":"subform_number_worktime","width":"50%","label":"工时","state":"0","type":"number","remove":"1"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","addView":"1","structure":"1"},"typeText":"单行文本","name":"subform_text_content","width":"50%","label":"内容","state":"0","type":"text","remove":"1"}],"active":"1","label":"任务工期","state":"1","type":"subform","remove":"1"}]}
         * companyId : 2
         * terminalPc : 1
         * terminalApp : 1
         * componentLayout : {"rows":[{"field":{},"typeText":"单行文本","icon":"icon-danhangwenben","label":"单行文本","type":"text","isDrag":true,"selected":true},{"field":{},"typeText":"多行文本","icon":"icon-duohangwenben","label":"多行文本","type":"textarea","isDrag":true,"selected":true},{"field":{},"typeText":"下拉选项","icon":"icon-xialaxuanxiang","label":"下拉选项","type":"picklist","isDrag":true,"selected":true},{"field":{},"typeText":"数字","icon":"icon-shuzi","label":"数字","type":"number","isDrag":true,"selected":true},{"field":{},"typeText":"日期/时间","icon":"icon-riqishijian","label":"日期/时间","type":"datetime","isDrag":true,"selected":true},{"field":{},"typeText":"附件","icon":"icon-fujian","label":"附件","type":"attachment","isDrag":true,"selected":true}]}
         * enableLayout : {"rows":[{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","isShowCard":"0","isFillReason":"0","addView":"1","structure":"1"},"typeText":"单行文本","name":"text_name","width":"100%","active":"0","label":"名称","state":"0","type":"text","remove":"1"},{"relevanceWhere":[{"fieldName":"","fieldLabel":"","operatorType":"","value":""}],"active":"0","label":"关联","type":"reference","remove":"1","field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","pointOut":"","isShowCard":"1","isFillReason":"0","addView":"1","structure":"1"},"relevanceModule":{"moduleName":"","moduleLabel":""},"typeText":"关联关系","name":"reference_relation","width":"100%","relevanceField":{"fieldName":"","fieldLabel":""},"seniorWhere":"1 AND 2","searchFields":[{"fieldName":"","fieldLabel":""}],"state":"0"},{"field":{"fieldControl":"0","editView":"1","defaultEntrys":[],"terminalPc":"1","terminalApp":"1","addView":"1","structure":"1","chooseType":"0"},"typeText":"下拉选项","name":"picklist_tag","width":"100%","active":"0","label":"标签","state":"0","type":"picklist","remove":"1","entrys":[{"color":"","label":"","value":""}]},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","isShowCard":"0","isFillReason":"0","addView":"1","structure":"1"},"typeText":"富文本","name":"multitext_desc","width":"100%","active":"0","label":"描述","state":"0","type":"multitext","remove":"1"},{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":1,"chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_execution","width":"50%","active":"0","label":"执行人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","datetimeType":"YYYY-MM-DD","terminalApp":1,"defaultValue":"","defaultValueId":"","addView":"1","structure":"1"},"typeText":"日期/时间","name":"datetime_deadline","width":"50%","active":"0","label":"截止日期","state":"0","type":"datetime","remove":"1"},{"field":{"fieldControl":"0","countLimit":"0","editView":"1","terminalPc":"1","terminalApp":1,"maxSize":"","addView":"1","maxCount":"","structure":"1"},"typeText":"附件","name":"attachment_1520248017179","width":"100%","active":"0","label":"附件","state":"0","type":"attachment","remove":"1"}]}
         * systemLayout : {"rows":[{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_create_by","width":"50%","label":"创建人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"","formatType":"yyyy-MM-dd HH:mm:ss","addView":"1","structure":"1"},"typeText":"日期时间","name":"datetime_create_time","width":"50%","label":"创建时间","state":"0","type":"datetime","remove":"1"},{"field":{"defaultPersonnel":[],"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","chooseRange":[],"addView":"1","structure":"1","chooseType":"0"},"typeText":"人员","name":"personnel_modify_by","width":"50%","label":"修改人","state":"0","type":"personnel","remove":"0"},{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","defaultValue":"","defaultValueId":"","formatType":"yyyy-MM-dd HH:mm:ss","addView":"1","structure":"1"},"typeText":"日期时间","name":"datetime_modify_time","width":"50%","label":"修改时间","state":"0","type":"datetime","remove":"1"}]}
         * _id : {"$oid":"5b2c723f410b862a6437da4d"}
         * version : 1.0.0
         * bean : project_custom_51
         */

        private String companyId;
        private EnableLayoutBean enableLayout;
        private String version;
        private String bean;



        public static class EnableLayoutBean {
            private List<CustomBean> rows;

            public List<CustomBean> getRows() {
                return rows;
            }

            public void setRows(List<CustomBean> rows) {
                this.rows = rows;
            }
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public EnableLayoutBean getEnableLayout() {
            return enableLayout;
        }

        public void setEnableLayout(EnableLayoutBean enableLayout) {
            this.enableLayout = enableLayout;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }
    }
}
