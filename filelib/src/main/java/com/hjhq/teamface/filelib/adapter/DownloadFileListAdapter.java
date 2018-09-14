package com.hjhq.teamface.filelib.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.filelib.R;

import java.io.File;
import java.util.List;


public class DownloadFileListAdapter extends BaseQuickAdapter<File, BaseViewHolder> {


    public DownloadFileListAdapter(List<File> data) {
        super(R.layout.filelib_file_list_item, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, File item) {

        if (item.isFile()) {

            //文件

            helper.setVisible(R.id.tv_folder_name, false);
            helper.setVisible(R.id.tv_file_name, true);
            helper.setVisible(R.id.tv_file_owner, true);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_file_size, true);
            helper.setText(R.id.tv_file_name, item.getName());
            helper.addOnClickListener(R.id.rl_menu);
            helper.setVisible(R.id.rl_menu, true);
            try {
                helper.setText(R.id.tv_file_owner, FormatFileSizeUtil.formatFileSize(FileUtils.getFileSize(item)));
            } catch (Exception e) {

            }
           /* try {
                helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(item.getTotalSpace()));
            } catch (NumberFormatException e) {

            }*/
            helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.icon_ai);
          /*  if(FileTypeUtils.isImage(item.)){

            }*/


        }


    }

}