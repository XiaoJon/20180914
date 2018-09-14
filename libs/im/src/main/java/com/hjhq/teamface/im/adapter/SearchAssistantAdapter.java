package com.hjhq.teamface.im.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class SearchAssistantAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    private String keyword;
    private int maxItemNum = -1;

    public SearchAssistantAdapter(List<Conversation> data) {
        super(R.layout.item_search_assistant, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        ImageView icon = helper.getView(R.id.icon);
        icon.setBackground(null);


        helper.setText(R.id.title, TextUtil.getSpannableString(keyword, item.getTitle()));
        helper.setText(R.id.number, item.getResultNum() + "条相关");
        int drawableRes = 0;
        switch (item.getTotalMsgNum()) {
            case MsgConstant.MODULE_ASSISTANT:
                //自定义模块助手
                icon.setBackground(icon.getContext().getResources().getDrawable(R.drawable.module_tag_round_bg));
                GradientDrawable drawable = (GradientDrawable) icon.getBackground();
                drawable.setColor(Color.RED);
                    /*if ("1".equals(item.getIcon_type())) {
                        int padding = (int) DeviceUtils.dpToPixel(mContext, 10);
                        ivAvatar.setPadding(padding, padding, padding, padding);
                    } else {
                        ivAvatar.setPadding(0, 0, 0, 0);
                    }*/

                String iconColor = item.getIcon_color();
                drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));

                String imageUrl = item.getIcon_url();
                if ("1".equals(item.getIcon_type())) {
                    ImageLoader.loadRoundImage(mContext, imageUrl, icon, R.drawable.ic_image);
                } else if (TextUtil.isEmpty(imageUrl)) {
                    ImageLoader.loadRoundImage(mContext, R.drawable.ic_image, icon);
                } else {
                    //将 - 转换成 _
                    String replace = imageUrl.replace("-", "_");
                    int resId = mContext.getResources().getIdentifier(replace, "drawable", mContext.getPackageName());
                    ImageLoader.loadRoundImage(mContext, resId, icon, R.drawable.ic_image);
                }
                break;
            case MsgConstant.IM_ASSISTANT:
                drawableRes = R.drawable.icon_chat_assistant;
                break;
            case MsgConstant.APPROVE_ASSISTANT:
                drawableRes = R.drawable.icon_approval_assistant;
                break;
            case MsgConstant.FILE_LIB_ASSISTANT:
                drawableRes = R.drawable.icon_file_assistant;
                break;
            case MsgConstant.EMAIL_ASSISTANT:
                drawableRes = R.drawable.icon_email_assistant;
                break;
            case MsgConstant.MEMO_ASSISTANT:
                drawableRes = R.drawable.icon_memo_assistant;
                break;
            case MsgConstant.TASK_ASSISTANT:
                drawableRes = R.drawable.icon_task_assistant;
                break;
        }

        if (drawableRes != 0) {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), drawableRes, icon);
        } else {
            String name = item.getTitle();
            if (TextUtils.isEmpty(name)) {
                name = "助手";
            } else {
                if (name.length() > 2) {
                    name = name.substring(0, 2);
                }
            }
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), "", icon, name);
        }
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

