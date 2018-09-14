package com.hjhq.teamface.common.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.util.DrawableUtils;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.view.image.SubsamplingScaleImageView;
import com.hjhq.teamface.common.R;

import java.util.List;

/**
 * @author Administrator
 * @绑定ViewPager 适配器
 */
public class ImageItemAdapter extends PagerAdapter {

    /**
     * 动态布局加载器
     */
    private LayoutInflater inflater;

    /**
     * 图片列表
     */
    private List<Photo> imageitems;

    /**
     * 当前context
     */
    private Activity context;

    public ImageItemAdapter(Context context, List<Photo> imageitems) {
        this.imageitems = imageitems;
        inflater = LayoutInflater.from(context);
        this.context = (Activity) context;
    }

    @Override
    public int getCount() {
        return imageitems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int positions) {
        // 加载 item_pager_image
        View imageLayout = inflater.inflate(R.layout.item_pager_image, view,
                false);
        imageLayout.setId(positions);
        // 得到item_pager_image中ImageView
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) imageLayout
                .findViewById(R.id.i_image);
        imageView.setMinimumDpi(50);
        imageView
                .setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        imageView.setOnClickListener(arg0 -> context.finish());
        Photo photo = imageitems.get(positions);

        ViewTarget viewTarget = new ViewTarget<SubsamplingScaleImageView, GlideDrawable>(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Bitmap bitmap = DrawableUtils.drawable2Bitmap(resource.getCurrent());
                this.view.setImageBitmap(bitmap);
            }
        };
        String url = photo.getUrl();
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("/storage")) {
                Glide.with(context)
                        .load(url)
                        .into(viewTarget);
            } else {
                GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("TOKEN", SPUtils.getString(context, AppConst.TOKEN_KEY)).build());
                Glide.with(context)
                        .load(glideUrl)
                        .placeholder(R.drawable.icon_default)
                        .into(viewTarget);
            }

        }

        //ImageLoader.loadImage(context,photo.getUrl(),imageView,R.drawable.default_image,R.drawable.jmui_send_error);


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public int getItemPosition(Object object) {
        if (getCount() > 0) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    /*
     * 当你实现 PagerAdapter 时，你必须重写至少下列方法： 否则我左右切换刷了选项卡会报异常的
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}