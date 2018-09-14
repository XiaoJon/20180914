package com.hjhq.teamface.oa.approve.adapter;

import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.utils.ApproveUtils;

import java.util.List;


/**
 * 选择审批列表适配器
 *
 * @author lx
 * @date 2017/5/15
 */

public class SelectApproveAdapter extends BaseQuickAdapter<ApproveListBean, BaseViewHolder> {


    public SelectApproveAdapter(List<ApproveListBean> data) {
        super(R.layout.item_select_approve, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApproveListBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView tvApproveState = helper.getView(R.id.tv_approve_state);
        TextView tvTime = helper.getView(R.id.tv_time);

        TextUtil.setText(tvTitle, item.getBegin_user_name() + "-" + item.getProcess_name());
        TextUtil.setText(tvApproveState, ApproveUtils.state2String(item.getProcess_status()));

        //设置审批状态颜色
        GradientDrawable myGrad = (GradientDrawable) tvApproveState.getBackground();
        String color = ApproveUtils.state2Color(item.getProcess_status());
        myGrad.setColor(ColorUtils.hexToColor(color));

        ivIcon.setSelected(item.isCheck());

        long createTime = TextUtil.parseLong(item.getCreate_time());
        if (createTime != 0) {
            String time = DateTimeUtil.longToStr(createTime, "yyyy-MM-dd HH:mm");
            TextUtil.setText(tvTime, "申请时间: " + time);
        }
    }
}
