package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.QuoteTaskResultBean;

import java.util.List;


/**
 * 选择任务
 *
 * @author lx
 * @date 2017/5/15
 */

public class SelectTaskAdapter extends BaseQuickAdapter<QuoteTaskResultBean.DataBean.DataListBean, BaseViewHolder> {


    public SelectTaskAdapter(List<QuoteTaskResultBean.DataBean.DataListBean> data) {
        super(R.layout.project_item_select_task, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuoteTaskResultBean.DataBean.DataListBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView tvExecutor = helper.getView(R.id.tv_executor);
        TextView tvTime = helper.getView(R.id.tv_time);

        TextUtil.setText(tvTitle, item.getTask_name());
        TextUtil.setText(tvExecutor, "执行人: " + item.getEmployee_name());

        ivIcon.setSelected(item.isCheck());

        long createTime = TextUtil.parseLong(item.getEnd_time());
        if (createTime != 0) {
            String time = DateTimeUtil.longToStr(createTime, "yyyy-MM-dd HH:mm");
            TextUtil.setText(tvTime, "截止时间: " + time);
        }
    }
}
