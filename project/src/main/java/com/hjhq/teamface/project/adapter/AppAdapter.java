package com.hjhq.teamface.project.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * 全部应用的适配器
 *
 * @author lx
 * @date 2017/5/15
 */

public class AppAdapter extends BaseQuickAdapter<AppModuleBean, BaseViewHolder> {

    public AppAdapter() {
        this(null);
    }

    public AppAdapter(List<AppModuleBean> data) {
        super(R.layout.item_all_function, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppModuleBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvName = helper.getView(R.id.tv_name);
        View iconBg = helper.getView(R.id.rl_icon);

        tvName.setText(item.getChinese_name());
        loadImage(ivIcon, iconBg, item);
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param item
     * @param iconBg
     * @return
     */
    public void loadImage(ImageView ivIcon, View iconBg, AppModuleBean item) {
        GradientDrawable drawable = (GradientDrawable) iconBg.getBackground();
        drawable.setColor(Color.WHITE);

        String english_name = item.getEnglish_name();
        if (english_name == null) english_name = "";
        switch (english_name) {
            case ProjectConstants.PROJECT_BEAN_NAME:
            case ProjectConstants.TASK_MODULE_BEAN:
                ImageLoader.loadRoundImage(mContext, R.drawable.icon_nav_project, ivIcon);
                break;
            case EmailConstant.BEAN_NAME:
                ImageLoader.loadRoundImage(mContext, R.drawable.module_icon_email, ivIcon);
                break;
            case FileConstants.BEAN_NAME:
                ImageLoader.loadRoundImage(mContext, R.drawable.icon_wenjianku1, ivIcon);
                break;
            case MemoConstant.BEAN_NAME:
                ImageLoader.loadRoundImage(mContext, R.drawable.icon_system_library_4, ivIcon);
                break;
            case ApproveConstants.APPROVAL_MODULE_BEAN:
                ImageLoader.loadRoundImage(mContext, R.drawable.icon_shenpi, ivIcon);
                break;
            case AttendanceConstants.BEAN_NAME:
                ImageLoader.loadRoundImage(mContext, R.drawable.module_icon_attendance, ivIcon);
                break;
            default:
                if (TextUtil.isEmpty(item.getApplication_id())) {
                    int padding = (int) DeviceUtils.dpToPixel(mContext, 5);
                    iconBg.setPadding(padding, padding, padding, padding);
                } else if ("1".equals(item.getIcon_type())) {
                    int padding = (int) DeviceUtils.dpToPixel(mContext, 10);
                    iconBg.setPadding(padding, padding, padding, padding);
                } else {
                    iconBg.setPadding(0, 0, 0, 0);
                }

                String iconColor = item.getIcon_color();
                drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));

                String imageUrl = item.getIcon_url();
                if ("1".equals(item.getIcon_type())) {
                    ImageLoader.loadRoundImage(mContext, imageUrl, ivIcon, R.drawable.ic_image);
                } else if (TextUtil.isEmpty(imageUrl)) {
                    ImageLoader.loadRoundImage(mContext, R.drawable.ic_image, ivIcon);
                } else {
                    //将 - 转换成 _
                    String replace = imageUrl.replace("-", "_");
                    int resId = mContext.getResources().getIdentifier(replace, "drawable", mContext.getPackageName());
                    ImageLoader.loadRoundImage(mContext, resId, ivIcon, R.drawable.ic_image);
                }
                break;
        }
    }
}
