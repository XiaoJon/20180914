package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2017/10/19
 * Describe：员工详细信息
 */

public class EmployeeDetailBean extends BaseBean {

    /**
     * data : {"companyInfo":{"website":"","address":"","phone":"","company_name":"汇聚华企","logo":"company_pic","id":2,"status":"0"},"employeeInfo":{"post_name":"","name":"员工","employee_name":"wkd","id":20}}
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
         * companyInfo : {"website":"","address":"","phone":"","company_name":"汇聚华企","logo":"company_pic","id":2,"status":"0"}
         * employeeInfo : {"post_name":"","name":"员工","employee_name":"wkd","id":20}
         */

        private CompanyInfoBean companyInfo;
        private EmployeeInfoBean employeeInfo;
        private List<DepartmentInfo> departmentInfo;
        private List<Photo> photo;
        private String fabulous_count;
        private String fabulous_status;

        public String getFabulous_status() {
            return fabulous_status;
        }

        public void setFabulous_status(String fabulous_status) {
            this.fabulous_status = fabulous_status;
        }

        public String getFabulous_count() {
            return fabulous_count;
        }

        public void setFabulous_count(String fabulous_count) {
            this.fabulous_count = fabulous_count;
        }

        public List<Photo> getPhoto() {
            return photo;
        }

        public void setPhoto(List<Photo> photo) {
            this.photo = photo;
        }

        public CompanyInfoBean getCompanyInfo() {
            return companyInfo;
        }

        public void setCompanyInfo(CompanyInfoBean companyInfo) {
            this.companyInfo = companyInfo;
        }

        public EmployeeInfoBean getEmployeeInfo() {
            return employeeInfo;
        }

        public void setEmployeeInfo(EmployeeInfoBean employeeInfo) {
            this.employeeInfo = employeeInfo;
        }

        public List<DepartmentInfo> getDepartmentInfo() {
            return departmentInfo;
        }

        public void setDepartmentInfo(List<DepartmentInfo> departmentInfo) {
            this.departmentInfo = departmentInfo;
        }

        public static class CompanyInfoBean {
            /**
             * website :
             * address :
             * phone :
             * company_name : 汇聚华企
             * logo : company_pic
             * id : 2
             * status : 0
             */

            private String website;
            private String address;
            private String phone;
            private String company_name;
            private String logo;
            private long id;
            private String status;

            public String getWebsite() {
                return website;
            }

            public void setWebsite(String website) {
                this.website = website;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        public static class EmployeeInfoBean {

            /**
             * mood :
             * phone : 18664959527
             * post_name : 员工
             * role_id : 1
             * sign_id : 10004
             * sex :
             * sign :
             * employee_name : 王克栋9
             * id : 1
             * email :
             * picture :
             * microblog_background :
             */

            private String mood;
            private String phone;
            private String mobile_phone;
            private String post_name;
            private String role_id;
            private String birth;
            private String sign_id;
            private String sex;
            private String sign;
            private String employee_name;
            private String id;
            private String email;
            private String picture;
            private String region;
            private String microblog_background;

            public String getMood() {
                return mood;
            }

            public void setMood(String mood) {
                this.mood = mood;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPost_name() {
                return post_name;
            }

            public void setPost_name(String post_name) {
                this.post_name = post_name;
            }

            public String getRole_id() {
                return role_id;
            }

            public void setRole_id(String role_id) {
                this.role_id = role_id;
            }

            public String getSign_id() {
                return sign_id;
            }

            public void setSign_id(String sign_id) {
                this.sign_id = sign_id;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public String getBirth() {
                return birth;
            }

            public void setBirth(String birth) {
                this.birth = birth;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getMobile_phone() {
                return mobile_phone;
            }

            public void setMobile_phone(String mobile_phone) {
                this.mobile_phone = mobile_phone;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getMicroblog_background() {
                return microblog_background;
            }

            public void setMicroblog_background(String microblog_background) {
                this.microblog_background = microblog_background;
            }
        }

        public static class DepartmentInfo {

            private String parent_id;
            private String department_name;
            private String status;
            private long id;

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getDepartment_name() {
                return department_name;
            }

            public void setDepartment_name(String department_name) {
                this.department_name = department_name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        }

        public static class Photo {
            private String fileUrl;

            /**
             * 上传时间
             */
            private Long uploadTime;

            /**
             * 文件大小
             */
            private Long fileSize;

            /**
             * 文件后缀
             */
            private String fileType;

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public Long getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(Long uploadTime) {
                this.uploadTime = uploadTime;
            }

            public Long getFileSize() {
                return fileSize;
            }

            public void setFileSize(Long fileSize) {
                this.fileSize = fileSize;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }
        }
    }
}