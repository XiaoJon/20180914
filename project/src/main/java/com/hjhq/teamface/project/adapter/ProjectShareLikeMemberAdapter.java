package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectMemberBean;

import java.util.List;

/**
 * 项目列表适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectShareLikeMemberAdapter extends BaseQuickAdapter<ProjectMemberBean, BaseViewHolder> {
    public ProjectShareLikeMemberAdapter(List<ProjectMemberBean> data) {
        super(R.layout.project_share_like_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectMemberBean item) {
        helper.setText(R.id.tv_name, item.getEmployee_name());
        ImageView avatar = helper.getView(R.id.iv_avatar);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), avatar, item.getName());

    }
}
