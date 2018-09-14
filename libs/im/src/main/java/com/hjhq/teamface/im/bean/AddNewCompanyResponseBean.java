package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2017/5/11.
 */

public class AddNewCompanyResponseBean extends BaseBean {

    /**
     * data : {"companyId":3369580679675904,"companyName":"公司名称测试","departmentId":3369580708249600,"employeeId":3369580680511488,"userName":"尹明亮","userId":3350769185210368}
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
         * companyId : 3369580679675904
         * companyName : 公司名称测试
         * departmentId : 3369580708249600
         * employeeId : 3369580680511488
         * userName : 尹明亮
         * userId : 3350769185210368
         */

        private long companyId;
        private String companyName;
        private long departmentId;
        private long employeeId;
        private String userName;
        private long userId;

        public long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(long companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public long getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(long departmentId) {
            this.departmentId = departmentId;
        }

        public long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(long employeeId) {
            this.employeeId = employeeId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }
}
