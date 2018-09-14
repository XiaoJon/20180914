package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2017/5/9.
 */

public class GetEmployeeAndCompanyResponseBean extends BaseBean {


    /**
     * data : {"company":{"photograph":null,"isDefault":1,"companyName":"程林根的团队","id":3346843534082048},"employee":{"employeeName":"马","companyId":3346843534082048,"verifyStatus":1,"imPassword":"9bfdb037842881a15ad18300f3c02797","disabled":0,"telephone":"15712113669","id":3346856335343616,"imRegistrationId":"8c927efe1894906b86a534cb2a5c1d60","imUsername":"8c927efe1894906b86a534cb2a5c1d60","userId":3346856328724480},"department":{"departmentName":"程林根的团队","companyId":3346843534082048,"isDefault":1,"id":3346843555594240},"user":{"photograph":null,"telephone":"15712113669","id":3346856328724480,"userName":"mmmm","userCode":"15712113669"},"token":"a46af1e32858c3a14cc612c8a2c4ec8c"}
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
         * company : {"photograph":null,"isDefault":1,"companyName":"程林根的团队","id":3346843534082048}
         * employee : {"employeeName":"马","companyId":3346843534082048,"verifyStatus":1,"imPassword":"9bfdb037842881a15ad18300f3c02797","disabled":0,"telephone":"15712113669","id":3346856335343616,"imRegistrationId":"8c927efe1894906b86a534cb2a5c1d60","imUsername":"8c927efe1894906b86a534cb2a5c1d60","userId":3346856328724480}
         * department : {"departmentName":"程林根的团队","companyId":3346843534082048,"isDefault":1,"id":3346843555594240}
         * user : {"photograph":null,"telephone":"15712113669","id":3346856328724480,"userName":"mmmm","userCode":"15712113669"}
         * token : a46af1e32858c3a14cc612c8a2c4ec8c
         */

        private CompanyBean company;
        private MyEmployeeBean employee;
        private DepartmentBean department;
        private UserBean user;
        private String token;

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
        }

        public MyEmployeeBean getEmployee() {
            return employee;
        }

        public void setEmployee(MyEmployeeBean employee) {
            this.employee = employee;
        }

        public DepartmentBean getDepartment() {
            return department;
        }

        public void setDepartment(DepartmentBean department) {
            this.department = department;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public static class DepartmentBean {
            /**
             * departmentName : 程林根的团队
             * companyId : 3346843534082048
             * isDefault : 1
             * id : 3346843555594240
             */

            private String departmentName;
            private long companyId;
            private int isDefault;
            private long id;

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public long getCompanyId() {
                return companyId;
            }

            public void setCompanyId(long companyId) {
                this.companyId = companyId;
            }

            public int getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(int isDefault) {
                this.isDefault = isDefault;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        }

        public static class UserBean {
            /**
             * photograph : null
             * telephone : 15712113669
             * id : 3346856328724480
             * userName : mmmm
             * userCode : 15712113669
             */

            private String photograph;
            private String telephone;
            private long id;
            private String userName;
            private String userCode;

            public String getPhotograph() {
                return photograph;
            }

            public void setPhotograph(String photograph) {
                this.photograph = photograph;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserCode() {
                return userCode;
            }

            public void setUserCode(String userCode) {
                this.userCode = userCode;
            }
        }
    }
}
