package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class GetAuthBean extends BaseBean {

    /**
     * data : {"rolePermssions":[{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479589175885824,"code":112101,"permssionName":"创建群组","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590535888896,"code":111001,"permssionName":"新建公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590683410432,"code":111002,"permssionName":"新建不公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590869417984,"code":111003,"permssionName":"管理公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591018250240,"code":111004,"permssionName":"共享任务","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591138852864,"code":111005,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591480705024,"code":111101,"permssionName":"企业文库中添加文件和文件夹","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591727071232,"code":111102,"permssionName":"管理公开文件夹","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592227799040,"code":111103,"permssionName":"共享文件","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592352694272,"code":111104,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592806612992,"code":111201,"permssionName":"共享笔记","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592921317376,"code":111202,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593268953088,"code":111301,"permssionName":"共享审批","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593436626944,"code":111302,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593780723712,"code":111401,"permssionName":"新建公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593937502208,"code":111402,"permssionName":"新建不公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594077487104,"code":111403,"permssionName":"管理公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594314809344,"code":111404,"permssionName":"共享日程","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594437754880,"code":111405,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594731339776,"code":111501,"permssionName":"公告分类管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594950344704,"code":111502,"permssionName":"公告管理（发布、删除）","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595072110592,"code":111503,"permssionName":"共享公告","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595184979968,"code":111504,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595580325888,"code":111701,"permssionName":"共享工作汇报","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479596342689792,"code":111702,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597586071552,"code":10008601,"permssionName":"员工管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597835927552,"code":10008602,"permssionName":"应用管理（开启/禁用模块）","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597989314560,"code":10008603,"permssionName":"角色管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598155628544,"code":10008604,"permssionName":"账单管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598324318208,"code":10008605,"permssionName":"企业基本设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598419476480,"code":10008606,"permssionName":"企业标识设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598513946624,"code":10008607,"permssionName":"安全设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598637776896,"code":10008608,"permssionName":"数据管理","moduleIdList":null}],"roleType":1}
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
         * rolePermssions : [{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479589175885824,"code":112101,"permssionName":"创建群组","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590535888896,"code":111001,"permssionName":"新建公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590683410432,"code":111002,"permssionName":"新建不公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479590869417984,"code":111003,"permssionName":"管理公开项目","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591018250240,"code":111004,"permssionName":"共享任务","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591138852864,"code":111005,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591480705024,"code":111101,"permssionName":"企业文库中添加文件和文件夹","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479591727071232,"code":111102,"permssionName":"管理公开文件夹","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592227799040,"code":111103,"permssionName":"共享文件","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592352694272,"code":111104,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592806612992,"code":111201,"permssionName":"共享笔记","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479592921317376,"code":111202,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593268953088,"code":111301,"permssionName":"共享审批","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593436626944,"code":111302,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593780723712,"code":111401,"permssionName":"新建公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479593937502208,"code":111402,"permssionName":"新建不公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594077487104,"code":111403,"permssionName":"管理公开日程分类","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594314809344,"code":111404,"permssionName":"共享日程","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594437754880,"code":111405,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594731339776,"code":111501,"permssionName":"公告分类管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479594950344704,"code":111502,"permssionName":"公告管理（发布、删除）","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595072110592,"code":111503,"permssionName":"共享公告","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595184979968,"code":111504,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479595580325888,"code":111701,"permssionName":"共享工作汇报","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479596342689792,"code":111702,"permssionName":"模块设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597586071552,"code":10008601,"permssionName":"员工管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597835927552,"code":10008602,"permssionName":"应用管理（开启/禁用模块）","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479597989314560,"code":10008603,"permssionName":"角色管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598155628544,"code":10008604,"permssionName":"账单管理","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598324318208,"code":10008605,"permssionName":"企业基本设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598419476480,"code":10008606,"permssionName":"企业标识设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598513946624,"code":10008607,"permssionName":"安全设置","moduleIdList":null},{"id":null,"createDate":null,"disabled":null,"roleId":null,"permssionId":3479598637776896,"code":10008608,"permssionName":"数据管理","moduleIdList":null}]
         * roleType : 1
         */

        private int roleType;
        private List<RolePermssionsBean> rolePermssions;

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public List<RolePermssionsBean> getRolePermssions() {
            return rolePermssions;
        }

        public void setRolePermssions(List<RolePermssionsBean> rolePermssions) {
            this.rolePermssions = rolePermssions;
        }

        public static class RolePermssionsBean {
            /**
             * id : null
             * createDate : null
             * disabled : null
             * roleId : null
             * permssionId : 3479589175885824
             * code : 112101
             * permssionName : 创建群组
             * moduleIdList : null
             */

            private Object id;
            private Object createDate;
            private Object disabled;
            private Object roleId;
            private long permssionId;
            private int code;
            private String permssionName;
            private Object moduleIdList;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
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

            public Object getRoleId() {
                return roleId;
            }

            public void setRoleId(Object roleId) {
                this.roleId = roleId;
            }

            public long getPermssionId() {
                return permssionId;
            }

            public void setPermssionId(long permssionId) {
                this.permssionId = permssionId;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getPermssionName() {
                return permssionName;
            }

            public void setPermssionName(String permssionName) {
                this.permssionName = permssionName;
            }

            public Object getModuleIdList() {
                return moduleIdList;
            }

            public void setModuleIdList(Object moduleIdList) {
                this.moduleIdList = moduleIdList;
            }
        }
    }
}
