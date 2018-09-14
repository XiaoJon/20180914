package com.hjhq.teamface.project.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;

import java.util.List;

/**
 * 项目成员适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectMemberAdapter extends BaseQuickAdapter<ProjectMemberResultBean.DataBean.DataListBean, BaseViewHolder> {
    public ProjectMemberAdapter(List<ProjectMemberResultBean.DataBean.DataListBean> data) {
        super(R.layout.project_member_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectMemberResultBean.DataBean.DataListBean item) {
        if ("0".equals(item.getIs_enable())) {
            //未激活
            helper.setTextColor(R.id.tv_nav, ColorUtils.resToColor(mContext, R.color.gray_90));
            helper.setText(R.id.tv_nav, item.getEmployee_name() + "(未激活)");
        } else {
            helper.setTextColor(R.id.tv_nav, ColorUtils.resToColor(mContext, R.color.black_17));
            helper.setText(R.id.tv_nav, item.getEmployee_name());
        }

        if ("1".equals(item.getProject_role())) {
            helper.setVisible(R.id.tv_admin, true);
        } else {
            helper.setVisible(R.id.tv_admin, false);
        }
        ImageLoader.loadCircleImage(mContext, item.getEmployee_pic(), helper.getView(R.id.iv_nav), item.getEmployee_name());

    }
}
