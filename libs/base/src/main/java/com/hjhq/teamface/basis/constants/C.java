package com.hjhq.teamface.basis.constants;

/**
 * 全局常量标记类
 *
 * @author Administrator
 * @date 2018/1/8
 */

public class C {

    /*  ------------------------------选人控件标记--------------------------------- */
    /**
     * 没有选择按钮
     */
    public static final int NOT_TO_SELECT = 0;
    /**
     * 可自由选择
     */
    public static final int FREE_TO_SELECT = 1;
    /**
     * 不可被选中或取消
     */
    public static final int CAN_NOT_SELECT = 2;
    /**
     * 隐藏不显示
     */
    public static final int HIDE_TO_SELECT = 4;
    /**
     * 不返回
     */
    public static final int NOT_FOR_SELECT = 8;


    //单选
    public static final int RADIO_SELECT = 1004;
    //多选
    public static final int MULTIL_SELECT = 1005;
    //不选
    public static final int NO_SELECT = 1006;

    //部门
    public static final int DEPARTMENT = 0;
    //员工
    public static final int EMPLOYEE = 1;
    //角色
    public static final int ROLE = 2;
    //动态参数
    public static final int PARAMETER = 3;

    //选择范围TAG
    public static final String CHOOSE_RANGE_TAG = "chooseRangeTag";
    //已选人员TAG
    public static final String SELECTED_MEMBER_TAG = "selectedMemberTag";
    //选择类型TAG  单选、多选、不选
    public static final String CHECK_TYPE_TAG = "checkTypeTag";
    //成员类型 部门，员工，角色，动态参数
    public static final String MEMBER_TYPE_TAG = "memberTypeTag";
    //特殊人员TAG
    public static final String SPECIAL_MEMBERS_TAG = "specialMembersTag";
    //可选择的人员TAG
    public static final String OPTIONAL_MEMBERS_TAG = "optionalMembersTag";
      /*  ------------------------------选人控件标记--------------------------------- */

    //最近联系人变化
    public static final String UPDATE_RECENT_CONTACT = "update_recent_contact";


}
