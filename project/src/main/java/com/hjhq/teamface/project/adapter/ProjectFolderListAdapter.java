package com.hjhq.teamface.project.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectFolderListBean;

import java.util.List;

/**
 * 项目列表适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectFolderListAdapter extends BaseQuickAdapter<ProjectFolderListBean.DataBean.DataListBean, BaseViewHolder> {
    public ProjectFolderListAdapter(List<ProjectFolderListBean.DataBean.DataListBean> data) {
        super(R.layout.project_folder_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectFolderListBean.DataBean.DataListBean item) {
        helper.setText(R.id.tv_nav, item.getFile_name());
        ImageView icon = helper.getView(R.id.iv_nav);
        //0系统文件夹,1用户文件夹
        if ("0".equals(item.getLibrary_type())) {
            icon.setBackgroundColor(Color.parseColor("#FBB23B"));
        } else {
            icon.setBackgroundColor(Color.parseColor("#18CDA5"));
        }

    }
}
