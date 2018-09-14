package com.hjhq.teamface.email.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.email.R;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class EmailListAdapter extends BaseQuickAdapter<EmailBean, BaseViewHolder> {
    private int boxTag;

    public EmailListAdapter(int boxTag, List<EmailBean> data) {
        super(R.layout.item_email, data);
        this.boxTag = boxTag;
    }


    @Override
    protected void convert(BaseViewHolder helper, EmailBean item) {
        ImageLoader.loadHoleImage(helper.getConvertView().getContext(), "", helper.getView(R.id.avatar_in_contacts_iv), item.getFrom_recipient());
        helper.setText(R.id.tv_account, item.getFrom_recipient() + " ");

        helper.setText(R.id.tv_title, item.getSubject() + "");
        if (TextUtils.isEmpty(item.getMail_content())) {
            helper.setText(R.id.tv_content, "[无内容]");
        } else {
            if (helper.getAdapterPosition() == 5) {

            }
            String content = Html.fromHtml(item.getMail_content()).toString();
            if (TextUtils.isEmpty(content)) {
                helper.setText(R.id.tv_content, "[无内容]");
            } else {
                //web端添加的标记
                content = content.replace("++--+!!!+--++", "");
                if (content.length() > 25) {
                    content = content.substring(0, 25);
                }
                content = content.replace("  ", "").replace("\n", "").replace("\r\n", "").replace("\t", "");
                helper.setText(R.id.tv_content, content);
            }
        }

        //未读状态
        helper.setVisible(R.id.read_state, false);
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        if ("1".equals(item.getIs_emergent())) {
            helper.setVisible(R.id.icon_emergency, true);
        } else {
            helper.setVisible(R.id.icon_emergency, false);
        }
        //紧急状态
        if ("1".equals(item.getIs_emergent())) {
            helper.setVisible(R.id.icon_emergency, true);
        } else {
            helper.setVisible(R.id.icon_emergency, false);
        }
        //已读未读
        if ("0".equals(item.getRead_status())) {
            helper.setVisible(R.id.read_state, true);
        } else {
            helper.setVisible(R.id.read_state, false);
        }
        //定时
        if ("1".equals(item.getTimer_status())) {
            helper.setVisible(R.id.iv_timer_state, true);
        } else {
            helper.setVisible(R.id.iv_timer_state, false);
        }
        //草稿
        if ("1".equals(item.getDraft_status())) {
            helper.setVisible(R.id.tv_draft_state, true);
            helper.setVisible(R.id.read_state, false);
        } else {
            helper.setVisible(R.id.tv_draft_state, false);
        }


        switch (boxTag) {
            case EmailConstant.RECEVER_BOX:
            case EmailConstant.UNREAD_BOX:
            case EmailConstant.TRASH_BOX:
                break;
            case EmailConstant.SENDED_BOX:
                //发送状态  // 0 未发送  1 已发送 2 部分发送
                helper.setVisible(R.id.iv_send_state, false);

                switch (item.getSend_status()) {
                    case "0":
                        helper.setVisible(R.id.iv_send_state, true);
                        helper.setImageResource(R.id.iv_send_state, R.drawable.email_has_bean_rejected_to_receive);
                        break;
                    case "1":
                        helper.setVisible(R.id.iv_send_state, false);
                        break;
                    case "2":
                        helper.setVisible(R.id.iv_send_state, true);
                        helper.setImageResource(R.id.iv_send_state, R.drawable.email_partly_send_success_state_icon);
                        break;
                    default:
                        helper.setVisible(R.id.iv_send_state, false);
                        break;
                }

                break;
            case EmailConstant.DRAFT_BOX:
                helper.setVisible(R.id.read_state, false);
                switch (item.getApproval_status()) {
                    case EmailConstant.APPROVAL_STATE_REJECTED:
                        //未通过
                        helper.setVisible(R.id.iv_approval_state, true);
                        helper.setImageResource(R.id.iv_approval_state, R.drawable.email_approval_state_rejected_icon);
                        break;
                    case EmailConstant.APPROVAL_STATE_PASS:
                        //已通过
                        helper.setVisible(R.id.iv_approval_state, true);
                        helper.setImageResource(R.id.iv_approval_state, R.drawable.email_approval_state_pass_icon);
                        break;
                    default:
                        helper.setVisible(R.id.iv_approval_state, false);
                        break;
                }
                break;
            case EmailConstant.DELETED_BOX:
                helper.setVisible(R.id.read_state, false);
                switch (item.getApproval_status()) {
                    case EmailConstant.APPROVAL_STATE_REJECTED:
                        //未通过
                        helper.setVisible(R.id.iv_approval_state, true);
                        helper.setImageResource(R.id.iv_approval_state, R.drawable.email_approval_state_rejected_icon);
                        break;
                    case EmailConstant.APPROVAL_STATE_PASS:
                        //已通过
                        helper.setVisible(R.id.iv_approval_state, true);
                        helper.setImageResource(R.id.iv_approval_state, R.drawable.email_approval_state_pass_icon);
                        break;
                    default:
                        helper.setVisible(R.id.iv_approval_state, false);
                        break;
                }
                break;
            default:

                break;


        }

    }
}

