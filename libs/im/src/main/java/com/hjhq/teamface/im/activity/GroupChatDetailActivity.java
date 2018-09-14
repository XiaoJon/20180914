package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.activity.EditActivity;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.HelperItemView;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.im.adapter.GroupChatMemberAdapter;
import com.hjhq.teamface.im.adapter.GroupMemberGridAdapter;
import com.hjhq.teamface.im.chat.GroupChatDetailView;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;


public class GroupChatDetailActivity extends BaseTitleActivity {
    private static final String TAG = "GroupChatDetailActivity";

    private GroupChatDetailView mChatDetailView;
    private HelperItemView mTopRl;
    private HelperItemView mNoDisturbRl;
    private RecyclerView mGridView;
    private RelativeLayout mPrincipleRl;
    private RelativeLayout mNoticeRl;
    private RelativeLayout mGroupNameRl;
    private RelativeLayout mViewFileRl;
    private RelativeLayout mTransferGroupRl;
    private Button mQuitGroup;
    private RelativeLayout mGroupMember;
    private TextView mGroupNameTv;
    private TextView mPrincipleTv;
    private TextView mNoticeTv;
    private GroupChatMemberAdapter mGridAdapter;
    private GroupChatDetailView mGroupChatDetailView;
    private ImageView groupNameArrow;
    private ImageView groupNoticeArrow;
    private ImageView groupPrincupalArrow;
    //private ChatDetailController mChatDetailController;
    public final static String START_FOR_WHICH = "which";
    private final static int GROUP_NAME_REQUEST_CODE = 1;
    private final static int MY_NAME_REQUEST_CODE = 2;
    public static final int EDIT_FRIEND_REQUEST_CODE = 3;
    private Context mContext;
    private long receiverId;
    private boolean dataFlag = false;
    private boolean mIsCreator = false;
    private GroupChatInfoBean mBean;
    private long conversationId;
    private ArrayList<Member> mUserInfoList = new ArrayList<>();
    //GridView的数据源
    private ArrayList<Member> mMemberInfoList = new ArrayList<Member>();
    private ArrayList<Member> mAllMemberList = new ArrayList<Member>();
    private List<Member> mListBeen = new ArrayList<>();
    private ArrayList<Member> notAllMember = new ArrayList<Member>();
    // 当前GridView群成员项数
    private int mCurrentNum;
    private String quitOrDismiss = "";
    private int groupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected int getChildView() {
        return R.layout.activity_group_chat_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        receiverId = TextUtil.parseLong(getIntent().getStringExtra(MsgConstant.RECEIVER_ID));
        conversationId = receiverId;
        mContext = this;
        mGroupChatDetailView = (GroupChatDetailView) findViewById(R.id.chat_detail_view);
        mTopRl = (HelperItemView) findViewById(R.id.top_rl);
        mNoDisturbRl = (HelperItemView) findViewById(R.id.no_disturb_rl);
        mGridView = (RecyclerView) findViewById(R.id.chat_detail_group_gv);//群成员
        mPrincipleRl = (RelativeLayout) findViewById(R.id.group_principle_rl);
        mNoticeRl = (RelativeLayout) findViewById(R.id.group_describe_rl);
        mGroupNameRl = (RelativeLayout) findViewById(R.id.group_name_rl);
        mViewFileRl = (RelativeLayout) findViewById(R.id.rl_view_chat_file);
        mTransferGroupRl = (RelativeLayout) findViewById(R.id.rl_transfer);
        mQuitGroup = (Button) findViewById(R.id.chat_detail_del_group);
        mGroupMember = (RelativeLayout) findViewById(R.id.all_member_rl);
        mGroupNameTv = (TextView) findViewById(R.id.chat_detail_group_name);
        mNoticeTv = (TextView) findViewById(R.id.chat_detail_group_describe_tv);
        mPrincipleTv = (TextView) findViewById(R.id.chat_detail_group_principal_tv);
        groupNameArrow = (ImageView) findViewById(R.id.arrow_iv_group_name);
        groupNoticeArrow = (ImageView) findViewById(R.id.arrow_iv_group_describe);
        groupPrincupalArrow = (ImageView) findViewById(R.id.arrow_iv_group_principal);
        groupPrincupalArrow.setVisibility(View.INVISIBLE);
        mGroupChatDetailView.initModule();
        setActivityTitle("聊天详情");
    }

    private void initAdapter() {
        if (groupType != MsgConstant.GROUP_TYPE_NORMAL) {
            mQuitGroup.setVisibility(View.GONE);

            mGroupNameRl.setClickable(false);
            mNoticeRl.setClickable(false);
            mPrincipleRl.setClickable(false);

            groupNameArrow.setVisibility(View.INVISIBLE);
            groupNoticeArrow.setVisibility(View.INVISIBLE);
            groupPrincupalArrow.setVisibility(View.INVISIBLE);
        } else {
            mQuitGroup.setVisibility(View.VISIBLE);
        }
        if (!mIsCreator) {
            mGroupNameRl.setClickable(false);
            mNoticeRl.setClickable(false);
            groupNameArrow.setVisibility(View.INVISIBLE);
            groupNoticeArrow.setVisibility(View.INVISIBLE);
            groupPrincupalArrow.setVisibility(View.INVISIBLE);
        } else {
            mNoticeRl.setClickable(true);
        }
        if (mIsCreator) {
            quitOrDismiss = getString(R.string.im_dissmiss_group_chat);
        } else {
            quitOrDismiss = getString(R.string.im_quit_group_chat);
        }
        mQuitGroup.setText(quitOrDismiss);
        // 初始化头像
        mGridAdapter = new GroupChatMemberAdapter(mIsCreator, null);
        if (mUserInfoList.size() > GroupMemberGridAdapter.MAX_GRID_ITEM) {
            mCurrentNum = GroupMemberGridAdapter.MAX_GRID_ITEM;
        } else {
            mCurrentNum = mMemberInfoList.size();
        }
        if (mIsCreator && mCurrentNum > GroupMemberGridAdapter.MAX_GRID_ITEM - 2) {
            mCurrentNum = GroupMemberGridAdapter.MAX_GRID_ITEM - 2;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GroupChatDetailActivity.this, 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridView.setLayoutManager(gridLayoutManager);
        if (mGridView.getAdapter() != null) {
            mGridView.swapAdapter(mGridAdapter, true);
        } else {
            mGridView.setAdapter(mGridAdapter);
        }

        if (groupType != MsgConstant.GROUP_TYPE_NORMAL && mIsCreator) {
            setOnClicks(mNoticeRl, mGroupMember, mViewFileRl);
        } else if (groupType != MsgConstant.GROUP_TYPE_NORMAL && !mIsCreator) {
            setOnClicks(mGroupMember, mViewFileRl);
        }
        if (groupType == MsgConstant.GROUP_TYPE_NORMAL && mIsCreator) {
            setOnClicks(mGroupNameRl, mNoticeRl, mPrincipleRl, mGroupMember, mQuitGroup, mViewFileRl);
        } else if (groupType == MsgConstant.GROUP_TYPE_NORMAL && !mIsCreator) {
            setOnClicks(mGroupMember, mViewFileRl, mQuitGroup);
        } else {
            setOnClicks(mGroupMember, mViewFileRl);

        }
    }

    @Override
    protected void setListener() {
        mGroupMember.setOnLongClickListener(v -> {
            if (Constants.IS_DEBUG) {
            }
            chooseMember();
            return true;
        });


        mNoDisturbRl.setSwitchButtonTag(R.string.app_name, R.id.no_disturb_rl);
        mTopRl.setSwitchButtonTag(R.string.app_name, R.id.put_on_top);
        mTopRl.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImLogic.getInstance().setTop(GroupChatDetailActivity.this, receiverId + "",
                        MsgConstant.GROUP,
                        new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                //ToastUtils.showError(mContext, "执行失败!");
                                mTopRl.setSelected(!mTopRl.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));
                                //ToastUtils.showSuccess(mContext, "设置成功");
                            }
                        });
            }
        });

        mGridView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                //添加:先获取全部成员,然后去除现有成员
                //移除:将群管理员去除后的所有成员

                if (notAllMember.get(position).getId() == -1L) {
                    chooseMember();

                } else if (notAllMember.get(position).getId() == -2L) {
                    ArrayList<Member> list2 = new ArrayList<>();
                    for (int i = 0; i < mUserInfoList.size(); i++) {
                        Member m = mUserInfoList.get(i);
                        if (!SPHelper.getUserId().equals(m.getSign_id())) {
                            m.setCheck(false);
                            m.setSelectState(C.FREE_TO_SELECT);
                            list2.add(m);
                        }
                    }
                    bundle.putSerializable(Constants.DATA_TAG1, list2);
                    bundle.putSerializable(Constants.DATA_TAG2, GroupMemberActivity.FLAG_REMOVE);
                    CommonUtil.startActivtiyForResult(GroupChatDetailActivity.this, GroupMemberActivity.class, Constants.REQUEST_CODE4, bundle);
                } else {
                    bundle = new Bundle();
                    bundle.putString(Constants.DATA_TAG3, notAllMember.get(position).getSign_id());
                    //CommonUtil.startActivtiy(mContext, EmployeeInfoActivity.class, bundle);
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle);
                }

            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mNoDisturbRl.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImLogic.getInstance().noBother(GroupChatDetailActivity.this, receiverId + "",
                        MsgConstant.GROUP,
                        new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                //ToastUtils.showToast(mContext, "执行失败!");
                                mNoDisturbRl.setSelected(!mNoDisturbRl.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));
                                ConversationListController.updateConversation(conversationId, mNoDisturbRl.getSelected() ? 1 : 0);
                                // CommonUtil.showToast("设置成功");
                            }
                        });
            }
        });
        mTransferGroupRl.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList<Member> list2 = new ArrayList<>();
            for (int i = 0; i < mUserInfoList.size(); i++) {
                Member m = mUserInfoList.get(i);
                if (!SPHelper.getUserId().equals(m.getSign_id())) {
                    m.setCheck(false);
                    list2.add(m);
                }
            }
            bundle.putSerializable(Constants.DATA_TAG1, list2);
            bundle.putSerializable(Constants.DATA_TAG2, GroupMemberActivity.FLAG_TRANSFER);
            CommonUtil.startActivtiyForResult(GroupChatDetailActivity.this, GroupMemberActivity.class, Constants.REQUEST_CODE5, bundle);
        });
        mNoticeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsCreator && groupType != MsgConstant.GROUP_TYPE_NORMAL) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(EditActivity.KEY_TITLE, "修改群聊公告");
                bundle.putString(EditActivity.KEY_HINT, "请输入标题");
                bundle.putString(EditActivity.KEY_ORIGINAL_TEXT, mNoticeTv.getText().toString().trim());
                bundle.putString(EditActivity.KEY_TAG, "群聊公告");
                bundle.putInt(EditActivity.KEY_MAX_LENGTH, 100);
                CommonUtil.startActivtiyForResult(GroupChatDetailActivity.this, EditActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
    }

    private void chooseMember() {
        Bundle bundle = new Bundle();
        ArrayList<Member> list1 = new ArrayList<>();
        for (int i = 0; i < mUserInfoList.size(); i++) {
            Member member = mUserInfoList.get(i);
            member.setCheck(true);
            member.setSelectState(C.CAN_NOT_SELECT);
            list1.add(member);
        }
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list1);
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(GroupChatDetailActivity.this, SelectMemberActivity.class, Constants.REQUEST_CODE3, bundle);
    }

    @Override
    protected void initData() {

        getNetData(true);

    }

    private void getNetData(boolean flag) {
        if (receiverId == -1L) {
            return;
        }
        ImLogic.getInstance().getGroupDetail(GroupChatDetailActivity.this, receiverId,
                new ProgressSubscriber<GroupChatInfoBean>(this, flag) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        ToastUtils.showToast(GroupChatDetailActivity.this, "获取数据失败!");
                        finish();
                    }

                    @Override
                    public void onNext(GroupChatInfoBean bean) {
                        super.onNext(bean);
                        mBean = bean;
                        mIsCreator = SPHelper.getUserId().equals(bean.getData().getGroupInfo().getPrincipal() + "");
                        //公司总群或普通群
                        groupType = "0".equals(bean.getData().getGroupInfo().getType()) ? 1 : 2;
                        initAdapter();
                        mUserInfoList.clear();
                        for (int i = 0; i < bean.getData().getEmployeeInfo().size(); i++) {
                            Member member = new Member();
                            member.setName(bean.getData().getEmployeeInfo().get(i).getEmployee_name());
                            member.setEmployee_name(bean.getData().getEmployeeInfo().get(i).getEmployee_name());
                            try {
                                member.setSign_id(Long.parseLong(bean.getData().getEmployeeInfo().get(i).getSign_id()));
                            } catch (NumberFormatException e) {
                            }
                            member.setPicture(bean.getData().getEmployeeInfo().get(i).getPicture());
                            member.setPost_id(bean.getData().getEmployeeInfo().get(i).getPost_id());
                            member.setId(bean.getData().getEmployeeInfo().get(i).getId());
                            //member.setDepartmentName(bean.getData().getEmployeeInfo().get(i).getPhone());
                            mUserInfoList.add(member);
                        }
                        EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size(), MsgConstant.MEMBER_CHANGED_TAG, bean.getData().getGroupInfo().getName()));
                        if (bean.getData().getEmployeeInfo() != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < bean.getData().getEmployeeInfo().size(); i++) {

                                if (i == bean.getData().getEmployeeInfo().size() - 1) {
                                    sb.append(bean.getData().getEmployeeInfo().get(i).getSign_id());
                                } else {
                                    sb.append(bean.getData().getEmployeeInfo().get(i).getSign_id() + ",");
                                }
                            }
                            EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size(), MsgConstant.UPDATE_MEMBER_LIST_TAG, sb.toString()));
                        }

                        notAllMember.clear();
                        if (mIsCreator && groupType == MsgConstant.GROUP_TYPE_NORMAL) {
                            if (mUserInfoList.size() >= 8) {
                                notAllMember.addAll(mUserInfoList.subList(0, 8));
                            } else {
                                notAllMember.addAll(mUserInfoList);
                            }
                            mTransferGroupRl.setVisibility(View.VISIBLE);

                        } else {
                            mTransferGroupRl.setVisibility(View.GONE);
                            if (mUserInfoList.size() >= 10) {
                                notAllMember.addAll(mUserInfoList.subList(0, 10));
                            } else {
                                notAllMember.addAll(mUserInfoList);
                            }
                        }
                        if (mIsCreator && mUserInfoList.size() >= 2 && groupType == MsgConstant.GROUP_TYPE_NORMAL) {
                            Member member1 = new Member();
                            member1.setId(-1L);
                            notAllMember.add(member1);
                            Member member2 = new Member();
                            member2.setId(-2L);
                            notAllMember.add(member2);
                        } else if (mIsCreator && mUserInfoList.size() == 1 && groupType == MsgConstant.GROUP_TYPE_NORMAL) {

                            Member member1 = new Member();
                            member1.setId(-1L);
                            notAllMember.add(member1);
                        }

                        mGridAdapter.notifyDataSetChanged();
                        mGridAdapter.setNewData(notAllMember);
                        mBean = bean;
                        mGroupChatDetailView.setGroupName(bean.getData().getGroupInfo().getName());

                        mGroupChatDetailView.setGroupNotice(bean.getData().getGroupInfo().getNotice());
                        mGroupChatDetailView.setGroupPrincipal(bean.getData().getGroupInfo().getPrincipal_name());
                        mGroupChatDetailView.setMembersNum(mUserInfoList.size());
//                        mGroupChatDetailView.initNoDisturb(Integer.parseInt(bean.getData().getGroupInfo().getNo_bother()));
//                        mGroupChatDetailView.initOnTop(Integer.parseInt(bean.getData().getGroupInfo().getTop_status()));
                        mTopRl.setSelected("1".equals(bean.getData().getGroupInfo().getTop_status()));
                        mNoDisturbRl.setSelected("1".equals(bean.getData().getGroupInfo().getNo_bother()));

                    }
                });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v.getId() == R.id.all_member_rl) {
            bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, mUserInfoList);
            CommonUtil.startActivtiy(GroupChatDetailActivity.this, GroupMemberActivity.class, bundle);
        } else if (v.getId() == R.id.group_principle_rl) {
            if (!mIsCreator && groupType != MsgConstant.GROUP_TYPE_NORMAL) {
                return;
            }
        } else if (v.getId() == R.id.group_describe_rl) {
            if (!mIsCreator && groupType != MsgConstant.GROUP_TYPE_NORMAL) {
                return;
            }
            bundle.putString(EditActivity.KEY_TITLE, "修改群聊公告");
            bundle.putString(EditActivity.KEY_HINT, "请输入标题");
            bundle.putString(EditActivity.KEY_ORIGINAL_TEXT, mNoticeTv.getText().toString().trim());
            bundle.putString(EditActivity.KEY_TAG, "群聊公告");
            bundle.putInt(EditActivity.KEY_MAX_LENGTH, 100);
            CommonUtil.startActivtiyForResult(this, EditActivity.class, Constants.REQUEST_CODE1, bundle);
        } else if (v.getId() == R.id.chat_detail_del_group) {
            DialogUtils.getInstance().sureOrCancel(GroupChatDetailActivity.this, quitOrDismiss,
                    "确定要" + quitOrDismiss + "吗?",
                    v.getRootView(), new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            quitOrDismissGroup();
                        }
                    });
        } else if (v.getId() == R.id.rl_view_chat_file) {
            bundle.putLong(MsgConstant.CONVERSATION_ID, conversationId);
            CommonUtil.startActivtiy(GroupChatDetailActivity.this, ChatFileActivity.class, bundle);
        } else if (v.getId() == R.id.group_name_rl) {
            if (!mIsCreator && groupType != MsgConstant.GROUP_TYPE_NORMAL) {
                return;
            }
            bundle.putString(EditActivity.KEY_TITLE, "修改群聊名称");
            bundle.putString(EditActivity.KEY_HINT, "12字以内");
            bundle.putString(EditActivity.KEY_ORIGINAL_TEXT, mGroupNameTv.getText().toString().trim());
            bundle.putString(EditActivity.KEY_TAG, "群聊名称");
            bundle.putInt(EditActivity.KEY_MAX_LENGTH, 12);
            bundle.putBoolean(EditActivity.KEY_MUST, true);
            CommonUtil.startActivtiyForResult(this, EditActivity.class, Constants.REQUEST_CODE2, bundle);

        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void receiveMessage(ImMessage result) {
        if (TextUtils.isEmpty(result.getTag())) {
            return;
        }
        switch (result.getTag()) {
            case MsgConstant.RECEIVE_RELEASE_GROUP_PUSH_MESSAGE:
                //解散群
                if (((String) result.getObject()).equals(conversationId + "")) {
                    ToastUtils.showToast(GroupChatDetailActivity.this, "该群已被解散!");
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                break;
            case MsgConstant.RECEIVE_GROUP_ADMIN_CHANGE_PUSH_MESSAGE:
                //群主变更
                getNetData(false);
                break;
            case MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE:
            case MsgConstant.RECEIVE_GROUP_NAME_CHANGE_PUSH_MESSAGE:
                //群成员变更或群名变更
                getNetData(false);
                break;

            case MsgConstant.RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE:
                //被移出该群或另一端已退出该群
                if ((conversationId + "").equals(((String) result.getObject()))) {
                    ToastUtils.showToast(mContext, "您已退出该群!");
                    finish();
                }
                break;
            default:

                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        /*if (Constants.MEMBER_LIST.equals(event.getTag()) && event.getCode() == GroupMemberActivity.FLAG_TRANSFER) {
            //转让群组
            ArrayList<Member> members = (ArrayList<Member>) event.getObject();
            if (members != null && members.size() == 1) {
                Member member = members.get(0);
                final long newGroupOwnerId = TextUtil.parseLong(member.getSign_id());
                String newChargerName = member.getName();
                if (newGroupOwnerId > 0) {
                    DialogUtils.getInstance().sureOrCancel(GroupChatDetailActivity.this, "",
                            "确定选择 " + newChargerName + " 为群主，你将自动放弃群主身份。",
                            mGroupChatDetailView, new DialogUtils.OnClickSureListener() {
                                @Override
                                public void clickSure() {
                                    transferGroup(newGroupOwnerId);
                                }
                            });
                }
            }


        }*/
    }

    /**
     * 转让群
     *
     * @param newGroupOwnerId
     */
    private void transferGroup(long newGroupOwnerId) {
        ImLogic.getInstance().transferGroup(GroupChatDetailActivity.this, conversationId, newGroupOwnerId,
                new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        getNetData(true);
                    }
                });
    }

    /**
     * 退出或解散群
     */
    private void quitOrDismissGroup() {
        if (mIsCreator) {
            ImLogic.getInstance().releaseGroup(GroupChatDetailActivity.this, conversationId,
                    new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);

                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);

                            ConversationListController.quitGroupChat(conversationId);
                            EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE1, MsgConstant.QUIT_OR_RELEASE_GROUP, conversationId));
                            DBManager.getInstance().deleteMessageByConversationId(conversationId);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });
        } else {
            ImLogic.getInstance().quitGroup(GroupChatDetailActivity.this, conversationId,
                    new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);

                            //  ToastUtils.showToast(GroupChatDetailActivity.this, "执行失败", ToastUtils.FAILED);
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);

                            ConversationListController.quitGroupChat(conversationId);
                            EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE1, MsgConstant.QUIT_OR_RELEASE_GROUP, conversationId));
                            DBManager.getInstance().deleteMessageByConversationId(conversationId);
                            setResult(Activity.RESULT_OK);
                            //退群
                            IM.getInstance().sendQuitTeamMessage(conversationId);
                            // ToastUtils.showToast(GroupChatDetailActivity.this, "执行成功", ToastUtils.SUCCESS);
                            finish();
                        }
                    });
        }
    }

    //设置群聊名称
    public void showGroupNameSettingDialog(final long groupID, String groupName) {
       /* ChatDialogUtil.showEditTextDialog(this, mContext.getString(R.string.group_name_hit), groupName, new ChatDialogUtil.OnClickListener() {
            @Override
            public void onClick(final String content, Dialog dialog) {
                if (TextUtils.isNotEmpty(content)) {
                    showToast(R.string.group_name_not_null_toast);
                } else {
                    dismissSoftInput();
                    EditGroupRequestBean bean = new EditGroupRequestBean();
                    bean.setName(content);
                    ContactLogic.getInstance().editGroup(GroupChatDetailActivity.this,bean,new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this){
                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            mChatDetailView.updateGroupName(content);
                            mChatDetailController.refreshGroupName(content);
                            showToast(R.string.modify_success_toast);
                        }
                    });
                }
            }
        });*/
    }


    //设置群聊描述
    public void showGroupDesSettingDialog(final long groupID, String groupDescription) {
        /*ChatDialogUtil.showEditTextDialog(this, mContext.getString(R.string.group_name_description), groupDescription, new ChatDialogUtil.OnClickListener() {
            @Override
            public void onClick(final String content, Dialog dialog) {
                if (TextUtils.isNotEmpty(content)) {
                    showToast(R.string.group_description_not_null_toast);
                } else {
                    dismissSoftInput();
                    EditGroupRequestBean bean = new EditGroupRequestBean();
                    bean.setDesc(content);
                    ContactLogic.getInstance().editGroup(GroupChatDetailActivity.this,bean,new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this){
                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            mChatDetailView.updateGroupName(content);
                            mChatDetailController.refreshGroupName(content);
                            showToast(R.string.modify_success_toast);
                        }
                    });
                }
            }
        });*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            //修改群公告
            String notice = data.getStringExtra(Constants.FILE_DESCRIPTION);
            if (TextUtils.isEmpty(notice)) {
                return;
            }
            modifyGroupInfo(receiverId, mGroupNameTv.getText().toString().trim(), notice);

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE2) {
            //修改群名
            String name = data.getStringExtra(Constants.FILE_DESCRIPTION);
            if (TextUtils.isEmpty(name)) {
                return;
            }
            modifyGroupInfo(receiverId, name, mNoticeTv.getText().toString().trim());

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE3) {
            //添加群成员
            List<Member> list1 = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            Iterator<Member> iterator = list1.iterator();
            while (iterator.hasNext()) {
                Member next = iterator.next();
                for (int i = 0; i < mUserInfoList.size(); i++) {
                    if (next.getSign_id().equals(mUserInfoList.get(i).getSign_id())
                            || next.getId() == mUserInfoList.get(i).getId()) {
                        iterator.remove();
                    }
                }
            }
            addMember(list1);

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE4) {
            //移除群成员
            List<Member> list2 = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            removeMember(list2);

        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE5) {
            //转让群组
            List<Member> members = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (members != null && members.size() > 0) {
                Member member = members.get(0);
                final long newGroupOwnerId = TextUtil.parseLong(member.getSign_id());
                String newChargerName = member.getName();
                if (newGroupOwnerId > 0) {
                    DialogUtils.getInstance().sureOrCancel(GroupChatDetailActivity.this, "转让群组",
                            "确定选择 " + newChargerName + " 为群主，你将自动放弃群主身份。",
                            mGroupChatDetailView, new DialogUtils.OnClickSureListener() {
                                @Override
                                public void clickSure() {
                                    transferGroup(newGroupOwnerId);
                                }
                            });
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 移除成员
     *
     * @param list
     */
    private void removeMember(List<Member> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder removeStr = new StringBuilder(" ");
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i).getSign_id() + "");
                removeStr.append(list.get(i).getEmployee_name() + "");
            } else {
                sb.append(list.get(i).getSign_id() + ",");
                removeStr.append(list.get(i).getEmployee_name() + ",");
            }
        }
        ImLogic.getInstance().kickPeople(GroupChatDetailActivity.this, conversationId,
                sb.toString(), new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showToast(GroupChatDetailActivity.this, getString(R.string.im_remove_failed_hint), ToastUtils.FAILED);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showToast(GroupChatDetailActivity.this, getString(R.string.im_remove_success_hint), ToastUtils.SUCCESS);
                        EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size() - list.size(), MsgConstant.MEMBER_CHANGED_TAG, mGroupNameTv.getText().toString().trim()));
                        getNetData(false);

                    }
                });

    }

    /**
     * 添加成员
     *
     * @param addList
     */
    private void addMember(List<Member> addList) {
        if (addList == null || addList.size() <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder addStr = new StringBuilder(" ");
        for (int i = 0; i < addList.size(); i++) {
            if (i == addList.size() - 1) {
                sb.append(addList.get(i).getSign_id() + "");
                addStr.append(addList.get(i).getEmployee_name() + "");
            } else {
                sb.append(addList.get(i).getSign_id() + ",");
                addStr.append(addList.get(i).getEmployee_name() + ",");
            }
        }
        ImLogic.getInstance().pullPeople(GroupChatDetailActivity.this, conversationId,
                sb.toString(), new ProgressSubscriber<BaseBean>(GroupChatDetailActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        //ToastUtils.showToast(GroupChatDetailActivity.this,"添加失败", ToastUtils.FAILED);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);

                        // ToastUtils.showToast(GroupChatDetailActivity.this,"添加成功", ToastUtils.SUCCESS);

                        IM.getInstance().sendTeamNotificationMessage(conversationId,
                                String.format(getString(R.string.im_welcome_notification1), addStr.toString())
                        );
                        EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size() + addList.size(), MsgConstant.MEMBER_CHANGED_TAG, mGroupNameTv.getText().toString().trim()));

                        getNetData(false);

                    }
                });

    }

    /**
     * 修改群信息
     *
     * @param receiverId
     * @param name
     * @param notice
     */
    private void modifyGroupInfo(long receiverId, String name, String notice) {
        ImLogic.getInstance().modifyGroupInfo(GroupChatDetailActivity.this, receiverId, name,
                notice, new ProgressSubscriber<BaseBean>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        //ToastUtils.showToast(GroupChatDetailActivity.this, "执行失败", ToastUtils.FAILED);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);

                        TextUtil.setText(mGroupNameTv, name);
                        TextUtil.setText(mNoticeTv, notice);
                        EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size(), MsgConstant.MEMBER_CHANGED_TAG, mGroupNameTv.getText().toString().trim()));
                        EventBusUtils.sendEvent(new MessageBean(mUserInfoList.size(), MsgConstant.GROUP_NAME_CHANGED_TAG, conversationId));
                        //   ToastUtils.showToast(GroupChatDetailActivity.this, "执行成功", ToastUtils.SUCCESS);

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (mChatDetailController.getAdapter() != null) {
            mChatDetailController.getAdapter().notifyDataSetChanged();
        }*/
    }


}
