package com.hjhq.teamface.common.adapter;

import android.text.SpannableString;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.bean.CommentDetailResultBean;
import com.hjhq.teamface.common.view.RichEditText;
import com.hjhq.teamface.emoji.EmojiUtil;

import java.io.IOException;
import java.util.List;

/**
 * 评论
 *
 * @author mou
 * @date 2017/4/5
 */

public class CommentAdapter extends BaseMultiItemQuickAdapter<CommentDetailResultBean.DataBean, BaseViewHolder> {
    private boolean isShowMore = true;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CommentAdapter(List<CommentDetailResultBean.DataBean> data) {
        super(data);
        addItemType(0, R.layout.custom_comment_item_move1);
        addItemType(1, R.layout.custom_comment_item_move2);
        addItemType(2, R.layout.custom_comment_item_move3);
        addItemType(3, R.layout.custom_comment_item_move4);
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return isShowMore ? itemCount : Math.min(itemCount, 5);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentDetailResultBean.DataBean item) {
        long time = TextUtil.parseLong(item.getDatetime_time());
        String date = "";
        if (time != 0) {
            date = DateTimeUtil.fromTime(time);
        }
        String name = item.getEmployee_name();
        List<UploadFileBean> information = item.getInformation();

        switch (item.getItemType()) {
            //文字类型评论
            case 0:
                //名字
                helper.setText(R.id.item_move1_tv_name, name);
                //头像
                ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.item_move1_iv_icon2), name);
                helper.addOnLongClickListener(R.id.item_move1_iv_icon2);
                //时间
                helper.setText(R.id.item_move1_tv_time2, date);
                //内容
                try {
                    String content = item.getContent();
                    SpannableString spannable = new SpannableString(content);
                    RichEditText.matchMention(spannable);
                    EmojiUtil.handlerEmojiText2(helper.getView(R.id.item_move1_tv_pubDesc), spannable, mContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            // 图片类型评论
            case 1: {
                UploadFileBean uploadFileBean = information.get(0);
                ImageLoader.loadImage(mContext, uploadFileBean.getFileUrl() + "&width=1080", helper.getView(R.id.item_move2_customImage), R.drawable.jmui_pick_picture, R.drawable.jmui_send_error);
                ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.item_move2_iv_icon2), name);
                helper.setText(R.id.item_move2_tv_name, name);
                helper.setText(R.id.item_move2_tv_time2, date);
                helper.getView(R.id.item_move2_customImage).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.item_move2_customImage);
                break;
            }
            //语音类型评论
            case 2: {
                UploadFileBean uploadFileBean = information.get(0);
                helper.setText(R.id.item_move3_tv_voive_time, Math.abs(uploadFileBean.getVoiceTime() / 1000) + "s");

                ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.item_move3_iv_icon2), name);
                helper.setText(R.id.item_move3_tv_name, name);
                helper.setText(R.id.item_move3_tv_time2, date);
                break;
            }
            //文件类型评论
            case 3: {
                UploadFileBean uploadFileBean = information.get(0);
                helper.setText(R.id.item_move4_tv_file_name, uploadFileBean.getFileName());

                String fileType = uploadFileBean.getFileType();
                int fileTypeIcon = FileTypeUtils.getFileTypeIcon(fileType);
                ImageLoader.loadImage(mContext, fileTypeIcon, helper.getView(R.id.item_move4_iv_file_type));

                ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.item_move4_iv_icon2), name);
                helper.setText(R.id.item_move4_tv_name, name);
                helper.setText(R.id.item_move4_tv_time2, date);
                helper.addOnClickListener(R.id.item_move4_iv_file_type);
                break;
            }
            default:
                break;
        }
    }


    public void setShowMore(boolean b) {
        isShowMore = b;
    }
}