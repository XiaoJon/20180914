package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.chat.GroupChatDetailView;


public class ChatDetailActivity extends BaseActivity {
    private static final String TAG = "GroupChatDetailActivity";
    private GroupChatDetailView mGroupChatDetailView;
    private TextView mTvTitle;
    //private ChatDetailController mChatDetailController;
    public final static String START_FOR_WHICH = "which";
    private final static int GROUP_NAME_REQUEST_CODE = 1;
    private final static int MY_NAME_REQUEST_CODE = 2;
    public static final int EDIT_FRIEND_REQUEST_CODE = 3;
    private Context mContext;


    @Override
    protected int getContentView() {
        return R.layout.activity_group_chat_detail;
    }

    @Override
    protected void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        TextUtil.setText(mTvTitle, "聊天详情");
        mContext = this;
        mGroupChatDetailView = (GroupChatDetailView) findViewById(R.id.chat_detail_view);


    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

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
                    ContactLogic.getInstance().editGroup(ChatDetailActivity.this,bean,new ProgressSubscriber<BaseBean>(ChatDetailActivity.this){
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
                    ContactLogic.getInstance().editGroup(ChatDetailActivity.this,bean,new ProgressSubscriber<BaseBean>(ChatDetailActivity.this){
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
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GROUP_NAME_REQUEST_CODE) {
            Log.i(TAG, "resultName = " + data.getStringExtra("resultName"));
            mChatDetailView.setGroupName(data.getStringExtra("resultName"));
        } else if (requestCode == MY_NAME_REQUEST_CODE) {
            Log.i(TAG, "myName = " + data.getStringExtra("resultName"));
        } else if (resultCode == MyApplication.RESULT_CODE_FRIEND_INFO) {
            if (data.getBooleanExtra("returnChatActivity", false)) {
                data.putExtra("deleteMsg", mChatDetailController.getDeleteFlag());
                data.putExtra(MyApplication.NAME, mChatDetailController.getName());
                data.putExtra(MyApplication.MEMBERS_COUNT, mChatDetailController.getCurrentCount());
                setResult(MyApplication.RESULT_CODE_CHAT_DETAIL, data);
                finish();
            }
        }  else if (requestCode == EDIT_FRIEND_REQUEST_CODE) {//修改成员
            long groupId = data.getLongExtra(Constants.DATA_TAG1, 0L);
            mChatDetailController.refresh(groupId);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (mChatDetailController.getAdapter() != null) {
            mChatDetailController.getAdapter().notifyDataSetChanged();
        }*/
    }


    /**
     * 从ContactsActivity中选择朋友加入到群组中
     */
    public void showContacts() {
        Intent intent = new Intent();
       /* List<UserInfo> members = mChatDetailController.getMembers();
        ArrayList<String> notAllMember = new ArrayList<>();
        for (UserInfo user : members) {
            notAllMember.add(user.getUserName());
        }
        intent.putExtra(Constants.DATA_TAG1, notAllMember);
        intent.putExtra(Constants.DATA_TAG2, mChatDetailController.getGroupId());
        intent.setClass(this, AddGroupMemberActivity.class);
        startActivityForResult(intent, EDIT_FRIEND_REQUEST_CODE);*/
    }

    private void dismissSoftInput() {
        //隐藏软键盘
        InputMethodManager imm = ((InputMethodManager) mContext
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
