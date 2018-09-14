package com.hjhq.teamface.memo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.memo.R;

import java.util.List;


public class MemoRelevanceAdapter extends BaseQuickAdapter<ModuleItemBean, BaseViewHolder> {
    private int type = 0;
    private boolean isContentChanged = false;

    public MemoRelevanceAdapter(int type, List<ModuleItemBean> data) {
        super(R.layout.memo_content_relevance, data);
        this.type = type;

    }


    @Override
    protected void convert(BaseViewHolder helper, ModuleItemBean item) {
        String title = item.getTitle() + ":";
        String subTitle = item.getSubTitle();
        SpannableString ss = new SpannableString(title + subTitle);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#17171A"));
        if (!TextUtils.isEmpty(item.getTitle())) {
            ss.setSpan(fcs, 0, title.length(), 0);
            ss.setSpan(new AbsoluteSizeSpan((int) DeviceUtils.dpToPixel(helper.getConvertView().getContext(), 16)), 0, title.length(), 0);
            helper.setText(R.id.tv_title, ss);
        } else {
            helper.setText(R.id.tv_title, item.getSubTitle());
        }
        ImageView delete = helper.getView(R.id.iv_delete);
        ImageView ivIcon = helper.getView(R.id.iv_type);
        Context context = ivIcon.getContext();
        ImageView avatar = helper.getView(R.id.iv_avatar);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), avatar, item.getCreatorName());
        if (type == 1) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> {
                remove(helper.getAdapterPosition());
                isContentChanged = true;
                if (mListener != null) {
                    mListener.onChange(getItemCount());
                }
                notifyDataSetChanged();
            });
        }
        ivIcon.setBackground(context.getResources().getDrawable(R.drawable.module_tag_stroke_bg));
        GradientDrawable drawable = (GradientDrawable) ivIcon.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.__picker_gray_f2));
        String iconColor = item.getIcon_color();
        drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));
        String imageUrl = item.getIcon_url();
        if ("1".equals(item.getIcon_type())) {
            ImageLoader.loadRoundImage(context, imageUrl, ivIcon, R.drawable.ic_image);
        } else if (TextUtil.isEmpty(imageUrl)) {
            ImageLoader.loadRoundImage(context, R.drawable.ic_image, ivIcon);
        } else {
            //将 - 转换成 _
            String replace = imageUrl.replace("-", "_");
            int resId = context.getResources().getIdentifier(replace, "drawable", context.getPackageName());
            ImageLoader.loadRoundImage(context, resId, ivIcon, R.drawable.ic_image);
        }
    }

    private OnDataSetChangeListener mListener;

    public void setOnDataSetChangeListener(OnDataSetChangeListener listener) {
        mListener = listener;
    }

    public boolean isContentChanged() {
        return isContentChanged;
    }

    public interface OnDataSetChangeListener {
        void onChange(int count);
    }
}