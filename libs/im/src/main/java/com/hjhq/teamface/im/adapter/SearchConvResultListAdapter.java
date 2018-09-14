package com.hjhq.teamface.im.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.FormatTimeUtil;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.db.DBManager;

import java.util.List;


public class SearchConvResultListAdapter extends BaseQuickAdapter<SocketMessage, BaseViewHolder> {
    int openitemPosition = -1;
    BaseActivity mBaseActivity;


    public SearchConvResultListAdapter(BaseActivity activity, List<SocketMessage> data) {
        super(R.layout.im_search_result_list, data);
        this.mBaseActivity = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, SocketMessage item) {
        helper.setIsRecyclable(false);
        if (item.getChatType() == MsgConstant.GROUP) {
            ImageLoader.loadCircleImage(helper.getView(R.id.iv_conversation_avatar).getContext(), item.getSenderAvatar(),
                    helper.getView(R.id.iv_conversation_avatar), item.getSenderName());
        } else {
            String signId = item.getOneselfIMID();
            String avatarUrl = DBManager.getInstance().qureyAvatarUrl(signId);
            ImageLoader.loadCircleImage(helper.getView(R.id.iv_conversation_avatar).getContext(), avatarUrl,
                    helper.getView(R.id.iv_conversation_avatar), item.getSenderName());

        }
        helper.setText(R.id.tv_conversation_title, item.getSenderName() + "");
        if (TextUtils.isEmpty(item.getMsgContent())) {
            helper.setText(R.id.tv_last_message, "[" + item.getFileType() + "]" + item.getFileName());
        } else {
            helper.setText(R.id.tv_last_message, item.getMsgContent());
        }

        helper.setText(R.id.tv_last_msg_time, FormatTimeUtil.getNewChatTime(item.getServerTimes()));


    }

}