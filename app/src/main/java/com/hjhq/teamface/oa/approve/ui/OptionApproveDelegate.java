package com.hjhq.teamface.oa.approve.ui;

import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;


/**
 * 操作审批 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class OptionApproveDelegate extends AppDelegate {

    private View llTopSelecter;
    private TextView tvTopTitle;
    private TextView tvTopContent;
    private TextView tvTag;
    private EditText etContent;
    private View llAddApprover;
    private TextView tvAddApproverTitle;
    public MembersView mMemberView;
    private View llBottomSelecter;
    private TextView tvBottomTitle;
    private TextView tvBottomContent;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_option_approve;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.main_green, "确定");
        llTopSelecter = get(R.id.ll_top_selecter);
        tvTopTitle = get(R.id.tv_top_title);
        tvTopContent = get(R.id.tv_top_content);
        tvTag = get(R.id.tv_tag);
        etContent = get(R.id.et_content);
        llAddApprover = get(R.id.ll_add_approver);
        tvAddApproverTitle = get(R.id.tv_add_approver_title);
        mMemberView = get(R.id.member_view);
        llBottomSelecter = get(R.id.ll_bottom_selecter);
        tvBottomTitle = get(R.id.tv_bottom_title);
        tvBottomContent = get(R.id.tv_bottom_content);
    }

    /**
     * 设置顶部内容
     *
     * @param content
     */
    public void setTopContent(String content) {
        TextUtil.setText(tvTopContent, content);
    }

    /**
     * 设置底部内容
     *
     * @param content
     */
    public void setBottomContent(String content) {
        TextUtil.setText(tvBottomContent, content);
    }

    /**
     * 显示催办界面
     */
    public void showUrd() {
        setTitle("审批催办");
        setTitle(tvTag, "催办原因", true);
        TextUtil.setHint(etContent, "请输入50字以内（必填）");
    }

    /**
     * 显示 通过 界面
     */
    public void showPass() {
        setTitle("审批通过");
        setTitle(tvTopTitle, "通过方式", true);
        TextUtil.setHint(tvTopContent, "请选择");

        setTitle(tvTag, "审批意见", false);
        TextUtil.setHint(etContent, "请输入200字以内（选填）");

        setTitle(tvAddApproverTitle, "下一节点审批人", true);
    }

    /**
     * 显示转交界面
     */
    public void showTransfer() {
        setTitle("转交");

        setTitle(tvTag, "转交理由", false);
        TextUtil.setHint(etContent, "请输入50字以内");

        setTitle(tvAddApproverTitle, "审批人", true);
        setAddApproverVisibility(View.VISIBLE);
    }

    /**
     * 显示驳回界面
     */
    public void showReject() {
        setTitle("审批驳回");
        setTitle(tvTopTitle, "驳回方式", true);
        TextUtil.setHint(tvTopContent, "请选择");

        setTitle(tvTag, "审批意见", false);
        TextUtil.setHint(etContent, "请输入200字以内（选填）");

        setTitle(tvBottomTitle, "驳回节点", true);
        TextUtil.setHint(tvBottomContent, "请选择");
    }

    /**
     * 设置顶部选择器是否可见
     *
     * @param visibility
     */
    public void setTopSelecterVisibility(int visibility) {
        llTopSelecter.setVisibility(visibility);
    }

    /**
     * 设置加人控件是否可见
     *
     * @param visibility
     */
    public void setAddApproverVisibility(int visibility) {
        llAddApprover.setVisibility(visibility);
    }

    /**
     * 加人控件是否可见
     */
    public int getAddApproverVisibility() {
        return llAddApprover.getVisibility();
    }

    /**
     * 设置底部选择器是否可见
     *
     * @param visibility
     */
    public void setBottomSelecterVisibility(int visibility) {
        llBottomSelecter.setVisibility(visibility);
    }


    /**
     * 根据是否必填来设置 *
     *
     * @param textView
     * @param title
     * @param isMust
     */
    private void setTitle(TextView textView, String title, boolean isMust) {
        if (isMust) {
            title = String.format("<font color='#F94C4A'>*</font>%s", title);
            textView.setText(Html.fromHtml(title));
            return;
        } else {
            title = "  " + title;
            TextUtil.setText(textView, title);
        }
    }

    /**
     * 获取内容
     */
    public String getContent() {
        return etContent.getText().toString().trim();
    }
}
