package com.hjhq.teamface.email.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.common.adapter.EmailAttachmentAdapter;
import com.hjhq.teamface.email.presenter.EmailDetailActivity;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：邮件详情视图
 */

public class NewEmailDelegate extends AppDelegate {

    private RecyclerView rvAttachment;
    private EditText mEmailContent;
    private EditText mEmailTheme;
    private FrameLayout flAction;
    private View mActionView;


    private String emailId;
    private EmailBean emailDetailBean;

    @Override
    public int getRootLayoutId() {
        return R.layout.email_add_new_activity_;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initWidget() {
        super.initWidget();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.icon_back_with_text);
        setRightMenuIcons(R.drawable.email_send_icon);
        //setTitle(R.string.combine_title);
        mEmailContent = get(R.id.content);
        mEmailTheme = get(R.id.text53);
        rvAttachment = get(R.id.rv_attachment);
        flAction = get(R.id.ll_action);

        //WebView横向滑动
        get(R.id.wv).setOnTouchListener(new View.OnTouchListener() {
            private float startx;
            private float starty;
            private float offsetx;
            private float offsety;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx = Math.abs(event.getX() - startx);
                        offsety = Math.abs(event.getY() - starty);
                        if (offsetx > offsety) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }

    /**
     * 选择自己发送邮件的账户
     *
     * @param listener
     */
    public void chooseEmailAccount(View.OnClickListener listener) {
        get(R.id.rl1).setOnClickListener(listener);
    }

    /**
     * 更改发送邮件账号
     *
     * @param email
     */
    public void setDefaultAccount(String email) {
        TextUtil.setText(((TextView) get(R.id.text13)), email);
    }

    /**
     * 获取邮件主题
     *
     * @return
     */
    public String getEmailTheme() {
        String emailTheme = "";
        String str = mEmailTheme.getText().toString();
        if (TextUtils.isEmpty(str)) {
            str = emailTheme;
        }
        return str;
    }

    /**
     * 初始邮件主题
     *
     * @param s
     */
    public void setEmailTheme(String s) {
        TextUtil.setText(mEmailTheme, s);
    }


    /**
     * 获取邮件正文
     *
     * @return
     */
    public String getEmailContent() {
        String str = mEmailContent.getText().toString();
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void setAttachmentAdapter(EmailAttachmentAdapter attachmentAdapter) {
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvAttachment.setAdapter(attachmentAdapter);
    }

    public void setRvListener(RecyclerView.OnItemTouchListener listener) {
        rvAttachment.addOnItemTouchListener(listener);
    }

    public void initAction(int tag) {
        flAction.removeAllViews();
        switch (tag) {
            case EmailConstant.DRAFT_BOX:
                mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_pg, null);
                mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                    commentEmail();
                });
                mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                    showMenu3();
                });
                flAction.addView(mActionView);
                flAction.setVisibility(View.VISIBLE);
                break;
            case EmailConstant.APPROVAL_FAILED:
                flAction.setVisibility(View.GONE);
                break;
            default:
                flAction.setVisibility(View.GONE);
                break;
        }


    }

    /**
     * 彻底删除
     */
    private void showMenu3() {

        PopUtils.showBottomMenu(getActivity(), getRootView(), "更多操作", new String[]{"彻底删除"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                ((EmailDetailActivity) getActivity()).clearMail();
                return false;
            }
        });

    }

    /**
     * 邮件评论
     */
    private void commentEmail() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, EmailConstant.BEAN_NAME);
        bundle.putString(Constants.DATA_ID, emailId);
        CommonUtil.startActivtiy(getActivity(), CommentActivity.class, bundle);
    }

    public void setListener(View.OnClickListener l1, View.OnClickListener l2) {
        mActionView.findViewById(R.id.rl_action1).setOnClickListener(l1);
        mActionView.findViewById(R.id.rl_action2).setOnClickListener(l2);

    }


    /**
     * 传数据
     *
     * @param bean
     */
    public void setData(EmailBean bean) {
        emailDetailBean = bean;
        emailId = bean.getId();

    }

    /**
     * 底部操作按钮
     *
     * @param b
     */
    public void showActionBar(boolean b) {
        if (b) {
            flAction.setVisibility(View.VISIBLE);
        } else {
            flAction.setVisibility(View.GONE);
        }

    }
}
