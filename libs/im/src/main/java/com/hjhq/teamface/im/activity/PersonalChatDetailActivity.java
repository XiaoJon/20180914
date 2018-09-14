package com.hjhq.teamface.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.HelperItemView;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.im.adapter.GroupChatMemberAdapter;
import com.hjhq.teamface.im.bean.SingleChatInfoBean;
import com.hjhq.teamface.im.db.DBManager;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonalChatDetailActivity extends BaseTitleActivity {
    private static final String TAG = "PersonalChatDetailActivity";

    private Context mContext;
    private HelperItemView mPutOnTop;
    private HelperItemView mNoDisturb;
    private RelativeLayout mViewFileRl;
    private RecyclerView mRecyclerView;
    private long receiverId;
    private long conversationId;
    private int noBotherState;
    private int putOnTopState;
    private GroupChatMemberAdapter mGridAdapter;
    private List<Member> memberList = new ArrayList<>();

    @Override
    protected int getChildView() {
        return R.layout.activity_personal_chat_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("聊天信息");
        receiverId = TextUtil.parseLong(getIntent().getStringExtra(MsgConstant.RECEIVER_ID));
        conversationId = getIntent().getLongExtra(MsgConstant.CONVERSATION_ID, -1L);
        mContext = this;
        mPutOnTop = (HelperItemView) findViewById(R.id.put_on_top);
        mNoDisturb = (HelperItemView) findViewById(R.id.no_disturb_rl);
        mViewFileRl = (RelativeLayout) findViewById(R.id.rl_view_chat_file);
        mRecyclerView = findViewById(R.id.rv_member);
        initAdapter();

    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mGridAdapter = new GroupChatMemberAdapter(true, memberList);
        mRecyclerView.setAdapter(mGridAdapter);

    }

    @Override
    protected void setListener() {
        setOnClicks(mViewFileRl);
        mPutOnTop.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImLogic.getInstance().setTop(PersonalChatDetailActivity.this, conversationId + "",
                        MsgConstant.SINGLE,
                        new ProgressSubscriber<BaseBean>(PersonalChatDetailActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mPutOnTop.setSelected(!mPutOnTop.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);

                                putOnTopState = putOnTopState == 0 ? 1 : 0;
                                EventBusUtils.sendEvent(new MessageBean(putOnTopState, MsgConstant.UPDATE_PUT_TOP_TAG, conversationId));
                                //EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));
                            }
                        });
            }
        });
        mNoDisturb.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtil.showToast("免打扰");
                ImLogic.getInstance().noBother(PersonalChatDetailActivity.this, conversationId + "",
                        MsgConstant.SINGLE,
                        new ProgressSubscriber<BaseBean>(PersonalChatDetailActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mNoDisturb.setSelected(!mNoDisturb.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
//                                    noBotherState = noBotherState == mNoDisturb.getSelected() ? 1 : 0;
//                                    EventBusUtils.sendEvent(new MessageBean(noBotherState, MsgConstant.UPDATE_NO_BOTHER_TAG, conversationId));
                                ConversationListController.updateConversation(conversationId, mNoDisturb.getSelected() ? 1 : 0);
                            }
                        });
            }
        });
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                if (memberList.get(position).getId() == -1L) {
                    //添加人员
                    ArrayList<Member> list = new ArrayList<Member>();
                    List<Member> my = DBManager.getInstance().getMemberBySignId(SPHelper.getUserId());
                    my.forEach(m -> {
                        m.setSelectState(C.NOT_FOR_SELECT);
                        m.setCheck(true);
                    });
                    List<Member> their = DBManager.getInstance().getMemberBySignId(memberList.get(0).getSign_id());
                    their.forEach(m -> {
                        m.setSelectState(C.NOT_FOR_SELECT);
                        m.setCheck(true);
                    });
                    list.addAll(my);
                    list.addAll(their);
                    bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list);
                    bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                    CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
                } else {
                    bundle.putString(Constants.DATA_TAG3, memberList.get(0).getSign_id());
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle, Constants.REQUEST_CODE8);
                }
            }
        });

    }

    @Override
    protected void initData() {
        ImLogic.getInstance().getSingleInfo(PersonalChatDetailActivity.this, conversationId + "",
                new ProgressSubscriber<SingleChatInfoBean>(PersonalChatDetailActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SingleChatInfoBean bean) {
                        super.onNext(bean);

                        mPutOnTop.setSelected("1".equals(bean.getData().getTop_status()));
                        putOnTopState = Integer.parseInt(bean.getData().getTop_status());
                        mNoDisturb.setSelected("1".equals(bean.getData().getNo_bother()));
                        noBotherState = Integer.parseInt(bean.getData().getTop_status());

                        Member m = new Member();
                        m.setName(bean.getData().getEmployee_name());
                        m.setPicture(bean.getData().getPicture());
                        m.setSign_id(bean.getData().getReceiver_id());
                        m.setId(bean.getData().getId());
                        memberList.clear();
                        memberList.add(m);
                        memberList.add(new Member(-1L, "", -1));
                        mGridAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_view_chat_file) {
            Bundle bundle = new Bundle();
            bundle.putLong(MsgConstant.CONVERSATION_ID, conversationId);
            CommonUtil.startActivtiy(PersonalChatDetailActivity.this, ChatFileActivity.class, bundle);
        }
    }

    public boolean createChat(ArrayList<Member> memberList) {
        if (memberList.size() >= 2) {
            String groupName = "";
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
                    if (nameSb.length() < 12) {
                        groupName = nameSb.toString();
                    }
                }
            }

            String peoples = idSb.toString();
            if (peoples.endsWith(",")) {
                peoples = peoples.substring(0, peoples.length() - 1);
            }

            if (!TextUtils.isEmpty(groupName) && groupName.endsWith(",")) {
                groupName = groupName.substring(0, groupName.length() - 1);
            }
            bean.setName(groupName);
            bean.setPeoples(peoples);
            bean.setType(MsgConstant.GROUP + "");
            bean.setPrincipal_name(SPHelper.getUserName());
            ImLogic.getInstance().addGroupChat(PersonalChatDetailActivity.this, bean, new ProgressSubscriber<AddGroupChatResponseBean>(mContext, true) {
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
                    CommonUtil.startActivtiy(mContext, ChatActivity.class, bundle);
                    if (bean.getData().getEmployeeInfo().size() > 0 && !TextUtils.isEmpty(nameSb)) {
                        IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),
                                String.format(getString(R.string.im_welcome_notification1), nameSb.toString().substring(0, nameSb.length() - 1))
                        );
                    } else {
                        IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),
                                getString(R.string.im_welcome_notification2)
                        );
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        return false;
    }

    @Override
    public void onBackPressed() {
       /* Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.putExtra(MyApplication.CONV_TITLE, mChatDetailController.getName());
        intent.putExtra(MyApplication.MEMBERS_COUNT, mChatDetailController.getCurrentCount());
        intent.putExtra("deleteMsg", mChatDetailController.getDeleteFlag());
        setResult(MyApplication.RESULT_CODE_CHAT_DETAIL, intent);
        finish();*/
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null && list.size() > 2) {
                createChat(list);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (mChatDetailController.getAdapter() != null) {
            mChatDetailController.getAdapter().notifyDataSetChanged();
        }*/
    }


}
