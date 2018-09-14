package com.hjhq.teamface.basis.constants;

/**
 * 自定义 常量类
 *
 * @author Administrator
 * @date 2017/11/30
 */

public class CustomConstants {

    /* ---------------------------------- 组件类型 ---------------------------------------*/
    /**
     * 自动编号
     */
    public static final String IDENTIFIER = "identifier";
    /**
     * 单行文本
     */
    public static final String TEXT = "text";
    /**
     * 多行文本
     */
    public static final String TEXTAREA = "textarea";
    /**
     * 下拉选项
     */
    public static final String PICKLIST = "picklist";
    /**
     * 复选框
     */
    public static final String MULTI = "multi";
    /**
     * 多级下拉
     */
    public static final String MUTLI_PICKLIST = "mutlipicklist";
    /**
     * 定位
     */
    public static final String LOCATION = "location";
    /**
     * 省市区
     */
    public static final String AREA = "area";
    /**
     * 时间
     */
    public static final String DATETIME = "datetime";
    /**
     * 数字
     */
    public static final String NUMBER = "number";
    /**
     * 电话
     */
    public static final String PHONE = "phone";
    /**
     * 邮箱
     */
    public static final String EMAIL = "email";
    /**
     * 邮件审批
     */
    public static final String MAIL_BOX_SCOPE = "mail_box_scope";
    /**
     * 超链接
     */
    public static final String URL = "url";
    /**
     * 关联关系
     */
    public static final String REFERENCE = "reference";
    /**
     * 附件
     */
    public static final String ATTACHMENT = "attachment";
    /**
     * 图片
     */
    public static final String PICTURE = "picture";
    /**
     * 公式
     */
    public static final String FORMULA = "formula";
    /**
     * 函数公式
     */
    public static final String FUNCTION_FORMULA = "functionformula";
    /**
     * 高级公式
     */
    public static final String SENIOR_FORMULA = "seniorformula";
    /**
     * 人员
     */
    public static final String PERSONNEL = "personnel";
    /**
     * 部门
     */
    public static final String DEPARTMENT = "department";
    /**
     * 条形码
     */
    public static final String BARCODE = "barcode";
    /**
     * 子表单
     */
    public static final String SUBFORM = "subform";
    /**
     * 富文本
     */
    public static final String RICH_TEXT = "multitext";

    /* ---------------------------- 权限 ----------------------------------- */
    /**
     * 新增  有新增就是有 复制
     */
    public static final String ADD_NEW = "1";
    /**
     * 修改
     */
    public static final String UPDATE = "3";
    /**
     * 共享
     */
    public static final String SHARE = "4";
    /**
     * 删除
     */
    public static final String DELETE = "5";
    /**
     * 转换
     */
    public static final String CONVERT = "6";
    /**
     * 转移负责人
     */
    public static final String TRANSFER = "7";

    /**
     * 审批详情界面
     */
    public static final int APPROVE_STATE = 1;
    /**
     * 新增标识
     */
    public static final int ADD_STATE = 2;
    /**
     * 编辑标识
     */
    public static final int EDIT_STATE = 3;
    /**
     * 详情标识
     */
    public static final int DETAIL_STATE = 4;


    /**
     * 审批详情标识
     */
    public static final int APPROVE_DETAIL_STATE = 5;
    /**
     * 审批驳回和撤销后重新编辑标识
     */
    public static final int APPROVE_AGAIN_STATE = 7;

    public static final String FIELD_NONE = "0";
    public static final String FIELD_READ = "1";
    public static final String FIELD_MUST = "2";

    /**
     * 下拉字段控制
     */
    public static final String CONTROL_FIELD_TAG = "controlField:";
    /**
     * 下拉字段控制，清除其选中的值
     */
    public static final String CONTROL_FIELD_CLEAR_TAG = "controlClearField:";

    /**
     * 清除下拉字段控制
     */
    public static final String CLEAR_FIELD_CONTROL_TAG = "clearFieldControl:";
    /**
     * 清除下拉字段隐藏
     */
    public static final String CLEAR_FIELD_HIDE_TAG = "clearFieldHide:";
    /**
     * 下拉组件数据变动时  清除下面所有下拉组件的值
     */
    public static final String CLEAR_FIELD_VALUE_TAG = "clearFieldValue:";


    /**
     * 新增
     */
    public static final int REQUEST_ADDCUSTOM_CODE = 0x245;
    /**
     * 详情
     */
    public static final int REQUEST_DETAIL_CODE = 0x246;

    public static final String FIELD_VALUE_TAG = "field_value_tag";
    /**
     * 联动标签
     */
    public static final String LINKAGE_TAG = "linkage_tag";


    /**
     * 锁定
     */
    public static final String LOCKED_STATE = "lockedState";

    /**
     * 通知评论数量
     */
//    public static final int MESSAGE_COMMENT_COUNT_CODE = 0x8246;
    //查看文件详情
    public static final int MESSAGE_FILE_DETAIL_CODE = 0x8345;
    //查重
    public static final int MESSAGE_REPEAT_CHECK_CODE = 0x8346;
    //详情
    public static final int MESSAGE_DATA_DETAIL_CODE = 0x8347;
    //关联列表 - requestCode
    public static final int MESSAGE_REFERENCE_TEMP_CODE = 0x8348;
    //子表单数据联动
    public static final int MESSAGE_SUBFORM_LINKAGE_CODE = 0x8349;
    //清楚焦点
    public static final int MESSAGE_CLEAR_FOCUS_CODE = 0x8350;
}
