package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 * 公司的所有员工
 */

public class GetEmployeeReponseBean extends BaseBean {


    /**
     * data : {"principal":null,"employeePageVo":{"totalRows":7,"totalPages":1,"pageNum":1,"pageSize":100,"curPageSize":7,"list":[{"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","department":null,"email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0,"employeeNumber":"88","positionId":3348364082577408,"position":"iOS"},{"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","department":null,"email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","department":null,"email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":0,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","department":null,"email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","department":null,"email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","department":null,"email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"employeeName":"测试","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","department":null,"email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]},"fieldPageVo":{"totalRows":15,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":15,"list":[{"id":3350770214387712,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeName","fieldNickName":"姓名","unvariable":1},{"id":3350770214387713,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNumber","fieldNickName":"工号","unvariable":0},{"id":3350770214387714,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"telephone","fieldNickName":"手机","unvariable":1},{"id":3350770214387715,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"department","fieldNickName":"部门","unvariable":0},{"id":3350770214387716,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"position","fieldNickName":"职务","unvariable":0},{"id":3350770214387717,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"roleName","fieldNickName":"角色","unvariable":1},{"id":3350770214387718,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"email","fieldNickName":"邮箱","unvariable":0},{"id":3350770214387719,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"gender","fieldNickName":"性别","unvariable":0},{"id":3350770214387720,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeAge","fieldNickName":"年龄","unvariable":0},{"id":3350770214387721,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNation","fieldNickName":"民族","unvariable":0},{"id":3350770214387722,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeEducation","fieldNickName":"学历","unvariable":0},{"id":3350770214387723,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"maritalStatus","fieldNickName":"婚姻状况","unvariable":0},{"id":3350770214387724,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"nativePlace","fieldNickName":"籍贯","unvariable":0},{"id":3350770214387725,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"entryTime","fieldNickName":"入职日期","unvariable":0},{"id":3350770214387726,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"incumbency","fieldNickName":"在职时长","unvariable":0}]}}
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
         * principal : null
         * employeePageVo : {"totalRows":7,"totalPages":1,"pageNum":1,"pageSize":100,"curPageSize":7,"list":[{"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","department":null,"email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0},{"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","department":null,"email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","department":null,"email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":0,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","department":null,"email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","department":null,"email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","department":null,"email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"employeeName":"测试","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","department":null,"email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]}
         * fieldPageVo : {"totalRows":15,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":15,"list":[{"id":3350770214387712,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeName","fieldNickName":"姓名","unvariable":1},{"id":3350770214387713,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNumber","fieldNickName":"工号","unvariable":0},{"id":3350770214387714,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"telephone","fieldNickName":"手机","unvariable":1},{"id":3350770214387715,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"department","fieldNickName":"部门","unvariable":0},{"id":3350770214387716,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"position","fieldNickName":"职务","unvariable":0},{"id":3350770214387717,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"roleName","fieldNickName":"角色","unvariable":1},{"id":3350770214387718,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"email","fieldNickName":"邮箱","unvariable":0},{"id":3350770214387719,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"gender","fieldNickName":"性别","unvariable":0},{"id":3350770214387720,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeAge","fieldNickName":"年龄","unvariable":0},{"id":3350770214387721,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNation","fieldNickName":"民族","unvariable":0},{"id":3350770214387722,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeEducation","fieldNickName":"学历","unvariable":0},{"id":3350770214387723,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"maritalStatus","fieldNickName":"婚姻状况","unvariable":0},{"id":3350770214387724,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"nativePlace","fieldNickName":"籍贯","unvariable":0},{"id":3350770214387725,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"entryTime","fieldNickName":"入职日期","unvariable":0},{"id":3350770214387726,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"incumbency","fieldNickName":"在职时长","unvariable":0}]}
         */

        private Object principal;
        private EmployeePageVoBean employeePageVo;
        private FieldPageVoBean fieldPageVo;

        public Object getPrincipal() {
            return principal;
        }

        public void setPrincipal(Object principal) {
            this.principal = principal;
        }

        public EmployeePageVoBean getEmployeePageVo() {
            return employeePageVo;
        }

        public void setEmployeePageVo(EmployeePageVoBean employeePageVo) {
            this.employeePageVo = employeePageVo;
        }

        public FieldPageVoBean getFieldPageVo() {
            return fieldPageVo;
        }

        public void setFieldPageVo(FieldPageVoBean fieldPageVo) {
            this.fieldPageVo = fieldPageVo;
        }

        public static class EmployeePageVoBean {
            /**
             * totalRows : 7
             * totalPages : 1
             * pageNum : 1
             * pageSize : 100
             * curPageSize : 7
             * list : [{"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","department":null,"email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0},{"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","department":null,"email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","department":null,"email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":0,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","department":null,"email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","department":null,"email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","department":null,"email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"employeeName":"测试","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","department":null,"email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]
             */

            private int totalRows;
            private int totalPages;
            private int pageNum;
            private int pageSize;
            private int curPageSize;
            private List<ListBean> list;

            public int getTotalRows() {
                return totalRows;
            }

            public void setTotalRows(int totalRows) {
                this.totalRows = totalRows;
            }

            public int getTotalPages() {
                return totalPages;
            }

            public void setTotalPages(int totalPages) {
                this.totalPages = totalPages;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getCurPageSize() {
                return curPageSize;
            }

            public void setCurPageSize(int curPageSize) {
                this.curPageSize = curPageSize;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * employeeName : 尹明亮
                 * incumbency : 2
                 * gender : 0
                 * telephone : 15818548636
                 * employeeAge : 12
                 * roleType : 1
                 * employeeStatus : 1
                 * userId : 3350769185210368
                 * companyId : 3350770212126720
                 * verifyStatus : 1
                 * roleName : 所有者
                 * disabled : 0
                 * id : 3350770213208064
                 *  : e6e152baeb25757112a41f0d312b86ac
                 * department : null
                 * email :
                 * maritalStatus : 0
                 * createDate : 1493349758178
                 * isPrincipal : 0
                 * employeeNumber : 88
                 * positionId : 3348364082577408
                 * position : iOS
                 */

                private String employeeName;
                private int incumbency;
                private int gender;
                private String telephone;
                private int employeeAge;
                private int roleType;
                private int employeeStatus;
                private long userId;
                private long companyId;
                private int verifyStatus;
                private String roleName;
                private int disabled;
                private long id;
                private String imUsername;
                private Object department;
                private String email;
                private int maritalStatus;
                private long createDate;
                private int isPrincipal;
                private String employeeNumber;
                private long positionId;
                private String position;

                public String getEmployeeName() {
                    return employeeName;
                }

                public void setEmployeeName(String employeeName) {
                    this.employeeName = employeeName;
                }

                public int getIncumbency() {
                    return incumbency;
                }

                public void setIncumbency(int incumbency) {
                    this.incumbency = incumbency;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public String getTelephone() {
                    return telephone;
                }

                public void setTelephone(String telephone) {
                    this.telephone = telephone;
                }

                public int getEmployeeAge() {
                    return employeeAge;
                }

                public void setEmployeeAge(int employeeAge) {
                    this.employeeAge = employeeAge;
                }

                public int getRoleType() {
                    return roleType;
                }

                public void setRoleType(int roleType) {
                    this.roleType = roleType;
                }

                public int getEmployeeStatus() {
                    return employeeStatus;
                }

                public void setEmployeeStatus(int employeeStatus) {
                    this.employeeStatus = employeeStatus;
                }

                public long getUserId() {
                    return userId;
                }

                public void setUserId(long userId) {
                    this.userId = userId;
                }

                public long getCompanyId() {
                    return companyId;
                }

                public void setCompanyId(long companyId) {
                    this.companyId = companyId;
                }

                public int getVerifyStatus() {
                    return verifyStatus;
                }

                public void setVerifyStatus(int verifyStatus) {
                    this.verifyStatus = verifyStatus;
                }

                public String getRoleName() {
                    return roleName;
                }

                public void setRoleName(String roleName) {
                    this.roleName = roleName;
                }

                public int getDisabled() {
                    return disabled;
                }

                public void setDisabled(int disabled) {
                    this.disabled = disabled;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getImUsername() {
                    return imUsername;
                }

                public void setImUsername(String imUsername) {
                    this.imUsername = imUsername;
                }

                public Object getDepartment() {
                    return department;
                }

                public void setDepartment(Object department) {
                    this.department = department;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public int getMaritalStatus() {
                    return maritalStatus;
                }

                public void setMaritalStatus(int maritalStatus) {
                    this.maritalStatus = maritalStatus;
                }

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public int getIsPrincipal() {
                    return isPrincipal;
                }

                public void setIsPrincipal(int isPrincipal) {
                    this.isPrincipal = isPrincipal;
                }

                public String getEmployeeNumber() {
                    return employeeNumber;
                }

                public void setEmployeeNumber(String employeeNumber) {
                    this.employeeNumber = employeeNumber;
                }

                public long getPositionId() {
                    return positionId;
                }

                public void setPositionId(long positionId) {
                    this.positionId = positionId;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }
            }
        }

        public static class FieldPageVoBean {
            /**
             * totalRows : 15
             * totalPages : 1
             * pageNum : 1
             * pageSize : 20
             * curPageSize : 15
             * list : [{"id":3350770214387712,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeName","fieldNickName":"姓名","unvariable":1},{"id":3350770214387713,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNumber","fieldNickName":"工号","unvariable":0},{"id":3350770214387714,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"telephone","fieldNickName":"手机","unvariable":1},{"id":3350770214387715,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"department","fieldNickName":"部门","unvariable":0},{"id":3350770214387716,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"position","fieldNickName":"职务","unvariable":0},{"id":3350770214387717,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"roleName","fieldNickName":"角色","unvariable":1},{"id":3350770214387718,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"email","fieldNickName":"邮箱","unvariable":0},{"id":3350770214387719,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"gender","fieldNickName":"性别","unvariable":0},{"id":3350770214387720,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeAge","fieldNickName":"年龄","unvariable":0},{"id":3350770214387721,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeNation","fieldNickName":"民族","unvariable":0},{"id":3350770214387722,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"employeeEducation","fieldNickName":"学历","unvariable":0},{"id":3350770214387723,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"maritalStatus","fieldNickName":"婚姻状况","unvariable":0},{"id":3350770214387724,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"nativePlace","fieldNickName":"籍贯","unvariable":0},{"id":3350770214387725,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"entryTime","fieldNickName":"入职日期","unvariable":0},{"id":3350770214387726,"createDate":1493349758250,"disabled":0,"companyId":3350770212126720,"fieldName":"incumbency","fieldNickName":"在职时长","unvariable":0}]
             */

            private int totalRows;
            private int totalPages;
            private int pageNum;
            private int pageSize;
            private int curPageSize;
            private List<ListBeanX> list;

            public int getTotalRows() {
                return totalRows;
            }

            public void setTotalRows(int totalRows) {
                this.totalRows = totalRows;
            }

            public int getTotalPages() {
                return totalPages;
            }

            public void setTotalPages(int totalPages) {
                this.totalPages = totalPages;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getCurPageSize() {
                return curPageSize;
            }

            public void setCurPageSize(int curPageSize) {
                this.curPageSize = curPageSize;
            }

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
                this.list = list;
            }

            public static class ListBeanX {
                /**
                 * id : 3350770214387712
                 * createDate : 1493349758250
                 * disabled : 0
                 * companyId : 3350770212126720
                 * fieldName : employeeName
                 * fieldNickName : 姓名
                 * unvariable : 1
                 */

                private long id;
                private long createDate;
                private int disabled;
                private long companyId;
                private String fieldName;
                private String fieldNickName;
                private int unvariable;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public int getDisabled() {
                    return disabled;
                }

                public void setDisabled(int disabled) {
                    this.disabled = disabled;
                }

                public long getCompanyId() {
                    return companyId;
                }

                public void setCompanyId(long companyId) {
                    this.companyId = companyId;
                }

                public String getFieldName() {
                    return fieldName;
                }

                public void setFieldName(String fieldName) {
                    this.fieldName = fieldName;
                }

                public String getFieldNickName() {
                    return fieldNickName;
                }

                public void setFieldNickName(String fieldNickName) {
                    this.fieldNickName = fieldNickName;
                }

                public int getUnvariable() {
                    return unvariable;
                }

                public void setUnvariable(int unvariable) {
                    this.unvariable = unvariable;
                }
            }
        }
    }
}
