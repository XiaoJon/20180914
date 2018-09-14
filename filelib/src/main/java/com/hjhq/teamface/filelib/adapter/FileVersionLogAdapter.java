package com.hjhq.teamface.filelib.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.NumberFormatUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.FileVersionLogBean;

import java.util.List;


public class FileVersionLogAdapter extends BaseQuickAdapter<FileVersionLogBean.DataBean, BaseViewHolder> {



    public FileVersionLogAdapter(List<FileVersionLogBean.DataBean> data) {
        super(R.layout.filelib_version_log_list_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FileVersionLogBean.DataBean item) {
        helper.setText(R.id.tv_folder_name, item.getEmployee_name());
        if (helper.getAdapterPosition() == 0) {
            helper.getView(R.id.tv_file_version).setSelected(true);
            helper.setBackgroundRes(R.id.tv_file_version, R.drawable.filelib_version_selected);
            helper.setTextColor(R.id.tv_file_version, Color.parseColor("#ffffff"));
            helper.setText(R.id.tv_file_version, "当前版本");

        } else {

            helper.getView(R.id.tv_file_version).setSelected(false);
            helper.setTextColor(R.id.tv_file_version, Color.parseColor("#FF8E53"));
            helper.setBackgroundRes(R.id.tv_file_version, R.drawable.filelib_version_unselected);
            helper.setText(R.id.tv_file_version, String.format(helper.getConvertView().getContext().getString(R.string.filelib_version), NumberFormatUtil.integerToString(getItemCount() - helper.getAdapterPosition())));

        }
        helper.setText(R.id.tv_file_name, item.getName());
        helper.setText(R.id.tv_file_owner, item.getEmployee_name());
        try {
            helper.setText(R.id.tv_time, DateTimeUtil.longToStr(Long.parseLong(item.getMidf_time()), "yyyy-MM-dd HH:mm"));
        } catch (NumberFormatException e) {

        }
        try {
            helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(Long.parseLong(item.getSize())));
        } catch (NumberFormatException e) {
            helper.setText(R.id.tv_file_size, "未知大小");
        }
        ImageView icon = helper.getView(R.id.iv_conversation_avatar);
        if (FileTypeUtils.isImage(item.getSuffix())) {
            String url = Constants.BASE_URL + "library/file/downloadCompressedPicture?id=" + item.getId() + "&width=64&type=2&time=" + item.getMidf_time();

            ImageLoader.loadImage(helper.getConvertView().getContext(), url, icon, R.drawable.ic_image, R.drawable.ic_image);
        } else {
            ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(item.getSuffix()), icon);
        }

    }

}