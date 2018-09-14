package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.PersonalSubTaskBean;

import java.util.List;

/**
 * 子任务适配器
 *
 * @author Administrator
 * @date 2018/4/10
 */

public class PersonalSubTaskAdapter extends BaseQuickAdapter<PersonalSubTaskBean, BaseViewHolder> {
    public PersonalSubTaskAdapter(List<PersonalSubTaskBean> data) {
        super(R.layout.project_task_detail_subtask_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalSubTaskBean item) {
        TextView tvTaskName = helper.getView(R.id.tv_task_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        ImageView ivHead = helper.getView(R.id.iv_head);
        ImageView ivCheck = helper.getView(R.id.iv_check);

        TextUtil.setText(tvTaskName, item.getName());

        String taskTime = DateTimeUtil.longToStr(TextUtil.parseLong(item.getEnd_time()), "MM-dd HH:mm");
        TextUtil.setText(tvTime, taskTime);
        ivCheck.setSelected("1".equals(item.getComplete_status()));

        helper.addOnClickListener(R.id.iv_check);
        ImageLoader.loadCircleImage(mContext, item.getPicture(), ivHead, item.getEmployee_name());
    }
}
