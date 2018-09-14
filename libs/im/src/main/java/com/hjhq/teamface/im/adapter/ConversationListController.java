package com.hjhq.teamface.im.adapter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.constants.MobileBrand;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.BadgeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.activity.AppAssistantActivity;
import com.hjhq.teamface.im.activity.ChatActivity;
import com.hjhq.teamface.im.bean.ConversationListBean;
import com.hjhq.teamface.im.db.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 会话列表控制器
 */
public class ConversationListController {

    private RecyclerView mConvListView;
    private Context mContext;
    private ArrayList<Conversation> mDatas = new ArrayList<>();
    public static ArrayList<Conversation> netConversationList = new ArrayList<>();
    private List<Conversation> localConversationList = new ArrayList<>();
    private static ConversationListAdapter mListAdapter;
    private int mWidth;
    private Uri mRingtoneUri;
    private Ringtone mRingtone;
    private AudioManager audioManager;
    private MediaPlayer mPlayer;
    private int requestCount = 0;
    private static Map<Long, ArrayList<SocketMessage>> msgMap = new HashMap<>();
    private static NotificationManager mNotificationManager;
    private static List<String> notifyTagList = new ArrayList<>();
    private PushMessage mPushmessage;
    private SocketMessage mSocketMessage;
    private long openedConversationId;
    private String PUSH_TITLE = "TEAMFACE";

    public ConversationListController(RecyclerView convListView, Context context) {
        mConvListView = convListView;
        mContext = context;
        //初始化企信服务
        //IM.getInstance().startService(mContext, getNotification());
        initRingTone();
        //初始化列表适配器
        initConvListAdapter();
        //获取会话列表
        getConversationList();
        //创建通知通道
        createNotificationChannel();
    }

    /**
     * 创建通知通道
     */
    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(MsgConstant.MESSAGE_CHANNEL, channelName, importance);
            channelName = "推送消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(MsgConstant.PUSH_CHANNEL, channelName, importance);
        }
    }

    /**
     * 初始化提醒声音
     */
    private void initRingTone() {
        mRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mRingtone = RingtoneManager.getRingtone(mContext, mRingtoneUri);
        mPlayer = MediaPlayer.create(mContext, mRingtoneUri);
    }


    /**
     * 获取会话列表
     */
    public void getConversationList() {
        ImLogic.getInstance().getListInfo(((BaseActivity) mContext),
                new ProgressSubscriber<ConversationListBean>(mContext, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        e.printStackTrace();
                        LogUtil.e("获取聊天列表失败");
                    }

                    @Override
                    public void onNext(ConversationListBean bean) {
                        super.onNext(bean);
                        mDatas.clear();
                        netConversationList.clear();
                        localConversationList.clear();
                        for (int i = 0; i < bean.getData().size(); i++) {
                            Conversation conversation = new Conversation();
                            conversation.setUpdateTime(bean.getData().get(i).getUpdate_time());
                            if (bean.getData().get(i).getChat_type() == MsgConstant.SELF_DEFINED) {
                                //小助手
                                setAssistantData(bean, i, conversation);
                                continue;
                            }
                            //群聊和单聊
                            setConversationData(bean, i, conversation);
                        }

                        //收到聊天消息后更新会话列表数据库
                        mDatas.addAll(netConversationList);
                        if (mSocketMessage != null) {
                            updateConversationList(openedConversationId, mSocketMessage);
                        }
                        //收到推送消息后更新会话列表数据库
                        if (mPushmessage != null) {
                            receiveMessage(mPushmessage);
                        }

                        mListAdapter.notifyDataSetChanged();
                        //合并本地数据与网络数据
                        mergeConversation();
                        Iterator<Conversation> iterator = netConversationList.iterator();
                        while (iterator.hasNext()) {
                            Conversation conv = iterator.next();
                            //公司总群
                            if (conv.getGroupType() == 1) {
                                continue;
                            }
                            //隐藏无本地消息会话
                            /*else if ((conv.getLastMessageType() == 0 && conv.getConversationType() != MsgConstant.SELF_DEFINED) || conv.getIsHide() == 1) {
                                iterator.remove();
                            }*/
                            //不隐藏无本地消息会话
                            else if (conv.getIsHide() == 1) {
                                iterator.remove();
                            }
                        }
                        mListAdapter.notifyDataSetChanged();
                        //更新总未读数
                        updateUnreadNum();
                        /*-------------企信登录相关----------------*/
                        //设置是否可登录
                        IMState.setImCanLogin(true);
                        //本地数据是否为空
                        IMState.setImIsFirstTimeUse(DBManager.getInstance().isMessageEmpty());
                        System.gc();
                        System.runFinalization();
                        //判断是否使用负载均衡(会弃用,在Constants变量中控制,服务端nginx部署后不再使用负载均衡)
                        if (Constants.USE_LOAD_LEVELING) {
                            if (!IMState.getImOnlineState() && IMState.isImCanLogin()) {
                                IM.getInstance().getLlServerUrl();
                            }

                        } else {
                            if (!IMState.getImOnlineState() && IMState.isImCanLogin()) {
                                IM.getInstance().login();
                            }
                        }
                         /*-------------企信登录相关----------------*/


                    }


                });
    }

    /**
     * 小助手数据
     *
     * @param bean
     * @param i
     * @param conversation
     */
    private void setAssistantData(ConversationListBean bean, int i, Conversation conversation) {
        conversation.setConversationType(bean.getData().get(i).getChat_type());
        try {
            conversation.setConversationId(Long.parseLong(bean.getData().get(i).getId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            conversation.setApplicationId(TextUtil.parseLong(bean.getData().get(i).getApplicationId()));
        } catch (NumberFormatException e) {

        }
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setTitle(bean.getData().get(i).getName());
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setMyId(SPHelper.getUserId());
        //头像
        conversation.setAvatarUrl("");
        conversation.setIcon_type(bean.getData().get(i).getIcon_type());
        conversation.setIcon_color(bean.getData().get(i).getIcon_color());
        conversation.setIcon_url(bean.getData().get(i).getIcon_url());
        //conversation.setLastMsgDate(bean.getData().get(i).getUpdate_time());

        long lastPushTime = TextUtil.parseLong(bean.getData().get(i).getLatest_push_time());
        if (lastPushTime <= 0) {
            lastPushTime = TextUtil.parseLong(bean.getData().get(i).getCreate_time());
        }
        conversation.setLastMsgDate(lastPushTime);
        conversation.setLastMessageType(MsgConstant.NOTIFICATION);
        conversation.setLatestMessage(bean.getData().get(i).getLatest_push_content());
        conversation.setTopStatus(TextUtil.parseInt(bean.getData().get(i).getTop_status(), 0));
        conversation.setNoBother(TextUtil.parseInt(bean.getData().get(i).getNo_bother(), 0));
        conversation.setUnreadMsgCount(TextUtil.parseInt(bean.getData().get(i).getUnread_nums(), 0));
        conversation.setIsHide(TextUtil.parseInt(bean.getData().get(i).getIs_hide(), 0));
        conversation.setUnreadMsgCount(TextUtil.parseInt(bean.getData().get(i).getUnread_nums()));
        //小助手类型1自定义模块助手,2企信助手3审批助手4文件库助手
        //此字段用于标记助手类型
        conversation.setTotalMsgNum(TextUtil.parseInt(bean.getData().get(i).getType()));
        //是否只查看未读,0否,1是
        conversation.setPeoples(bean.getData().get(i).getShow_type());

        if (conversation.getUnreadMsgCount() > 0 && conversation.getIsHide() == 1) {
            conversation.setIsHide(0);
            //小助手未读数不为0并且是隐藏时,请求为显示.
            ImLogic.getInstance().hideSessionWithStatus(((BaseActivity) mContext),
                    conversation.getConversationId(), MsgConstant.SELF_DEFINED, 0,
                    new ProgressSubscriber<BaseBean>(((BaseActivity) mContext), false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);


                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);


                        }
                    });
        }
        conversation.setConversationId(conversation.getConversationId() + MsgConstant.DEFAULT_VALUE);
        try {
            DBManager.getInstance().saveOrReplace(conversation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        netConversationList.add(conversation);
        return;
    }

    /**
     * 聊天数据
     *
     * @param bean
     * @param i
     * @param conversation
     */
    private void setConversationData(ConversationListBean bean, int i, Conversation conversation) {
        conversation.setPeoples(bean.getData().get(i).getPeoples());
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setMyId(SPHelper.getUserId());
        try {
            conversation.setConversationId(Long.parseLong(bean.getData().get(i).getId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        conversation.setConversationType(bean.getData().get(i).getChat_type());
        conversation.setReceiverId(bean.getData().get(i).getReceiver_id() + "");
        conversation.setAvatarUrl(bean.getData().get(i).getPicture());
        conversation.setIsHide(Integer.parseInt(bean.getData().get(i).getIs_hide()));

        try {
            conversation.setNoBother(Integer.parseInt(bean.getData().get(i).getNo_bother()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            conversation.setTopStatus(Integer.parseInt(bean.getData().get(i).getTop_status()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            conversation.setConversationId(Long.parseLong(bean.getData().get(i).getId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        conversation.setApplicationId(bean.getData().get(i).getUpdate_time());
        if (bean.getData().get(i).getChat_type() == MsgConstant.SINGLE) {
            conversation.setTitle(bean.getData().get(i).getEmployee_name());
            conversation.setTitle(bean.getData().get(i).getEmployee_name());
        }
        if (bean.getData().get(i).getChat_type() == MsgConstant.GROUP) {
            conversation.setTitle(bean.getData().get(i).getName());
            try {
                conversation.setPrincipal(Long.parseLong(bean.getData().get(i).getPrincipal()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                conversation.setPrincipal(Long.parseLong(bean.getData().get(i).getPrincipal()));
            } catch (NumberFormatException e) {

            }
            //群类型
            if ("0".equals(bean.getData().get(i).getType())) {
                //公司总群
                conversation.setGroupType(1);
                conversation.setIsHide(0);

            } else if ("1".equals(bean.getData().get(i).getType())) {
                //普通群
                conversation.setGroupType(2);
            }
        }
        if (bean.getData().get(i).getChat_type() == MsgConstant.SELF_DEFINED) {
            conversation.setTitle(bean.getData().get(i).getName());
            try {
                conversation.setConversationId(Long.parseLong(bean.getData().get(i).getId()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        netConversationList.add(conversation);
    }

    /**
     * 更新未读数
     */
    public void updateUnreadNum() {
        EventBusUtils.sendEvent(new MessageBean(getTotalUnreadNum(), MsgConstant.IM_TOTAL_UNREAD_NUM, null));
    }

    /**
     * 如果是企信小助手,将信息标记为全部已读
     *
     * @param conversation
     */
    public void markAllRead(Conversation conversation) {
        try {
            mNotificationManager.cancel((conversation.getConversationId() - MsgConstant.DEFAULT_VALUE) + "", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //小助手类型1自定义模块助手,2企信助手3审批助手4文件库助手5备忘录助手6邮件小助手
        //getTotalMsgNum为小助手类型
        if (conversation.getTotalMsgNum() == MsgConstant.IM_ASSISTANT) {
            //全部标记为已读
            ImLogic.getInstance().markAllRead(((BaseActivity) mContext),
                    (conversation.getConversationId() - MsgConstant.DEFAULT_VALUE) + "",
                    new ProgressSubscriber<BaseBean>(((BaseActivity) mContext), false) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            getConversationList();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                        }
                    });

        }
    }

    /**
     * 标记未读数为0
     *
     * @param conversationId
     */
    public void markAllRead(long conversationId) {
        //小助手类型1自定义模块助手,2企信助手3审批助手4文件库助手5备忘录助手6邮件小助手
        //getTotalMsgNum为小助手类型
        String assistantId = (conversationId - MsgConstant.DEFAULT_VALUE) + "";
        DBManager.getInstance().markAllPushMessageRead(assistantId);
        EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.MARK_ALL_READ, conversationId));
        for (int i = 0; i < netConversationList.size(); i++) {
            if (conversationId == netConversationList.get(i).getConversationId()) {
                netConversationList.get(i).setUnreadMsgCount(0);
                mListAdapter.notifyDataSetChanged();
                updateUnreadNum();
                break;
            }

        }

    }

    /**
     * 合并网络获取的会话列表和本地数据库查到的数据
     */
    private void mergeConversation() {
        localConversationList = DBManager.getInstance().qureyConversationList(SPHelper.getCompanyId());

        for (int i = 0; i < netConversationList.size(); i++) {
            for (int j = 0; j < localConversationList.size(); j++) {
                if (localConversationList.get(j).getConversationId() == netConversationList.get(i).getConversationId()) {
                    netConversationList.get(i).setDraft(localConversationList.get(j).getDraft());
                    //草稿
                    if (netConversationList.get(i).getConversationType() == MsgConstant.SELF_DEFINED) {
                        netConversationList.get(i).setLastMsgDate(localConversationList.get(j).getLastMsgDate());
                        String msg = localConversationList.get(j).getLatestMessage();
                        //用来标记小助手类型
                        netConversationList.get(i).setLastMessageType(localConversationList.get(j).getLastMessageType());
                    }
                }
            }
            if (netConversationList.get(i).getConversationType() == MsgConstant.SINGLE || netConversationList.get(i).getConversationType() == MsgConstant.GROUP) {
                ArrayList<SocketMessage> list = (ArrayList<SocketMessage>) DBManager.getInstance().
                        qureyMessageByConversationId(SPHelper.getCompanyId(),
                                netConversationList.get(i).getConversationId());
                //缓存消息到map,进入聊天界面时使用,减少空白期.
                msgMap.put(netConversationList.get(i).getConversationId(), list);
                if (list.size() > 0 &&
                        (netConversationList.get(i).getConversationType() == MsgConstant.SINGLE || netConversationList.get(i).getConversationType() == MsgConstant.GROUP)) {
                    SocketMessage message = list.get(list.size() - 1);
                    if (netConversationList.get(i).getConversationType() == MsgConstant.SINGLE) {
                        netConversationList.get(i).setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                    } else if (netConversationList.get(i).getConversationType() == MsgConstant.GROUP) {
                        netConversationList.get(i).setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                    }
                    netConversationList.get(i).setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                    netConversationList.get(i).setLastMsgDate(message.getServerTimes());
                    netConversationList.get(i).setLastMessageType(message.getMsgType());
                }
                if (netConversationList.get(i).getLastMsgDate() <= 0) {
                    netConversationList.get(i).setLastMsgDate(netConversationList.get(i).getApplicationId());
                }
                int unreadNum = 0;
                for (int k = 0; k < list.size(); k++) {
                    if (!list.get(k).getIsRead()) {
                        if (list.get(k).isAtAll()) {
                            netConversationList.get(i).setLastMessageState(MsgConstant.SB_AT_ALL);
                        } else if (list.get(k).isAtMe()) {
                            netConversationList.get(i).setLastMessageState(MsgConstant.SB_AT_ME);
                        }
                        unreadNum++;
                    }
                }
                netConversationList.get(i).setUnreadMsgCount(unreadNum);
                netConversationList.get(i).setCompanyId(SPHelper.getCompanyId());
                netConversationList.get(i).setMyId(SPHelper.getUserId());
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));

            }

        }
        // TODO: 2018/2/3 从数据库获取小助手信息

        //非置顶按时间排序
        List<Conversation> topC = new ArrayList<Conversation>();
        List<Conversation> notTopC = new ArrayList<Conversation>();

        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getTopStatus() == 1) {
                topC.add(netConversationList.get(i));
            } else {
                notTopC.add(netConversationList.get(i));
            }
        }

        Collections.sort(topC, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                // TODO: 2018/5/14 置顶会话排序
                return o1.getUpdateTime() < o2.getUpdateTime() ? 1 : -1;
            }
        });
        Collections.sort(notTopC, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return o1.getLastMsgDate() < o2.getLastMsgDate() ? 1 : -1;
            }
        });
        netConversationList.clear();
        netConversationList.addAll(topC);
        netConversationList.addAll(notTopC);
    }

    /**
     * 初始化列表适配器与点击事件
     */
    private void initConvListAdapter() {


        //对会话列表进行时间排序
        mConvListView.setLayoutManager(new LinearLayoutManager(mContext));
        //mConvListView.addItemDecoration(new MyLinearDeviderDecoration(mContext, R.color.main_green));
        mListAdapter = new ConversationListAdapter(((BaseActivity) mContext), netConversationList);
        mConvListView.setAdapter(mListAdapter);

        mConvListView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                //Log.e("时间1", "" + System.currentTimeMillis() + (1 / 0));
                Conversation conversation = netConversationList.get(position);
                if (conversation.getConversationType() == MsgConstant.SELF_DEFINED) {
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString(MsgConstant.CONV_TITLE, netConversationList.get(position).getTitle() + "");
                        bundle.putString(MsgConstant.CONVERSATION_ID, (netConversationList.get(position).getConversationId() - MsgConstant.DEFAULT_VALUE) + "");
                        bundle.putString(MsgConstant.APPLICATION_ID, (netConversationList.get(position).getApplicationId() + ""));
                        bundle.putString(MsgConstant.BEAN_NAME, "");
                        //小助手类型
                        bundle.putString(Constants.DATA_TAG1, netConversationList.get(position).getTotalMsgNum() + "");
                        bundle.putLong(Constants.DATA_TAG2, netConversationList.get(position).getLastMsgDate());
                        //是否查看已读,借用peoples字段使用
                        bundle.putString(MsgConstant.VIEW_READED, netConversationList.get(position).getPeoples());
                        bundle.putString(Constants.DATA_TAG3, netConversationList.get(position).getIcon_type());
                        bundle.putString(Constants.DATA_TAG4, netConversationList.get(position).getIcon_color());
                        bundle.putString(Constants.DATA_TAG5, netConversationList.get(position).getIcon_url());
                        CommonUtil.startActivtiyForResult(mContext, AppAssistantActivity.class, Constants.REQUEST_CODE1, bundle);
                        markAllRead(netConversationList.get(position));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(MsgConstant.CONVERSATION_TAG, conversation);
                    intent.putExtra(MsgConstant.CHAT_TYPE, conversation.getConversationType());
                    intent.putExtra(MsgConstant.CONVERSATION_ID, conversation.getConversationId());
                    intent.putExtra(MsgConstant.RECEIVER_ID, conversation.getReceiverId());
                    intent.putExtra(MsgConstant.FROM_GROUP, false);
                    ((BaseActivity) mContext).startActivityForResult(intent, Constants.REQUEST_CODE1, null);
                    //((MainActivity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    // Log.e("时间2", "" + System.currentTimeMillis());
                    openedConversationId = conversation.getConversationId();
                    netConversationList.get(position).setUnreadMsgCount(0);
                    netConversationList.get(position).setLastMessageState(MsgConstant.NO_AT_MSG);
                    adapter.notifyItemChanged(position);
                    if (netConversationList.get(position).getConversationType() == MsgConstant.SINGLE) {
                        List<Member> memberBySignId = DBManager.getInstance().getMemberBySignId(netConversationList.get(position).getReceiverId() + "");
                        if (memberBySignId != null && memberBySignId.size() > 0) {
                            Member member = memberBySignId.get(0);
                            Member m = new Member();
                            m.setEmployee_name(member.getEmployee_name());
                            m.setName(member.getName());
                            m.setId(member.getId());
                            m.setSign_id(member.getSign_id());
                            m.setDepartment_name(member.getDepartment_name());
                            m.setPost_name(member.getPost_name());
                            ImLogic.getInstance().saveRecentContact(m);
                        }
                    }
                }
                if (mListAdapter.isMenuOpen()) {
                    mListAdapter.notifyDataSetChanged();
                }
                mNotificationManager.cancel(netConversationList.get(position).getConversationId() + "", 2);
                updateUnreadNum();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemLongClick(adapter, view, position);
            }

        });

    }

    /**
     * 获取适配器
     *
     * @return
     */
    public ConversationListAdapter getAdapter() {
        return mListAdapter;
    }


    /**
     * 更新会话列表界面
     *
     * @param message
     */
    public void updateConversationList(long openedConversationId, SocketMessage message) {

        Conversation c = null;
        if (message.getUsCmdID() != MsgConstant.IM_TEAM_CHAT_CMD
                && message.getUsCmdID() != MsgConstant.IM_PERSONAL_CHAT_CMD) {
            return;
        }

        if (message == null || message.getConversationId() <= 0) {
            return;
        }

        if (message.getUsCmdID() == 1) {
            return;
        }
        // this.openedConversationId = openedConversationId;
        this.mSocketMessage = message;
        boolean sbAtMe = false;
        boolean flag = true;

        for (int i = 0; i < mDatas.size(); i++) {

            if (mDatas.get(i).getConversationId() == message.getConversationId() &&
                    (mDatas.get(i).getConversationType() == MsgConstant.GROUP
                            || mDatas.get(i).getConversationType() == MsgConstant.SINGLE)) {
                //在列表中是隐藏的
                if (mDatas.get(i).getIsHide() == 1) {
                    //置顶
                    showConversation(message);
                    mDatas.get(i).setIsHide(0);
                    int position = getPosition(netConversationList, message);
                    c = mDatas.get(i);
                    c.setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                    c.setLastMsgDate(message.getServerTimes());
                    c.setLastMessageType(message.getMsgType());
                    netConversationList.add(position, c);
                    sbAtMe = showAtNotification(position, message);
                    requestCount = 0;

                    if (message.getUcFlag() == 1) {
                        if (MsgConstant.openedConversationId != message.getConversationId()) {
                            netConversationList.get(0).setUnreadMsgCount(0 + 1);
                            if (netConversationList.get(0).getNoBother() == MsgConstant.NO_BOTHER_FALSE && !sbAtMe) {
                                if (!mPlayer.isPlaying()) {
                                    mPlayer.start();
                                }
                                //msgNotify(position, message);
                                receiveMessage(0);
                            }
                        } else {
                            netConversationList.get(0).setUnreadMsgCount(0);
                        }
                    }
                    //时间显示
                    showTime(message, netConversationList.get(0));
                    mSocketMessage = null;
                    flag = false;
                } else if (mDatas.get(i).getIsHide() == 0) {
                    //未隐藏,但是未添加到显示列表
                    boolean containFlag = false;
                    for (int j = 0; j < netConversationList.size(); j++) {
                        if (netConversationList.get(j).getConversationId() == mDatas.get(i).getConversationId()) {
                            containFlag = true;
                        }

                    }
                    if (!containFlag) {
                        int position = getPosition(netConversationList, message);
                        c = mDatas.get(i);
                        c.setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                        c.setLastMsgDate(message.getServerTimes());
                        c.setLastMessageType(message.getMsgType());
                        netConversationList.add(position, c);
                        //过滤掉另一端发送同步过来的消息
                        if (message.getUcFlag() == 1 && !SPHelper.getUserId().equals(message.getOneselfIMID())) {
                            sbAtMe = showAtNotification(position, message);
                            requestCount = 0;
                            if (MsgConstant.openedConversationId != message.getConversationId()) {
                                netConversationList.get(position).setUnreadMsgCount(netConversationList.get(position).getUnreadMsgCount() + 1);
                                if (netConversationList.get(position).getNoBother() == MsgConstant.NO_BOTHER_FALSE) {
                                    if (MsgConstant.showNotificationFlag && !sbAtMe) {
                                        receiveMessage(position);
                                    } else {
                                       /* //播放提示音
                                        if (!mPlayer.isPlaying() && !sbAtMe) {
                                            mPlayer.start();
                                        }*/
                                    }
                                    //播放提示音
                                    if (!mPlayer.isPlaying() && !sbAtMe) {
                                        mPlayer.start();
                                    }

                                }
                            } else {
                                netConversationList.get(position).setUnreadMsgCount(position);
                                if (netConversationList.get(position).getNoBother() == MsgConstant.NO_BOTHER_FALSE) {
                                    if (ChatActivity.showNotificationFlag && !sbAtMe) {
                                        receiveMessage(position);
                                    } else {
                                        //播放提示音
                                        if (!mPlayer.isPlaying() && !sbAtMe) {
                                            mPlayer.start();
                                        }
                                    }

                                }
                            }
                        }
                        //时间显示
                        showTime(message, netConversationList.get(0));
                        mSocketMessage = null;
                        flag = false;
                    } else {
                        //在显示列表
                        for (int j = 0; j < netConversationList.size(); j++) {

                            if (netConversationList.get(j).getConversationId() == message.getConversationId() && (netConversationList.get(j).getConversationType() == MsgConstant.GROUP || netConversationList.get(j).getConversationType() == MsgConstant.SINGLE)) {
                                requestCount = 0;
                                c = netConversationList.get(j);
                                netConversationList.get(j).setLatestMessage(message.getSenderName() + ":" + message.getMsgContent());
                                netConversationList.get(j).setLastMsgDate(message.getServerTimes());
                                netConversationList.get(j).setLastMessageType(message.getMsgType());
                                sbAtMe = showAtNotification(j, message);
                                if (message.getUcFlag() == 1 && !SPHelper.getUserId().equals(message.getOneselfIMID())) {
                                    if (MsgConstant.openedConversationId != message.getConversationId()) {
                                        if (netConversationList.get(j).getNoBother() == MsgConstant.NO_BOTHER_FALSE) {
                                            if (MsgConstant.showNotificationFlag && !sbAtMe) {
                                                receiveMessage(j);

                                            } else {
                                                //播放提示音
                                                if (!mRingtone.isPlaying() && !sbAtMe) {
                                                    mRingtone.play();
                                                }
                                            }
                                        }
                                        netConversationList.get(j).setUnreadMsgCount(netConversationList.get(j).getUnreadMsgCount() + 1);
                                    } else {
                                        // DBManager.getInstance().qureyUnreadMessage(netConversationList.get(j).getConversationId());
                                        netConversationList.get(j).setUnreadMsgCount(0);
                                        if (netConversationList.get(j).getNoBother() == MsgConstant.NO_BOTHER_FALSE) {
                                            if (ChatActivity.showNotificationFlag && !sbAtMe) {
                                                receiveMessage(j);
                                            } else {
                                                //播放提示音
                                                if (!mRingtone.isPlaying() && !sbAtMe) {
                                                    mRingtone.play();
                                                }
                                            }
                                        }
                                    }

                                }
                                showTime(message, netConversationList.get(j));
                                // mListAdapter.notifyDataSetChanged();
                                mSocketMessage = null;
                                flag = false;
                            }
                        }
                    }
                }
                flag = false;
            }
        }
        mListAdapter.notifyDataSetChanged();
        //更新未读数
        updateUnreadNum();
        //未从网络拉取会话/会话id错误
        if (requestCount >= 3) {
            requestCount = 0;
            return;
        }
        if (flag) {
            requestCount++;
            getConversationList();
        } else {
            //置顶会话
            putConvOnTop(message);
            if (c != null && c.getIsHide() == 1) {
                setConvVisibile(message);
            }


        }
    }

    /**
     * 获取会话位置
     *
     * @param netConversationList
     * @param message
     * @return
     */
    private int getPosition(ArrayList<Conversation> netConversationList, SocketMessage message) {
        int position = 0;
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getTopStatus() == 1) {
                position = i;
            }

        }
        return position;


    }

    /**
     * 置顶会话
     *
     * @param message
     */
    private void putConvOnTop(SocketMessage message) {
        Conversation c = null;
        Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c1 = iterator.next();
            if (c1.getConversationId() == message.getConversationId()) {
                c = c1;
                if (c.getTopStatus() == 1) {
                    return;
                }
                iterator.remove();
                //break;
            }
        }
        int topIndex = 0;
        if (c != null && c.getTopStatus() == 1) {
            netConversationList.add(topIndex, c);
            mListAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < netConversationList.size(); i++) {
                if (netConversationList.get(i).getTopStatus() == 1) {
                    topIndex = i + 1;
                }

                //break;
            }
            if (c != null) {
                netConversationList.add(topIndex, c);
                mListAdapter.notifyDataSetChanged();
            }

        }


    }

    /**
     * 置顶会话
     *
     * @param conversationId
     */
    private void putConvOnTop(long conversationId) {
        Conversation c = null;
        Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c1 = iterator.next();
            if (c1.getConversationId() == conversationId) {
                c = c1;
                if (c.getTopStatus() == 1) {
                    return;
                }
                iterator.remove();
                //break;
            }
        }
        int topIndex = 0;
        if (c != null && c.getTopStatus() == 1) {
            netConversationList.add(topIndex, c);
            mListAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < netConversationList.size(); i++) {
                if (netConversationList.get(i).getTopStatus() == 1) {
                    topIndex = i + 1;
                }

                //break;
            }
            if (c != null) {
                netConversationList.add(topIndex, c);
                mListAdapter.notifyDataSetChanged();
            }

        }


    }

    /**
     * 显示@消息通知
     *
     * @param pos
     * @param msg
     * @return
     */
    private boolean showAtNotification(int pos, SocketMessage msg) {
        try {
            if (msg.getChatType() == MsgConstant.GROUP) {
                JSONObject jsonObject = new JSONObject(msg.getContent());
                JSONArray atList = jsonObject.optJSONArray("atList");
                if (atList != null && atList.length() > 0) {
                    for (int i = 0; i < atList.length(); i++) {
                        JSONObject jo = atList.optJSONObject(i);
                        //去掉自己发送的@所有人消息提醒
                        if (SPHelper.getUserId().equals(msg.getOneselfIMID())) {
                            return false;
                        }
                        if (jo != null) {
                            if (SPHelper.getUserId().equals(jo.optString("id"))) {
                                if (MsgConstant.openedConversationId != netConversationList.get(pos).getConversationId()) {
                                    netConversationList.get(pos).setLastMessageState(MsgConstant.SB_AT_ME);
                                }

                                atMsgNotify(pos, msg);
                                return true;
                            } else if ("0".equals(jo.optString("id"))) {
                                if (MsgConstant.openedConversationId != netConversationList.get(pos).getConversationId()) {
                                    netConversationList.get(pos).setLastMessageState(MsgConstant.SB_AT_ALL);
                                }
                                atMsgNotify(pos, msg);
                                return true;
                            }
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 显示通知
     *
     * @param position
     */
    private void receiveMessage(int position) {
        if (MsgConstant.showNotificationFlag) {
            msgNotify(position, mSocketMessage);
        }


    }

    /**
     * 通知栏提醒
     *
     * @param position
     * @param message
     */
    private void msgNotify(int position, SocketMessage message) {

        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //聊天类型
        intent.putExtra(MsgConstant.CHAT_TYPE, netConversationList.get(position).getConversationType());
        //会话实体类
        intent.putExtra(MsgConstant.CONVERSATION_TAG, netConversationList.get(position));
        //会话id
        intent.putExtra(MsgConstant.CONVERSATION_ID, netConversationList.get(position).getConversationId());
        //接收人id
        intent.putExtra(MsgConstant.RECEIVER_ID, netConversationList.get(position).getReceiverId());
        //会话标题
        intent.putExtra(MsgConstant.CONV_TITLE, netConversationList.get(position).getSenderName() + "");
        String title = "TEAMFACE";
        String content = "";
        //PUSH_TITLE = netConversationList.get(position).getTitle();
        if (netConversationList.get(position).getConversationType() == MsgConstant.SELF_DEFINED) {
            content = netConversationList.get(position).getLatestMessage();
        } else if (message != null) {
            switch (message.getMsgType()) {
                case MsgConstant.TEXT:
                    content = netConversationList.get(position).getLatestMessage();
                    break;
                case MsgConstant.FILE:
                    content = "[文件]";
                    content = message.getSenderName() + ":" + content;
                    break;
                case MsgConstant.IMAGE:
                    content = "[图片]";
                    content = message.getSenderName() + ":" + content;
                    break;
                case MsgConstant.VOICE:
                    content = "[语音]";
                    content = message.getSenderName() + ":" + content;
                    break;
                case MsgConstant.NOTIFICATION:
                    content = "[通知]";
                    content = message.getSenderName() + ":" + content;
                    break;
                default:

                    break;
            }

        }

        PendingIntent pi = PendingIntent.getActivity(mContext,
                Constants.REQUEST_CODE1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(mContext, MsgConstant.MESSAGE_CHANNEL)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new Notification.Builder(mContext)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker(Constants.APP_NAME)
                    /**点击跳转**/
                    .setContentIntent(pi)
                    /**设置通知的标题**/
                    .setContentTitle(title)
                    /**设置通知的内容**/
                    .setContentText(content)
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(false)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：Notification.DEFAULT_ALL就是3种全部提醒**/
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(PendingIntent.getActivity(mContext, MsgConstant.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
        }


        // mNotificationManager.notify(MsgConstant.MEMBER_TAG, 2, notification);
        if (MobileBrand.XIAOMI.equals(Build.BRAND)) {
            BadgeUtil.setBadgeOfXiaomi(mContext, getTotalUnreadNum(), notification);
        } else {
            mNotificationManager.notify(MsgConstant.MESSAGE_CHANNEL, 2, notification);
        }
        notifyTagList.add(netConversationList.get(position).getConversationId() + "");
    }

    /**
     * 显示@消息通知
     *
     * @param position
     * @param msg
     */
    private void atMsgNotify(int position, SocketMessage msg) {
        //mListAdapter.notifyItemChanged(position);
        // mListAdapter.notifyDataSetChanged();
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MsgConstant.CHAT_TYPE, netConversationList.get(position).getConversationType());
        intent.putExtra(MsgConstant.CONVERSATION_TAG, netConversationList.get(position));
        intent.putExtra(MsgConstant.CONVERSATION_TAG, netConversationList.get(position));
        intent.putExtra(MsgConstant.CONVERSATION_ID, netConversationList.get(position).getConversationId());
        intent.putExtra(MsgConstant.CONV_TITLE, netConversationList.get(position).getSenderName() + "");
        String notifyContent = "";
        if (MsgConstant.SB_AT_ALL == netConversationList.get(position).getLastMessageState()) {
            notifyContent = "[@所有人]" + msg.getSenderName() + ":" + msg.getMsgContent();
        } else if (MsgConstant.SB_AT_ME == netConversationList.get(position).getLastMessageState()) {
            notifyContent = "[有人@我]" + msg.getSenderName() + ":" + msg.getMsgContent();
        } else {
            notifyContent = msg.getSenderName() + ":" + msg.getMsgContent();
        }
        PendingIntent pi = PendingIntent.getActivity(mContext,
                Constants.REQUEST_CODE1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(mContext, MsgConstant.MESSAGE_CHANNEL)
                    .setContentTitle(PUSH_TITLE)
                    .setContentText(notifyContent)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    .setAutoCancel(true)
                    .build();

        } else {
            notification = new Notification.Builder(mContext)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker(Constants.APP_NAME)
                    /**点击跳转**/
                    .setContentIntent(pi)
                    /**设置通知的标题**/
                    .setContentTitle(PUSH_TITLE)
                    /**设置通知的内容**/
                    .setContentText(TextUtils.isEmpty(msg.getSenderName()) ? "有人@了我" : msg.getSenderName() + "@了我")
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(false)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：Notification.DEFAULT_ALL就是3种全部提醒**/
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(PendingIntent.getActivity(mContext, MsgConstant.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();

                /*.setSmallIcon(R.mipmap.logo)
                .setContentTitle(netConversationList.get(position).getTitle())
                .setContentText(netConversationList.get(position).getLatestMessage())
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setWhen(System.currentTimeMillis())
                .build();*/


        }
        // mNotificationManager.notify(netConversationList.get(position).getConversationId() + "", 2, notification);
        if (MobileBrand.XIAOMI.equals(Build.BRAND)) {
            BadgeUtil.setBadgeOfXiaomi(mContext, getTotalUnreadNum(), notification);
        } else {
            mNotificationManager.notify(MsgConstant.PUSH_CHANNEL, 2, notification);
        }
        notifyTagList.add(netConversationList.get(position).getConversationId() + "");
    }

    /**
     * 聊天界面是否显示时间
     *
     * @param message
     */

    private void showTime(SocketMessage message, Conversation conv) {
        if (message.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD
                || message.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD
                || message.getUsCmdID() == MsgConstant.IM_USER_DEFINED_PERSONAL_CMD
                || message.getUsCmdID() == MsgConstant.IM_USER_DEFINED_TEAM_CMD) {
            if (message.getServerTimes() - conv.getNextShowTime() > 0L) {
                message.setShowTime(true);
                boolean flag = DBManager.getInstance().showTimeMessage(message, true);
                conv.setNextShowTime(message.getServerTimes() + MsgConstant.SHOW_TIME_INTERVAL);
            } else {
                message.setShowTime(false);
                boolean flag = DBManager.getInstance().showTimeMessage(message, false);
            }

        }
       /* LogUtil.e("1间隔时间===" + (message.getServerTimes() - conv.getNextShowTime()) + "");
        LogUtil.e("消息时间===" + message.getServerTimes() + "");
        LogUtil.e("会话时间===" + conv.getNextShowTime() + "");*/

    }

    /**
     * 最后收到消息的会话排在最上面
     *
     * @param message
     */
    private void showConversation(SocketMessage message) {
        ImLogic.getInstance().hideSessionWithStatus(((BaseActivity) mContext), message.getConversationId(),
                message.getChatType(), 0, new ProgressSubscriber<BaseBean>((BaseActivity) mContext, false) {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {

                    }
                });
    }

    /**
     * 请求会话为显示
     *
     * @param message
     */
    private void setConvVisibile(SocketMessage message) {
        ImLogic.getInstance().hideSessionWithStatus(((BaseActivity) mContext), message.getConversationId(),
                message.getChatType(), 0, new ProgressSubscriber<BaseBean>((BaseActivity) mContext, false) {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {

                    }
                });
    }

    /**
     * 隐藏会话请求
     *
     * @param conversationId
     */
    private void setConvVisibility(Long conversationId, boolean visibile) {
        ImLogic.getInstance().hideSessionWithStatus(((BaseActivity) mContext), conversationId,
                MsgConstant.SELF_DEFINED, visibile ? 0 : 1, new ProgressSubscriber<BaseBean>((BaseActivity) mContext, false) {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {

                    }
                });
    }

    /**
     * 更新免打扰
     *
     * @param conversationId
     * @param state
     */
    public static void updateConversation(long conversationId, int state) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == conversationId) {
                netConversationList.get(i).setNoBother(state);
                mListAdapter.notifyDataSetChanged();
            }

        }

    }

    /**
     * 更新草稿
     *
     * @param conversationId
     * @param draft
     */
    public static void updateConversationDraft(long conversationId, String draft) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == conversationId) {
                netConversationList.get(i).setDraft(draft);
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                mListAdapter.notifyDataSetChanged();
            }

        }

    }

    /**
     * 清空未读数
     *
     * @param conversationId
     */
    public static void cleanConversationUnreadNum(long conversationId) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == conversationId) {
                netConversationList.get(i).setUnreadMsgCount(0);
                netConversationList.get(i).setLastMessageState(MsgConstant.NO_AT_MSG);
                mListAdapter.notifyItemChanged(i);
            }
        }
    }

    /**
     * 退出群聊
     *
     * @param conversationId
     */
    public static void quitGroupChat(long conversationId) {
        Iterator<Conversation> it = netConversationList.iterator();
        while (it.hasNext()) {
            if (it.next().getConversationId() == conversationId) {
                it.remove();
            }
        }
        EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));

    }

    /**
     * 清除草稿
     *
     * @param object
     */
    public void cleanDraft(long object) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == object) {
                netConversationList.get(i).setDraft("");
            }

        }

    }

    /**
     * 清空列表数据(切换公司或退出登录时清除内存缓存)
     */
    public void cleanData() {
        netConversationList.clear();
        mListAdapter.notifyDataSetChanged();

    }

    /**
     * 更新免打扰
     *
     * @param state
     * @param id
     */
    public void updateNotifyState(int state, long id) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == id) {
                netConversationList.get(i).setNoBother(state);
                mListAdapter.notifyDataSetChanged();
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
            }

        }


    }

    public void updateTopState(int state, long id) {

        boolean flag = true;
        int position = 0;
        Conversation conversation = null;
        Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            conversation = iterator.next();
            if (conversation.getConversationId() == id) {
                iterator.remove();
                conversation.setTopStatus(1);
                DBManager.getInstance().saveOrReplace(conversation);
            }
        }
        if (conversation != null) {
            for (int i = 0; i < netConversationList.size(); i++) {
                if (netConversationList.get(i).getTopStatus() == 0) {
                    position = i;
                }
            }
            netConversationList.add(position, conversation);
        }
    }

    /**
     * 获取缓存的数据
     *
     * @param conversationId
     * @return
     */
    public static ArrayList<SocketMessage> getMessageFromCache(Long conversationId) {
        if (msgMap.get(conversationId) == null || msgMap.get(conversationId).size() <= 0) {
            return new ArrayList<SocketMessage>();
        }
        return msgMap.get(conversationId);

    }

    private boolean mRequestFlag = true;

    /**
     * //显示推送提醒
     *
     * @param message
     */
    public void receiveMessage(PushMessage message) {
        this.mPushmessage = message;
        if (MsgConstant.TYPE_MEMO.equals(message.getType())) {
            getConversationList();
        }

        if (message == null) {
            return;
        } else {
            mRequestFlag = true;
        }
        long assistanId = 0;
        try {
            assistanId = TextUtil.parseLong(message.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            getConversationList();
            mRequestFlag = false;
            mPushmessage = null;
            return;
        }
        String groupId = message.getGroup_id();

        //删除会话列表中的会话
        if (MsgConstant.TYPE_GROUP_OPERATION.equals(message.getType())) {
            if (!TextUtils.isEmpty(groupId)) {
                Iterator<Conversation> iterator = netConversationList.iterator();
                while (iterator.hasNext()) {
                    if (groupId.equals(iterator.next().getConversationId() + "")) {
                        iterator.remove();
                        mListAdapter.notifyDataSetChanged();

                        mPushmessage = null;
                        return;
                    }
                }
                updateUnreadNum();
            }
        }
        //更改小助手中的信息
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == assistanId
                    && netConversationList.get(i).getConversationType() == MsgConstant.SELF_DEFINED
                    ) {
                mRequestFlag = false;
                mPushmessage = null;
                netConversationList.get(i).setUnreadMsgCount(netConversationList.get(i).getUnreadMsgCount() + 1);
                netConversationList.get(i).setLastMsgDate(TextUtil.parseLong(message.getCreate_time()));
                netConversationList.get(i).setLatestMessage(message.getPush_content());
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                putConvOnTop(assistanId);
                mListAdapter.notifyDataSetChanged();
                updateUnreadNum();
                if (netConversationList.get(i).getNoBother() == 1) {
                    mPushmessage = null;
                    mRequestFlag = false;
                    return;
                }
                break;
            }
        }
        requestCount = requestCount++;
        if (requestCount > 2) {
            mPushmessage = null;
            requestCount = 0;
            mRequestFlag = false;
            return;
        }
        //不在列表
        if (mRequestFlag) {
            BaseActivity ac = ((BaseActivity) mContext);
            ImLogic.getInstance().hideSessionWithStatus(ac,
                    TextUtil.parseLong(message.getAssistant_id()),
                    MsgConstant.SELF_DEFINED,
                    0,
                    new ProgressSubscriber<BaseBean>(ac, false) {
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            // getConversationList();
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            if (requestCount > 2) {
                                mPushmessage = null;
                                requestCount = 0;
                                mRequestFlag = false;
                                return;
                            } else {
                                getConversationList();
                                mRequestFlag = false;
                                mPushmessage = null;
                                requestCount = 0;
                            }
                        }
                    });
            return;
        }
        mPushmessage = null;

        showPushNotification(message);
    }

    /**
     * 显示推送消息
     *
     * @param message
     */
    private void showPushNotification(PushMessage message) {
        Intent intent = new Intent(mContext, AppAssistantActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == TextUtil.parseLong(message.getAssistant_id()) + MsgConstant.DEFAULT_VALUE) {
                intent.putExtra(MsgConstant.CONV_TITLE, netConversationList.get(i).getTitle());
                //是否查看已读,借用peoples字段使用
                intent.putExtra(MsgConstant.VIEW_READED, netConversationList.get(i).getPeoples());
                intent.putExtra(MsgConstant.APPLICATION_ID, netConversationList.get(i).getApplicationId() + "");
                intent.putExtra(Constants.DATA_TAG3, netConversationList.get(i).getIcon_type());
                intent.putExtra(Constants.DATA_TAG4, netConversationList.get(i).getIcon_color());
                intent.putExtra(Constants.DATA_TAG5, netConversationList.get(i).getIcon_url());
            }
        }
        intent.putExtra(MsgConstant.CONVERSATION_ID, message.getAssistant_id());
        intent.putExtra(MsgConstant.BEAN_NAME, message.getBean_name());
        intent.putExtra(Constants.DATA_TAG1, message.getType());
        intent.putExtra(Constants.DATA_TAG2, TextUtil.parseLong(message.getCreate_time()));


        PendingIntent pi = PendingIntent.getActivity(mContext,
                Constants.REQUEST_CODE1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        String PUSH_TITLE = message.getBean_name_chinese();

        String content = message.getPush_content();
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(content) && "3".equals(message.getType()) && message.getField_info() != null) {
            for (int i = 0; i < message.getField_info().size(); i++) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(message.getField_info().get(i).getField_label());
                sb.append(":");
                sb.append(message.getField_info().get(i).getField_value());
            }
            content = sb.toString();
        } else {
            content = "";
        }

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(mContext, MsgConstant.PUSH_CHANNEL)
                    .setContentTitle(PUSH_TITLE)
                    .setContentText(message.getPush_content())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    .setAutoCancel(true)
                    .build();

        } else {
            notification = new Notification.Builder(mContext)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker(Constants.APP_NAME)
                    /**点击跳转**/
                    .setContentIntent(pi)
                    /**设置通知的标题**/
                    .setContentTitle(PUSH_TITLE)
                    /**设置通知的内容**/
                    .setContentText(message.getPush_content())
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(false)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：Notification.DEFAULT_ALL就是3种全部提醒**/
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(PendingIntent.getActivity(mContext, MsgConstant.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
        }

        if (MobileBrand.XIAOMI.equals(Build.BRAND)) {
            BadgeUtil.setBadgeOfXiaomi(mContext, getTotalUnreadNum(), notification);
        } else {
            mNotificationManager.notify(MsgConstant.PUSH_CHANNEL, 2, notification);
        }
        notifyTagList.add(message.getAssistant_id());

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotificationManager.createNotificationChannel(channel);
    }

    /**
     * 清除通知栏提醒
     */
    public static void cleanNotify() {
        if (mNotificationManager != null) {
            for (int i = 0; i < notifyTagList.size(); i++) {
                try {
                    mNotificationManager.cancel(notifyTagList.get(i), 2);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 小助手数据未读数-1
     *
     * @param object
     */
    public void readAssistantMessage(String object) {
        for (int i = 0; i < netConversationList.size(); i++) {
            if (netConversationList.get(i).getConversationId() == TextUtil.parseLong(object) + MsgConstant.DEFAULT_VALUE) {
                netConversationList.get(i).setUnreadMsgCount(netConversationList.get(i).getUnreadMsgCount() > 0 ? netConversationList.get(i).getUnreadMsgCount() - 1 : 0);
                mListAdapter.notifyDataSetChanged();
            }
        }
        updateUnreadNum();
    }

    /**
     * 解散或退出群
     *
     * @param conversationId
     */
    public void quitOrReleaseGroup(long conversationId) {
        Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversationId == conversation.getConversationId()) {
                iterator.remove();
                mListAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 获取所有未读消息数
     *
     * @return
     */
    public int getTotalUnreadNum() {
        int num = 0;
        for (int i = 0; i < netConversationList.size(); i++) {
            num = num + netConversationList.get(i).getUnreadMsgCount();
        }
        return num;
    }


    /**
     * 回复已读ack
     *
     * @param type
     * @param conversationId
     */
    public void markConvAllReaded(int type, long conversationId) {

        for (int i = 0; i < localConversationList.size(); i++) {
            Conversation c = localConversationList.get(i);
            if (c.getConversationId() == conversationId) {
                c.setUnreadMsgCount(0);
                DBManager.getInstance().saveOrReplace(c);
            }
        }
        List<SocketMessage> messages = DBManager.getInstance().qureyMessageByConversationId(SPHelper.getCompanyId(), conversationId);
        if (type == MsgConstant.SINGLE) {
            boolean flag = true;
            for (int i = 0; i < messages.size(); i++) {
                SocketMessage m = messages.get(i);
                if (m.getUcFlag() == 1 && !m.isRead()) {
                    m.setIsRead(true);
                    m.setRead(true);
                    DBManager.getInstance().saveOrReplace(m);
                    if (flag) {
                        IM.getInstance().sendReadAck(m, MsgConstant.IM_PERSONSL_RESPONSE_READ_CMD);
                        flag = false;
                    }
                }

            }
        } else if (type == MsgConstant.GROUP) {
            for (int i = 0; i < messages.size(); i++) {
                SocketMessage m = messages.get(i);
                if (m.getUcFlag() == 1 && !m.getIsRead()) {
                    m.setIsRead(true);
                    m.setRead(true);
                    IM.getInstance().sendReadAck(m, MsgConstant.IM_TEAM_RESPONSE_READ_CMD);
                    DBManager.getInstance().saveOrReplace(m);
                }

            }

        }
        updateUnreadNum();
    }

    /**
     * 更改会话/小助手名字
     *
     * @param assistant_id
     * @param assistant_name
     */
    public void changeConversationName(String assistant_id, String assistant_name) {
        long convId = TextUtil.parseLong(assistant_id);
        for (int i = 0; i < netConversationList.size(); i++) {
            if (convId == netConversationList.get(i).getConversationId()) {
                netConversationList.get(i).setTitle(assistant_name);
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                mListAdapter.notifyDataSetChanged();
                break;
            }
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (convId == mDatas.get(i).getConversationId()) {
                mDatas.get(i).setTitle(assistant_name);
                break;
            }
        }


    }

    /**
     * 从列表移除会话
     *
     * @param conversationId
     */
    public void removeConversation(long conversationId) {
        Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversationId == conversation.getConversationId()) {
                iterator.remove();
                mListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 置顶状态改变
     *
     * @param assistantId
     */
    public void topStateChange(String assistantId) {
        long assistanId = TextUtil.parseLong(assistantId) + MsgConstant.DEFAULT_VALUE;
        boolean inTop = false;
        Conversation c = null;
        List<Conversation> topList = new ArrayList<>();
        List<Conversation> normalList = new ArrayList<>();
        for (int i = 0; i < netConversationList.size(); i++) {
            if ("1".equals(netConversationList.get(i).getTopStatus())) {
                topList.add(netConversationList.get(i));
            } else if ("0".equals(netConversationList.get(i).getTopStatus())) {
                normalList.add(netConversationList.get(i));
            }
        }
        Iterator<Conversation> iterator1 = topList.iterator();
        while (iterator1.hasNext()) {
            Conversation next = iterator1.next();
            if (next.getConversationId() == assistanId) {
                inTop = true;
                c = next;
                iterator1.remove();
            }
        }
        if (!inTop) {
            Iterator<Conversation> iterator2 = normalList.iterator();
            while (iterator2.hasNext()) {
                Conversation next = iterator2.next();
                if (next.getConversationId() == assistanId) {
                    inTop = false;
                    c = next;
                    iterator2.remove();
                }
            }
        }
        if (c != null) {
            if (inTop) {
                c.setTopStatus(0);
                normalList.add(c);
                Collections.sort(normalList, new Comparator<Conversation>() {
                    @Override
                    public int compare(Conversation o1, Conversation o2) {
                        return o1.getLastMsgDate() < o2.getLastMsgDate() ? 1 : -1;
                    }
                });
            } else {
                c.setTopStatus(1);
                topList.add(0, c);
            }
            netConversationList.clear();
            netConversationList.addAll(topList);
            netConversationList.addAll(normalList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 免打扰状态改变
     *
     * @param assistantId
     */
    public void notifyStateChange(String assistantId) {
        long assistanId = TextUtil.parseLong(assistantId) + MsgConstant.DEFAULT_VALUE;
        for (int i = 0; i < netConversationList.size(); i++) {
            if (assistanId == netConversationList.get(i).getConversationId()) {
                int noBother = netConversationList.get(i).getNoBother();
                if (0 == noBother) {
                    netConversationList.get(i).setNoBother(1);
                } else if (1 == noBother) {
                    netConversationList.get(i).setNoBother(0);
                }
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                mListAdapter.notifyDataSetChanged();
            }


        }
    }

    /**
     * 只查看未读状态变更
     *
     * @param assistantId
     */
    public void viewStateChange(String state, String assistantId) {
        long assistanId = TextUtil.parseLong(assistantId) + MsgConstant.DEFAULT_VALUE;
        for (int i = 0; i < netConversationList.size(); i++) {
            if (assistanId == netConversationList.get(i).getConversationId()) {
                //只查看未读状态
                netConversationList.get(i).setPeoples(state);
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                mListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 标记小助手中一条数据为已读
     *
     * @param conversationId 会话id(转换后的小助手id)
     * @param data_id        消息id
     */
    public void markOneMessageRead(long conversationId, String data_id) {
        boolean b = DBManager.getInstance().markOnePushMessageRead(data_id);
        if (b) {
            EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.TYPE_MARK_ONE_ITEM_READ, data_id));
            for (int i = 0; i < netConversationList.size(); i++) {
                if (conversationId == netConversationList.get(i).getConversationId()) {
                    int unreadMsgCount = netConversationList.get(i).getUnreadMsgCount() - 1;
                    if (unreadMsgCount >= 0) {
                        netConversationList.get(i).setUnreadMsgCount(unreadMsgCount);
                    }
                    DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                    mListAdapter.notifyDataSetChanged();
                    updateUnreadNum();
                }
            }
        }
    }

    /**
     * 拉取离线消息后更新会话列表
     *
     * @param message
     */
    public void updateConversationMessage(SocketMessage message) {
        long convId = message.getConversationId();
        List<SocketMessage> list = DBManager.getInstance().qureyMessageByConversationId(IM.getInstance().getCompanyId(), convId);
        SocketMessage m = message;
        if (list.size() > 0) {
            m = list.get(list.size() - 1);
        }
        for (int i = 0; i < netConversationList.size(); i++) {
            if (convId == netConversationList.get(i).getConversationId() && netConversationList.get(i).getLastMsgDate() <= 0) {
                netConversationList.get(i).setLastMsgDate(m.getServerTimes());
                String lastMessage = "[无消息]";
                switch (m.getMsgType()) {
                    case MsgConstant.FILE:
                        lastMessage = m.getSenderName() + ":[文件]";
                        break;
                    case MsgConstant.VOICE:
                        lastMessage = m.getSenderName() + ":[语音]";
                        break;
                    case MsgConstant.VIDEO:
                        lastMessage = m.getSenderName() + ":[视频]";
                        break;
                    case MsgConstant.IMAGE:
                        lastMessage = m.getSenderName() + ":[图片]";
                        break;
                    case MsgConstant.NOTIFICATION:
                        lastMessage = m.getMsgContent();
                        break;
                    case MsgConstant.LOCATION:
                        lastMessage = m.getSenderName() + ":[位置]";
                        break;
                    case MsgConstant.TEXT:
                        lastMessage = m.getSenderName() + ":" + m.getMsgContent();
                        break;
                    default:

                        break;
                }
                netConversationList.get(i).setLastMessageType(m.getMsgType());
                netConversationList.get(i).setLatestMessage(lastMessage);
                DBManager.getInstance().updateConversation(netConversationList.get(i));
                break;

            }

        }
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * 更新之前的推送状态(审批相关)
     *
     * @param object
     */
    public void updatePushMessage(PushMessage object) {
        DBManager.getInstance().updateOldPushMessage(object);

    }

    /**
     * 应用被删除
     *
     * @param pushMessage
     */
    public void deleteAppAssistant(PushMessage pushMessage) {
        long id = TextUtil.parseLong(pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        final Iterator<Conversation> iterator = netConversationList.iterator();
        while (iterator.hasNext()) {
            final Conversation next = iterator.next();
            if (id == next.getConversationId()) {
                DBManager.getInstance().deletePushMessageByAssistantId(id + "");
                iterator.remove();
                mListAdapter.notifyDataSetChanged();
                break;
            }
        }


    }

    /**
     * 应用更新
     *
     * @param pushMessage
     */
    public void updateAppAssistant(PushMessage pushMessage) {
        long id = TextUtil.parseLong(pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        for (int i = 0; i < netConversationList.size(); i++) {
            if (id == netConversationList.get(i).getConversationId()) {
                netConversationList.get(i).setIcon_color(pushMessage.getIcon_color());
                netConversationList.get(i).setIcon_url(pushMessage.getIcon_url());
                netConversationList.get(i).setIcon_type(pushMessage.getIcon_type());
                netConversationList.get(i).setIcon_color(pushMessage.getIcon_color());
                netConversationList.get(i).setTitle(pushMessage.getTitle());
                DBManager.getInstance().saveOrReplace(netConversationList.get(i));
                mListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 助手名更新
     *
     * @param pushMessage
     */
    public void updateAppAssistantName(PushMessage pushMessage) {
        long id = TextUtil.parseLong(pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        DBManager.getInstance().updateAssistantName(id, pushMessage);
    }

    /**
     * 同步消息
     */
    public void updateLocalData() {
        EventBusUtils.sendEvent(new ImMessage(netConversationList.size(), MsgConstant.UPDATE_MAX_PROGRESS_TAG, null));
        for (int i = 0; i < netConversationList.size(); i++) {
            final Conversation conversation = netConversationList.get(i);
            int type = -1;
            long id = -1L;
            if (conversation.getConversationType() == MsgConstant.GROUP) {
                type = 2;
                id = conversation.getConversationId();
            } else if (conversation.getConversationType() == MsgConstant.SINGLE) {
                type = 1;
                id = TextUtil.parseLong(conversation.getReceiverId());
            }
            if (type > 0 && id > 0) {
                mConvListView.postDelayed(new PullRunnable(type, id, i), i * 50);
            } else {
                mConvListView.postDelayed(new PullRunnable(type, id, i), i * 50);
            }
        }
    }

    /**
     * 小助手状态变更
     * 0隐藏,1显示
     *
     * @param message
     */
    public void assistantStateChange(PushMessage message) {
        if (message != null) {
            if ("0".equals(message.getStyle())) {
                removeAssistant(message);
            } else if ("1".equals(message.getStyle())) {
                showAssistant(message);
            }
        }

    }

    /**
     * 移除助手并清除数据
     *
     * @param pushMessage
     */
    public void removeAssistant(PushMessage pushMessage) {
        long id = TextUtil.parseLong(pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        //从列表移除并请求为隐藏
        removeConversation(id);
        setConvVisibility(TextUtil.parseLong(pushMessage.getAssistant_id()), false);
        //清除助手及消息
        DBManager.getInstance().deleteAssistant(pushMessage.getAssistant_id());
        DBManager.getInstance().deletePushMessageByAssistantId(pushMessage.getAssistant_id());
    }

    /**
     * 显示助手
     *
     * @param pushMessage
     */
    public void showAssistant(PushMessage pushMessage) {
        long id = TextUtil.parseLong(pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        ImLogic.getInstance().hideSessionWithStatus(((BaseActivity) mContext), TextUtil.parseLong(pushMessage.getAssistant_id()),
                MsgConstant.SELF_DEFINED, 0, new ProgressSubscriber<BaseBean>((BaseActivity) mContext, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        getConversationList();
                    }
                });
    }

    /**
     * 初始化本地消息
     */
    public class PullRunnable implements Runnable {
        int type;
        long id;
        int index;

        public PullRunnable(int type, long id, int index) {
            this.type = type;
            this.id = id;
            this.index = index;
        }

        @Override
        public void run() {
            if (type > 0 && id > 0) {
                // IM.getInstance().sendPullHistoryMessage(type, id, IM.getInstance().getServerTime());
                IM.getInstance().sendPullHistoryMessage(type, id, 0L, 1);
                // Log.e("拉取历史聊天数据", id + "");
                // Log.e("index=" + index, "       id=" + netConversationList.size() + "");
                // Log.e("id=" + id, "       id=" + netConversationList.get(netConversationList.size() - 1).getConversationId() + "");
            }
            if (index == netConversationList.size() - 1) {
                mConvListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getConversationList();
                    }
                }, 1500);
            }
            EventBusUtils.sendEvent(new ImMessage(index + 1, MsgConstant.UPDATE_PULL_MESSAGE_PROGRESS_TAG, null));

        }
    }
}