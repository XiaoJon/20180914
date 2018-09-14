package com.hjhq.teamface.view.quickaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class ActionAdapter extends BaseAdapter {

    private Context context;

    private List<ActionItem> actionItems = new ArrayList<>();

    public ActionAdapter(Context context, List<ActionItem> actionItems) {
        super();
        this.context = context;
        this.actionItems = actionItems;
    }

    @Override
    public int getCount() {
        return actionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return actionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.action_item_horizontal, null);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            holder.tv_title = convertView
                    .findViewById(R.id.tv_title);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        ActionItem actionItem = actionItems.get(position);
        loadImage(context, holder.iv_icon, actionItem);
        holder.tv_title.setText(actionItem.getTitle());

        return convertView;
    }

    class Holder {
        ImageView iv_icon;
        TextView tv_title;
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param context
     * @param item
     * @return
     */
    public void loadImage(Context context, ImageView ivIcon, ActionItem item) {
        GradientDrawable drawable = (GradientDrawable) ivIcon.getBackground();
        drawable.setColor(Color.WHITE);
        String english_name = item.getModuleBean();
        if (english_name == null) english_name = "";
        switch (english_name) {
            case ProjectConstants.PROJECT_BEAN_NAME:
            case ProjectConstants.TASK_MODULE_BEAN:
                ImageLoader.loadRoundImage(context, R.drawable.icon_nav_project, ivIcon);
                break;
            case EmailConstant.BEAN_NAME:
                ImageLoader.loadRoundImage(context, R.drawable.module_icon_email, ivIcon);
                break;
            case FileConstants.BEAN_NAME:
                ImageLoader.loadRoundImage(context, R.drawable.icon_wenjianku1, ivIcon);
                break;
            case MemoConstant.BEAN_NAME:
                ImageLoader.loadRoundImage(context, R.drawable.icon_system_library_4, ivIcon);
                break;
            case ApproveConstants.APPROVAL_MODULE_BEAN:
                ImageLoader.loadRoundImage(context, R.drawable.icon_shenpi, ivIcon);
                break;
            case AttendanceConstants.BEAN_NAME:
                ImageLoader.loadRoundImage(context, R.drawable.module_icon_attendance, ivIcon);
                break;
            default:
                String iconColor = item.getIconColor();
                if (!TextUtil.isEmpty(iconColor)) {
                    drawable.setColor(ColorUtils.hexToColor(iconColor,"#3689E9"));
                }
                String imageName = item.getIcon();
                if ("1".equals(item.getIconType())) {
                    ImageLoader.loadRoundImage(context, imageName, ivIcon, R.drawable.ic_image);
                } else if (TextUtil.isEmpty(imageName)) {
                    ImageLoader.loadRoundImage(context, R.drawable.ic_image, ivIcon);
                } else {
                    //将 - 转换成 _
                    String replace = imageName.replace("-", "_");
                    int resId = context.getResources().getIdentifier(replace, "drawable", context.getPackageName());
                    ImageLoader.loadRoundImage(context, resId, ivIcon, R.drawable.ic_image);
                }
                break;
        }
    }
}
