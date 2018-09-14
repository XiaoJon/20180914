package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

/**
 * 小助手消息列表适配器
 *
 * @author Administrator
 */

public class AssistantListAdapter extends BaseQuickAdapter<PushMessage, BaseViewHolder> {

    public AssistantListAdapter(List<PushMessage> data) {
        super(R.layout.jmui_chat_item_assistant, data);
    }

    @Override
    protected void convert( BaseViewHolder helper, PushMessage item) {

        ImageView icon = helper.getView(R.id.jmui_recever_avatar_iv);
        loadImage(helper.getConvertView().getContext(), icon, item);

        helper.setText(R.id.jmui_display_name_tv, item.getBean_name_chinese());
        helper.setText(R.id.tv_receive_file_size, "时间:" + DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        helper.setVisible(R.id.red_dot, "0".equals(item.getRead_status()));
        helper.setText(R.id.jmui_send_time_txt, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));

        if (TextUtils.isEmpty(item.getPush_content())) {
            helper.setText(R.id.tv_name, item.getFieldInfo());
            helper.setVisible(R.id.tv_receive_file_sender_name, false);
        } else {
            helper.setText(R.id.tv_name, item.getPush_content());
            if (TextUtils.isEmpty(item.getFieldInfo())) {
                helper.setVisible(R.id.tv_receive_file_sender_name, false);
            } else {
                helper.setVisible(R.id.tv_receive_file_sender_name, true);
                helper.setText(R.id.tv_receive_file_sender_name, item.getFieldInfo() + "");
            }
        }
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param context
     * @param item
     * @return
     */
    public void loadImage(Context context, ImageView ivIcon, PushMessage item) {

        String english_name = item.getBean_name();
        if (english_name == null) english_name = "";
        int drawableRes = -1;
        ivIcon.setBackground(null);
        switch (english_name) {
            case ProjectConstants.PROJECT_BEAN_NAME:
            case ProjectConstants.TASK_MODULE_BEAN:
                ImageLoader.loadCircleImage(context, R.drawable.icon_task_assistant, ivIcon);
                break;
            case EmailConstant.BEAN_NAME:
                ImageLoader.loadCircleImage(context, R.drawable.icon_email_assistant, ivIcon);
                break;
            case FileConstants.BEAN_NAME:
                ImageLoader.loadCircleImage(context, R.drawable.icon_file_assistant, ivIcon);
                break;
            case MemoConstant.BEAN_NAME:
                ImageLoader.loadCircleImage(context, R.drawable.icon_memo_assistant, ivIcon);
                break;
            case ApproveConstants.APPROVAL_MODULE_BEAN:
                ImageLoader.loadCircleImage(context, R.drawable.icon_approval_assistant, ivIcon);
                break;
            case AttendanceConstants.BEAN_NAME:
                ImageLoader.loadCircleImage(context, R.drawable.module_icon_attendance, ivIcon);
                break;
            case "":
                switch (item.getType()) {
                    case MsgConstant.TYPE_GROUP_OPERATION:
                    case MsgConstant.TYPE_REMOVE_MEMBER:
                    case MsgConstant.TYPE_ADD_MEMBER:
                    case MsgConstant.TYPE_QUIT_GROUP:
                    case MsgConstant.TYPE_GROUP_INFO_CHANGED:
                        item.setBean_name_chinese("企信");
                        break;
                    case MsgConstant.TYPE_FRIEND_CIRCLE:
                        item.setBean_name_chinese("同事圈");
                        break;
                    default:
                        break;
                }
                drawableRes = R.drawable.icon_chat_assistant;
                ivIcon.setBackground(null);
                ImageLoader.loadRoundImage(context, drawableRes, ivIcon);
                break;
            default:
                switch (item.getType()) {
                    case MsgConstant.TYPE_APPROVE:
                    case MsgConstant.TYPE_APPROVE_OPERATION:
                        drawableRes = R.drawable.icon_approval_assistant;
                        break;
                    case MsgConstant.TYPE_FROM_FILE_LIB:
                        drawableRes = R.drawable.icon_file_assistant;
                        break;
                    case MsgConstant.TYPE_MEMO:
                        drawableRes = R.drawable.icon_memo_assistant;
                        break;
                    case MsgConstant.TYPE_EMAIL:
                        drawableRes = R.drawable.icon_email_assistant;
                        break;
                    case MsgConstant.TYPE_PROJECT_TAST_PUSH:
                    case MsgConstant.TYPE_PROJECT_TASK:
                    case MsgConstant.TYPE_AT_ME:
                        drawableRes = R.drawable.icon_task_assistant;
                        break;
                    default:
                        break;
                }

                if (drawableRes > 0) {
                    ivIcon.setBackground(null);
                    ImageLoader.loadCircleImage(context, drawableRes, ivIcon);
                    return;
                } else {
                    ivIcon.setBackground(context.getResources().getDrawable(R.drawable.module_tag_round_bg));
                    GradientDrawable drawable = (GradientDrawable) ivIcon.getBackground();
                    drawable.setColor(context.getResources().getColor(R.color.__picker_gray_f2));
                    String iconColor = item.getIcon_color();
                    drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));
                    String imageUrl = item.getIcon_url();
                    if ("1".equals(item.getIcon_type())) {
                        ImageLoader.loadCircleImage(context, imageUrl, ivIcon, R.drawable.ic_image);
                        return;
                    } else if (TextUtil.isEmpty(imageUrl)) {
                        ImageLoader.loadCircleImage(context, R.drawable.ic_image, ivIcon);
                        return;
                    } else {
                        //将 - 转换成 _
                        String replace = imageUrl.replace("-", "_");
                        int resId = context.getResources().getIdentifier(replace, "drawable", context.getPackageName());
                        ImageLoader.loadCircleImage(context, resId, ivIcon, R.drawable.ic_image);
                        return;
                    }

                }

        }
    }
}
