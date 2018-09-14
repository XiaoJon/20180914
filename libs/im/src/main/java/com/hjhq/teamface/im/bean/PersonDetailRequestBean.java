package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by lx on 2017/7/10.
 */

public class PersonDetailRequestBean extends BaseBean {


    /**
     * data : {"departmentName":"","employeeName":"","imUserName":"dd070dbe86104caafd6f16f43ce1c9f8","gender":0,"photoes":[],"companyName":"","employeeId":3449887114772480,"telephone":"","personSignature":"","photograph":"0","microblogBackground":null,"position":null,"region":"110101","email":""}
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
         * departmentName :
         * employeeName :
         * imUserName : dd070dbe86104caafd6f16f43ce1c9f8
         * gender : 0
         * photoes : []
         * companyName :
         * employeeId : 3449887114772480
         * telephone :
         * personSignature :
         * photograph : 0
         * microblogBackground : null
         * position : null
         * region : 110101
         * email :
         */

        private String departmentName;
        private String employeeName;
        private String imUserName;
        private int gender;
        private String companyName;
        private long employeeId;
        private String telephone;
        private String personSignature;
        private String photograph;
        private String microblogBackground;
        private String position;
        private String region;
        private String email;
        private List<PhotoBean> photoes;

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getImUserName() {
            return imUserName;
        }

        public void setImUserName(String imUserName) {
            this.imUserName = imUserName;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(long employeeId) {
            this.employeeId = employeeId;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getPersonSignature() {
            return personSignature;
        }

        public void setPersonSignature(String personSignature) {
            this.personSignature = personSignature;
        }

        public String getPhotograph() {
            return photograph;
        }

        public void setPhotograph(String photograph) {
            this.photograph = photograph;
        }

        public String getMicroblogBackground() {
            return microblogBackground;
        }

        public void setMicroblogBackground(String microblogBackground) {
            this.microblogBackground = microblogBackground;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
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

        public List<PhotoBean> getPhotoes() {
            return photoes;
        }

        public void setPhotoes(List<PhotoBean> photoes) {
            this.photoes = photoes;
        }

        public static class PhotoBean{

            /**
             * uploadTime : 1498463395069
             * id : 3434552040030208
             * fileUrl : http://192.168.1.172:9400/4/0383a1c4e31c3c
             * fileType : jpg
             * fileSize : 28373
             */

            private Long uploadTime;
            private long id;
            private String fileUrl;
            private String fileType;
            private int fileSize;

            public Long getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(Long uploadTime) {
                this.uploadTime = uploadTime;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }

            public int getFileSize() {
                return fileSize;
            }

            public void setFileSize(int fileSize) {
                this.fileSize = fileSize;
            }
        }
    }
}
