package com.hjhq.teamface.filelib.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.ProjectListBean;

import java.util.List;


public class ProjectFolderAdapter extends BaseQuickAdapter<ProjectListBean.DataBean.DataListBean, BaseViewHolder> {

    public ProjectFolderAdapter(List<ProjectListBean.DataBean.DataListBean> data) {
        super(R.layout.filelib_project_folder_item, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, ProjectListBean.DataBean.DataListBean item) {
        helper.setText(R.id.tv_nav, item.getFile_name());
        helper.setVisible(R.id.iv_nav, true);
        ImageView icon = helper.getView(R.id.iv_nav);
        icon.setImageResource(R.drawable.file_main);
        icon.setBackgroundColor(Color.parseColor("#FBB23B"));
    }
}