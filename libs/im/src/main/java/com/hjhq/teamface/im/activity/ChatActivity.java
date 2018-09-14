package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.services.core.PoiItem;
import com.andview.refreshview.XRefreshView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.SharePreferenceManager;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.activity.FullscreenViewImageActivity;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.ui.location.LocationPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.RecordVoiceButton;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.im.adapter.MessageListAdapter;
import com.hjhq.teamface.im.chat.ChatView;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;
import com.hjhq.teamface.im.util.ParseUtil;
import com.hjhq.teamface.common.view.refresh.PullHeaderView;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Administrator
 */
@RouteNode(path = "/chat", desc = "聊天")
public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener,
        ChatView.OnSizeChangedListener, ChatView.OnKeyBoardChangeListener {

    private static final String TAG = "ChatActivity";
    private static final int REQUEST_CODE_TAKE_PHOTO = 4;
    private static final int REQUEST_CODE_SELECT_PICTURE = 6;
    private static final int RESULT_CODE_SELECT_PICTURE = 8;
    private static final int REQUEST_CODE_CHAT_DETAIL = 14;
    private static final int RESULT_CODE_CHAT_DETAIL = 15;
    private static final int RESULT_CODE_FRIEND_INFO = 17;
    private static final int REFRESH_LAST_PAGE = 0x1023;
    private static final int REFRESH_CHAT_TITLE = 0x1024;
    private static final int REFRESH_GROUP_NAME = 0x1025;
    private static final int REFRESH_GROUP_NUM = 0x1026;

    private List<SocketMessage> msgList = new ArrayList<>();
    private boolean isInputByKeyBoard = true;
    private boolean mShowSoftInput = false;
    private MessageListAdapter mChatAdapter;
    private ChatView mChatView;
    private XRefreshView xRefreshView;
    private Conversation mConversation;
    private MyReceiver mReceiver;
    private String senderId;
    private String receiverId;
    private long conversationId;
    //private GroupInfo mGroupInfo;
    private Window mWindow;
    private InputMethodManager mImm;
    private String groupId;
    private String mTitle;
    //private UserInfo mMyInfo;
    //private EmployeeBean employee;
    private String draft;
    private boolean fromGroup;
    private int membersCount;
    private int chatType;
    //拍照图片
    private File imageFromCamera;
    //请求头
    private Map<String, String> headers;
    //最后一条消息时间
    //当前Activity是否显示在最前
    public static boolean showNotificationFlag = false;
    //震动
    private Vibrator vibrator;
    //消息临时缓存
    private byte[] b = null;
    //记录当前群成员id字符串
    private String peoples;


    private SocketMessage mSocketMessage;
    private List<Member> atList = new ArrayList<>();
    private List<Member> forDel = new ArrayList<>();
    private Bundle mBundle;
    private int groupMemberNum;
    //搜索后定位消息使用
    private long msgId = -1L;
    private long SERVER_TIME = -1L;
    private int msgPosition = -1;
    //草稿变化
    private boolean textChanged = false;
    //文字是增加还是删减
    private boolean textChangeLonger = false;
    private String textInEditText = "";
    //当前第一条消息服务器时间戳
    private long firstMessageTimeStamp = 0L;
    //所有历史消息拉取完成
    private boolean getAllHistoryMessageFinished = false;
    //是否拉取过历史消息
    private boolean pullHistoryMessageBefore = false;
    //是否是拉取历史消息后查询数据库
    private boolean isGetNewHistoryMessage = false;
    //本地数据是否加载完成,用于判断是否拉取历史消息
    private boolean allMessageLoaded = false;
    //新收到的消息
    private int newMessageNum = 0;
    //是否是刚打开当前会话
    private boolean isFirstOpen = true;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mBundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mBundle = savedInstanceState;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mBundle = intent.getExtras();
        if (mBundle != null) {
            initIntent();
            initChat();
            initData();
            EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.UPDATE_UNREAD_MSG_NUM, null));
        }

    }

    @Override
    protected void initView() {
        LogUtil.e("时间1===", System.currentTimeMillis() + "");
        mChatView = (ChatView) findViewById(R.id.jmui_chat_view);
        xRefreshView = (XRefreshView) mChatView.findViewById(R.id.xrefreshview);
        PullHeaderView headerView = new PullHeaderView(mContext);
        xRefreshView.setCustomHeaderView(headerView);
        mChatView.initActivity(ChatActivity.this);
        mChatView.initModule(mDensity, mDensityDpi);
        LogUtil.e("时间2===", System.currentTimeMillis() + "");

        //监听下拉刷新
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(false);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.setLoadComplete(true);
        createMsgListAdapter();

        mChatView.setChatListAdapter(mChatAdapter);
        if (mBundle == null) {
            mBundle = getIntent().getExtras();

        }
        if (mBundle != null) {
            initIntent();
            initChat();
        } else {
            ToastUtils.showToast(ChatActivity.this, "初始数据错误,请重试!");
            finish();
        }
    }

    /**
     * 会话状态,群成员信息,草稿等初始化
     */
    private void initChat() {
        if (mConversation != null) {
            //如果会话是隐藏状态则请求后台将会话改为显示.
            if (mConversation.getIsHide() == 1) {
                ImLogic.getInstance().hideSessionWithStatus(ChatActivity.this,
                        conversationId, mConversation.getConversationType(), 0,
                        new ProgressSubscriber<BaseBean>(ChatActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(BaseBean baseBean) {

                            }
                        });
            }
            //判断是否有草稿
            if (!TextUtil.isEmpty(mConversation.getDraft())) {
                textInEditText = mConversation.getDraft();
                mChatView.getInputView().setText(mConversation.getDraft());
                mChatView.getInputView().setSelection(mChatView.getInputView().getText().length());
            }
            //详情图标,群所有成员id字符串(用于群成员已读未读)
            switch (chatType) {
                case MsgConstant.GROUP:
                    mChatView.setGroupIcon();
                    mChatView.setAtIcon(true);
                    fromGroup = true;
                    if (!TextUtils.isEmpty(mConversation.getPeoples())) {
                        String[] peoples = mConversation.getPeoples().split(",");
                        if (peoples != null) {
                            groupMemberNum = peoples.length;
                            IM.getInstance().setGroupNumberNum(groupMemberNum);
                            mChatView.setChatTitle(mConversation.getTitle() + "(" + groupMemberNum + ")");
                        }
                    } else {
                        mChatView.setChatTitle(mConversation.getTitle() + " ");
                    }

                    break;
                case MsgConstant.SINGLE:
                    mChatView.setAtIcon(false);
                    fromGroup = false;
                    mChatView.setChatTitle(mConversation.getTitle() + " ");
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
        this.mWindow = getWindow();
        this.mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initReceiver();
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = getString(R.string.group);
        }
        if (draft != null && !TextUtils.isEmpty(draft)) {
            mChatView.setInputText(draft);
        }
        // 滑动到底部
        mChatView.setToBottom();
    }


    /**
     * 初始化Intent数据
     */
    private void initIntent() {
        LogUtil.e("时间3===", System.currentTimeMillis() + "");
        isFirstOpen = true;
        receiverId = mBundle.getString(MsgConstant.RECEIVER_ID);
        conversationId = mBundle.getLong(MsgConstant.CONVERSATION_ID);
        msgId = mBundle.getLong(MsgConstant.MSG_ID, -1L);
        SERVER_TIME = mBundle.getLong(MsgConstant.SERVER_TIME, -1L);

//        msgList.addAll(ConversationListController.getMessageFromCache(conversationId));
//        mChatAdapter.notifyDataSetChanged();
        //将当前会话未读数设置为0
        // DBManager.getInstance().qureyUnreadMessage(conversationId);
        ConversationListController.cleanConversationUnreadNum(conversationId);
        //取消通知栏信息
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(conversationId + "", MsgConstant.NOTIFICATION_ID);
        MsgConstant.openedConversationId = conversationId;
        // EventBusUtils.sendEvent(new MessageBean(MsgConstant.NOTICE_CONVERSATION_ID, MsgConstant.NOTICE_CONVERSATION_TAG, conversationId));
        senderId = SPHelper.getUserId();
        mTitle = mBundle.getString(MsgConstant.CONV_TITLE);
        draft = mBundle.getString(MsgConstant.DRAFT);
        //聊天类型
        chatType = mBundle.getInt(MsgConstant.CHAT_TYPE, -1);
        //成员数量
        membersCount = mBundle.getInt(MsgConstant.MEMBERS_COUNT, 0);
        //会话实体类
        mConversation = (Conversation) mBundle.getSerializable(MsgConstant.CONVERSATION_TAG);
        if (mConversation != null && mConversation.getUnreadMsgCount() > 10) {
            mChatView.showUnreadMessageNum(mConversation.getUnreadMsgCount());
        }
        if (chatType == MsgConstant.GROUP) {
            peoples = mConversation.getPeoples();
        }
        LogUtil.e("时间4===", System.currentTimeMillis() + "");
    }

    @Override
    protected void initData() {
        initConversation();
        headers = new HashMap<>();
        headers.put("TOKEN", SPHelper.getToken());
    }


    /**
     * 初始化会话
     */
    private void initConversation() {
        LogUtil.e("时间5===", System.currentTimeMillis() + "");
        //读取数据库消息
        if (SERVER_TIME > 0) {
            getNewMessage(SERVER_TIME - 100);
        } else {
            getMessageFromDatabase();
        }
        LogUtil.e("时间6===", System.currentTimeMillis() + "");

    }

    /**
     * 创建会话适配器
     */
    private void createMsgListAdapter() {
        try {
            mChatAdapter = new MessageListAdapter(msgList);
            mChatView.setChatListAdapter(mChatAdapter);
            mChatAdapter.setMemberNum(groupMemberNum);
        } catch (NullPointerException e) {
            e.printStackTrace();
            ToastUtils.showToast(ChatActivity.this, "初始化会话失败");
            finish();
        }
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ImMessage result) {
        if (MsgConstant.IM_PULL_HISTORY_MSG_FINISH.equals(result.getTag())) {
            //拉取历史消息完成IM_PULL_HISTORY_MSG_FINISH
            pullHistoryMessageBefore = true;
            isGetNewHistoryMessage = true;
            Log.d(TAG, "Pull history message finished.");
            if (result.getCode() > 0) {
                mChatView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOldMessage(1);
                    }
                }, 500);
                return;
            } else {
                return;
            }
        }
        if (MsgConstant.IM_PULL_OFFLINE_MSG_FINISH.equals(result.getTag())) {
            //拉取离线消息完成
            updateAllMessage();
        }
        //群成员变更/群名变更
        if (MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE.equals(result.getTag())
                || MsgConstant.RECEIVE_GROUP_NAME_CHANGE_PUSH_MESSAGE.equals(result.getTag())) {
            if (chatType == MsgConstant.SINGLE) {
                return;
            }
            if (!(conversationId + "").equals(((String) result.getObject()))) {
                return;
            }
            getNetData(false);
        }
        if (MsgConstant.MSG_RESULT.equals(result.getTag())) {
            SocketMessage bean = ((SocketMessage) result.getObject());
            //收到消息
            if (result.getCode() == MsgConstant.IM_TEAM_CHAT_CMD
                    || bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD
                    || bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_TEAM_CMD
                    || bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_PERSONAL_CMD) {
                if (bean.getConversationId() == conversationId) {
                    if (bean.getChatType() == MsgConstant.GROUP && bean.getUcFlag() == 1) {
                        IM.getInstance().sendReadAck(bean, MsgConstant.IM_TEAM_RESPONSE_READ_CMD);

                    }
                    if (bean.getChatType() == MsgConstant.SINGLE && bean.getUcFlag() == 1) {
                        IM.getInstance().sendReadAck(bean, MsgConstant.IM_PERSONSL_RESPONSE_READ_CMD);
                    }
                    if (bean.getUcFlag() == 1) {
                        bean.setRead(true);
                        DBManager.getInstance().saveOrReplace(bean);
                    }
                }
                getNewMessage(0L);
            } else if (result.getCode() == MsgConstant.IM_ACK_CHAT_PERSONAL_CMD || result.getCode() == MsgConstant.IM_ACK_CHAT_TEAM_CMD) {
                //收到发送成功ack
                if (SPHelper.getUserId().equals(bean.getOneselfIMID())) {
                    updateAllMessage();
                    LogUtil.e(TAG, "ack更新");
                }


            }

        }
        //收到已读回复
        if (MsgConstant.READ_MESSAGE_TAG.equals(result.getTag())) {
            //如果是单聊,将当前聊天中发送状态改为已读
            if (result.getCode() == MsgConstant.IM_PERSONSL_RESPONSE_READ_CMD) {
                if (((String) result.getObject()).equals(receiverId)) {
                    mChatView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (msgList.size() > 0) {
                                updateAllMessage();
                            } else {
                                getNewMessage(0L);
                            }

                        }
                    }, 500);

                }

            } else if (result.getCode() == MsgConstant.IM_TEAM_RESPONSE_READ_CMD) {
                //群聊
                if (((String) result.getObject()).equals(conversationId + "")) {
                    mChatView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (msgList.size() > 0) {
                                updateAllMessage();
                            } else {
                                getNewMessage(0L);
                            }
                        }
                    }, 500);
                }
            }

        }
        if (MsgConstant.RECEIVE_RELEASE_GROUP_PUSH_MESSAGE.equals(result.getTag())) {
            //解散群
            if (((String) result.getObject()).equals(conversationId + "")) {
                ToastUtils.showToast(ChatActivity.this, "该群已被解散!");
                finish();
            }
        }

        if (MsgConstant.RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE.equals(result.getTag())) {
            //我被移出了群
            if (((String) result.getObject()).equals(conversationId + "")) {
                ToastUtils.showToast(ChatActivity.this, "您被移出了该群!");
                finish();
            }
        }
        if (MsgConstant.SEND_FILE_FAILED.equals(result.getTag())) {
            long messageId = TextUtil.parseLong((String) result.getObject());

            for (int i = 0; i < msgList.size(); i++) {
                if (msgList.get(i).getMsgID() == messageId) {
                    msgList.get(i).setSendState(2);
                    mChatAdapter.notifyItemChanged(i);
                }
            }

        }
    }

    /**
     * 更新所有数据
     */
    private void updateAllMessage() {
        if (msgList.size() <= 0) {
            getMessageFromDatabase();
        } else {
            long l = msgList.get(0).getServerTimes() - 100;
            msgList.clear();
            getNewMessage(l);
        }

    }

    /**
     * 获取群详情
     *
     * @param flag
     */
    private void getNetData(boolean flag) {

        ImLogic.getInstance().getGroupDetail(ChatActivity.this, conversationId,
                new ProgressSubscriber<GroupChatInfoBean>(this, flag) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(GroupChatInfoBean bean) {
                        super.onNext(bean);
                        if (bean.getData() == null || bean.getData().getGroupInfo() == null) {
                            return;
                        }
                        GroupChatInfoBean.DataBean.GroupInfoBean groupInfo = bean.getData().getGroupInfo();
                        String groupName = groupInfo.getName();
                        if (TextUtils.isEmpty(groupName)) {
                            groupName = "";
                        }
                        List<GroupChatInfoBean.DataBean.EmployeeInfoBean> employeeInfo = bean.getData().getEmployeeInfo();
                        mChatView.setChatTitle(groupName + "(" + employeeInfo.size() + ")");
                        if (employeeInfo != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < employeeInfo.size(); i++) {
                                if (TextUtils.isEmpty(sb)) {
                                    sb.append(employeeInfo.get(i).getId() + "");
                                } else {
                                    sb.append("," + employeeInfo.get(i).getId());
                                }
                            }
                            peoples = sb.toString();
                        }

                    }
                });
    }

    /**
     * 从数据库查询数据
     */
    private void getMessageFromDatabase() {
        msgList.clear();
        msgList.addAll(DBManager.getInstance()
                .qureyMessageByConversationId(SPHelper.getCompanyId(), conversationId));
        mChatAdapter.notifyDataSetChanged();
        if (!isGetNewHistoryMessage) {
            if (msgList.size() > 0) {
                mChatView.getListView().scrollToPosition(mChatAdapter.getItemCount() - 1);
            }

        } else {
            isGetNewHistoryMessage = false;
            if (msgList.size() >= 20) {
                mChatView.getListView().scrollToPosition(19);
            } else if (msgList.size() < 20 && msgList.size() > 0) {
                mChatView.getListView().scrollToPosition(msgList.size() - 1);
            }
            return;
        }
        if (msgList.size() <= 1) {
            if (!pullHistoryMessageBefore) {
                getHistoryMessage();
                return;
            }
        } else {
            if (msgList.get(0).getServerTimes() == firstMessageTimeStamp) {
                getAllHistoryMessageFinished = true;
            }
        }
        //回应已读
        if (msgList.size() > 0 && conversationId != -1L) {

            if (!fromGroup && msgList.get(msgList.size() - 1).getUcFlag() == 1 && !msgList.get(msgList.size() - 1).getIsRead()) {
                if (!showNotificationFlag) {
                    responseSingleRead();
                }
            }
            if (fromGroup
                // && msgList.get(msgList.size() - 1).getUcFlag() == 1
                    ) {
                if (!showNotificationFlag) {
                    responseGroupRead(msgList);
                }
            }
        }
        //定位到搜索的消息
        if (msgId != -1L) {
            for (int i = 0; i < msgList.size(); i++) {
                if (msgList.get(i).getMsgID() == msgId) {
                    msgPosition = i;
                }
            }
            msgId = -1L;
            if (msgPosition != -1) {
                mChatView.getListView().scrollToPosition(msgPosition);

            }
            msgPosition = -1;
        }
    }

    /**
     * 获取数据数量
     *
     * @return
     */
    public int getDataSize() {
        return msgList.size();
    }

    public int getLastItemPosition() {


        RecyclerView.LayoutManager layoutManager = mChatView.getListView().getLayoutManager();
        int position = -1;
        if (msgList.size() <= 0) {
            return position;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            position = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
        } else {
            position = -1;
        }
        return position;
    }

    /**
     * 获取第一条可见条目的index
     *
     * @return
     */
    public int getFirstItemPosition() {
        RecyclerView.LayoutManager layoutManager = mChatView.getListView().getLayoutManager();
        int position = -1;
        if (msgList.size() <= 0) {
            return position;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            int firstItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            position = linearManager.findFirstVisibleItemPosition();
        } else {
            position = -1;
        }
        return position;
    }

    /**
     * 获取新消息
     */
    public void getNewMessage(long time) {
        LogUtil.e(TAG, "获取新鲜消息" + time);
        boolean lastMessageVisiable = false;
        if (msgList.size() > 0 && msgList.size() - 1 == getLastItemPosition()) {
            lastMessageVisiable = true;
        }
        if (time == 0L) {
            if (msgList != null && msgList.size() > 0) {
                time = msgList.get(msgList.size() - 1).getServerTimes();
            } else {
                getMessageFromDatabase();
                return;
            }
        }
        List<SocketMessage> newList = DBManager.getInstance().qureyMessageByConversationId(MsgConstant.GREAT_THAN, conversationId, time);
        if (newList != null && newList.size() > 0) {
            msgList.addAll(newList);
            mChatAdapter.notifyDataSetChanged();
            newMessageNum = newMessageNum + newList.size();
            if (SPHelper.getUserId().equals(msgList.get(msgList.size() - 1).getOneselfIMID()) && SoftKeyboardUtils.isShown(mChatView.getInputView())) {
                lastMessageVisiable = true;
            }
        }
        //如果之前最后一条消息可见则收到新消息滚动至最新消息
        if (lastMessageVisiable) {
            mChatView.getListView().scrollToPosition(msgList.size() - 1);
            newMessageNum = 0;
        }
        if (isFirstOpen && newMessageNum > 0 && msgList.size() > 0 && msgList.get(msgList.size() - 1).getUcFlag() == 1) {
            isFirstOpen = false;
            mChatView.setNewMessageCount(newMessageNum);
        }

    }

    /**
     * 获取更早的消息
     *
     * @param type 1拉取历史消息完成,2获取本地更早的消息
     */
    public void getOldMessage(int type) {
        long time = 0L;
        if (msgList != null && msgList.size() > 0) {
            time = msgList.get(0).getServerTimes();
        } else {
            time = System.currentTimeMillis();
            time = Long.MAX_VALUE;
        }
        List<SocketMessage> oldList = DBManager.getInstance().qureyMessageByConversationId(MsgConstant.LITTLE_THAN, conversationId, time);
        if (oldList.size() <= 0) {
            allMessageLoaded = true;
            switch (type) {
                case 1:
                    //拉取历史消息为空
                    break;
                case 2:
                    //加载本地消息为空,从网络获取消息
                    getHistoryMessage();
                    break;
                default:
                    break;
            }
        }

        int size = msgList.size();
        msgList.addAll(0, oldList);
        mChatAdapter.notifyDataSetChanged();

        //获取更多消息后滚动到指定的位置
        if (oldList.size() > 0) {
            mChatView.getListView().scrollToPosition(oldList.size());
        }
        //若获取历史消息前列表为空则滚动到最后一条
        if (size == 0 && msgList.size() > 0) {
            mChatView.getListView().scrollToPosition(msgList.size() - 1);
        }
    }

    /**
     * 单聊信息已读
     */
    private void responseSingleRead() {
        for (int i = 0; i < msgList.size(); i++) {
            if (!msgList.get(i).getIsRead() && msgList.get(i).getUcFlag() == 1) {
                IM.getInstance().sendReadAck(msgList.get(i), MsgConstant.IM_PERSONSL_RESPONSE_READ_CMD);
                DBManager.getInstance().qureyUnreadMessage(conversationId);
                return;
            }
        }

    }

    /**
     * 群聊已读
     *
     * @param list
     */
    private void responseGroupRead(List<SocketMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isRead()) {
                IM.getInstance().sendReadAck(list.get(i), MsgConstant.IM_TEAM_RESPONSE_READ_CMD);
                list.get(i).setRead(true);
                list.get(i).setSendState(3);
                DBManager.getInstance().saveOrReplace(list.get(i));
            }

        }
    }


    @Override
    protected int getContentView() {
        return R.layout.jmui_activity_chat2;
    }

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        // TODO: 2018/2/2 检查登录状态
        if (savedInstanceState == null) {
        }
    }


    @Override
    protected void setListener() {
        mChatView.setListeners(this);
        mChatView.setOnTouchListener(this);
        mChatView.setOnSizeChangedListener(this);
        mChatView.setOnKbdStateListener(this);


        mChatView.getInputView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textChanged = true;


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(textInEditText)) {
                    textInEditText = s.toString();

                } else {
                    if (s.length() <= textInEditText.length()) {
                        textInEditText = s.toString();
                        return;
                    } else {
                        textInEditText = s.toString();
                    }
                }


                if (!TextUtils.isEmpty(mConversation.getDraft()) && mConversation.getDraft().equals(s.toString()) && !textChanged) {
                    return;
                }

                mChatView.chooseAtMember(ChatActivity.this, s);
                if (atList != null && atList.size() > 0) {
                    for (Member info : atList) {
                        String name = info.getName();

                        if (!s.toString().contains("@" + name)) {
                            forDel.add(info);
                        }
                    }
                    atList.removeAll(forDel);
                    forDel.clear();
                }

            }
        });
        mChatView.getListView().addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*if (SoftKeyboardUtils.isShown(mChatView.getInputView())) {
                    SoftKeyboardUtils.hide(mChatView.getInputView());
                }*/
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {


            }


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SoftKeyboardUtils.hide(mChatView.getInputView());
                Bundle bundle = new Bundle();
                if (view.getId() == R.id.jmui_sender_avatar_iv || view.getId() == R.id.jmui_recever_avatar_iv) {
                    //查看人员详情
                    viewEmployeeInfo(position, bundle);
                } else if (view.getId() == R.id.jmui_send_picture_iv || view.getId() == R.id.jmui_receive_picture_iv) {
                    //点击图片类型消息
                    viewImageMessage((MessageListAdapter) adapter, position);
                } else if (view.getId() == R.id.rl_send_file || view.getId() == R.id.rl_receive_file) {
                    //点击文件类型消息
                    bundle.putSerializable(MsgConstant.MSG_DATA, msgList.get(position));
                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle);
                } else if (view.getId() == R.id.text_send_state) {
                    //查看已读未读
                    if (msgList.get(position).getReadnum() + 1 >= msgList.get(position).getGroupMemeberNum() || msgList.get(position).getSendState() == MsgConstant.ALL_READ) {
                        return;
                    }
                    bundle.putLong(MsgConstant.CONVERSATION_ID, conversationId);
                    bundle.putLong(MsgConstant.MESSAGE_ID, msgList.get(position).getMsgID());
                    bundle.putSerializable(MsgConstant.MSG_DATA, msgList.get(position));
                    CommonUtil.startActivtiy(ChatActivity.this, MessageReadStateActivity.class, bundle);
                } else if (view.getId() == R.id.jmui_receive_video_iv || view.getId() == R.id.jmui_send_video_iv) {
                    //查看文件
                    bundle.putSerializable(MsgConstant.MSG_DATA, msgList.get(position));
                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle);
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                // SoftKeyboardUtils.hide(mChatView.getInputView());
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //  vibrator.vibrate(pattern, 0);
                vibrator.vibrate(100L);
                vibrator.cancel();
                int location;
                List<String> list = new ArrayList<String>();
                if (view.getId() == R.id.jmui_recever_avatar_iv) {
                    if (msgList.get(position).getChatType() == MsgConstant.GROUP) {

                        Member m = new Member();
                        m.setSign_id(msgList.get(position).getSenderID());
                        m.setName(msgList.get(position).getSenderName());
                        atList.add(m);
                        mChatView.getInputView().appendMention(m.getName());
                        mChatView.getInputView().setSelection(mChatView.getInputView().getText().length());
                    }
                } else if (view.getId() == R.id.jmui_msg_content_send) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    if (msgList.get(position).getSendState() == 2) {
                        list.add("复制");
                        list.add("重发");
                    } else if (msgList.get(position).getSendState() == 6) {
                        list.add("复制");
                    } else {
                        if (msgList.get(position).getUcFlag() == 0
                                && System.currentTimeMillis() - msgList.get(position).getClientTimes() < MsgConstant.RECALL_TIME) {
                            list.add("复制");
                            list.add("转发");
                            if (msgList.get(position).getSendState() != 4 && msgList.get(position).getSendState() != 5) {
                                list.add("撤回");
                            }
                        } else {
                            list.add("复制");
                            list.add("转发");
                        }
                    }
                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    SystemFuncUtils.copyTextToClipboard(ChatActivity.this, msgList.get(position).getMsgContent());
                                    break;
                                case 1:
                                    if (msgList.get(position).getSendState() == 2) {
                                        resendFailedMessage(msgList.get(position));
                                    } else if (msgList.get(position).getSendState() != 6) {
                                        resendToSb(msgList.get(position));
                                    }


                                    break;
                                case 2:
                                    //撤回
                                    recallMessage(position);
                                       /* if (msgList.get(position).getSendState() == 2) {
                                            CommonUtil.showToast("重发");
                                            resendFailedMessage(msgList.get(position));
                                        } else {
                                            //撤回
                                            recallMessage(position);
                                        }*/


                                    break;
                                default:

                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.jmui_msg_content_receive) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    list.add("复制");
                    list.add("转发");

                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    SystemFuncUtils.copyTextToClipboard(ChatActivity.this, msgList.get(position).getMsgContent());
                                    break;
                                case 1:
                                    resendToSb(msgList.get(position));
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.jmui_msg_send_voice) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    if (msgList.get(position).getSendState() == 2) {
                        list.add("重发");

                    } else {
                        if (msgList.get(position).getUcFlag() == 0 && System.currentTimeMillis() - msgList.get(position).getClientTimes() < MsgConstant.RECALL_TIME) {

                            if (msgList.get(position).getSendState() != 4 && msgList.get(position).getSendState() != 5) {
                                list.add("撤回");
                            }
                        } else {
                        }
                    }


                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    if (msgList.get(position).getSendState() == 2) {
                                        //ToastUtils.showToast(ChatActivity.this, "重发");
                                        resendFailedMessage(msgList.get(position));
                                    } else {
                                        // ToastUtils.showToast(ChatActivity.this, "撤回");
                                        recallMessage(position);
                                    }
                                    break;
                                default:

                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.jmui_send_picture_iv) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    if (msgList.get(position).getSendState() == 2) {
                        list.add("重发");

                    } else {
                        if (msgList.get(position).getUcFlag() == 0 && System.currentTimeMillis() - msgList.get(position).getClientTimes() < MsgConstant.RECALL_TIME) {
                            list.add("转发");
                            if (msgList.get(position).getSendState() != 4 && msgList.get(position).getSendState() != 5) {
                                list.add("撤回");
                            }
                        } else {

                            list.add("转发");
                        }
                    }


                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    if (msgList.get(position).getSendState() == 2) {
                                        resendFailedMessage(msgList.get(position));
                                    } else {
                                        resendToSb(msgList.get(position));
                                    }
                                    break;
                                case 1:
                                    if (msgList.get(position).getSendState() == 2) {
                                        resendFailedMessage(msgList.get(position));
                                    } else {
                                        recallMessage(position);
                                    }
                                    break;

                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.jmui_receive_picture_iv) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    list.add("转发");
                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    resendToSb(msgList.get(position));
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.rl_send_file) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    if (msgList.get(position).getSendState() == 2) {
                        list.add("重发");

                    } else {
                        if (msgList.get(position).getUcFlag() == 0 && System.currentTimeMillis() - msgList.get(position).getClientTimes() < MsgConstant.RECALL_TIME
                                ) {
                            list.add("转发");
                            if (msgList.get(position).getSendState() != 4 && msgList.get(position).getSendState() != 5) {
                                list.add("撤回");
                            }
                        } else {
                            list.add("转发");
                        }
                    }
                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:

                                    if (msgList.get(position).getSendState() == 2) {
                                        ToastUtils.showToast(ChatActivity.this, "重发");
                                        resendFailedMessage(msgList.get(position));
                                    } else {
                                        ToastUtils.showToast(ChatActivity.this, "转发");

                                        resendToSb(msgList.get(position));
                                    }
                                    break;
                                case 1:
                                    if (msgList.get(position).getSendState() == 2) {
                                        //ToastUtils.showToast(ChatActivity.this, "重发");
                                        resendFailedMessage(msgList.get(position));
                                    } else {
                                        //ToastUtils.showToast(ChatActivity.this, "撤回");
                                        recallMessage(position);
                                    }
                                    break;
                                case 2:

                                    break;
                                default:

                                    break;
                            }
                            return true;
                        }
                    });
                } else if (view.getId() == R.id.rl_receive_file) {
                    if (0 == position) {
                        location = Gravity.TOP;
                    } else {
                        location = Gravity.NO_GRAVITY;
                    }
                    list.add("转发");
                    PopUtils.showMessagePopupMenu(ChatActivity.this, view, location, list, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int pos) {
                            switch (pos) {
                                case 0:
                                    resendToSb(msgList.get(position));
                                    break;

                                default:

                                    break;
                            }
                            return true;
                        }
                    });
                }
            }
        });
        xRefreshView.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newMessageNum > 0) {
                    newMessageNum = 0;
                    mChatView.setNewMessageCount(newMessageNum);
                }
                LogUtil.e(TAG, "       onScrollStateChanged");
                mChatView.hideUnreadMessageNum();
                mChatView.hideNewMessageNum();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                getOldMessage(2);
                if (msgList != null && msgList.size() > 0) {
                    firstMessageTimeStamp = msgList.get(0).getServerTimes();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                    }
                }, 200);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }
        });

    }

    private void getHistoryMessage() {
        if (msgList.size() <= 0) {
            firstMessageTimeStamp = 0L;
        } else {
            firstMessageTimeStamp = msgList.get(0).getServerTimes();
        }
        if (chatType == MsgConstant.GROUP) {
            IM.getInstance().sendPullHistoryMessage(2, conversationId, firstMessageTimeStamp, 20);
        } else if (chatType == MsgConstant.SINGLE) {
            IM.getInstance().sendPullHistoryMessage(1, TextUtil.parseLong(receiverId), firstMessageTimeStamp, 20);
        }
    }

    /**
     * 查看图片消息
     *
     * @param adapter
     * @param position
     */
    private void viewImageMessage(MessageListAdapter adapter, int position) {
        Bundle bundle = new Bundle();
        ArrayList<Photo> photoList = new ArrayList<Photo>();
        if (TextUtil.isEmpty(adapter.getData().get(position).getFileLocalPath())) {
            Photo photo = new Photo(adapter.getData().get(position).getFileUrl());
            photoList.add(photo);
        } else {
            Photo photo = new Photo(adapter.getData().get(position).getFileLocalPath());
            photoList.add(photo);
        }
        bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, photoList);
        CommonUtil.startActivtiy(ChatActivity.this, FullscreenViewImageActivity.class, bundle);
    }

    /**
     * 查看聊天人员信息
     *
     * @param position
     * @param bundle
     */
    private void viewEmployeeInfo(int position, Bundle bundle) {
        if (msgList.get(position).getUcFlag() == 1) {
            bundle.putString(Constants.DATA_TAG3, msgList.get(position).getSenderID() + "");
            UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle, Constants.REQUEST_CODE8);
        } else {
            bundle.putString(Constants.DATA_TAG3, SPHelper.getUserId());
            UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle, Constants.REQUEST_CODE8);
        }
    }

    /**
     * 撤回消息
     *
     * @param position
     */
    private void recallMessage(int position) {

        SocketMessage m = msgList.get(position);
        IM.getInstance().recallMessage(m);
        m.setSendState(4);
        msgList.get(position).setSendState(4);
        mChatAdapter.notifyDataSetChanged();
        DBManager.getInstance().saveOrReplace(m);
        if (msgList.size() > 0) {
            getNewMessage(msgList.get(msgList.size() - 1).getServerTimes());
        } else {
            getNewMessage(0L);
        }
        mChatView.postDelayed(() -> {
            DBManager.getInstance().updateMessageRecallState(m);
            if (msgList.size() > 0) {
                getNewMessage(msgList.get(0).getServerTimes() - 100);
            } else {
                getNewMessage(0L);
            }
        }, MsgConstant.RECALL_MSG_TIMEOUT);
    }

    /**
     * 重发
     *
     * @param message
     */
    private void resendFailedMessage(SocketMessage message) {
        if (message.getMsgType() == MsgConstant.FILE
                || message.getMsgType() == MsgConstant.VIDEO
                || message.getMsgType() == MsgConstant.VOICE
                || message.getMsgType() == MsgConstant.IMAGE) {
            if (TextUtils.isEmpty(message.getFileUrl())) {
                //文件未上传成功
                String fileLocalPath = message.getFileLocalPath();
                File file = new File(fileLocalPath);
                if (file.exists()) {
                    //再次上传发送
//                    IM.getInstance().resendUploadFailedMessage(message);
                    QxMessage qxMessage = new QxMessage();
                    qxMessage.setSenderAvatar(IM.getInstance().getavatar());
                    qxMessage.setSenderName(IM.getInstance().getName());
                    qxMessage.setType(MsgConstant.IMAGE);
                    qxMessage.setChatId(conversationId);
                    qxMessage.setFileName(file.getName());
                    qxMessage.setFilePath(fileLocalPath);
                    qxMessage.setFileSize(file.length());
                    byte[] bytes = null;
                    if (chatType == MsgConstant.SINGLE) {
                        bytes = IM.getInstance().createMessage(TextUtil.parseLong(receiverId), message, qxMessage);
                    } else if (chatType == MsgConstant.GROUP) {
                        message.setAllPeoples(peoples);
                        bytes = IM.getInstance().createMessage(conversationId, message, qxMessage);
                    }
                    checkFileSizeAndUpload(file, bytes, qxMessage);
                } else {
                    ToastUtils.showToast(ChatActivity.this, "本地文件不存在,请重新选择!");
                }

            } else {
                ToastUtils.showToast(ChatActivity.this, "本地文件不存在,请重新选择!");
                IM.getInstance().resendFailedMessage(message);
            }

        } else {

            IM.getInstance().resendFailedMessage(message);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.jmui_return_btn) {
            SoftKeyboardUtils.hide(mChatView.getInputView());
            finish();
            return;

        }
        if (v.getId() == R.id.tv_at) {
            mChatView.chooseAtMember(ChatActivity.this, "@");
            return;

        }
        if (v.getId() == R.id.iv_at) {
            mChatView.chooseAtMember(ChatActivity.this, "@");
            return;

        }
        if (v.getId() == R.id.ib_at) {
            mChatView.chooseAtMember(ChatActivity.this, "@");
            return;

        }
        if (!IMState.getImOnlineState()) {
            // ToastUtils.showToast(ChatActivity.this, "您已离线!正在尝试重新登录.");
            IM.getInstance().startIMService(ChatActivity.this);
            if (Constants.USE_LOAD_LEVELING) {
                IM.getInstance().getLlServerUrl();
            } else {
                IM.getInstance().login();
            }
        }
        if (v.getId() == R.id.tv_unread_msg_count) {
            //未查看消息
            if (msgList.size() > mConversation.getUnreadMsgCount()) {
                mChatView.getListView().scrollToPosition(msgList.size() - mConversation.getUnreadMsgCount());
            } else if (msgList.size() > 0) {
                mChatView.getListView().scrollToPosition(0);
                getOldMessage(2);
            }
            mChatView.hideUnreadMessageNum();
        } else if (v.getId() == R.id.tv_new_msg_count) {
            //未读提示栏
            if (msgList.size() > 0) {
                mChatView.getListView().scrollToPosition(msgList.size() - 1);
                mChatView.setNewMessageCount(0);
            }
        } else if (v.getId() == R.id.jmui_right_btn) {
            //详情
            SoftKeyboardUtils.hide(mChatView.getInputView());
            if (chatType == 1) {
                Bundle bundle = new Bundle();
                bundle.putString(MsgConstant.RECEIVER_ID, conversationId + "");
                bundle.putInt(MsgConstant.GROUP_TYPE, mConversation.getGroupType());
                bundle.putBoolean(MsgConstant.IS_CREATOR, SPHelper.getUserId().equals(mConversation.getPrincipal() + ""));
                CommonUtil.startActivtiyForResult(this, GroupChatDetailActivity.class, Constants.REQUEST_CODE6, bundle);
            } else if (chatType == 2) {

                Intent intent = new Intent(this, PersonalChatDetailActivity.class);
                intent.putExtra(MsgConstant.RECEIVER_ID, receiverId);
                intent.putExtra(MsgConstant.CONVERSATION_ID, conversationId);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.jmui_switch_voice_ib) {
            //切换为语音
            mChatView.dismissMoreMenu();
            isInputByKeyBoard = !isInputByKeyBoard;
            //当前为语音输入，点击后切换为文字输入，弹出软键盘
            if (isInputByKeyBoard) {
                mChatView.isKeyBoard();
                showSoftInputAndDismissMenu();
            } else {
                //否则切换到语音输入
                SoftKeyboardUtils.hide(mChatView.getInputView());
                mChatView.notKeyBoard();
                if (mShowSoftInput) {
                    if (mImm != null) {
                        mImm.hideSoftInputFromWindow(mChatView.getInputView().getWindowToken(), 0); //强制隐藏键盘
                        mShowSoftInput = false;
                    }
                } else if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
                    mChatView.dismissMoreMenu();
                }
            }
        } else if (v.getId() == R.id.jmui_send_msg_btn) {
            //发送文本消息
            String msgContent = mChatView.getChatInput();
            if (msgContent.equals("")) {
                return;
            }
            if (chatType == MsgConstant.GROUP) {
                ArrayList<QxMessage.AtListBean> list = new ArrayList<>();
                for (int i = 0; i < atList.size(); i++) {
                    QxMessage.AtListBean at = new QxMessage.AtListBean();
                    at.setId(atList.get(i).getSign_id());
                    at.setName(atList.get(i).getName());
                    list.add(at);
                }
                IM.getInstance().sendTeamTextMessage(list, conversationId, msgContent, peoples);
            } else if (chatType == MsgConstant.SINGLE) {
                //mChatView.postDelayed(mMyRunnable, 100);
                IM.getInstance().sendSingleTextMessage(msgContent, TextUtil.parseLong(receiverId), conversationId);
                // IM.getInstance().sendSinglePushTextMessage(msgContent, receiverId, conversationId);
            }
            if (getDataSize() > 0) {
                mChatView.getListView().scrollToPosition(getDataSize() - 1);
            }
            mChatView.clearInput();
            mChatView.setToBottom();
            DBManager.getInstance().cleanDraft(conversationId);
            mConversation.setDraft("");
            EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.CLEAN_DRAFT, conversationId));

            //清空@列表
            atList.clear();
        } else if (v.getId() == R.id.jmui_add_file_btn) {
            //更多菜单
            if (!isInputByKeyBoard) {
                mChatView.isKeyBoard();
                isInputByKeyBoard = true;
                mChatView.showMoreMenu();
            } else {
                //如果弹出软键盘 则隐藏软键盘
                if (mChatView.getMoreMenu().getVisibility() != View.VISIBLE) {
                    dismissSoftInputAndShowMenu();
                    mChatView.focusToInput(false);
                    //如果弹出了更多选项菜单，则隐藏菜单并显示软键盘
                } else {
                    showSoftInputAndDismissMenu();
                }
            }
        } else if (v.getId() == R.id.jmui_pick_from_camera_btn) {
            //拍照
            takePhoto();
        } else if (v.getId() == R.id.jmui_pick_from_local_btn) {
            if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
                mChatView.dismissMoreMenu();
            }
            CommonUtil.getImageFromAlbumByMultiple(ChatActivity.this, 1);
        } else if (v.getId() == R.id.jmui_send_location_btn) {
            //位置
            if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
                mChatView.dismissMoreMenu();
            }

            CommonUtil.startActivtiyForResult(ChatActivity.this, LocationPresenter.class,
                    Constants.REQUEST_CODE_SEND_LOCATION);
        } else if (v.getId() == R.id.jmui_phone_btn) {
            //文件
            FileHelper.showFileChooser(ChatActivity.this);
        } else if (v.getId() == R.id.jmui_expression_btn) {
            //表情
            mChatView.showEmoji();
        }

    }

    MyRunnable mMyRunnable = new MyRunnable();

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            IM.getInstance().sendSingleTextMessage(DateTimeUtil.longToStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:sss"), TextUtil.parseLong(receiverId), conversationId);
            mChatView.postDelayed(mMyRunnable, 50);
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
            mChatView.dismissMoreMenu();
        }
        if (FileHelper.isSdCardExist()) {
            SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                if (aBoolean) {
                    imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                } else {
                    ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                }
            });
        } else {
            ToastUtils.showToast(mContext, R.string.jmui_sdcard_not_exist_toast);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (RecordVoiceButton.mIsPressed) {
            SoftKeyboardUtils.hide(mChatView.getInputView());
            mChatView.dismissRecordDialog();
            mChatView.releaseRecorder();
            RecordVoiceButton.mIsPressed = false;
            conversationId = -1L;
            MsgConstant.openedConversationId = conversationId;

        }
        if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
            mChatView.dismissMoreMenu();
        } else {
        }

    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {

        unregisterReceiver(mReceiver);
        mChatView.getChatInput();
        mChatAdapter.release();
        MsgConstant.openedConversationId = -1L;
        super.onDestroy();
    }

    /**
     * 检查网络类型及文件大小限制
     *
     * @param file
     */
    public void checkFileSizeAndUpload(File file, byte[] bytes, QxMessage message) {
        if (FileTransmitUtils.checkLimit(file)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续上传吗?",
                    mChatView, new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {

                            sendFileMessage(file, bytes, message);
                        }
                    });
        } else {

            sendFileMessage(file, bytes, message);
        }
    }

    /**
     * 上传文件并发送消息
     *
     * @param file
     * @param bytes
     * @param message
     */
    private void sendFileMessage(File file, byte[] bytes, QxMessage message) {
        ParseUtil.saveMessage(bytes);
        DownloadService.sendFileMessage(file.getAbsolutePath(), bytes, message, new DownloadCallback() {
            @Override
            public void onSuccess(Call call, Response response) {
                LogUtil.e("DownloadService   onSuccess");
            }

            @Override
            public void onLoading(long total, long progress) {
                LogUtil.e("DownloadService  total" + total);
                LogUtil.e("DownloadService   progress" + progress);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LogUtil.e("DownloadService   失败");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        RecordVoiceButton.mIsPressed = false;
        showNotificationFlag = true;
        MsgConstant.showNotificationFlag = true;
        LogUtil.e("ChatActivity", "onPause");
        MsgConstant.openedConversationId = -1L;
        SoftKeyboardUtils.hide(mChatView.getInputView());
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        RecordVoiceButton.mIsPressed = false;
        showNotificationFlag = true;
        MsgConstant.showNotificationFlag = true;
        LogUtil.e("ChatActivity", "onPause");
        MsgConstant.openedConversationId = -1L;
        SoftKeyboardUtils.hide(mChatView.getInputView());
        EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.UPDATE_UNREAD_MSG_NUM, null));
        mChatAdapter.stop();
    }

    @Override
    protected void onStop() {

        // 2017/11/30  保存草稿
        if (!TextUtils.isEmpty(mChatView.getInputView().getText().toString().trim())) {
            DBManager.getInstance().updateDraft(conversationId, mChatView.getInputView().getText().toString());
        }
        ConversationListController.updateConversationDraft(conversationId, mChatView.getInputView().getText().toString());
        MsgConstant.openedConversationId = -1L;
        super.onStop();
    }

    @Override
    protected void onResume() {

        if (!RecordVoiceButton.mIsPressed) {
            mChatView.dismissRecordDialog();
        }
        if (fromGroup) {
            responseGroupRead(msgList);
        } else {
            responseSingleRead();
        }
        MsgConstant.openedConversationId = conversationId;
        MsgConstant.showNotificationFlag = false;
        showNotificationFlag = false;
        Log.i(TAG, "[Life cycle] - onResume");
        super.onResume();
    }

    /**
     * 用于处理拍照发送图片返回结果以及从其他界面回来后刷新聊天标题
     * 或者聊天消息
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新头像
        if (requestCode == Constants.REQUEST_CODE8) {
            mChatAdapter.notifyDataSetChanged();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        //从相册获取多张照片发送
        /**
         * 思路:
         * 1:根据选择的照片逐一生成SocketMessage,保存到数据库,不发送;
         * 2:待上传完成,根据返回的文件细信息更新数据库数据并发送;
         * 3:发送成功后更新数据库中发送状态;
         */
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                // showToast("" + photos.get(0).toString());
                Map<String, byte[]> map = new HashMap<>();
                String url;
                File file;

                for (int i = 0; i < photos.size(); i++) {
                    QxMessage message = new QxMessage();
                    message.setSenderAvatar(IM.getInstance().getavatar());
                    message.setSenderName(IM.getInstance().getName());
                    message.setType(MsgConstant.IMAGE);
                    message.setChatId(conversationId);
                    url = photos.get(i);
                    file = new File(url);
                    message.setFileName(file.getName());
                    message.setFilePath(url);
                    message.setFileSize(file.length());
                    if (chatType == MsgConstant.SINGLE) {
                        map.put(url, IM.getInstance().createSingleImageMessage(TextUtil.parseLong(receiverId), message));
                    } else if (chatType == MsgConstant.GROUP) {
                        message.setAllPeoples(peoples);
                        map.put(url, IM.getInstance().createTeamImageMessage(conversationId, message));
                    }
                    checkFileSizeAndUpload(file, map.get(url), message);
                    /*DownloadService.sendFileMessage(file.getAbsolutePath(), chatMap.get(url), message, new DownloadCallback() {
                        @Override
                        public void onSuccess(Call call, Response response) {
                            LogUtil.e("DownloadService   onSuccess");
                        }

                        @Override
                        public void onLoading(long total, long progress) {
                            LogUtil.e("DownloadService  total" + total);
                            LogUtil.e("DownloadService   progress" + progress);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            LogUtil.e("DownloadService   失败");
                        }
                    });*/
                }

            }
        }
        //定位 位置
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE_SEND_LOCATION) {
            if (data != null) {

                PoiItem poiInfo = data.getParcelableExtra(LocationPresenter.LOCATION_RESULT_CODE);
                poiInfo.getLatLonPoint().getLongitude();
                poiInfo.getLatLonPoint().getLatitude();
                QxMessage message = new QxMessage();
                message.setType(MsgConstant.LOCATION);
                message.setChatId(conversationId);
                message.setLocation(poiInfo.getProvinceName() + poiInfo.getCityName() + poiInfo.getAdName());
                message.setLongitude(poiInfo.getLatLonPoint().getLongitude() + "");
                message.setLatitude(poiInfo.getLatLonPoint().getLatitude() + "");
                if (chatType == MsgConstant.SINGLE) {
                    IM.getInstance().sendSingleLocationMessage(TextUtil.parseLong(receiverId), message);

                } else if (chatType == MsgConstant.GROUP) {
                    message.setAllPeoples(peoples);
                    IM.getInstance().sendTeamLocationMessage(TextUtil.parseLong(receiverId), message);
                }
            }
        }
        //拍照新建图片,未做压缩处理
        if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {
            if (imageFromCamera != null && imageFromCamera.exists()) {
                sendSingleImageMessage();
            }

        }
        //退群 解散
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE6) {
            MsgConstant.openedConversationId = -1L;
            finish();
        }
        //发送文件
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHOOSE_LOCAL_FILE) {
            Uri uri = data.getData();
            String path = UriUtil.getPhotoPathFromContentUri(ChatActivity.this, uri);
            File file = new File(path);

            if (file.exists()) {
                QxMessage message = new QxMessage();
                message.setSenderAvatar(IM.getInstance().getavatar());
                message.setSenderName(IM.getInstance().getName());
                message.setType(MsgConstant.FILE);
                message.setFileName(file.getName());
                message.setFilePath(file.getAbsolutePath());
                message.setFileSize(file.length());
                message.setChatId(conversationId);
                try {
                    message.setFileSize(((int) FileUtils.getFileSize(file)));
                } catch (Exception e) {

                }
                byte[] b;
                if (fromGroup) {
                    message.setAllPeoples(peoples);
                    b = IM.getInstance().createTeamFileMessage(conversationId, message);
                } else {
                    b = IM.getInstance().createSingleFileMessage(TextUtil.parseLong(receiverId), message);
                }

                checkFileSizeAndUpload(file, b, message);
                /*DownloadService.sendFileMessage(file.getAbsolutePath(), b, message, new DownloadCallback() {
                    @Override
                    public void onSuccess(Call call, Response response) {
                        LogUtil.e("DownloadService   onSuccess");
                    }

                    @Override
                    public void onLoading(long total, long progress) {
                        LogUtil.e("DownloadService  total" + total);
                        LogUtil.e("DownloadService   progress" + progress);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        LogUtil.e("DownloadService   onFailure");
                    }
                });*/
            }
        }
    }

    /**
     * 发送从拍照获取的照片
     */
    private void sendSingleImageMessage() {
        //数据库插入一条数据,不发送,界面显示发送中
        byte[] bytes = null;
        QxMessage message = new QxMessage();
        message.setSenderAvatar(IM.getInstance().getavatar());
        message.setSenderName(IM.getInstance().getName());
        message.setChatId(conversationId);
        message.setType(MsgConstant.IMAGE);
        message.setFileName(imageFromCamera.getName());
        message.setFilePath(imageFromCamera.getAbsolutePath());
        message.setFileSize(imageFromCamera.length());
        if (chatType == MsgConstant.SINGLE) {
            bytes = IM.getInstance().createSingleImageMessage(conversationId, TextUtil.parseLong(receiverId));
        } else {
            message.setAllPeoples(peoples);
            bytes = IM.getInstance().createTeamImageMessage(conversationId, message);
        }

        //上传图片
        checkFileSizeAndUpload(imageFromCamera, bytes, message);
        /*DownloadService.sendFileMessage(imageFromCamera.getAbsolutePath(), bytes, message, new DownloadCallback() {
            @Override
            public void onSuccess(Call call, Response response) {
                LogUtil.e("DownloadService   成功");
            }

            @Override
            public void onLoading(long total, long progress) {
                LogUtil.e("DownloadService  total" + total);
                LogUtil.e("DownloadService   progress" + progress);
                   *//* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtil.showToast("进度" + (int) ((float) progress / total * 100) + " %");
                        }
                    });*//*


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LogUtil.e("DownloadService   失败");
            }

        });*/
    }


    /**
     * 显示软键盘,隐藏菜单
     */
    private void showSoftInputAndDismissMenu() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        mChatView.invisibleMoreMenu();
        mChatView.getInputView().requestFocus();
        if (mImm != null) {
            //强制显示键盘
            mImm.showSoftInput(mChatView.getInputView(), InputMethodManager.SHOW_FORCED);
        }
        mChatView.scrollToBottom();
        mChatView.isKeyBoard();
        mShowSoftInput = true;
        mChatView.setMoreMenuHeight();
    }

    /**
     * 隐藏软键盘,显示菜单
     */
    public void dismissSoftInputAndShowMenu() {
        //隐藏软键盘
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mChatView.showMoreMenu();
        if (mImm != null) {
            //强制隐藏键盘
            mImm.hideSoftInputFromWindow(mChatView.getInputView().getWindowToken(), 0);
            if (msgList.size() > 0) {
                mChatView.getListView().scrollToPosition(msgList.size() - 1);
            }
        }
        mChatView.setMoreMenuHeight();
        mShowSoftInput = false;
    }


    private static class UIHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;

        public UIHandler(ChatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            ChatActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                   /* case REFRESH_LAST_PAGE:
                        activity.mChatAdapter.dropDownToRefresh();
                        activity.mChatView.getListView().onDropDownComplete();
                        if (activity.mChatAdapter.isHasLastPage()) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                activity.mChatView.getListView()
                                        .setSelectionFromTop(activity.mChatAdapter.getOffset(),
                                                activity.mChatView.getListView().getHeaderHeight());
                            } else {
                                activity.mChatView.getListView().setSelection(activity.mChatAdapter
                                        .getOffset());
                            }
                            activity.mChatAdapter.refreshStartPosition();
                        } else {
                            activity.mChatView.getListView().setSelection(0);
                        }
                        activity.mChatView.getListView()
                                .setOffset(activity.mChatAdapter.getOffset());
                        break;*/
                   /* case REFRESH_GROUP_NAME:
                        if (activity.mConv != null) {
                            int num = msg.getData().getInt(Constants.MEMBERS_COUNT);
                            String groupName = msg.getData().getString(Constants.GROUP_NAME);
                            if (mActivity.get().special == 0) {
                                activity.mChatView.setChatTitle(SPECIAL_GROUP[0]);
                                activity.mChatView.dismissGroupNum();
                            } else {
                                activity.mChatView.setChatTitle(groupName, num);
                            }
                        }
                        break;*/
                    case REFRESH_GROUP_NUM:
//                        int num = msg.getData().getInt(Constants.MEMBERS_COUNT);
//                        activity.mChatView.setChatTitle(R.string.group, num);
                        break;
                    case REFRESH_CHAT_TITLE:
//                        if (activity.mGroupInfo != null) {
//                            //检查自己是否在群组中
//                            UserInfo info = activity.mGroupInfo.getGroupMemberInfo(activity.mMyInfo.getUserName());
//                            if (!TextUtils.isNotEmpty(activity.mGroupInfo.getGroupName())) {
//                                activity.mGroupName = activity.mGroupInfo.getGroupName();
//                                if (info != null) {
//                                    activity.mChatView.setChatTitle(activity.mGroupName,
//                                            activity.mGroupInfo.getGroupMembers().size());
//                                    activity.mChatView.showRightBtn();
//                                } else {
//                                    activity.mChatView.setChatTitle(activity.mGroupName);
//                                    activity.mChatView.dismissRightBtn();
//                                }
//                            }
//                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onKeyBoardStateChange(int state) {
        switch (state) {
            case ChatView.KEYBOARD_STATE_INIT:
                if (mImm != null) {
                    mImm.isActive();
                }
                if (mChatView.getMoreMenu().getVisibility() == View.INVISIBLE
                        || (!mShowSoftInput && mChatView.getMoreMenu().getVisibility() == View.GONE)) {

                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mChatView.getMoreMenu().setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 发送语音
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        if (Constants.MEMBER_LIST.equals(event.getTag()) && event.getCode() == GroupMemberActivity.FLAG_AT) {
            //@功能
            ArrayList<Member> members = (ArrayList<Member>) event.getObject();
            atList.addAll(members);
            mChatView.setAtList(atList);
            if (atList.size() > 0) {
                LogUtil.e("@" + atList.get(atList.size() - 1).getSign_id());
                mChatView.getInputView().appendMention(atList.get(atList.size() - 1).getName());
                mChatView.getInputView().setSelection(mChatView.getInputView().getText().length());
            }

        }
        if (Constants.DATA_TAG1.equals(event.getTag())) {

            File file = (File) event.getObject();
            sendVoiceMessage(event, file);
        }
        if (MsgConstant.RECORDING_TAG.equals(event.getTag())) {
            //录音时停止播放
            mChatAdapter.stop();
            /*if (mp != null && mp.isPlaying()) {
                mp.stop();
                if (imageView != null) {
                    if (imageView.getId() == R.id.send_voice_iv) {
                        imageView.setImageResource(R.drawable.jmui_voice_send);
                        drawable = R.drawable.jmui_send_3;
                    } else if (imageView.getId() == R.id.receive_voice_iv) {
                        imageView.setImageResource(R.drawable.jmui_voice_receive);
                        drawable = R.drawable.jmui_receive_3;
                    }
                }
            }*/
        }

        if (MsgConstant.RESEND_MEMBER_TAG.equals(event.getTag())) {
            //转发
            Conversation c = (Conversation) event.getObject();
            String fileMessage = "消息内容";
            switch (mSocketMessage.getMsgType()) {
                case MsgConstant.FILE:
                    fileMessage = "文件";
                    break;
                case MsgConstant.IMAGE:
                    fileMessage = "图片";
                    break;
                case MsgConstant.VIDEO:
                    fileMessage = "视频";
                    break;
                default:

                    break;
            }
            DialogUtils.getInstance().share2cantact(ChatActivity.this, c.getTitle(), mSocketMessage.getMsgContent(),
                    fileMessage, mSocketMessage.getFileName(), "", mChatView.getListView(), new DialogUtils.OnShareClickListner() {
                        @Override
                        public void clickSure(String desc) {
                            int cmd = -1;
                            if (c.getConversationType() == MsgConstant.SINGLE) {
                                cmd = MsgConstant.IM_PERSONAL_CHAT_CMD;
                            }
                            if (c.getConversationType() == MsgConstant.GROUP) {
                                cmd = MsgConstant.IM_TEAM_CHAT_CMD;
                                mSocketMessage.setAllPeoples(c.getPeoples());
                            }
                            if (cmd == -1) {
                                return;
                            }
                            IM.getInstance().sendToSb(c.getConversationId(), c.getReceiverId(), cmd, mSocketMessage);
                            if (!TextUtils.isEmpty(desc)) {
                                IM.getInstance().sendTextMessage(c, desc);
                            }
                            ToastUtils.showToast(mContext, "转发成功");
                        }
                    });

        }
        if (MsgConstant.UPDATE_MEMBER_LIST_TAG.equals(event.getTag())) {
            setPeoples(((String) event.getObject()));
        }
        if (MsgConstant.MEMBER_MAYBE_CHANGED_TAG.equals(event.getTag())) {
            mChatAdapter.notifyDataSetChanged();
        }
        if (MsgConstant.MEMBER_CHANGED_TAG.equals(event.getTag())) {
            groupMemberNum = event.getCode();
            mConversation.setTitle(((String) event.getObject()));
            IM.getInstance().setGroupNumberNum(groupMemberNum);
            mChatView.setChatTitle(mConversation.getTitle() + "(" + groupMemberNum + ")");
            mChatAdapter.setMemberNum(event.getCode());
        }
        if (MsgConstant.QUIT_OR_RELEASE_GROUP.equals(event.getTag())) {
            finish();
        }
        if (MsgConstant.RECALL_MESSAGE_SUCCESS.equals(event.getTag())) {
            if (conversationId == ((long) event.getObject())) {
                updateAllMessage();
            }
        }

    }

    /**
     * 发送语音
     *
     * @param event
     * @param file
     */
    private void sendVoiceMessage(MessageBean event, File file) {
        if (!file.exists()) {
            ToastUtils.showToast(ChatActivity.this, "语音文件错误!");
            return;
        }
        QxMessage message = new QxMessage();
        message.setSenderAvatar(IM.getInstance().getavatar());
        message.setSenderName(IM.getInstance().getName());
        message.setChatId(conversationId);
        message.setType(MsgConstant.VOICE);
        message.setDuration(event.getCode() / 1000);
        message.setFileType(".mp3");
        message.setFilePath(file.getAbsolutePath());
        message.setFileSize(file.length());
        if (chatType == MsgConstant.SINGLE) {
            b = IM.getInstance().createSingleVoiceMessage(TextUtil.parseLong(receiverId), message);
        } else if (chatType == MsgConstant.GROUP) {
            message.setAllPeoples(peoples);
            b = IM.getInstance().createTeamVoiceMessage(conversationId, message);
        }
        mChatView.getInputView().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFileSizeAndUpload(file, b, message);
            }
        }, message.getDuration() / 10);

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mShowSoftInput = true;
            if (SharePreferenceManager.getCachedWritableFlag()) {
                SharePreferenceManager.setCachedKeyboardHeight(oldh - h);
                SharePreferenceManager.setCachedWritableFlag(false);
            }

            mChatView.setMoreMenuHeight();
        } else {
            mShowSoftInput = false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view.getId() == R.id.jmui_chat_input_et) {
                    if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE && !mShowSoftInput) {
                        showSoftInputAndDismissMenu();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
                    mChatView.dismissMoreMenu();
                } else if (mShowSoftInput) {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        mShowSoftInput = false;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    // 监听耳机插入
    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, filter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent data) {
            if (data != null) {
                //插入了耳机
                if (data.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    // mChatAdapter.setAudioPlayByEarPhone(data.getIntExtra("state", 0));
                }
            }
        }

    }

    /**
     * 转发
     *
     * @param msg
     */
    public void resendToSb(SocketMessage msg) {
        this.mSocketMessage = msg;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, MsgConstant.MEMBER_TAG);
        CommonUtil.startActivtiyForResult(ChatActivity.this,
                ChooseSendMemberActivity.class, Constants.REQUEST_CODE5, bundle);
    }

    /**
     * 当前群成员id字符串
     *
     * @param peoples
     */
    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //栈切换
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> appTasks = am.getAppTasks();
            if (appTasks.size() <= 1) {
                finish();
                return true;
            }
            String[] menu = new String[appTasks.size()];
            for (int i = 0; i < appTasks.size(); i++) {
                menu[i] = appTasks.get(i).getTaskInfo().taskDescription.getLabel();
                menu[i] = appTasks.get(i).getTaskInfo().taskDescription.getLabel();
            }
            PopUtils.showBottomMenu(mContext, mChatView, "选择页面", menu, new OnMenuSelectedListener() {
                @Override
                public boolean onMenuSelected(int p) {
                    appTasks.get(p).moveToFront();
                    return false;
                }
            });
        }

        return super.onKeyDown(keyCode, event);
    }
*/

}
