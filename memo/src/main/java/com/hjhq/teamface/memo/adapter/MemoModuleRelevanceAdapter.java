package com.hjhq.teamface.memo.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.memo.R;

import java.util.List;


public class MemoModuleRelevanceAdapter extends BaseQuickAdapter<AppModuleBean, BaseViewHolder> {


    public MemoModuleRelevanceAdapter(List<AppModuleBean> data) {
        super(R.layout.memo_choose_relevance_item, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, AppModuleBean item) {
        helper.setText(R.id.tv_nav, item.getChinese_name());
        ImageView ivIcon = helper.getView(R.id.iv_nav);
        Context context = helper.getConvertView().getContext();
        ivIcon.setBackground(context.getResources().getDrawable(R.drawable.module_tag_stroke_bg));
        GradientDrawable drawable = (GradientDrawable) ivIcon.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.__picker_gray_f2));
        String iconColor = item.getIcon_color();
        drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));
        String imageUrl = item.getIcon_url();
        if ("1".equals(item.getIcon_type())) {
            ImageLoader.loadRoundImage(context, imageUrl, ivIcon, R.drawable.ic_image);
            return;
        } else if (TextUtil.isEmpty(imageUrl)) {
            ImageLoader.loadRoundImage(context, R.drawable.ic_image, ivIcon);
            return;
        } else {
            //将 - 转换成 _
            String replace = imageUrl.replace("-", "_");
            int resId = context.getResources().getIdentifier(replace, "drawable", context.getPackageName());
            ImageLoader.loadRoundImage(context, resId, ivIcon, R.drawable.ic_image);
            return;
        }
    }
}