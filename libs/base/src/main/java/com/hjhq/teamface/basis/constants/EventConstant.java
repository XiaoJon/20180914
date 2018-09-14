package com.hjhq.teamface.basis.constants;

/**
 * @Description:EventBus消息类型常量类
 */
public class EventConstant {

    //系统推送类型

    /**
     * 人员与组织架构变化
     */
    public static final String TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES = "1000";

    /**
     * 被系统强制离线
     */
    public static final String TYPE_BE_FORCED_OFFLINE_BY_SYSTEM = "1001";
    /**
     * 助手名更改/应用
     */
    public static final String TYPE_ASSISTANT_NAME_CHANGED = "1002";
    /**
     * 应用修改
     */
    public static final String TYPE_APPLICATION_UPDATE = "1002";
    /**
     * 模块修改
     */
    public static final String TYPE_MODULE_UPDATE = "1003";
    /**
     * 应用删除
     */
    public static final String TYPE_APPLICATION_DEL = "100?";
    /**
     * 隐藏/显示应用及助手
     */
    public static final String TYPE_ASSISTANT_VISIBILE_STATE = "1005";

    /**
     * 被pc端强制离线
     */
    public static final String TYPE_FORCE_OFFLINE_BY_PC_CLIENT = "1100";
    /**
     * 登录通知
     */
    public static final int LOGIN_TAG = 0x1024;

    /**
     * 项目主节点删除
     */
    public static final int PROJECT_MAIN_NODE_DEL_TAG = 0x13024;

    /**
     * 项目主节点新增
     */
    public static final int PROJECT_MAIN_NODE_ADD_TAG = 0x13027;

    /**
     * 项目主节点修改
     */
    public static final int PROJECT_MAIN_NODE_EDIT_TAG = 0x13028;


    /**
     * 项目主节点排序
     */
    public static final int PROJECT_MAIN_NODE_SORT_TAG = 0x13030;

}

