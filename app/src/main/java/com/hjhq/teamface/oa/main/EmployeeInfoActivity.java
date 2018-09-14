package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.im.activity.ChatActivity;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 人员信息
 *
 * @author Administrator
 */
@RouteNode(path = "/employee/info", desc = "人员信息")
public class EmployeeInfoActivity extends BaseTitleActivity {
    private TextView mName;
    private TextView mDepartment;
    private TextView tvSign;
    private TextView mTvLikeNumn;
    private TextView mCellphone;
    private TextView mFixedPhone;
    private TextView mDepart;
    private TextView mLeader;
    private TextView mPost;
    private TextView mGender;
    private TextView mBirthday;
    private TextView mAddress;
    private TextView mEmail;
    private ImageView mAvatar;
    private ImageView mIvLike;
    private ImageView mIvGender;
    private LinearLayout mBottom;
    private RelativeLayout rlChat;
    private RelativeLayout rlEmail;
    private String likeStatus;
    private boolean isClickable = false;
    private String imUserName;
    String employeeId;
    private String signId;
    private Member mMember;

    private String avatarUrl;
    private String sign;
    private String mood;
    private String employeeName;
    private boolean isEditSign;
    private EmployeeDetailBean mDataBean;

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState == null) {
            employeeId = getIntent().getStringExtra(Constants.DATA_TAG1);
            imUserName = getIntent().getStringExtra(Constants.DATA_TAG2);
            signId = getIntent().getStringExtra(Constants.DATA_TAG3);
        } else {
            employeeId = savedInstanceState.getString(Constants.DATA_TAG1);
            imUserName = savedInstanceState.getString(Constants.DATA_TAG2);
            signId = savedInstanceState.getString(Constants.DATA_TAG3);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (employeeId != null) {
            outState.putString(Constants.DATA_TAG1, employeeId);
        }
        if (imUserName != null) {
            outState.putString(Constants.DATA_TAG2, imUserName);
        }
        if (signId != null) {
            outState.putString(Constants.DATA_TAG3, signId);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("个人信息");
        mName = findViewById(R.id.name);
        mDepartment = findViewById(R.id.department);
        tvSign = findViewById(R.id.tv_sign);
        mTvLikeNumn = findViewById(R.id.like);
        mCellphone = findViewById(R.id.tv_phone);
        mFixedPhone = findViewById(R.id.tv_fixed_phone);
        mEmail = findViewById(R.id.tv_email);
        mAvatar = findViewById(R.id.iv_avatar);
        mBottom = findViewById(R.id.ll_bottom);
        rlChat = findViewById(R.id.rl_chat);
        rlEmail = findViewById(R.id.rl_email);
        mIvLike = findViewById(R.id.icon_like);
        mIvGender = findViewById(R.id.gender);
        mDepart = findViewById(R.id.tv_department);
        mLeader = findViewById(R.id.tv_leader);
        mPost = findViewById(R.id.tv_post);
        mGender = findViewById(R.id.tv_gender);
        mBirthday = findViewById(R.id.tv_birthday);
        mAddress = findViewById(R.id.tv_address);
        mBottom.setVisibility(View.GONE);

    }

    @Override
    protected int getChildView() {
        return R.layout.activity_employee_info_v2;
    }


    @Override
    protected void initData() {
        if (!TextUtil.isEmpty(employeeId)) {
            MainLogic.getInstance().queryEmployeeInfo(this, employeeId, progressSubscriber());
        } else if (!TextUtil.isEmpty(signId)) {
            MainLogic.getInstance().queryEmployeeInfoBySingId(this, signId, progressSubscriber());
        } else {
            ToastUtils.showError(this, "id为null");
        }
    }

    private ProgressSubscriber progressSubscriber() {
        return new ProgressSubscriber<EmployeeDetailBean>(this) {
            @Override
            public void onNext(EmployeeDetailBean baseBean) {
                super.onNext(baseBean);
                mDataBean = baseBean;
                fillData(baseBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                isClickable = true;
            }
        };
    }

    /**
     * 填充数据
     *
     * @param baseBean
     */
    private void fillData(EmployeeDetailBean baseBean) {
        isClickable = true;
        EmployeeDetailBean.DataBean personData = baseBean.getData();
        EmployeeDetailBean.DataBean.EmployeeInfoBean employeeInfo = personData.getEmployeeInfo();
        EmployeeDetailBean.DataBean.CompanyInfoBean companyInfo = personData.getCompanyInfo();
        List<EmployeeDetailBean.DataBean.DepartmentInfo> departmentInfos = personData.getDepartmentInfo();



        mMember = new Member();
        mMember.setName(employeeInfo.getEmployee_name());
        mMember.setEmployee_name(employeeInfo.getEmployee_name());
        mMember.setSign_id(employeeInfo.getSign_id());
        mMember.setPhone(employeeInfo.getPhone());
        mMember.setPicture(employeeInfo.getPicture());
        mMember.setId(TextUtil.parseLong(employeeInfo.getId()));
        employeeId = employeeInfo.getId();
        mMember.setEmail(employeeInfo.getEmail());
        mMember.setPost_name(employeeInfo.getPost_name());
        if (departmentInfos != null && departmentInfos.size() > 0) {
            mMember.setDepartment_name(departmentInfos.get(0).getDepartment_name());
        }

        //设置员工数据
        StringBuilder sb = new StringBuilder();
        sb.append(mMember.getDepartment_name());
        if (!TextUtils.isEmpty(sb)) {
            sb.append("-");
        }
        sb.append(mMember.getPost_name());

        if (!SPHelper.getUserId().equals(signId) && !SPHelper.getEmployeeId().equals(employeeId)) {
            mBottom.setVisibility(View.VISIBLE);
        }
        sign = employeeInfo.getSign();
        mood = employeeInfo.getMood();
        employeeName = employeeInfo.getEmployee_name();
        TextUtil.setText(mName, employeeName);

        TextUtil.setText(mCellphone, employeeInfo.getPhone());
        TextUtil.setText(mFixedPhone, employeeInfo.getMobile_phone());
        TextUtil.setText(mEmail, employeeInfo.getEmail());
        final long birthdayLong = TextUtil.parseLong(employeeInfo.getBirth());
        if (birthdayLong > 0) {
            TextUtil.setText(mBirthday, DateTimeUtil.longToStr(birthdayLong, "yyyy-MM-dd"));
        }


        avatarUrl = employeeInfo.getPicture();
        DBManager.getInstance().updateUserAvatarByEmployeeId(employeeId, avatarUrl);
        setResult(RESULT_OK);
        ImageLoader.loadCircleImage(this, avatarUrl, mAvatar, employeeName);

        //签名/公司名/地址
        String address = employeeInfo.getRegion();
        if (!TextUtils.isEmpty(address)) {
            String[] split1 = address.split(",");
            if (split1 != null & split1.length > 0) {
                StringBuilder addressSb = new StringBuilder();
                for (int i = 0; i < split1.length; i++) {
                    String s = split1[i].substring(split1[i].indexOf(":") + 1, split1[i].length());
                    addressSb.append(s);
                }
                if (!TextUtils.isEmpty(addressSb.toString())) {
                    TextUtil.setText(mAddress, addressSb.toString());
                }
            }

        }
        TextUtil.setText(tvSign, companyInfo.getCompany_name());
        try {
            TextUtil.setText(mDepartment, employeeInfo.getSign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //部门
            TextUtil.setText(mDepart, departmentInfos.get(0).getDepartment_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //上级
            //TextUtil.setText(mLeader, departmentInfos.get(0).getDepartment_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //职务
        TextUtil.setText(mPost, employeeInfo.getPost_name());
        /*try {
            if (!TextUtils.isEmpty(mood)) {
                EmojiUtil.handlerEmojiText2(tvSign, mood + " " + sign, mContext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //点赞
        likeStatus = personData.getFabulous_status();
        if ("0".equals(likeStatus)) {
            mIvLike.setImageResource(R.drawable.heart_gray);
        } else {
            mIvLike.setImageResource(R.drawable.heart_red);
        }
        //点攒数
        String fabulousCount = personData.getFabulous_count();
        if (!TextUtils.isEmpty(fabulousCount) && fabulousCount.length() > 6) {
            mTvLikeNumn.setText("999999+");
        } else {
            TextUtil.setText(mTvLikeNumn, fabulousCount);
        }
        //性别
        if ("0".equals(employeeInfo.getSex())) {
            mIvGender.setImageResource(R.drawable.icon_male);
            TextUtil.setText(mGender, "男");
        } else if ("1".equals(employeeInfo.getSex())) {
            mIvGender.setImageResource(R.drawable.icon_female);
            TextUtil.setText(mGender, "女");
        } else {
        }
    }

    @Override
    protected void setListener() {
        setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        if (isEditSign) {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, sign);
            intent.putExtra(Constants.DATA_TAG2, mood);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @OnClick({R.id.rl_chat, R.id.rl_email, R.id.rl_like, R.id.iv_avatar, R.id.ll_bottom, R.id.tv_phone, R.id.tv_fixed_phone, R.id.tv_sign, R.id.rl_user_info})
    public void onViewClicked(View view) {
        if (!isClickable) {
            return;
        }
        if (mMember == null) {
            ToastUtils.showError(this, "未获取到人员信息");
            return;
        }
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_chat:
                sendChat();
                break;
            case R.id.rl_email:
                sendEmail();
                break;
            case R.id.iv_avatar:
                //是自己跳转修改头像，不是只能查看
                bundle.putString(Constants.DATA_TAG1, avatarUrl == null ? "" : avatarUrl);
                bundle.putString(Constants.DATA_TAG2, employeeName);
                bundle.putBoolean(Constants.DATA_TAG3, SPHelper.getEmployeeId().equals(mMember.getId() + ""));
                CommonUtil.startActivtiy(this, ChangeAvatarActivity.class, bundle);
                break;
            case R.id.rl_like:
                whetherFabulous();
                break;
            case R.id.tv_phone:
                call(mCellphone.getText().toString());
                break;
            case R.id.tv_fixed_phone:
                call(mFixedPhone.getText().toString());
                break;
            case R.id.tv_sign:
                if (!employeeId.equals(SPHelper.getEmployeeId())) {
                    return;
                }
                bundle.putString(Constants.DATA_TAG1, sign);
                bundle.putString(Constants.DATA_TAG2, mood);
                CommonUtil.startActivtiyForResult(this, SignatureActivity.class, Constants.REQUEST_CODE1, bundle);
                break;
            case R.id.rl_user_info:
                bundle.putSerializable(Constants.DATA_TAG1, mMember.getId() + "");
                CommonUtil.startActivtiy(mContext, UserInfoActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    /**
     * 发邮件
     */
    private void sendEmail() {
        EmailBean bean = new EmailBean();
        ArrayList<ReceiverBean> list = new ArrayList<>();
        ReceiverBean rb = new ReceiverBean();
        if (mDataBean != null && mDataBean.getData() != null && mDataBean.getData().getEmployeeInfo() != null) {
            rb.setEmployee_name(mDataBean.getData().getEmployeeInfo().getEmployee_name());
            rb.setMail_account(mDataBean.getData().getEmployeeInfo().getEmail());
            if (!TextUtils.isEmpty(rb.getMail_account())) {
                list.add(rb);
            }
        }
        bean.setTo_recipients(list);
        bean.setAttachments_name(new ArrayList<>());
        bean.setBcc_recipients(new ArrayList<>());
        bean.setCc_recipients(new ArrayList<>());
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG3, EmailConstant.EDIT_AGAIN);
        bundle.putSerializable(Constants.DATA_TAG5, bean);
        UIRouter.getInstance().openUri(mContext, "DDComp://email/new_email", bundle, Constants.REQUEST_CODE2);

    }


    /**
     * 打电话
     */
    private void call(String phone) {
        if (TextUtil.isEmpty(phone)) {
            return;
        }
        DialogUtils.getInstance().sureOrCancel(this, phone, null, "呼叫", "取消", mName, () -> {
            SystemFuncUtils.callPhone(EmployeeInfoActivity.this, phone);
            MainLogic.getInstance().saveRecentContact(mMember);
        });
    }

    /**
     * 点赞
     */
    private void whetherFabulous() {
        MainLogic.getInstance().whetherFabulous(EmployeeInfoActivity.this, employeeId + "",
                "0".equals(likeStatus) ? "1" : "0", new ProgressSubscriber<BaseBean>(EmployeeInfoActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        initData();
                    }
                });
    }

    /**
     * 发起聊天
     */
    private void sendChat() {
        MainLogic.getInstance().saveRecentContact(mMember);
        //如果会话为空，使用EventBus通知会话列表添加新会话
        MainLogic.getInstance().addSingleChat(EmployeeInfoActivity.this,
                mMember.getSign_id(), new ProgressSubscriber<AddSingleChatResponseBean>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(AddSingleChatResponseBean bean) {
                        super.onNext(bean);
                        Bundle bundle = new Bundle();
                        MainLogic.getInstance().saveRecentContact(mMember);
                        Conversation conversation = new Conversation();
                        conversation.setConversationId(TextUtil.parseLong(bean.getData().getId()));
                        conversation.setCompanyId(bean.getData().getId() + "");
                        conversation.setOneselfIMID(SPHelper.getUserId());
                        conversation.setReceiverId(bean.getData().getReceiver_id());
                        conversation.setConversationType(MsgConstant.SINGLE);
                        conversation.setTitle(bean.getData().getEmployee_name());
                        conversation.setIsHide(1);
                        conversation.setAvatarUrl(bean.getData().getPicture());
                        conversation.setSenderAvatarUrl(bean.getData().getPicture());
                        bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                        bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getReceiver_id());
                        bundle.putLong(MsgConstant.CONVERSATION_ID, TextUtil.parseLong(bean.getData().getId()));
                        bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getEmployee_name());
                        bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getChat_type());
                        CommonUtil.startActivtiy(EmployeeInfoActivity.this, ChatActivity.class, bundle);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            isEditSign = true;
            //签名
            sign = data.getStringExtra(Constants.DATA_TAG1);
            mood = data.getStringExtra(Constants.DATA_TAG2);
            try {
                if (TextUtil.isEmpty(sign) && TextUtil.isEmpty(mood)) {
                    tvSign.setText("");
                } else {
                    EmojiUtil.handlerEmojiText2(tvSign, mood + " " + sign, mContext);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageBean event) {
        if (event.getCode() == Constants.EDIT_USER_INFO) {
            initData();
        }
    }
}
