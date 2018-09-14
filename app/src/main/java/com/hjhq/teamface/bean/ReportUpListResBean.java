package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/7/18 17:41 .
 *
 * @name branch20170710
 * @class name：com.hjhq.teamface.bean
 * @class describe
 * @anthor Administrator TEL:13163739593
 * @time 2017/7/18 17:41
 * @change
 * @chang time
 * @class describe
 */
public class ReportUpListResBean extends BaseBean{


    /**
     * data : {"isUp":1,"upList":[{"employeeName":"王克栋3","photograph":"http://192.168.1.172:9400/7/04200aa4e4c19b","employeeId":3449912759992320,"id":3465677500727296,"planOrDailyId":3465659592933376}]}
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
         * isUp : 1
         * upList : [{"employeeName":"王克栋3","photograph":"http://192.168.1.172:9400/7/04200aa4e4c19b","employeeId":3449912759992320,"id":3465677500727296,"planOrDailyId":3465659592933376}]
         */

        private int isUp;
        private List<UpListBean> upList;

        public int getIsUp() {
            return isUp;
        }

        public void setIsUp(int isUp) {
            this.isUp = isUp;
        }

        public List<UpListBean> getUpList() {
            return upList;
        }

        public void setUpList(List<UpListBean> upList) {
            this.upList = upList;
        }

        public static class UpListBean {
            /**
             * employeeName : 王克栋3
             * photograph : http://192.168.1.172:9400/7/04200aa4e4c19b
             * employeeId : 3449912759992320
             * id : 3465677500727296
             * planOrDailyId : 3465659592933376
             */

            private String employeeName;
            private String photograph;
            private long employeeId;
            private long id;
            private long planOrDailyId;

            public String getEmployeeName() {
                return employeeName;
            }

            public void setEmployeeName(String employeeName) {
                this.employeeName = employeeName;
            }

            public String getPhotograph() {
                return photograph;
            }

            public void setPhotograph(String photograph) {
                this.photograph = photograph;
            }

            public long getEmployeeId() {
                return employeeId;
            }

            public void setEmployeeId(long employeeId) {
                this.employeeId = employeeId;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getPlanOrDailyId() {
                return planOrDailyId;
            }

            public void setPlanOrDailyId(long planOrDailyId) {
                this.planOrDailyId = planOrDailyId;
            }
        }
    }
}
