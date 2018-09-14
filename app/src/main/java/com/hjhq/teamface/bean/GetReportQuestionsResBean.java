package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/6/21 19:32 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.bean
 * @class http://192.168.1.172:8095/teamface/workReport/workReportDaily/new?moduleSonId=3427531370872832&employeeId=3427531333255168
 * @anthor Administrator TEL:13163739593
 * @time 2017/6/21 19:32
 * @change
 * @chang time
 * @class describe
 */
public class GetReportQuestionsResBean extends BaseBean {

    /**
     * data : {"workReportDaily":[{"id":3428596631650304,"createDate":null,"disabled":null,"dailyQuestion":"本日完成了哪些工作？最有成就的是什么？","versionNumber":1498099905788,"moduleSonId":3428596630306816},{"id":3428596631650305,"createDate":null,"disabled":null,"dailyQuestion":"本日工作你遇到哪些问题？需要得到什么帮助？","versionNumber":1498099905788,"moduleSonId":3428596630306816},{"id":3428596631650306,"createDate":null,"disabled":null,"dailyQuestion":"明天你计划做什么？要完成什么目标？","versionNumber":1498099905788,"moduleSonId":3428596630306816}],"dailyTitle":1498118305879}
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
         * workReportDaily : [{"id":3428596631650304,"createDate":null,"disabled":null,"dailyQuestion":"本日完成了哪些工作？最有成就的是什么？","versionNumber":1498099905788,"moduleSonId":3428596630306816},{"id":3428596631650305,"createDate":null,"disabled":null,"dailyQuestion":"本日工作你遇到哪些问题？需要得到什么帮助？","versionNumber":1498099905788,"moduleSonId":3428596630306816},{"id":3428596631650306,"createDate":null,"disabled":null,"dailyQuestion":"明天你计划做什么？要完成什么目标？","versionNumber":1498099905788,"moduleSonId":3428596630306816}]
         * dailyTitle : 1498118305879
         */

        private long dailyTitle;
        private List<WorkReportDailyBean> workReportDaily;

        public long getDailyTitle() {
            return dailyTitle;
        }

        public void setDailyTitle(long dailyTitle) {
            this.dailyTitle = dailyTitle;
        }

        public List<WorkReportDailyBean> getWorkReportDaily() {
            return workReportDaily;
        }

        public void setWorkReportDaily(List<WorkReportDailyBean> workReportDaily) {
            this.workReportDaily = workReportDaily;
        }

        public static class WorkReportDailyBean {
            /**
             * id : 3428596631650304
             * createDate : null
             * disabled : null
             * dailyQuestion : 本日完成了哪些工作？最有成就的是什么？
             * versionNumber : 1498099905788
             * moduleSonId : 3428596630306816
             */

            private long id;
            private Object createDate;
            private Object disabled;
            private String dailyQuestion;
            private long versionNumber;
            private long moduleSonId;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public Object getCreateDate() {
                return createDate;
            }

            public void setCreateDate(Object createDate) {
                this.createDate = createDate;
            }

            public Object getDisabled() {
                return disabled;
            }

            public void setDisabled(Object disabled) {
                this.disabled = disabled;
            }

            public String getDailyQuestion() {
                return dailyQuestion;
            }

            public void setDailyQuestion(String dailyQuestion) {
                this.dailyQuestion = dailyQuestion;
            }

            public long getVersionNumber() {
                return versionNumber;
            }

            public void setVersionNumber(long versionNumber) {
                this.versionNumber = versionNumber;
            }

            public long getModuleSonId() {
                return moduleSonId;
            }

            public void setModuleSonId(long moduleSonId) {
                this.moduleSonId = moduleSonId;
            }
        }
    }
}