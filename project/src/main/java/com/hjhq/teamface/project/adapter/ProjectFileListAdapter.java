package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.FileIconHelper;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectFileListBean;

import java.util.List;

/**
 * 项目列表适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectFileListAdapter extends BaseQuickAdapter<ProjectFileListBean.DataBean.DataListBean, BaseViewHolder> {
    String folderType;
    String projectId;

    public ProjectFileListAdapter(String projectId, String folderType, List<ProjectFileListBean.DataBean.DataListBean> data) {
        super(R.layout.project_file_list_item, data);
        this.folderType = folderType;
        this.projectId = projectId;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectFileListBean.DataBean.DataListBean item) {
        helper.setVisible(R.id.rl_menu, false);
        helper.addOnClickListener(R.id.rl_menu);
        helper.setText(R.id.tv_file_name, item.getFile_name());
        helper.setText(R.id.tv_file_owner, item.getEmployee_name());
        helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(TextUtil.parseLong(item.getSize())));
        helper.setText(R.id.tv_time, DateTimeUtil.longToStr(TextUtil.parseLong(item.getCreate_time()), "yyyy-MM-dd"));
        ImageView icon = helper.getView(R.id.iv_conversation_avatar);
        if (FileTypeUtils.isImage(item.getSuffix())) {
            String path = String.format(ProjectConstants.PROJECT_FILE_DOWNLOAD_URL, item.getId(), projectId) + "&width=64";
            ImageLoader.loadImage(helper.getConvertView().getContext(), icon, path, R.drawable.ic_image);
        } else {
            ImageLoader.loadImage(helper.getConvertView().getContext(), FileIconHelper.getFileIcon(item.getSuffix()), icon);
        }
    }


}
