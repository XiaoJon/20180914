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


public class FileListAdapter extends BaseQuickAdapter<FileListResBean.DataBean.DataListBean, BaseViewHolder> {
    int folderLevel;
    int folderStyle;


    public FileListAdapter(int folderLevel, int style, List<FileListResBean.DataBean.DataListBean> data) {

        super(R.layout.filelib_file_list_item, data);
        this.folderLevel = folderLevel;
        this.folderStyle = style;
    }


    @Override
    protected void convert(BaseViewHolder helper, FileListResBean.DataBean.DataListBean item) {

        ImageView avatar = helper.getView(R.id.iv_conversation_avatar);
        if ("0".equals(item.getSign())) {
            //文件夹


            if (folderStyle == 1) {
                if ("1".equals(item.getIs_manage())) {
                    helper.addOnClickListener(R.id.rl_menu);
                    helper.setVisible(R.id.rl_menu, true);
                } else {

                    helper.setVisible(R.id.rl_menu, false);
                }

            } else if (folderStyle == 4 || folderStyle == 5) {

                if (folderLevel < 1) {
                    helper.addOnClickListener(R.id.rl_menu);
                    helper.setVisible(R.id.rl_menu, true);
                }

            } else if (folderStyle == 3) {
                helper.addOnClickListener(R.id.rl_menu);
                helper.setVisible(R.id.rl_menu, true);
            }
            helper.setVisible(R.id.tv_folder_name, true);
            helper.setVisible(R.id.tv_file_name, false);
            helper.setVisible(R.id.tv_file_owner, false);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_file_size, false);
            helper.setText(R.id.tv_folder_name, item.getName());


            if (folderLevel == 0) {
                // 0 公有 1私有
                if ("0".equals(item.getType())) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_main, avatar);
                } else if ("1".equals(item.getType())) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_head, avatar);
                } else {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.file_main, avatar);
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
            avatar.setBackgroundResource(R.color.white);
            //文件
            String url = FileConstants.FILE_THUMB_BASE_URL + item.getId() + "&width=64&time=" + item.getCreate_time();
            /*if (!TextUtils.isEmpty(item.getFile_id())) {
                url = FileConstants.FILE_THUMB_BASE_URL + item.getFile_id() + "&width=64&time=" + item.getCreate_time();
            }*/
            if (FileTypeUtils.isImage(item.getSiffix())) {
                ImageLoader.loadImage(helper.getConvertView().getContext(), avatar, url, R.drawable.icon_jpg);
            } else {
                ImageLoader.loadImage(helper.getConvertView().getContext(), avatar, "", FileTypeUtils.getFileTypeIcon(item.getSiffix()));
            }

            if (folderLevel < 1) {
                if (folderStyle == 4 || folderStyle == 5) {
                    helper.addOnClickListener(R.id.rl_menu);
                    helper.setVisible(R.id.rl_menu, true);
                } else {
                    helper.setVisible(R.id.rl_menu, false);
                }
            } else {
                helper.setVisible(R.id.rl_menu, false);
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

}