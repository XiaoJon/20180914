package com.hjhq.teamface.filelib.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.FileListResBean;

import java.util.List;


public class MoveFileListAdapter extends BaseQuickAdapter<FileListResBean.DataBean.DataListBean, BaseViewHolder> {
    int folderLevel;

    public MoveFileListAdapter(int folderLevel, List<FileListResBean.DataBean.DataListBean> data) {
        super(R.layout.filelib_move_file_item, data);
        this.folderLevel = folderLevel;
    }


    @Override
    protected void convert(BaseViewHolder helper, FileListResBean.DataBean.DataListBean item) {
        helper.setText(R.id.tv_conversation_title, item.getName());
        //((CheckBox) helper.getView(R.id.check_btn)).setChecked(item.isChecked());
        helper.setVisible(R.id.check_btn, item.isChecked());

        helper.addOnClickListener(R.id.bg);

        if (folderLevel == 0) {
            if ("1".equals(item.getType())) {
                helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_head);
            }
            // 0 公有 1私有
            if ("0".equals(item.getType())) {
                helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
            } else {
                helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
            }
        }
        if (folderLevel > 0) {
            helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_main);
        }

        /*else if ("1".equals(item.getType())) {
            helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.file_head);
        } */


        try {
            helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor(item.getColor()));
        } catch (Exception e) {
            e.printStackTrace();
            helper.setBackgroundColor(R.id.iv_conversation_avatar, Color.parseColor("#77ff99"));

        }


    }

}