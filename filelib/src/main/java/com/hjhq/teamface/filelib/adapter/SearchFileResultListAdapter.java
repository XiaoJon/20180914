package com.hjhq.teamface.filelib.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.SearchFileListResBean;

import java.util.List;


public class SearchFileResultListAdapter extends BaseQuickAdapter<SearchFileListResBean.DataBean, BaseViewHolder> {
    int openitemPosition = -1;
    String keyword;


    public SearchFileResultListAdapter( List<SearchFileListResBean.DataBean> data) {
        super(R.layout.filelib_file_list_item, data);
        //super(R.layout.file_search_result_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, SearchFileListResBean.DataBean item) {
        helper.setVisible(R.id.rl_menu, false);
        if ("0".equals(item.getSign())) {
            //文件夹


            helper.setVisible(R.id.tv_folder_name, true);
            helper.setVisible(R.id.tv_file_name, false);
            helper.setVisible(R.id.tv_file_owner, true);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_file_size, false);
            helper.setText(R.id.tv_folder_name, TextUtil.getSpannableString(keyword, item.getName()));
            // TODO: 2018/1/24 文件上传人 
            helper.setText(R.id.tv_file_owner, item.getSiffix());
            helper.addOnClickListener(R.id.rl_menu);
            //1管理员,2成员,3公开
            item.getRole_type();
            helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
            try {
                helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor(item.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
                helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor("#F9B239"));

            }

           /* if (folderLevel == 0) {
                // 0 公有 1私有
                if ("0".equals(item.getType())) {
                    helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
                } else if ("1".equals(item.getType())) {
                    helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_head);
                } else {
                    helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
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
            }*/


        } else if ("1".equals(item.getSign())) {
            //文件
            ImageView icon = helper.getView(R.id.iv_conversation_avatar);
            helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor("#FFFFFF"));
            if (FileTypeUtils.isImage(item.getSiffix())) {
                String url = FileConstants.FILE_THUMB_BASE_URL + item.getId() + "&width=64&height=64&url=" + item.getUrl();
                ImageLoader.loadImage(helper.getConvertView().getContext(), url, icon, R.drawable.ic_image, R.drawable.ic_image);
            } else {
                ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(item.getSiffix()), icon);
            }

            helper.setVisible(R.id.tv_folder_name, false);
            helper.setVisible(R.id.rl_menu, false);
            helper.setVisible(R.id.tv_file_name, true);
            helper.setVisible(R.id.tv_file_owner, true);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_file_size, true);
            helper.setText(R.id.tv_file_name, TextUtil.getSpannableString(keyword, item.getName()));
            helper.setText(R.id.tv_file_owner, item.getEmployee_name() + " ");
            try {
                helper.setText(R.id.tv_time, DateTimeUtil.fromTime(Long.parseLong(item.getCreate_time())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(Long.parseLong(item.getSize())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}