package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SideBar;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ChooseGroupChatAdapter;
import com.hjhq.teamface.im.bean.GetGroupListBean;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * @author Administrator
 */
@RouteNode(path = "/choose_group", desc = "选择群聊")
public class ChooseGroupChatActivity extends BaseTitleActivity {


    private ChooseGroupChatAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<GetGroupListBean.DataBean> mGroupList = new ArrayList<>();
    private ArrayList<GetGroupListBean.DataBean> resultList = new ArrayList<>();
    private boolean chooseMember = false;
    private String chooseTag = "";
    private SideBar mSideBar;
    private EditText mEditText;


    @Override
    protected int getChildView() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(getString(R.string.im_group_chat_list));
        mSideBar = findViewById(R.id.sidrbar);
        mEditText = findViewById(R.id.et);
        mSideBar.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chooseTag = bundle.getString(Constants.DATA_TAG1);
            if (MsgConstant.MEMBER_TAG.equals(chooseTag)) {
                chooseMember = true;
                setActivityTitle(getResources().getString(R.string.im_send_to_sb));
            } else if (MsgConstant.SEND_FILE_TO_SB.equals(chooseTag)) {
                chooseMember = true;
                setActivityTitle(R.string.im_group_chat);
            } else if (MsgConstant.CHOOSE_GROUP_CHAT.equals(chooseTag)) {
                setActivityTitle(getResources().getString(R.string.im_group_chat));
            }
        }


        recyclerView = (RecyclerView) findViewById(R.id.rv_group_member);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyLinearDeviderDecoration(this, R.color.red));
        mAdapter = new ChooseGroupChatAdapter(null);
        recyclerView.setAdapter(mAdapter);

        getNetData();
    }

    private void getNetData() {
        ImLogic.getInstance().getAllGroupsInfo(ChooseGroupChatActivity.this,
                new ProgressSubscriber<GetGroupListBean>(ChooseGroupChatActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        ToastUtils.showError(mContext, "获取数据失败!");
                    }

                    @Override
                    public void onNext(GetGroupListBean bean) {
                        super.onNext(bean);
                        mGroupList.clear();
                        mGroupList.addAll(bean.getData());
                        CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), mGroupList);
                    }
                });

    }

    @Override
    protected void setListener() {
        findViewById(R.id.search).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, mGroupList);
            boolean tag = !TextUtils.isEmpty(chooseTag) && !MsgConstant.CHOOSE_GROUP_CHAT.equals(chooseTag);
            bundle.putBoolean(Constants.DATA_TAG2, tag);
            bundle.putString(Constants.DATA_TAG3, chooseTag);
            /*if(tag){

            }*/
            CommonUtil.startActivtiyForResult(mContext, SearchGroupChatActivity.class, Constants.REQUEST_CODE1, bundle);
        });
        mEditText.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    searchGroup(mEditText.getText().toString());
                    return true;

                } else {
                    return true;
                }
            }
            return false;
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchGroup(s.toString());
                    }
                }, 500);
            }
        });
        recyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (chooseTag) {
                    case MsgConstant.SEND_FILE_TO_SB:
                        sendFileToGroup(position);
                        break;
                    case MsgConstant.MEMBER_TAG:
                        sendFileToGroup(position);
                        break;
                    case MsgConstant.CHOOSE_GROUP_CHAT:
                        openGroupChat(position);
                        break;
                    default:
                        openGroupChat(position);
                        break;
                }

            }
        });

    }

    private void searchGroup(String keyword) {
        resultList.clear();
        CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), new ArrayList());
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(keyword.trim())) {
            CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), mGroupList);
            return;
        }

        if (mGroupList != null) {
            for (int i = 0; i < mGroupList.size(); i++) {
                if (!TextUtils.isEmpty(mGroupList.get(i).getName()) && mGroupList.get(i).getName().contains(keyword)) {
                    resultList.add(mGroupList.get(i));
                }
            }
            CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), resultList);
        }


    }

    /**
     * 文件库发送文件-选择群
     *
     * @param position
     */
    private void sendFileToGroup(int position) {

        Conversation conversation = new Conversation();
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setReceiverId(mGroupList.get(position).getId() + "");
        conversation.setConversationType(MsgConstant.GROUP);
        conversation.setTitle(mGroupList.get(position).getName());
        conversation.setPeoples(mGroupList.get(position).getPeoples());
        conversation.setConversationId(mGroupList.get(position).getId());
        conversation.setSenderAvatarUrl(IM.getInstance().getavatar());
        conversation.setAvatarUrl("");
        conversation.setSenderName(IM.getInstance().getName());
        EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE5, MsgConstant.RESEND_MEMBER_TAG, conversation));
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, conversation);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 打开群聊界面
     *
     * @param position
     */
    private void openGroupChat(int position) {
        Conversation conversation = new Conversation();
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setReceiverId(mGroupList.get(position).getId() + "");
        conversation.setConversationType(MsgConstant.GROUP);
        conversation.setTitle(mGroupList.get(position).getName());
        conversation.setPeoples(mGroupList.get(position).getPeoples());
        conversation.setConversationId(mGroupList.get(position).getId());
        try {
            conversation.setIsHide(Integer.parseInt(mGroupList.get(position).getIs_hide()));
        } catch (NumberFormatException e) {
            conversation.setIsHide(0);
        }
        // TODO: 2017/12/14 群头像
        conversation.setAvatarUrl(mGroupList.get(position).getNotice());
        if (!chooseMember) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
            bundle.putString(MsgConstant.RECEIVER_ID, mGroupList.get(position).getId() + "");
            bundle.putLong(MsgConstant.CONVERSATION_ID, mGroupList.get(position).getId());
            bundle.putString(MsgConstant.CONV_TITLE, mGroupList.get(position).getName());
            bundle.putInt(MsgConstant.CHAT_TYPE, MsgConstant.GROUP);
            CommonUtil.startActivtiy(ChooseGroupChatActivity.this, ChatActivity.class, bundle);
            if (!TextUtils.isEmpty(chooseTag)) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, conversation);
            EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE5, MsgConstant.RESEND_MEMBER_TAG, conversation));
            EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE5, MsgConstant.SEND_FILE_TO_SB, conversation));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ImMessage message) {
        if (MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE.equals(message.getTag())) {
            getNetData();
        }
        if (MsgConstant.RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE.equals(message.getTag())) {
            getNetData();
        }
        if (MsgConstant.RECEIVE_RELEASE_GROUP_PUSH_MESSAGE.equals(message.getTag())) {
            getNetData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean message) {
        if (MsgConstant.QUIT_OR_RELEASE_GROUP.equals(message.getTag())) {
            getNetData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            setResult(Activity.RESULT_OK);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
