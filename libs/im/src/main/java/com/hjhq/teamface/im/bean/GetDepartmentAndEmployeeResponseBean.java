package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 16:25 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class 获取部门与员工信息
 * @anthor Administrator TEL:
 * @time 2017/5/13 16:25
 * @change
 * @chang time
 * @class describe
 */
public class GetDepartmentAndEmployeeResponseBean extends BaseBean {

    /**
     * data : {"employeePageVo":{"totalRows":7,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":7,"list":[{"departmentName":null,"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0,"employeeNumber":"88","positionId":3348364082577408,"position":"iOS"},{"departmentName":null,"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"departmentName":null,"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"departmentName":null,"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"departmentName":null,"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"departmentName":null,"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"departmentName":null,"employeeName":"测试138","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"employeeNumber":"88","companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]},"departmentPageVo":{"totalRows":8,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":8,"list":[{"id":3350770251251712,"createDate":1493349760500,"disabled":0,"companyId":3350770212126720,"departmentName":"明亮公司","parentDepartmentId":0,"principalId":null,"isDefault":1,"employeeName":null,"photograph":null},{"id":3351165875290112,"createDate":1493373907475,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166015324160,"createDate":1493373916022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试2部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166113628160,"createDate":1493373922022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试3部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166305009664,"createDate":1493373933703,"disabled":0,"companyId":3350770212126720,"departmentName":"测试4部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351169052557312,"createDate":1493374101400,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171785834496,"createDate":1493374268226,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组1班","parentDepartmentId":3351169052557312,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171924115456,"createDate":1493374276666,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部2组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null}]}}
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
         * employeePageVo : {"totalRows":7,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":7,"list":[{"departmentName":null,"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0,"employeeNumber":"88","positionId":3348364082577408,"position":"iOS"},{"departmentName":null,"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"departmentName":null,"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"departmentName":null,"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"departmentName":null,"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"departmentName":null,"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"departmentName":null,"employeeName":"测试138","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"employeeNumber":"88","companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]}
         * departmentPageVo : {"totalRows":8,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":8,"list":[{"id":3350770251251712,"createDate":1493349760500,"disabled":0,"companyId":3350770212126720,"departmentName":"明亮公司","parentDepartmentId":0,"principalId":null,"isDefault":1,"employeeName":null,"photograph":null},{"id":3351165875290112,"createDate":1493373907475,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166015324160,"createDate":1493373916022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试2部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166113628160,"createDate":1493373922022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试3部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166305009664,"createDate":1493373933703,"disabled":0,"companyId":3350770212126720,"departmentName":"测试4部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351169052557312,"createDate":1493374101400,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171785834496,"createDate":1493374268226,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组1班","parentDepartmentId":3351169052557312,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171924115456,"createDate":1493374276666,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部2组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null}]}
         */

        private EmployeePageVoBean employeePageVo;
        private DepartmentPageVoBean departmentPageVo;

        public EmployeePageVoBean getEmployeePageVo() {
            return employeePageVo;
        }

        public void setEmployeePageVo(EmployeePageVoBean employeePageVo) {
            this.employeePageVo = employeePageVo;
        }

        public DepartmentPageVoBean getDepartmentPageVo() {
            return departmentPageVo;
        }

        public void setDepartmentPageVo(DepartmentPageVoBean departmentPageVo) {
            this.departmentPageVo = departmentPageVo;
        }

        public static class EmployeePageVoBean {
            /**
             * totalRows : 7
             * totalPages : 1
             * pageNum : 1
             * pageSize : 20
             * curPageSize : 7
             * list : [{"departmentName":null,"employeeName":"尹明亮","incumbency":2,"gender":0,"telephone":"15818548636","employeeAge":12,"roleType":1,"employeeStatus":1,"userId":3350769185210368,"companyId":3350770212126720,"verifyStatus":1,"roleName":"所有者","disabled":0,"id":3350770213208064,"imUsername":"e6e152baeb25757112a41f0d312b86ac","email":"","maritalStatus":0,"createDate":1493349758178,"isPrincipal":0},{"departmentName":null,"employeeName":"邹毅","incumbency":2,"gender":0,"telephone":"13617312230","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3350964526907392,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3351105254866944,"imUsername":"a594be0b1b04ee3726d7c6d38b06e2bc","email":"","maritalStatus":0,"createDate":1493370207498,"isPrincipal":0},{"departmentName":null,"employeeName":"程林根","incumbency":2,"gender":0,"telephone":"13751041565","employeeAge":12,"roleType":2,"employeeStatus":1,"userId":3346842603487232,"employeeNumber":"88","companyId":3350770212126720,"verifyStatus":1,"roleName":"管理员","disabled":0,"id":3351172321017856,"imUsername":"5a431ffb2d695b442b372a4f92b29a36","email":"","maritalStatus":0,"createDate":1493374300891,"isPrincipal":0},{"departmentName":null,"employeeName":"陈宇亮","incumbency":2,"gender":0,"telephone":"15974267842","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3358378015752192,"companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3365301812854784,"position":"iOS","imUsername":"9e9615e50d7c545957de2c381234922a","email":"","maritalStatus":0,"createDate":1494236696633,"isPrincipal":0},{"departmentName":null,"employeeName":"","incumbency":2,"gender":0,"telephone":"","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3356812542001152,"companyId":3350770212126720,"positionId":3366624072679424,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3366624183861248,"position":"安卓","imUsername":"82ca479774574cc7927714b685aee11b","email":"","maritalStatus":0,"createDate":1494317407754,"isPrincipal":0},{"departmentName":null,"employeeName":"李萌","incumbency":2,"gender":0,"telephone":"17198669671","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942275006464,"companyId":3350770212126720,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367948147818496,"imUsername":"72bdcd8c87ee219d0e93a2ad64d9e037","email":"","maritalStatus":0,"createDate":1494398216101,"isPrincipal":0},{"departmentName":null,"employeeName":"测试138","incumbency":2,"gender":0,"telephone":"13888888888","employeeAge":12,"roleType":3,"employeeStatus":1,"userId":3367942387957760,"employeeNumber":"88","companyId":3350770212126720,"positionId":3348364082577408,"verifyStatus":1,"roleName":"成员","disabled":0,"id":3367960765284352,"position":"iOS","imUsername":"5e060e253c7fe565f5ecef4a3b2ee93d","email":"","maritalStatus":0,"createDate":1494398986210,"isPrincipal":0}]
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
                 * departmentName : null
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
                 * imUsername : e6e152baeb25757112a41f0d312b86ac
                 * email :
                 * maritalStatus : 0
                 * createDate : 1493349758178
                 * isPrincipal : 0
                 * employeeNumber : 88
                 * positionId : 3348364082577408
                 * position : iOS
                 */

                private Object departmentName;
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
                private String email;
                private int maritalStatus;
                private long createDate;
                private int isPrincipal;
                private String employeeNumber;
                private long positionId;
                private String position;

                public Object getDepartmentName() {
                    return departmentName;
                }

                public void setDepartmentName(Object departmentName) {
                    this.departmentName = departmentName;
                }

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

        public static class DepartmentPageVoBean {
            /**
             * totalRows : 8
             * totalPages : 1
             * pageNum : 1
             * pageSize : 20
             * curPageSize : 8
             * list : [{"id":3350770251251712,"createDate":1493349760500,"disabled":0,"companyId":3350770212126720,"departmentName":"明亮公司","parentDepartmentId":0,"principalId":null,"isDefault":1,"employeeName":null,"photograph":null},{"id":3351165875290112,"createDate":1493373907475,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166015324160,"createDate":1493373916022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试2部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166113628160,"createDate":1493373922022,"disabled":0,"companyId":3350770212126720,"departmentName":"测试3部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351166305009664,"createDate":1493373933703,"disabled":0,"companyId":3350770212126720,"departmentName":"测试4部","parentDepartmentId":3350770251251712,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351169052557312,"createDate":1493374101400,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171785834496,"createDate":1493374268226,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部1组1班","parentDepartmentId":3351169052557312,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null},{"id":3351171924115456,"createDate":1493374276666,"disabled":0,"companyId":3350770212126720,"departmentName":"测试1部2组","parentDepartmentId":3351165875290112,"principalId":null,"isDefault":0,"employeeName":null,"photograph":null}]
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
                 * id : 3350770251251712
                 * createDate : 1493349760500
                 * disabled : 0
                 * companyId : 3350770212126720
                 * departmentName : 明亮公司
                 * parentDepartmentId : 0
                 * principalId : null
                 * isDefault : 1
                 * employeeName : null
                 * photograph : null
                 */

                private long id;
                private long createDate;
                private int disabled;
                private long companyId;
                private String departmentName;
                private int parentDepartmentId;
                private Object principalId;
                private int isDefault;
                private Object employeeName;
                private Object photograph;

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

                public String getDepartmentName() {
                    return departmentName;
                }

                public void setDepartmentName(String departmentName) {
                    this.departmentName = departmentName;
                }

                public int getParentDepartmentId() {
                    return parentDepartmentId;
                }

                public void setParentDepartmentId(int parentDepartmentId) {
                    this.parentDepartmentId = parentDepartmentId;
                }

                public Object getPrincipalId() {
                    return principalId;
                }

                public void setPrincipalId(Object principalId) {
                    this.principalId = principalId;
                }

                public int getIsDefault() {
                    return isDefault;
                }

                public void setIsDefault(int isDefault) {
                    this.isDefault = isDefault;
                }

                public Object getEmployeeName() {
                    return employeeName;
                }

                public void setEmployeeName(Object employeeName) {
                    this.employeeName = employeeName;
                }

                public Object getPhotograph() {
                    return photograph;
                }

                public void setPhotograph(Object photograph) {
                    this.photograph = photograph;
                }
            }
        }
    }
}
