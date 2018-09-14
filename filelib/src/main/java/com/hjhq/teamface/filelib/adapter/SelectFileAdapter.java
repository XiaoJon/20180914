package com.hjhq.teamface.filelib.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.FileListResBean;

import java.util.List;


public class SelectFileAdapter extends BaseQuickAdapter<FileListResBean.DataBean.DataListBean, BaseViewHolder> {
    private int folderLevel;

    public SelectFileAdapter(int folderLevel, List<FileListResBean.DataBean.DataListBean> data) {
        super(R.layout.filelib_selcet_file_list_item, data);
        this.folderLevel = folderLevel;
    }


    @Override
    protected void convert(BaseViewHolder helper, FileListResBean.DataBean.DataListBean item) {

        if ("0".equals(item.getSign())) {
            //文件夹
            helper.setVisible(R.id.tv_folder_name, true);
            helper.setVisible(R.id.tv_file_name, false);
            helper.setVisible(R.id.tv_file_owner, false);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_file_size, false);
            helper.setText(R.id.tv_folder_name, item.getName());


            if (folderLevel == 0) {
                // 0 公有 1私有
                if ("0".equals(item.getType())) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_main, helper.getView(R.id.iv_conversation_avatar));
                } else if ("1".equals(item.getType())) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_head, helper.getView(R.id.iv_conversation_avatar));
                } else {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_main, helper.getView(R.id.iv_conversation_avatar));
                }
                try {
                    helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor(item.getColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                    helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor("#77ff99"));

                }
            } else if (folderLevel > 0) {
                helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
                try {
                    helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor(item.getColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                    helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor("#77ff99"));

                }
            }


        } else if ("1".equals(item.getSign())) {

            //文件
            String url = FileConstants.FILE_THUMB_BASE_URL + item.getId() + "&width=64&time=" + item.getCreate_time();
            ImageView icon = helper.getView(R.id.iv_conversation_avatar);
            icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (FileTypeUtils.isImage(item.getSiffix())) {
                ImageLoader.loadImage(helper.getConvertView().getContext(), icon, url, R.drawable.ic_image);
            } else {
                ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(item.getSiffix()), icon);
            }
            helper.setVisible(R.id.tv_folder_name, false);
            helper.setVisible(R.id.tv_file_name, true);
            helper.setVisible(R.id.tv_file_owner, true);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_file_size, true);
            helper.setText(R.id.tv_file_name, item.getName());
            helper.setText(R.id.tv_file_owner, item.getEmployee_name());
            helper.setText(R.id.tv_time, DateTimeUtil.fromTime(item.getCreate_time()));
            try {
                helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(Long.parseLong(item.getSize())));
            } catch (NumberFormatException e) {

            }
        }

    }

    public void setFolderLevel(int folderLevel) {
        this.folderLevel = folderLevel;
    }
}