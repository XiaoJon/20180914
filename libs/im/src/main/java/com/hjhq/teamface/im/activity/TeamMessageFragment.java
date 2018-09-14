package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ToolMenu;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.load.LVCircularRing;
import com.hjhq.teamface.common.activity.CaptureActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.im.db.DBManager;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 企信主界面
 *
 * @author ked 2017年11月28日修改
 */

public class TeamMessageFragment extends BaseFragment {
    RecyclerView conversationRv;
    RelativeLayout searchLayout;
    TextView searchEditText;
    RelativeLayout mRlScan;
    RelativeLayout mRlContacts;
    RelativeLayout mRlMenu;
    RelativeLayout mRlProgress;
    LinearLayout mLlRoot;
    ProgressBar mProgressBar;
    LVCircularRing mLVCircularRing;
    TextView mTvProgress;
    View rlToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View mErrorView;
    private HandlerThread mThread;
    private static final int REFRESH_CONVERSATION_LIST = 0x3000;
    //消息列表控制器
    private static ConversationListController mConvListController;

    /**
     * 菜单条目列表
     */
    private List<ToolMenu> list = new ArrayList<>();

    private Member member;

    @Override
    protected int getContentView() {
        return R.layout.fragment_team_message;
    }

    @Override
    protected void initView(View view) {

        conversationRv = getActivity().findViewById(R.id.conversation_rv);
        searchLayout = getActivity().findViewById(R.id.search_rl);
        searchEditText = getActivity().findViewById(R.id.search_edit_text);
        mRlScan = getActivity().findViewById(R.id.rl1);
        mRlContacts = getActivity().findViewById(R.id.rl2);
        mProgressBar = getActivity().findViewById(R.id.progress_bar);
        mRlProgress = getActivity().findViewById(R.id.rl_progress);
        mLVCircularRing = getActivity().findViewById(R.id.lvcr);
        mRlMenu = getActivity().findViewById(R.id.rl3);
        mLlRoot = getActivity().findViewById(R.id.ll_root);
        mTvProgress = getActivity().findViewById(R.id.tv_progress);
        rlToolbar = getActivity().findViewById(R.id.rl_toolbar);
        mSwipeRefreshLayout = getActivity().findViewById(R.id.refresh_layout);
        mErrorView = getActivity().findViewById(R.id.net_error_notify);
        //撑起状态栏
        mRlProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        rlToolbar.setPadding(0, statusBarHeight, 0, 0);
        initLogin();
        initListView();
        mRlProgress.setVisibility(View.GONE);
        mLlRoot.setVisibility(View.VISIBLE);

    }

    private void initLogin() {

    }

    @Override
    protected void initData() {
        String[] menuString = getActivity().getResources().getStringArray(R.array.im_menu_array_v2);
        ToolMenu menu1 = new ToolMenu(1, menuString[0], R.drawable.icon_add_group);
        ToolMenu menu2 = new ToolMenu(2, menuString[1], R.drawable.icon_scan);
        list.add(menu1);
        list.add(menu2);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (View.VISIBLE == mErrorView.getVisibility()) {
                //尝试重连
                if (Constants.USE_LOAD_LEVELING) {
                    IM.getInstance().getLlServerUrl();
                } else {
                    IM.getInstance().login();
                }
            } else {
                mConvListController.getConversationList();
            }
            mSwipeRefreshLayout.postDelayed(() -> {
                mSwipeRefreshLayout.setRefreshing(false);


            }, 500);
        });
        UserInfoBean bean = SPHelper.getUserInfo(UserInfoBean.class);
        member = new Member();
        member.setName(bean.getData().getEmployeeInfo().getName());
        try {
            member.setId(Long.parseLong(bean.getData().getEmployeeInfo().getId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        member.setEmployee_name(bean.getData().getEmployeeInfo().getName());
        member.setPicture(bean.getData().getEmployeeInfo().getPicture());
        member.setSign_id(bean.getData().getEmployeeInfo().getSign_id());
    }

    /**
     * 初始化recycelerview
     */
    private void initListView() {
        ((DefaultItemAnimator) conversationRv.getItemAnimator()).setSupportsChangeAnimations(false);
        mThread = new HandlerThread("Work on MainActivity");
        mThread.start();
        mConvListController = new ConversationListController(conversationRv, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MsgConstant.showNotificationFlag = false;

        LogUtil.e("TeamFragment   onResume");
        MsgConstant.openedConversationId = -1L;

    }

    @Override
    public void onPause() {
        super.onPause();
        MsgConstant.showNotificationFlag = true;
        LogUtil.e("TeamFragment   onPause");
    }

    @Override
    public void onDestroy() {
        // TODO: 2017/11/20 将状态改为离线(WebSocket连接,不用主动去请求断开)
        super.onDestroy();
        IM.getInstance().stopService(getActivity());
    }

    @Override
    protected void setListener() {
        searchEditText.setText(getActivity().getString(R.string.search));
        setOnClicks(searchEditText, searchLayout, mRlContacts, mRlMenu, mRlScan);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_edit_text || view.getId() == R.id.search_rl) {
            CommonUtil.startActivtiy(getActivity(), SearchChatActivityV2.class);
            getActivity().overridePendingTransition(0, 0);
        } else if (view.getId() == R.id.rl1) {
            //扫码
            CommonUtil.startActivtiy(mContext, CaptureActivity.class);
        } else if (view.getId() == R.id.rl2) {
            //通讯录
            //CommonUtil.startActivtiy(getActivity(), ContactsActivity.class);
            UIRouter.getInstance().openUri(mContext, "DDComp://app/contacts", new Bundle());
        } else if (view.getId() == R.id.rl3) {
            //菜单
            showChatMenu(view);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 退出登录或切换账号时清除上个账号的内存缓存
     */
    public static void clear() {
        if (mConvListController != null) {
            mConvListController.cleanData();
        }
    }

    private void showChatMenu(View view) {
        PopUtils.showMenuPopupWindow(getActivity(), view, list, position -> {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    //发起聊天
                    ArrayList<Member> members = new ArrayList<Member>();
                    Member m = new Member();
                    m.setSelectState(C.HIDE_TO_SELECT);
                    m.setCheck(false);
                    m.setName(IM.getInstance().getName());
                    m.setEmployee_name(IM.getInstance().getName());
                    m.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
                    members.add(m);
                    bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                    bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                    bundle.putString(C.CHOOSE_RANGE_TAG, "0");
                    bundle.putString(Constants.NAME, "选择联系人");
                    bundle.putBoolean(Constants.DATA_TAG1, true);
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/select_member2", bundle, Constants.REQUEST_CODE2);
                    //CommonUtil.startActivtiyForResult(getActivity(),SelectMemberActivity2.class, Constants.REQUEST_CODE2, bundle);
                    break;
                case 1:
                    //扫一扫
                    CommonUtil.startActivtiy(mContext, CaptureActivity.class);
                    /*ArrayList<Member> members = new ArrayList<Member>();
                    Member m = new Member();
                    m.setSelectState(C.HIDE_TO_SELECT);
                    m.setCheck(false);
                    m.setName(IM.getInstance().getName());
                    m.setEmployee_name(IM.getInstance().getName());
                    m.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
                    members.add(m);
                    bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                    bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
                    bundle.putString(C.CHOOSE_RANGE_TAG, "0");
                    bundle.putString(Constants.NAME, "发起私聊");

                    CommonUtil.startActivtiyForResult(getActivity(),
                            SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);*/
                    break;
                case 2:
                    //选择群聊
                    bundle.putString(Constants.DATA_TAG1, MsgConstant.CHOOSE_GROUP_CHAT);
                    CommonUtil.startActivtiy(getActivity(), ChooseGroupChatActivity.class, bundle);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 使用EventBus
     *
     * @return
     */
    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ImMessage result) {
        if (TextUtils.isEmpty(result.getTag())) {
            return;
        }
        PushMessage pushMessage;
        switch (result.getTag()) {
            case MsgConstant.IM_NEED_UPDATE_LOCAL_DATA:
                boolean flag = (boolean) result.getObject();
                if (flag) {
                    updateLocalData();
                } else {
                    mRlProgress.setVisibility(View.GONE);
                    mLlRoot.setVisibility(View.VISIBLE);
                }
                break;
            case MsgConstant.UPDATE_PULL_MESSAGE_PROGRESS_TAG:
                updateProgress(result.getCode());
                break;
            case MsgConstant.UPDATE_MAX_PROGRESS_TAG:
                mProgressBar.setMax(result.getCode());
                mLVCircularRing.startAnim();
                break;
            case MsgConstant.MSG_RESULT:
                //登录成功&收到聊天消息
                if (result.getCode() == MsgConstant.IM_LOG_IN_CMD) {
                    IMState.setImOnlineState(true);
                    mErrorView.setVisibility(View.GONE);
                }
                mConvListController.updateConversationList(MsgConstant.openedConversationId, ((SocketMessage) result.getObject()));
                break;
            case MsgConstant.IM_OFFLINE_TAG:
                //企信离线
                mErrorView.setVisibility(View.VISIBLE);
                break;
            case MsgConstant.RECEIVE_PUSH_MESSAGE:
                //收到推送消息
                mConvListController.receiveMessage(((PushMessage) result.getObject()));
                break;
            case MsgConstant.IM_PULL_HISTORY_MSG_FINISH:
                //拉取历史消息完成
                SocketMessage message2 = (SocketMessage) result.getObject();
                conversationRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mConvListController.updateConversationMessage(message2);
                    }
                }, 1000);

                break;
            case MsgConstant.IM_PULL_OFFLINE_MSG_FINISH:
                //拉取离线消息完成
                mConvListController.getConversationList();
                break;
            case MsgConstant.SHOW_PUSH_MESSAGE:
                //收到推送
                mConvListController.receiveMessage(((PushMessage) result.getObject()));
                break;
            case MsgConstant.TYPE_APPROVE_OPERATION:
                //收到审批操作推送
                mConvListController.updatePushMessage(((PushMessage) result.getObject()));
                break;
            case MsgConstant.RECEIVE_RELEASE_GROUP_PUSH_MESSAGE:
                //解散群
                try {
                    long conversationId = (long) result.getObject();
                    mConvListController.quitOrReleaseGroup(conversationId);
                } catch (Exception e) {

                }
                mConvListController.getConversationList();
                break;
            case MsgConstant.RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE:
                //被移出群
                try {
                    long conversationId = (long) result.getObject();
                    mConvListController.quitOrReleaseGroup(conversationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mConvListController.getConversationList();
                break;
            case MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE:
            case MsgConstant.RECEIVE_GROUP_NAME_CHANGE_PUSH_MESSAGE:
                //收到群成员变更/群名变更
                mConvListController.getConversationList();
                //mConvListController.receiveMessage(((PushMessage) result.getObject()))
                break;
            case MsgConstant.RECEIVE_ASSISTANT_NAME_CHANGE_PUSH_MESSAGE:
                //小助手名变更
                PushMessage message = (PushMessage) result.getObject();
                mConvListController.updateAppAssistantName(message);
                //  mConvListController.changeConversationName(message.getAssistant_id(), message.getAssistant_name());
                break;
            case MsgConstant.TYPE_TOP_OR_NOT_TOP:
                //置顶与取消置顶
                mConvListController.topStateChange((String) result.getObject());
                break;
            case MsgConstant.TYPE_VIEW_READED_MESSAGE:
                //只查看未读消息/查看全部(来自推送)
                mConvListController.viewStateChange(result.getCode() + "", (String) result.getObject());
                break;
            case MsgConstant.TYPE_NOTIFY_OR_NOT_NOTIFY:
                //免打扰与取消免打扰
                mConvListController.notifyStateChange((String) result.getObject());
                break;
            case MsgConstant.TYPE_MARK_ALL_ITEM_READ:
                //标记全部已读
                mConvListController.markAllRead(TextUtil.parseLong((String) result.getObject()) + MsgConstant.DEFAULT_VALUE + MsgConstant.DEFAULT_VALUE);
                break;
            case MsgConstant.TYPE_MARK_ONE_ITEM_READ:
                //标记一条推送为已读
                pushMessage = (PushMessage) result.getObject();
                mConvListController.markOneMessageRead(TextUtil.parseLong((String) pushMessage.getAssistant_id()) + MsgConstant.DEFAULT_VALUE, pushMessage.getData_id());
                break;
            case EventConstant.TYPE_MODULE_UPDATE:
                //模块变更
                pushMessage = (PushMessage) result.getObject();
                mConvListController.updateAppAssistantName(pushMessage);
                break;
            case EventConstant.TYPE_ASSISTANT_VISIBILE_STATE:
                //小助手显示/隐藏状态变更
                pushMessage = (PushMessage) result.getObject();
                mConvListController.assistantStateChange(pushMessage);
                break;
            case EventConstant.TYPE_APPLICATION_UPDATE:
                //应用变更
                pushMessage = (PushMessage) result.getObject();
                mConvListController.updateAppAssistant(pushMessage);
                break;
            case EventConstant.TYPE_APPLICATION_DEL:
                //应用被删除
                pushMessage = (PushMessage) result.getObject();
                mConvListController.deleteAppAssistant(pushMessage);
                break;
            default:
                break;
        }
    }

    /**
     * 软件刚安装或
     */
    private void updateLocalData() {
        showProgress();
        DBManager.getInstance().deleteLocalDataByImID();
        pullMessage();

    }

    /**
     * 拉取初始化消息
     */
    private void pullMessage() {
        mConvListController.updateLocalData();
    }

    /**
     * 显示进度
     */
    private void showProgress() {
        mRlProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 更新初始化进度
     *
     * @param progress
     */
    public void updateProgress(int progress) {
        mProgressBar.setProgress(progress);
        // Log.e("进度值== ", "" + progress);
        TextUtil.setText(mTvProgress, "正在初始化..." + progress * 100 / mProgressBar.getMax() + "%");
        if (mProgressBar.getMax() == progress) {
            mLlRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLVCircularRing.stopAnim();
                    mRlProgress.setVisibility(View.GONE);
                    mLlRoot.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean bean) {
        if (MsgConstant.NOTICE_CONVERSATION_TAG.equals(bean.getTag())) {
            MsgConstant.openedConversationId = (long) bean.getObject();
        }
        if (MsgConstant.UPDATE_NO_BOTHER_TAG.equals(bean.getTag())) {
            mConvListController.getAdapter().notifyDataSetChanged();
            mConvListController.updateNotifyState(bean.getCode(), ((long) bean.getObject()));

        }
        if (MsgConstant.UPDATE_CONVERSATION_TAG.equals(bean.getTag())) {
            mConvListController.getConversationList();

        }
        if (MsgConstant.ASSISTANT_READ_TAG.equals(bean.getTag())) {
            mConvListController.readAssistantMessage(((String) bean.getObject()));

        }
        if (MsgConstant.UPDATE_PUT_TOP_TAG.equals(bean.getTag())) {
            mConvListController.getConversationList();
            //  mConvListController.updateTopState(bean.getCode(), ((long) bean.getObject()));

        }
        if (MsgConstant.CLEAN_DRAFT.equals(bean.getTag())) {
            mConvListController.getAdapter().notifyDataSetChanged();
            mConvListController.cleanDraft(((long) bean.getObject()));
        }
        if (MsgConstant.MARK_ALL_READ.equals(bean.getTag())) {

            if (bean.getCode() == 1) {
                //标记会话消息已读
                mConvListController.markAllRead(((long) bean.getObject()));
            }

        }
        if (MsgConstant.QUIT_OR_RELEASE_GROUP.equals(bean.getTag())) {

            mConvListController.getConversationList();
            mConvListController.removeConversation(((long) bean.getObject()));
        }
        if (MsgConstant.GROUP_NAME_CHANGED_TAG.equals(bean.getTag())) {
            mConvListController.getConversationList();
        }
        if (MsgConstant.IM_MARK_CONV_ALL_READ.equals(bean.getTag())) {
            //标记全部已读(聊天类型,非小助手)
            mConvListController.markConvAllReaded(bean.getCode(), ((long) bean.getObject()));
        }
        if (MsgConstant.VIEW_READED.equals(bean.getTag())) {
            //只查看未读消息/查看全部(来自客户端设置)
            mConvListController.viewStateChange(bean.getCode() + "", ((String) bean.getObject()));
        }
        if (MsgConstant.UPDATE_UNREAD_MSG_NUM.equals(bean.getTag())) {
            //更新未读消息数
            mConvListController.updateUnreadNum();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            MsgConstant.openedConversationId = -1L;
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE2) {

            List<Member> memberList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (memberList == null || memberList.size() <= 0) {
                return;
            } else {
                ImLogic.getInstance().saveRecentContact(memberList.get(0));
            }
            if (memberList.size() == 1) {
                ImLogic.getInstance().addSingleChat(((BaseActivity) getActivity()),
                        memberList.get(0).getSign_id() + "",
                        new ProgressSubscriber<AddSingleChatResponseBean>(getActivity(), false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(AddSingleChatResponseBean bean) {
                                super.onNext(bean);

                                Bundle bundle = new Bundle();
                                Conversation conversation = new Conversation();
                                conversation.setCompanyId(bean.getData().getId());
                                conversation.setOneselfIMID(SPHelper.getUserId());
                                conversation.setReceiverId(bean.getData().getReceiver_id());
                                conversation.setConversationType(MsgConstant.SINGLE);
                                conversation.setTitle(bean.getData().getEmployee_name());
                                conversation.setSenderAvatarUrl(bean.getData().getPicture());
                                try {
                                    conversation.setIsHide(Integer.parseInt(bean.getData().getIs_hide()));
                                } catch (NumberFormatException e) {
                                    conversation.setIsHide(0);
                                }
                                bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                                bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getReceiver_id());
                                bundle.putLong(MsgConstant.CONVERSATION_ID, TextUtil.parseLong(bean.getData().getId()));
                                bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getEmployee_name());
                                bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getChat_type());
                                CommonUtil.startActivtiy(getActivity(), ChatActivity.class, bundle);
                            }
                        });
            } else if (memberList.size() >= 2) {
                memberList.add(member);
                AddGroupChatReqBean bean = new AddGroupChatReqBean();
                StringBuilder idSb = new StringBuilder();
                StringBuilder nameSb = new StringBuilder();
                Iterator<Member> iterator = memberList.iterator();
                while (iterator.hasNext()) {
                    Member member = iterator.next();
                    if (SPHelper.getUserId().equals(member.getSign_id())) {
                        iterator.remove();
                    } else {
                        idSb.append(member.getSign_id() + ",");
                        nameSb.append(member.getName() + ",");
                    }
                }

                String peoples = idSb.toString();
                if (peoples.endsWith(",")) {
                    peoples = peoples.substring(0, peoples.length() - 1);
                }
                String groupName = nameSb.toString();
                if (TextUtils.isEmpty(groupName) && groupName.length() > 12) {
                    nameSb.toString().substring(0, 12);
                }
                bean.setName(groupName);
                bean.setPeoples(peoples);
                bean.setType(MsgConstant.GROUP + "");
                bean.setPrincipal_name(member.getName());
                ImLogic.getInstance().addGroupChat(((BaseActivity) getActivity()), bean, new ProgressSubscriber<AddGroupChatResponseBean>(getActivity()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(AddGroupChatResponseBean bean) {
                        super.onNext(bean);
                        Bundle bundle = new Bundle();
                        Conversation conversation = new Conversation();
                        conversation.setCompanyId(SPHelper.getCompanyId());
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < bean.getData().getEmployeeInfo().size(); i++) {
                            if (i == bean.getData().getEmployeeInfo().size() - 1) {
                                sb.append(bean.getData().getEmployeeInfo().get(i).getSign_id() + "");
                            } else {

                                sb.append(bean.getData().getEmployeeInfo().get(i).getSign_id() + ",");
                            }

                        }
                        conversation.setPeoples(sb.toString());
                        conversation.setOneselfIMID(SPHelper.getUserId());
                        conversation.setIsHide(Integer.parseInt(bean.getData().getGroupInfo().getIs_hide()));
                        conversation.setReceiverId(bean.getData().getGroupInfo().getId());
                        conversation.setConversationId(TextUtil.parseLong(bean.getData().getGroupInfo().getId()));
                        conversation.setConversationType(bean.getData().getGroupInfo().getChat_type());
                        conversation.setTitle(bean.getData().getGroupInfo().getName());
                        conversation.setPrincipal(TextUtil.parseLong(SPHelper.getUserId()));
                        conversation.setGroupType(2);
                        // TODO: 2017/12/27 群聊无头像
                        // conversation.setAvatarUrl(bean.getData().getGroupInfo().getPrincipal());
                        bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                        bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getGroupInfo().getId());
                        try {
                            bundle.putLong(MsgConstant.CONVERSATION_ID, Long.parseLong(bean.getData().getGroupInfo().getId()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getGroupInfo().getName());
                        bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getGroupInfo().getChat_type());
                        bundle.putInt(MsgConstant.GROUP_TYPE, MsgConstant.GROUP_TYPE_NORMAL);
                        bundle.putBoolean(MsgConstant.IS_CREATOR, true);
                        DBManager.getInstance().saveOrReplace(conversation);
                        CommonUtil.startActivtiy(getContext(), ChatActivity.class, bundle);
                        if (bean.getData().getEmployeeInfo().size() > 0 && !TextUtils.isEmpty(nameSb)) {
                            IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),
                                    String.format(getString(R.string.im_welcome_notification1), nameSb.toString().substring(0, nameSb.length() - 1))
                            );
                        } else {
                            IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),
                                    getString(R.string.im_welcome_notification2)
                            );
                        }
                    }
                });
            }


        }
    }
}
