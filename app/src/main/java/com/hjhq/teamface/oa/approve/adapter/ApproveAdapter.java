package com.hjhq.teamface.oa.approve.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.utils.ApproveUtils;

import java.util.List;


/**
 * 审批列表适配器
 *
 * @author lx
 * @date 2017/5/15
 */

public class ApproveAdapter extends BaseQuickAdapter<ApproveListBean, BaseViewHolder> {


    public ApproveAdapter(List<ApproveListBean> data) {
        super(R.layout.item_approve, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApproveListBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ImageView ivUnread = helper.getView(R.id.iv_unread);
        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView tvApproveState = helper.getView(R.id.tv_approve_state);
        TextView tvTime = helper.getView(R.id.tv_time);

        TextUtil.setText(tvTitle, item.getBegin_user_name() + "-" + item.getProcess_name());
        TextUtil.setText(tvApproveState, ApproveUtils.state2String(item.getProcess_status()));

        //设置审批状态颜色
        GradientDrawable myGrad = (GradientDrawable) tvApproveState.getBackground();
        String color = ApproveUtils.state2Color(item.getProcess_status());
        myGrad.setColor(ColorUtils.hexToColor(color));


        ImageLoader.loadCircleImage(mContext, item.getPicture(), ivIcon, item.getBegin_user_name());
        long create_time = TextUtil.parseLong(item.getCreate_time());
        if (create_time != 0) {
            String time = DateTimeUtil.longToStr(create_time, "yyyy-MM-dd HH:mm");
            TextUtil.setText(tvTime, "申请时间: " + time);
        }
        boolean isUnread = ApproveConstants.UNREAD.equals(item.getStatus());
        ivUnread.setVisibility(isUnread ? View.VISIBLE : View.GONE);
    }
}
