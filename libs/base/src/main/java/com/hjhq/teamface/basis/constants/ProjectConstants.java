package com.hjhq.teamface.basis.constants;

/**
 * 项目常量类
 * Created by Administrator on 2018/4/24.
 */

public class ProjectConstants {

    //项目文件下载地址

    public static final String PROJECT_FILE_DOWNLOAD_URL = Constants.BASE_URL + "common/file/projectDownload?id=%1$s&project_id=%2$s";
    public static final String TASK_MODULE_BEAN = "task";

    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String TASK_ID = "TASK_ID";
    public static final String MAIN_TASK_ID = "MAIN_TASK_ID";
    public static final String TASK_NAME = "TASK_NAME";
    public static final String NODE_ID = "NODE_ID";
    public static final String SUBNODE_ID = "SUBNODE_ID";
    public static final String EDIT_FOLDER_TYPE = "edit_folder_type";


    public static final String CHECK_STATUS = "checkStatus";
    public static final String CHECK_MEMBER = "checkMember";
    public static final String ASSOCIATE_STATUS = "associatesStatus";
    public static final String LAYOUT_ID = "layout_id";

    public static final String PROJECT_CUSTOM_ID = "project_custom_id";
    /**
     * 个人任务关联组件 id
     */
    public static final String RELATION_ID = "relation_id";
    /**
     * 个人任务关联组件 name
     */
    public static final String RELATION_DATA = "relation_data";
    //项目星标
    public static final String PROJECT_STAR = "project_star";
    public static final String PROJECT_NAME = "project_name";


    //未确定的beanNane
    public static final String PROJECT_BEAN_NAME = "project";
    public static final String PROJECT_BEAN_NAME_CAPITAL = "PROJECT";
    public static final String PROJECT_FILE_BEAN_NAME = "project_file";
    public static final String PROJECT_SHARE_BEAN_NAME = "dynamic_type_share";
    public static final String PROJECT_TASK_DYNAMIC_BEAN_NAME = "project_task_dynamic";
    public static final String PROJECT_DYNAMIC_BEAN_NAME = "project_dynamic";
    public static final String PERSONAL_TASK_BEAN_NAME = "personel_task";
    /**
     * 任务来源,用于区分主任务和子任务
     */
    public static final String TASK_FROM_TYPE = "fromType";
    /**
     * 项目自动生成的文件夹
     */
    public static final String PROJECT_FOLDER = "0";
    /**
     * 用户创建的文件夹
     */
    public static final String USER_FOLDER = "1";

    /**
     * 置顶
     */
    public static final String PUT_ON_TOP_FLAG = "put_on_top_flag";

    /**
     * 个人任务bean
     */
    public static final String PERSONAL_TASK_BEAN = "project_custom";
    /**
     * 项目任务bean
     */
    public static final String PROJECT_TASK_MOBULE_BEAN = PERSONAL_TASK_BEAN + "_";
    /**
     * 标签名称
     */
    public static final String PROJECT_TASK_LABEL = "picklist_tag";
    /**
     * 截止时间
     */
    public static final String PROJECT_TASK_DEADLINE = "datetime_deadline";
    /**
     * 执行人
     */
    public static final String PROJECT_TASK_EXECUTOR = "personnel_execution";
    /**
     * 开始时间
     */
    public static final String PROJECT_TASK_STARTTIME = "datetime_starttime";
    /**
     * 关联关系
     */
    public static final String PROJECT_TASK_RELATION = "reference_relation";
    /**
     * 个人任务名称
     */
    public static final String PROJECT_TASK_NAME = "text_name";

    /**
     * 项目角色权限标识
     */
    public static final String PROJECT_ROLE_TAG = "priviledgeIds";

    public static final int APP_MODULE = 0x5412;


    /**
     * 主节点排序
     */
    public static final int MAIN_NODE_SORT_REQUEST_CODE = 0x2314;
    /**
     * 项目子节点新增
     */
    public static final int PROJECT_SUB_NODE_ADD_TAG = 0x3110;
    /**
     * 搜索项目
     */
    public static final int SEARCH_PROJECT_SELF = 0x100;
    /**
     * 搜索项目文件
     */
    public static final int SEARCH_PROJECT_FILE = 0x101;
    /**
     * 搜索项目分享
     */
    public static final int SEARCH_PROJECT_SHARE = 0x102;
    /**
     * 搜索项目任务
     */
    public static final int SEARCH_PROJECT_TASK = 0x103;
    /**
     * 项目状态-进行中
     */
    public static final String PROJECT_STATUS_RUNNING = "0";
    /**
     * 项目状态-归档
     */
    public static final String PROJECT_STATUS_FILED = "1";
    /**
     * 项目状态-暂停
     */
    public static final String PROJECT_STATUS_PAUSE = "2";
    /**
     * 项目状态-删除
     */
    public static final String PROJECT_STATUS_DELETED = "3";
    /**
     * 项目公开状态 - 不公开
     */
    public static final String PROJECT_OPEN_STATE_NO = "0";
    /**
     * 项目公开状态 - 公开
     */
    public static final String PROJECT_OPEN_STATE_YES = "1";

    /**
     * 项目进度-自动计算
     */
    public static final String PROJECT_PROGRESS_STATUS_AUTO = "0";
    /**
     * 项目进度-手动输入
     */
    public static final String PROJECT_PROGRESS_STATUS_INPUT = "1";

    /**
     * 标签相关,无修改权限,只能查看
     */
    public static final int VIEW_MODE = 0x1001;
    /**
     * 标签相关,可进行删除可添加
     */
    public static final int EDIT_MODE = 0x1002;
    /**
     * 标签相关,选择标签
     */
    public static final int SELECT_MODE = 0x1003;


    /**
     * 工作台指示器数量
     */
    public static final int WORK_BENCH_INDICATOR_COUNT = 4;


    /**
     * 项目文件库
     */
    public static final int EDIT_FOLDER = 1;
    public static final int ADD_FOLDER = 2;
    public static final int ADD_SUB_FOLDER = 3;
    /**
     * 项目分享
     */
    public static final int TYPE_ADD_SHARE = 1;
    public static final int TYPE_EDIT_SHARE = 2;
    public static final int TYPE_SHARE_DETAIL = 3;

    /**
     * 备忘录类型数据
     */
    public static final int DATA_MEMO_TYPE = 1;
    /**
     * 任务类型数据
     */
    public static final int DATA_TASK_TYPE = 2;
    /**
     * 自定义类型数据
     */
    public static final int DATA_CUSTOM_TYPE = 3;
    /**
     * 审批类型数据
     */
    public static final int DATA_APPROVE_TYPE = 4;

    /**
     * 项目信息修改
     */
    public static final int PROJECT_INFO_EDIT_EVENT_CODE = 0x3000;


    /**
     * 编辑任务列表
     */
    public static final int EDIT_TASK_TEMP_REQUEST_CODE = 0x5000;
    /**
     * 新增--任务
     */
    public static final int ADD_TASK_REQUEST_CODE = 0x5001;
    /**
     * 新增任务--备忘录
     */
    public static final int ADD_TASK_MEMO_REQUEST_CODE = 0x5002;
    /**
     * 新增任务--审批
     */
    public static final int ADD_TASK_APPROVE_REQUEST_CODE = 0x5003;
    /**
     * 新增任务--任务
     */
    public static final int ADD_TASK_TASK_REQUEST_CODE = 0x5004;
    /**
     * 引用--任务
     */
    public static final int QUOTE_TASK_REQUEST_CODE = 0x5102;
    /**
     * 引用任务--备忘录
     */
    public static final int QUOTE_TASK_MEMO_REQUEST_CODE = 0x5103;
    /**
     * 引用任务--审批
     */
    public static final int QUOTE_TASK_APPROVE_REQUEST_CODE = 0x5104;
    /**
     * 引用任务--任务
     */
    public static final int QUOTE_TASK_TASK_REQUEST_CODE = 0x5105;
    /**
     * 引用任务--自定义
     */
    public static final int QUOTE_TASK_CUSTOM_REQUEST_CODE = 0x5106;

    /**
     * 编辑任务
     */
    public static final int EDIT_TASK_REQUEST_CODE = 0x5200;
    /**
     * 移动任务
     */
    public static final int MOVE_TASK_REQUEST_CODE = 0x5201;
    /**
     * 新增子任务
     */
    public static final int ADD_SUB_TASK_REQUEST_CODE = 0x5202;

    /**
     * 待检验
     */
    public static final String CHECK_STATUS_WAIT = "0";
    /**
     * 检验通过
     */
    public static final String CHECK_STATUS_PASS = "1";
    /**
     * 检验驳回
     */
    public static final String CHECK_STATUS_REJECT = "2";


    /**
     * 个人任务筛选通知
     */
    public static final int PERSONAL_TASK_FILTER_CODE = 0x6400;
    /**
     * 项目任务筛选通知
     */
    public static final int PROJECT_TASK_FILTER_CODE = 0x6401;
    public static final int PROJECT_TASK_REFRESH_CODE = 0x6402;
    public static final int PERSONAL_TASK_REFRESH_CODE = 0x6403;

}
