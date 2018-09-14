package com.hjhq.teamface.im.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.im.R;

import java.util.List;


public class ChatFileListAdapter extends BaseQuickAdapter<SocketMessage, BaseViewHolder> {


    public ChatFileListAdapter(List<SocketMessage> data) {
        super(R.layout.file_list_item, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, SocketMessage item) {

        helper.setVisible(R.id.tv_folder_name, false);
        helper.setVisible(R.id.tv_file_name, true);
        helper.setVisible(R.id.tv_file_owner, true);
        helper.setVisible(R.id.tv_time, true);
        helper.setVisible(R.id.tv_file_size, true);
        helper.setText(R.id.tv_file_name, item.getFileName());
        helper.setText(R.id.tv_file_owner, item.getSenderName());
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(item.getServerTimes()));
        helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(item.getFileSize()));
        helper.setVisible(R.id.rl_menu, false);

           /* try {
                helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(item.getTotalSpace()));
            } catch (NumberFormatException e) {

            }*/
        helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.icon_ai);
          /*  if(FileTypeUtils.isImage(item.)){

            }*/
        ImageView icon = helper.getView(R.id.iv_conversation_avatar);
        String fileUrl = item.getFileUrl() + "";
        if (FileTypeUtils.isImage(item.getFileType())) {
            ImageLoader.loadImage(helper.getConvertView().getContext(), icon, fileUrl, R.drawable.ic_image);
        } else {
            ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(item.getFileType()), icon);
        }

    }

}