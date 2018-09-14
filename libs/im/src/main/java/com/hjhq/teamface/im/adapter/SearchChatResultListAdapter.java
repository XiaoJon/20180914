package com.hjhq.teamface.im.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.db.DBManager;

import java.util.List;


public class SearchChatResultListAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    int openitemPosition = -1;
    BaseActivity mBaseActivity;
    String keyword;
    private int maxItemNum = -1;


    public SearchChatResultListAdapter(BaseActivity activity, List<Conversation> data) {
        super(R.layout.im_search_result_list, data);
        this.mBaseActivity = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        ImageView ivAvatar = helper.getView(R.id.iv_conversation_avatar);
        helper.setText(R.id.tv_conversation_title, TextUtil.getSpannableString(keyword, item.getTitle()));

        helper.setIsRecyclable(false);
        if (item.getConversationType() == MsgConstant.GROUP) {
            if (item.getGroupType() == 2) {
                ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_normal_group, ivAvatar);

            } else if (item.getGroupType() == 1) {
                ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_first_group, ivAvatar);
            }
        } else {
            String signId = item.getTargetId();
            String avatarUrl = DBManager.getInstance().qureyAvatarUrl(signId);
            ImageLoader.loadCircleImage(helper.getView(R.id.iv_conversation_avatar).getContext(), avatarUrl,
                    helper.getView(R.id.iv_conversation_avatar), item.getTitle());
        }


        if (item.getResultNum() > 1) {
            helper.setText(R.id.tv_last_message, item.getResultNum() + "条相关聊天记录");
            Log.e("条相关聊天记录 ==", "" + item.getResultNum());
        } else {
            if (TextUtils.isEmpty(item.getLatestText())) {
                helper.setText(R.id.tv_last_message, "[无消息]");
            } else {
                helper.setText(R.id.tv_last_message, item.getLatestText() + " ");
            }
        }
        helper.setVisible(R.id.tv_last_msg_time, false);


    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getMaxItemNum() {
        return maxItemNum;
    }

    public void setMaxItemNum(int maxItemNum) {
        this.maxItemNum = maxItemNum;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (maxItemNum > -1 && itemCount > maxItemNum) {
            return maxItemNum;
        }
        return itemCount;
    }
}