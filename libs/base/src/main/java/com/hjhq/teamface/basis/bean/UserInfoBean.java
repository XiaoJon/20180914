package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 * Describe：登录返回信息
 */

public class UserInfoBean extends BaseBean {


    /**
     * data : {"companyInfo":{"website":"","address":"","phone":"","company_name":"九州海上牧云记","logo":"","id":9,"status":"0"},"photo":[],"employeeInfo":{"phone":"13163739593","post_name":"","sign_id":22,"sex":"","name":"王克栋","sign":"","id":3,"picture":"http://192.168.1.58:8281/custom-gateway/common/file/download?bean=user111&fileName=user111/1514174569429.3a5682bb8e626dc7c9afdf5b337ebe92.jpeg","microblog_background":""},"departmentInfo":[{"parent_id":"","department_name":"九州海上牧云记","id":1,"status":"0"}]}
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
         * companyInfo : {"website":"","address":"","phone":"","company_name":"九州海上牧云记","logo":"","id":9,"status":"0"}
         * photo : []
         * employeeInfo : {"phone":"13163739593","post_name":"","sign_id":22,"sex":"","name":"王克栋","sign":"","id":3,"picture":"http://192.168.1.58:8281/custom-gateway/common/file/download?bean=user111&fileName=user111/1514174569429.3a5682bb8e626dc7c9afdf5b337ebe92.jpeg","microblog_background":""}
         * departmentInfo : [{"parent_id":"","department_name":"九州海上牧云记","id":1,"status":"0"}]
         */

        private CompanyInfoBean companyInfo;
        private EmployeeInfoBean employeeInfo;
        private List<Photo> photo;
        private List<DepartmentInfoBean> departmentInfo;

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

        public List<?> getPhoto() {
            return photo;
        }

        public void setPhoto(List<Photo> photo) {
            this.photo = photo;
        }

        public List<DepartmentInfoBean> getDepartmentInfo() {
            return departmentInfo;
        }

        public void setDepartmentInfo(List<DepartmentInfoBean> departmentInfo) {
            this.departmentInfo = departmentInfo;
        }

        public static class CompanyInfoBean {
            /**
             * website :
             * address :
             * phone :
             * company_name : 九州海上牧云记
             * logo :
             * id : 9
             * status : 0
             */

            private String website;
            private String address;
            private String phone;
            private String company_name;
//            private List<String> banner;
            private String logo;
            private String id;
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

//            public List<String> getBanner() {
//                return banner;
//            }
//
//            public void setBanner(List<String> banner) {
//                this.banner = banner;
//            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
             * phone : 13163739593
             * post_name :
             * sign_id : 22
             * sex :
             * name : 王克栋
             * sign :
             * id : 3
             * picture : http://192.168.1.58:8281/custom-gateway/common/file/download?bean=user111&fileName=user111/1514174569429.3a5682bb8e626dc7c9afdf5b337ebe92.jpeg
             * microblog_background :
             */

            private String phone;
            private String mobile_phone;
            private String post_name;
            private String sign_id;
            private String sex;
            private String name;
            private String sign;
            private String id;
            private String picture;
            private String region;
            private String email;
            private String microblog_background;

            public String getPhone() {
                return phone;
            }

            public String getMobile_phone() {
                return mobile_phone;
            }

            public void setMobile_phone(String mobile_phone) {
                this.mobile_phone = mobile_phone;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

        public static class DepartmentInfoBean {
            /**
             * parent_id :
             * department_name : 九州海上牧云记
             * id : 1
             * status : 0
             */

            private String parent_id;
            private String department_name;
            private String id;
            private String status;
            private String is_main;

            public String getIs_main() {
                return is_main;
            }

            public void setIs_main(String is_main) {
                this.is_main = is_main;
            }

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

    public static class Photo {
        private String fileUrl;

        /**
         * 上传时间
         */
        private String uploadTime;

        /**
         * 文件大小
         */
        private String fileSize;

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

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
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