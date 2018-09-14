package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.db.DBManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 创建群组
 *
 * @author Administrator
 * @date 2017/4/17
 */

public class CreateGroupActivity extends BaseActivity {


    TextView mIvBack;
    TextView mTitleBarRightTv;
    EditText mActCreateGroupEtName;
    com.hjhq.teamface.basis.view.member.AddMemberView mMemberCreateGroup;
    MembersView mMembersView;
    EditText mActCreateGroupEtDesc;
    boolean flag = true;
    boolean clickable = true;

    private List<Member> memberList = new ArrayList<>();
    private Member member;

    @Override
    protected int getContentView() {
        return R.layout.acitivity_create_group;
    }

    @Override
    protected void initView() {
        mTitleBarRightTv = (TextView) findViewById(R.id.title_bar_right_menu_tv);
        mIvBack = (TextView) findViewById(R.id.ivBack);
        mActCreateGroupEtName = (EditText) findViewById(R.id.act_create_group_et_Name);
        mMemberCreateGroup = (AddMemberView) findViewById(R.id.member_create_group);
        mActCreateGroupEtDesc = (EditText) findViewById(R.id.act_create_group_et_desc);
    }

    @Override
    protected void setListener() {
        //setOnClicks(mIvBack, mTitleBarRightTv);
        mIvBack.setOnClickListener(this);
        mTitleBarRightTv.setOnClickListener(this);
        mMemberCreateGroup.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                //CommonUtil.showToast("选择群成员1");
                Bundle bundle = new Bundle();
                ArrayList<Member> members = (ArrayList<Member>) mMemberCreateGroup.getMembers();
                for (Member member : members) {
                    member.setCheck(true);
                    if (!TextUtils.isEmpty(SPHelper.getEmployeeId()) && SPHelper.getEmployeeId().equals(member.getId() + "")) {
                        member.setSelectState(C.CAN_NOT_SELECT);
                    }
                }
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                CommonUtil.startActivtiyForResult(CreateGroupActivity.this, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
       /* mMemberCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showToast("选择群成员2");
                Bundle bundle = new Bundle();
                ArrayList<Member> members = (ArrayList<Member>) mMemberCreateGroup.getMembers();
                for (Member member : members) {
                    member.setCheck(true);
                    if (!TextUtils.isEmpty(SPHelper.getEmployeeId()) && SPHelper.getEmployeeId().equals(member.getId() + "")) {
                        member.setSelectState(C.CAN_NOT_SELECT);
                    }
                }
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                Intent intent = new Intent(CreateGroupActivity.this, SelectMemberActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE1, bundle);
            }
        });*/
    }

    @Override
    protected void initData() {
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
        memberList.add(member);
        mMemberCreateGroup.setMembers(memberList);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            finish();
        } else if (v.getId() == R.id.title_bar_right_menu_tv) {
            if (!clickable) {
                ToastUtils.showToast(mContext, "正在执行");
                return;
            }
            clickable = false;
            AddGroupChatReqBean bean = new AddGroupChatReqBean();
            String groupName = mActCreateGroupEtName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName) || groupName.length() > 12) {
                ToastUtils.showToast(mContext, getString(R.string.im_group_name_err_hint));
                clickable = true;
                return;
            }
            bean.setName(groupName);
            bean.setNotice(mActCreateGroupEtDesc.getText().toString().trim());
            StringBuilder sb = new StringBuilder();
            StringBuilder nameSb = new StringBuilder();
            List<Member> list = new ArrayList<>();
            list.addAll(memberList);
            if (list.size() <= 1) {
                ToastUtils.showToast(mContext, "请选择群成员!");
                clickable = true;
                return;
            }
            Iterator<Member> iterator = list.iterator();
            while (iterator.hasNext()) {
                Member member = iterator.next();
                if (SPHelper.getUserId().equals(member.getSign_id())) {
                    iterator.remove();
                } else {
                    sb.append(member.getSign_id() + ",");
                    nameSb.append(member.getName() + ",");
                }
            }

            String peoples = sb.toString();
            if (peoples.endsWith(",")) {
                peoples = peoples.substring(0, peoples.length() - 1);
            }

            bean.setPeoples(peoples);
            bean.setType("1");
            bean.setPrincipal_name(member.getName());

            ImLogic.getInstance().addGroupChat(CreateGroupActivity.this, bean,
                    new ProgressSubscriber<AddGroupChatResponseBean>(CreateGroupActivity.this, true) {
                        @Override
                        public void onError(Throwable e) {
                            clickable = true;
                            if (flag) {
                                ToastUtils.showToast(mContext, getResources().getString(R.string.im_create_group_chat_fail));
                            }

                        }

                        @Override
                        public void onNext(AddGroupChatResponseBean bean) {


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
                            CommonUtil.startActivtiy(CreateGroupActivity.this, ChatActivity.class, bundle);
                            if (bean.getData().getEmployeeInfo().size() > 0 && !TextUtils.isEmpty(nameSb)) {
                                IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),

                                        String.format(getString(R.string.im_welcome_notification1), nameSb.toString().substring(0, nameSb.length() - 1))

                                );
                            } else {
                                IM.getInstance().sendTeamNotificationMessage(conversation.getConversationId(),
                                        getString(R.string.im_welcome_notification2)
                                );
                            }
                            flag = false;
                            clickable = true;
                            finish();
                        }
                    });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            memberList.clear();
            memberList.add(member);
            if (members != null) {
                for (int i = 0; i < members.size(); i++) {
                    if (SPHelper.getUserId().equals(members.get(i).getSign_id())) {
                        continue;
                    } else {
                        memberList.add(members.get(i));
                    }
                }
            }
            mMemberCreateGroup.setMembers(memberList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}