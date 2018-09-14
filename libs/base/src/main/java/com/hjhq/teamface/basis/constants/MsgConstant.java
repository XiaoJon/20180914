package com.hjhq.teamface.basis.constants;

/**
 * @Description:
 * @author: chenxiaomin
 * @date: 2017年10月12日 上午9:53:58
 * @version: 1.0
 */
public class MsgConstant {
    /**
     * 需要重新登录
     */
    public static final int TYPE_NEED_RELOGIN = 1;
    /**
     * 被系统强制离线
     */
    public static final int TYPE_FORCE_OFFLINE_BY_SYSTEM = 2;
    /**
     * 被PC端强制离线
     */
    public static final int TYPE_FORCE_OFFLINE_BY_PC = 3;
    /**
     * 账号在其他设备登录
     */
    public static final int TYPE_LOGIN_ON_OTHER_DEVICE = 4;
    /**
     * 需要修改密码
     */
    public static final int TYPE_NEED_CHANGE_PASSWORD = 5;
    /**
     * im账号不存在
     */
    public static final int TYPE_IM_ACCOUNT_ERROR = 6;
    /**
     * 未定义
     */
    public static final int TYPE_ = 7;

    /**
     * 消息通知
     */
    public static final String MESSAGE_CHANNEL = "message";
    /**
     * 推送通知
     */
    public static final String PUSH_CHANNEL = "push";
    /**
     * 当前打开的会话id
     */
    public static long openedConversationId = -1L;
    /**
     * 是否显示通知
     */
    public static boolean showNotificationFlag = false;

    public static final int BADGE_NOTIFICATION_ID = 2018;

    /**
     * 搜索结果首页面最多显示数量
     */
    public static final int MAX_SEARCH_RESULT_NUM = 2;
    /**
     * 创建聊天
     */
    public static final int CREATE_CHAT = 1;
    /**
     * 查看详情
     */
    public static final int VIEW_DETAIL = 2;
    /**
     * 网络查询
     */
    public static final int VIEW_NET_DATA = 1;
    /**
     * 本地查询
     */
    public static final int VIEW_LOCAL_DATA = 2;
    /**
     * 负载均衡成功标识符
     */
    public static final int LL_IDENTITY = 0x51534eba;

    /**
     * 负载均衡Socket
     */
    public static final int LL_SOCKET = 1;
    /**
     * 聊天Socket
     */
    public static final int IM_SOCKET = 2;


    //消息状态 //1发送成功,2发送失败,3对方已读,4正在撤销,5撤回失败,6发送中,7全部已读
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_FAILURE = 2;
    public static final int SINGLE_READ = 3;
    public static final int RECALLING = 4;
    public static final int RECALL_FAILURE = 5;
    public static final int SENDING = 6;
    public static final int ALL_READ = 7;
    /**
     * 会话中消息状态-没@消息
     */
    public static final int NO_AT_MSG = 0;
    /**
     * 会话中消息状态-有人@我
     */
    public static final int SB_AT_ME = 1;
    /**
     * 会话中消息状态-@所有人
     */
    public static final int SB_AT_ALL = 2;
    /**
     * 会话中消息状态-@所有人
     */
    public static final int SB_AT_BOTH = 3;
    //查询数据类型
    /**
     * 查询新消息
     */
    public static final int GREAT_THAN = 1;
    /**
     * 查询旧消息
     */
    public static final int LITTLE_THAN = 2;
    //消息被读后发EventBus通知用
    public static final int READ_MESSAGE_CODE = 3;
    public static final String READ_MESSAGE_TAG = "read_message_tag";
    /**
     * IM离线
     */
    public static final String IM_OFFLINE_TAG = "im_offline_tag";
    //将会话中消息全标为已读
    public static final String IM_MARK_ALL_READ = "im_mark_all_read";
    public static final String SEND_FILE_FAILED = "send_file_failed";
    public static final String CLEAN_DRAFT = "clean_draft";
    public static final String MARK_ALL_READ = "mark_all_read";
    //撤回消息成功
    public static final String RECALL_MESSAGE_SUCCESS = "recall_message_success";
    public static final String MARK_ASSISTANT_MSG_ALL_READ = "mark_assistant_msg_all_read";
    public static final int READ_GROUP_MESSAGE = 1;
    public static final int READ_SINGLE_MESSAGE = 2;
    public static final String READ_GROUP_MESSAGE_TAG = "read_group_message_tag";
    public static final String READ_SINGLE_MESSAGE_TAG = "read_single_message_tag";
    //发送文件消息时上传文件失败
    public static final String SEND_FILE_MESSAGE_FAILED = "send_file_message_failed";
    public static final int SEND_FILE_MESSAGE_FAILED_CODE = 0x9001;
    //发送文件消息-上传文件成功
    public static final String SEND_UPLOADED_FILE_MESSAGE = "send_uploaded_file_message";
    public static final int SEND_UPLOADED_FILE_MESSAGE_CODE = 0x9002;


    //通知id,用不到,用tag做判断
    public static final int NOTIFICATION_ID = 99;
    //最大显示的准确条数
    public static final int SHOW_MAX_EXACT_NUM = 99;
    //消息间隔小于该值不显示时间
    //消息可撤回时间  2分钟
    public static final int RECALL_TIME = 2 * 60 * 1000;
    //消息显示时间的间隔 3分钟
    public static final int SHOW_TIME_INTERVAL = 3 * 60 * 1000;
    //发送超时时间 2分钟
    public static final int SEND_MSG_TIMEOUT = 2 * 60 * 1000;
    //撤回超时时间
    public static final int RECALL_MSG_TIMEOUT = 5 * 1000;
    //免打扰
    public static final int NO_BOTHER_FALSE = 0;
    public static final int NO_BOTHER_YES = 1;
    //置顶
    public static final int SET_TOP_NO = 0;
    public static final int SET_TOP_YES = 1;
    //隐藏与显示
    public static final int HIDE_NO = 0;
    public static final int HIDE_YES = 1;


    //消息类型1文本,2图片3语音4文件,5小视频6位置7提醒

    public static final int TEXT = 1;
    public static final int IMAGE = 2;
    public static final int VOICE = 3;
    public static final int FILE = 4;
    public static final int VIDEO = 5;
    public static final int LOCATION = 6;
    public static final int NOTIFICATION = 7;
    //搜索类型
    public static final int SEARCH_CONTACTS = 1;
    public static final int SEARCH_GROUP = 2;
    public static final int SEARCH_CHAT = 3;
    public static final int SEARCH_ASSISTANT = 4;


    //数据库名字
    public static final String DATABASE_NAME = "teamface.db";
    //数据标签
    public static final String MSG_DATA = "msg_data";

    //消息内容类型
    public static final int TYPE_TEXT = 101;
    public static final int TYPE_TEXT_SEND = 102;
    public static final int TYPE_TEXT_RECEIVE = 103;


    public static final int TYPE_VOICE = 201;
    public static final int TYPE_VOICE_SEND = 202;
    public static final int TYPE_VOICE_RECEIVE = 203;


    public static final int TYPE_IMAGE = 301;
    public static final int TYPE_IMAGE_SEND = 302;
    public static final int TYPE_IMAGE_RECEIVE = 303;


    public static final int TYPE_VIDEO = 401;
    public static final int TYPE_VIDEO_SEND = 402;
    public static final int TYPE_VIDEO_RECEIVE = 403;


    public static final int TYPE_FILE = 501;
    public static final int TYPE_FILE_SEND = 502;
    public static final int TYPE_FILE_RECEIVE = 503;


    public static final int TYPE_LOCATION = 601;
    public static final int TYPE_LOCATION_SEND = 602;
    public static final int TYPE_LOCATION_RECEIVE = 603;


    public static final int TYPE_NOTIFICATION = 11;
    public static final int TYPE_NOTIFICATION_SEND = 12;
    public static final int TYPE_NOTIFICATION_RECEIVE = 13;


    public static final int TYPE_CUSTOM = 801;
    public static final int TYPE_CUSTOM_SEND = 802;
    public static final int TYPE_CUSTOM_RECEIVE = 803;

    public static final int TYPE_PROMPT = 901;
    public static final int TYPE_PROMPT_SEND = 902;
    public static final int TYPE_PROMPT_RECEIVE = 903;
    //未知类型
    public static final int TYPE_UNKNOWN = 1001;
    public static final int TYPE_UNKNOWN_SEND = 1002;
    public static final int TYPE_UNKNOWN_RECEIVE = 1003;

    //群聊
    public static final int GROUP = 1;
    //单聊
    public static final int SINGLE = 2;
    //单聊
    public static final int SELF_DEFINED = 3;

    public static final int CLOSE_SOCKET = 0x01;
    public static final int OPEN_SOCKET = 0x02;
    public static final int DEVICE_TYPE = 3;

    //用于打开聊天界面时通知会话列表当前会话的id
    public static final int NOTICE_CONVERSATION_ID = 100;
    public static final String NOTICE_CONVERSATION_TAG = "notice_conversation_tag";
    public static final String UPDATE_CONVERSATION_TAG = "update_conversation_tag";
    public static final String UPDATE_PULL_MESSAGE_PROGRESS_TAG = "update_pull_message_progress_tag";
    public static final String UPDATE_MAX_PROGRESS_TAG = "update_max_progress_tag";
    public static final String ASSISTANT_READ_TAG = "assistant_read_tag";
    public static final String UPDATE_NO_BOTHER_TAG = "update_no_bother_tag";
    public static final String UPDATE_PUT_TOP_TAG = "update_put_top_tag";

    //显示推送提醒
    public static final String SHOW_PUSH_MESSAGE = "show_push_message";
    //解散群通知
    public static final String RECEIVE_RELEASE_GROUP_PUSH_MESSAGE = "receive_release_group_push_message";
    //被移出了群
    public static final String RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE = "receive_remove_from_group_push_message";
    //群成员变更
    public static final String RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE = "receive_group_member_change_push_message";
    //群主变更
    public static final String RECEIVE_GROUP_ADMIN_CHANGE_PUSH_MESSAGE = "receive_group_admin_change_push_message";
    //群名变更
    public static final String RECEIVE_GROUP_NAME_CHANGE_PUSH_MESSAGE = "receive_group_name_change_push_message";
    //小助手名字变更
    public static final String RECEIVE_ASSISTANT_NAME_CHANGE_PUSH_MESSAGE = "receive_assistant_name_change_push_message";
    //撤回
    public static final String IM_ACTION_RECALL = "im_action_recall";
    //历史消息
    public static final String IM_ACTION_HISTORY = "im.action.history";
    //新开Socket
    public static final String IM_ACTION_OPEN_NEW_SOCKET = "im.action.open.new.socket";
    //负载均衡
    public static final String IM_ACTION_LOAD_LEVELING = "im.action.load.leveling";
    //登录
    public static final String IM_ACTION_LOGIN = "im.action.login";
    //退出登录
    public static final String IM_ACTION_LOGOUT = "im.action.logout";
    //发送消息
    public static final String IM_ACTION_SEND = "im.action.send";
    //回应ack
    public static final String IM_ACTION_ACK = "im.action.ack";
    //录音完成
    public static final String ACTION_RECORD_AUDIO_FINISH = "action.record.audio.finish";
    public static final String MSG_ACK = "msg_ack";
    public static final String MSG_ID = "msg_id";
    public static final String QUIT_OR_RELEASE_GROUP = "quit_or_release_group";
    /**
     * 更新未读消息数
     */
    public static final String UPDATE_UNREAD_MSG_NUM = "update_unread_msg_num";
    public static final String RECEIVE_PUSH_MESSAGE = "receive_push_message";
    public static final String MSG_RESULT = "msg_result";
    public static final String CONV_TITLE = "conv_title";
    public static final String CHAT_TYPE = "chat_type";
    public static final String SERVER_TIME = "server_time";
    public static final String IS_CREATOR = "is_creator";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String TARGET_ID = "targetId";
    public static final String AVATAR = "avatar";
    public static final String UER_ID = "uer_id";
    public static final String MEMBERS_COUNT = "membersCount";
    public static final String DRAFT = "draft";
    public static final String FROM_GROUP = "fromGroup";
    public static final String GROUP_TYPE = "group_type";
    //im登录时间
    public static final String IM_LOGIN_TIME = "im_login_time";


    //小助手类型
    //自定义应用助手
    public static final int MODULE_ASSISTANT = 1;
    //企信助手
    public static final int IM_ASSISTANT = 2;
    //审批助手
    public static final int APPROVE_ASSISTANT = 3;
    //文件库助手
    public static final int FILE_LIB_ASSISTANT = 4;
    //备忘录助手
    public static final int MEMO_ASSISTANT = 5;
    //邮件小助手
    public static final int EMAIL_ASSISTANT = 6;
    //任务小助手
    public static final int TASK_ASSISTANT = 7;

    //助手类会话id添加的默认值,防止和会话id相同
    public static final long DEFAULT_VALUE = 1234567890L;


    //小助手**数据**类型
    //企信小助手
    public static final String TYPE_GROUP_OPERATION = "1";
    //@推送
    public static final String TYPE_AT_ME = "2";
    //自定义应用
    public static final String TYPE_SELF_DEFINE = "3";
    //审批
    public static final String TYPE_APPROVE = "4";
    //文件库
    public static final String TYPE_FROM_FILE_LIB = "5";
    //朋友圈
    public static final String TYPE_FRIEND_CIRCLE = "6";
    //备忘录
    public static final String TYPE_MEMO = "7";
    //邮件
    public static final String TYPE_EMAIL = "8";
    //流程自动化
    public static final String TYPE_FLOW = "9";
    //移除成员
    public static final String TYPE_REMOVE_MEMBER = "10";
    //添加群成员
    public static final String TYPE_ADD_MEMBER = "11";
    //退群
    public static final String TYPE_QUIT_GROUP = "12";
    //群信息变化
    public static final String TYPE_GROUP_INFO_CHANGED = "13";
    //任务小助手推送
    public static final String TYPE_PROJECT_TAST_PUSH = "14";
    //置顶与取消置顶
    public static final String TYPE_TOP_OR_NOT_TOP = "15";
    //标记一条已读
    public static final String TYPE_MARK_ONE_ITEM_READ = "16";
    //标记全部已读
    public static final String TYPE_MARK_ALL_ITEM_READ = "17";
    //免打扰状态变化
    public static final String TYPE_NOTIFY_OR_NOT_NOTIFY = "18";
    //只查看未读消息
    public static final String TYPE_VIEW_READED_MESSAGE = "19";
    //审批操作推送消息
    public static final String TYPE_APPROVE_OPERATION = "20";
    //转让群推送
    public static final String TYPE_TRANSFER_GROUP = "21";
    //个人任务
    public static final String TYPE_PERSONAL_TASK = "25";
    //项目任务
    public static final String TYPE_PROJECT_TASK = "26";
    //
    /**
     * 权限
     */
    public static final String AUTH_NO = "0";
    public static final String AUTH_YES = "1";
    public static final String AUTH_DATA_DELETED = "2";
    public static final String AUTH_SEAS_POOL = "3";

    //小助手自定义消息体字段类型
    //文本
    public static final String TYPE_PLAIN_TEXT = "text";
    //人员
    public static final String TYPE_PERSONNEL = "personnel";
    //下拉列表
    public static final String TYPE_PICKLIST = "picklist";
    //日期
    public static final String TYPE_DATETIME = "datetime";
    //超链接
    public static final String TYPE_URL = "url";
    //电话
    public static final String TYPE_PHONE = "phone";

    //自定义模块小助手权限判断使用
    /**
     * 数据存在
     */
    public static final String DATA_EXIST = "0";
    /**
     * 数据不存在
     */
    public static final String DATA_NOT_EXIST = "1";
    /**
     * 有权限查看
     */
    public static final String HAVE_PREVIEW_AUTH = "0";
    /**
     * 无权限查看
     */
    public static final String NO_PREVIEW_AUTH = "1";
    //文件库数据查看权限
    //无权查查看
    public static final String NO_FILE_PREVIEW_AUTH = "0";
    //有权查看
    public static final String HAVE_FILE_PREVIEW_AUTH = "1";


    /**
     * 公司总群
     */
    public static final int GROUP_TYPE_SYSTEM = 1;
    /**
     * 普通群
     */
    public static final int GROUP_TYPE_NORMAL = 2;
    //发送方ID
    public static final String SENDER_ID = "sender_id";
    //接收方ID
    public static final String RECEIVER_ID = "receiver_id";
    //会话ID
    public static final String CONVERSATION_ID = "conversation_id";
    //应用ID
    public static final String APPLICATION_ID = "application_id";

    //是否查看已读,0否1是
    public static final String VIEW_READED = "view_readed";
    //消息ID
    public static final String MESSAGE_ID = "message_id";
    public static final String CONVERSATION_TAG = "conversation_tag";
    //public static final String SOCKET_URI = "wss://192.168.1.188:9002";
    //        public static final String SOCKET_URI = "wss://push.teamface.cn";
    //    public static final String SOCKET_URI = "wss://192.168.1.168:9002";
    //    public static final String SOCKET_URI = "ws://192.168.1.168:9000";
    //是否使用加密
    public static boolean USE_TLS = true;
    //其他设备登录
    public static final String LOGIN_ON_OTHER_DEVICES = "login_on_other_devices";
    //被系统强制离线
    public static final String BE_OFFLINE_BY_SYSTEM = "be_offline_by_system";
    /**
     * 被PC强制离线
     */
    public static final String BE_OFFLINE_BY_PC_CLIENT = "be_offline_by_pc_client";
    //104 imid查询不存在
    public static final String IM_ACCOUNT_NOT_EXIST = "im_account_not_exist";
    //数据库异常
    public static final String IM_DATABASE_ERROR = "im_database_error";
    //拉取历史消息
    public static final String IM_PULL_HISTORY_MSG_FINISH = "im_pull_history_msg_finish";
    //离线消息获取完成
    public static final String IM_PULL_OFFLINE_MSG_FINISH = "im_pull_offline_msg_finish";
    //模块名
    public static final String BEAN_NAME = "bean_name";
    //成员
    public static final String MEMBER_TAG = "member_tag";
    //发送文件到聊天
    public static final String SEND_FILE_TO_SB = "send_file_to_sb";
    //选择群聊
    public static final String CHOOSE_GROUP_CHAT = "choose_group_chat";
    //成员
    public static final String RESEND_MEMBER_TAG = "resend_member_tag";
    //成员变更
    public static final String MEMBER_CHANGED_TAG = "member_changed_tag";
    //聊天对象头像或名字可能变更了
    public static final String MEMBER_MAYBE_CHANGED_TAG = "member_maybe_changed_tag";
    //更新成员列表
    public static final String UPDATE_MEMBER_LIST_TAG = "update_member_list_tag";
    //群名变更
    public static final String GROUP_NAME_CHANGED_TAG = "group_name_changed_tag";
    //移除成员
    public static final String REMOVE_MEMBER_TAG = "remove_member_tag";
    //正在录音
    public static final String RECORDING_TAG = "recording_tag";
    //登录命令
    public static final int IM_LOG_IN_CMD = 1;
    //退出
    public static final int IM_LOG_OUT_CMD = 2;

    //离线消息请求命令
    public static final int IM_REQUEST_OFFLINE_MSG = 3;
    //未签收的ack消息请求命令
    public static final int IM_REQUEST_NOTSIGN_ACK_MSG = 4;
    //退群发送通知命令
    public static final int IM_QUIT_GROUP_FLAG = 5;

    //普通两个用户聊天
    public static final int IM_PERSONAL_CHAT_CMD = 5;
    //群聊
    public static final int IM_TEAM_CHAT_CMD = 6;

    //自定义消息发送个人
    public static final int IM_USER_DEFINED_PERSONAL_CMD = 7;
    //自定义消息发送群
    public static final int IM_USER_DEFINED_TEAM_CMD = 8;

    //ACK命令
    //单用户消息ack
    public static final int IM_ACK_USER_DEFINED_PERSONAL_CMD = 9;
    //群用户消息ack
    public static final int IM_ACK_USER_DEFINED_TEAM_CMD = 10;
    //聊天ACK命令
    //单用户消息ack
    public static final int IM_ACK_CHAT_PERSONAL_CMD = 11;
    //群用户消息ack
    public static final int IM_ACK_CHAT_TEAM_CMD = 12;

    //群操作命令
    //public static final int IM_INVITE_MEMBERS_JOIN_TEAM_CMD = 11; //邀请成员加入群,支持一次操作多个
    //public static final int IM_ADMIN_DEL_TEAM_MEMBERS_CMD = 12; //管理员删除群成员,支持一次操作多个
    //public static final int IM_MEMBER_QUIT_TEAM_CMD = 13; //成员退群
    //public static final int IM_CREATOR_DISMISS_TEAM_CMD = 14; //解散群

    //错误返回命令
    //错误返回错误代码,和错误信息。服务器返回给客户端的
    public static final int IM_ERROR_INFO_CMD = 17;
    //个人消息已读
    public static final int IM_PERSONSL_RESPONSE_READ_CMD = 18;
    //群消息已读
    public static final int IM_TEAM_RESPONSE_READ_CMD = 19;
    //拉取历史消息
    public static final int IM_PULL_HISTORY_MESSAGE = 20;
    //群历史消息
    public static final int IM_TEAM_HISTORY_MESSAGE = 22;
    //个人历史消息
    public static final int IM_PERSONAL_HISTORY_MESSAGE = 21;
    //拉取消息完成
    public static final int IM_PULL_MESSAGE_FINISH = 23;
    //撤回个人聊天消息
    public static final int IM_RECALL_PERSONAL_MSG = 24;
    //撤回个人聊天消息成功
    public static final int IM_RECALL_PERSONAL_MSG_OK = 25;
    //撤回群聊消息
    public static final int IM_RECALL_TEAM_MSG = 26;
    //撤回群聊消息成功
    public static final int IM_RECAL_TEAM_MSG_OK = 27;
    //登录成功
    public static final int IM_LOGIN_SUCCESSS = 0;
    //企信登录成功
    public static final String IM_LOGIN_SUCCESSS_TAG = "im_login_successs_tag";
    public static final String IM_NEED_UPDATE_LOCAL_DATA = "im_need_update_local_data";
    //没有登录
    public static final int IM_ERROR_NOT_LOGIN = 99;
    //未知错误
    public static final int IM_ERROR_UNKNOWN = 87;
    //连接socket跟服务器存储的不一致
    //登录的socket和本次通讯的socket不相等（可能socket创建了多次，也可能账号在别处登录）
    public static final int IM_ERROR_SOCKET_DISAFFINITY = 100;
    //发送的登录数据包长度不够
    public static final int IM_LOGIN_ERR_PACKET_LEN = 101;
    //用户名和imid都没填充
    public static final int IM_LOGIN_ERR_NO_IMID_AND_USERNAME = 102;
    //用户名查询不存在
    public static final int IM_LOGIN_ERR_USER_QUERY_FAIL = 103;
    //imid查询不存在
    public static final int IM_LOGIN_ERR_IMID_QUERY_FAIL = 104;
    //设备类型不识别
    public static final int IM_LOGIN_ERR_DEVICE_TYPE = 105;
    //账号在别的地方登录,服务器返回给客户端，抢占登录出现的错误
    public static final int IM_USER_LOGIN_ELSEWHERE = 106;
    //聊天或者推送的接收者IMID redis中不存在
    public static final int IM_MSB_ERR_RECEIVERID_NONEXISTENCE = 107;
    //聊天或者推送的群id redis中不存在
    public static final int IM_MSB_ERR_TEAMID_NONEXISTENCE = 108;

    //企信服务死亡
    public static final String IM_SERVICE_DYING = "im_service_dying";
    //总未读数
    public static final String IM_TOTAL_UNREAD_NUM = "im_total_unread_num";
    //删除会话时回复ack
    public static final String IM_MARK_CONV_ALL_READ = "im_mark_conv_all_read";
    /**
     * 下载新版本apk时使用的id
     */
    public static final String NEW_VERSION_APK_ID = "version_";
}

