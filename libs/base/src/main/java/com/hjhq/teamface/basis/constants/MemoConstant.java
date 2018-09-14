package com.hjhq.teamface.basis.constants;

/**
 * @Description:
 * @author: chenxiaomin
 * @date: 2017年10月12日 上午9:53:58
 * @version: 1.0
 */
public interface MemoConstant {

    public static final String BEAN_NAME = "memo";
    /**
     * 项目数据
     */
    public static final String TYPE_PROJECT = "1";
    /**
     * 自定义数据
     */
    public static final String TYPE_CUSTOM = "2";
    /**
     * 数据变化
     */
    public static final String MEMO_DATA_CHANGED = "memo_data_changed";

    //备忘录操作类型
    //删除
    public static final String MEMO_OPERATION_DELETE = "1";
    //彻底删除
    public static final String MEMO_OPERATION_DELETE_FOREVER = "2";
    //恢复备忘
    public static final String MEMO_OPERATION_RECOVER = "3";
    //退出共享
    public static final String MEMO_OPERATION_QUIT = "4";

    //条目类型
    public static final int ITEM_TEXT = 1;
    public static final int ITEM_IMAGE = 2;
    public static final int ITEM_PROJECT = 3;
    public static final int ITEM_REMIND = 4;
    public static final int ITEM_LOCATION = 5;
    public static final int ITEM_MEMBER = 6;


    //图片文件位置类型

    public static final int LOCAL_FILE = 1;
    public static final int NET_FILE = 2;

    //备忘录列表类型
    public static final int TYPE_ALL = 0;
    public static final int TYPE_MINE = 1;
    public static final int TYPE_SHARED = 2;
    public static final int TYPE_DELETE = 3;

    //提醒模式
    public static final int REMIND_MODE_ALL = 0;
    public static final int REMIND_MODE_SELF = 1;
    //删除/退出共享/彻底删除
    public static final int DELETE_ITEM = 0x1001;
    public static final int QUIT_SHARE = 0x1002;
    public static final int DELETE_FOREVER = 0x1003;
    public static final int RECOVER_MEMO = 0x1004;
    public static final int DATALIST_CHANGE = 0x1005;


}

