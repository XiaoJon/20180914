package com.hjhq.teamface.basis.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPUtils;

import java.io.File;

/**
 * 图片加载
 * Created by Samda on 2017/2/27.
 */

public class ImageLoader {

    /**
     * 使用GlideUrl 包装 网络图片请求
     *
     * @param path 图片路径
     * @return GlideUrl包装类
     */
    private static Object glideUrl(Context context, String path) {
        if (TextUtil.isEmpty(path)) {
            path = "www.baidu.com";
        }
        if (path.startsWith("/storage")) {
            return path;
        }
        //添加TOKEN来获取图片
        return new GlideUrl(path,
                new LazyHeaders.Builder()
                        .addHeader("TOKEN", SPUtils.getString(context, AppConst.TOKEN_KEY))
                        .build());
    }

    /**
     * 加载图片
     *
     * @param mContext   Any context, will not be retained.
     * @param path       图片路径
     * @param mImageView 图片控件
     */
    public static void loadImage(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).placeholder(R.drawable.ic_image).into(mImageView);
    }

    public static void loadImageNoAnimCenterCrop(Context mContext, String path, ImageView mImageView) {


        Glide.with(mContext).load(glideUrl(mContext, path)).centerCrop().dontAnimate().placeholder(R.drawable.ic_image).into(mImageView);
    }

    /**
     * 加载文件图片
     *
     * @param mContext   Any context, will not be retained.
     * @param file       文件图片
     * @param mImageView 图片控件
     */
    public static void loadImage(Context mContext, File file, ImageView mImageView) {
        Glide.with(mContext).load(file).error(R.drawable.ic_image).into(mImageView);
    }

    /**
     * 加载资源图片
     *
     * @param mContext   Any context, will not be retained.
     * @param res        图片资源
     * @param mImageView 图片控件
     */
    public static void loadImage(Context mContext, int res, ImageView mImageView) {
        Glide.with(mContext).load(res).error(R.drawable.ic_image).into(mImageView);
    }

    /**
     * 通用加载图片
     *
     * @param mContext   Any context, will not be retained.
     * @param path       图片路径 可以是网络或本地路径、图片文件和资源图片
     * @param mImageView 图片控件
     */
    public static void loadImage(Context mContext, Object path, ImageView mImageView) {
        if (path instanceof String) {
            loadImage(mContext, (String) path, mImageView);
        } else if (path instanceof File) {
            loadImage(mContext, (File) path, mImageView);
        } else if (path instanceof Integer) {
            loadImage(mContext, (int) path, mImageView);
        }
    }

    /**
     * 加载圆形图
     *
     * @param mContext   Any context, will not be retained.
     * @param path       图片路径
     * @param mImageView 图片控件
     */
    public static void loadCircleImage(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }


    /**
     * @param mContext
     * @param path        网络路径
     * @param placeholder 占位资源文件
     * @param mImageView
     */
    public static void loadCircleImage(Context mContext, String path, int placeholder, ImageView mImageView) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .placeholder(mContext.getResources().getDrawable(placeholder))
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆形图，替代图片使用文字生成代替
     *
     * @param mContext   Any context, will not be retained.
     * @param path       图片路径
     * @param mImageView 图片控件
     * @param name       生成图片的文字
     */
    public static void loadCircleImage(Context mContext, String path, ImageView mImageView, String name) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .asBitmap()
                //加载gif图片(华为)手机会报警告
                //Bitmap: Called reconfigure on a bitmap that is in use! This may cause graphical corruption!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name))
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载中空头像
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param name
     */
    public static void loadHoleImage(Context mContext, String path, ImageView mImageView, String name) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .asBitmap()
                //加载gif图片(华为)手机会报警告
                //Bitmap: Called reconfigure on a bitmap that is in use! This may cause graphical corruption!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(TextIamgeTransform.getHoleTextAvatar(mContext, name))
                .transform(new GlideCircleTransform(mContext))// 调用transform方法
                .into(mImageView);
    }

    /**
     * 加载圆形图
     *
     * @param mContext   Any context, will not be retained.
     * @param res        图片资源
     * @param mImageView 图片控件
     */
    public static void loadCircleImage(Context mContext, @DrawableRes int res, ImageView mImageView) {
        Glide.with(mContext)
                .load(res)
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆形图
     *
     * @param mContext
     * @param res
     * @param mImageView
     * @param error
     */
    public static void loadCircleImage(Context mContext, @DrawableRes int res, ImageView mImageView, @DrawableRes int error) {
        Glide.with(mContext)
                .load(res)
                .error(error)
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆形图
     *
     * @param mContext    Any context, will not be retained.
     * @param path        图片路径
     * @param mImageView  图片控件
     * @param lodingImage 替换图片
     */
    public static void loadCircleImage(Context mContext, String path, ImageView mImageView, int lodingImage) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .placeholder(lodingImage)
                .transform(new GlideCircleTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆形图
     *
     * @param mContext    Any context, will not be retained.
     * @param path        图片路径
     * @param mImageView  图片控件
     * @param name        名称
     * @param borderWidth 图片控件
     * @param borderColor 图片控件
     */
    public static void loadCircleImage(Context mContext, String path, ImageView mImageView, String name, int borderWidth, int borderColor) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .asBitmap()
                //加载gif图片(华为)手机会报警告
                //Bitmap: Called reconfigure on a bitmap that is in use! This may cause graphical corruption!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name))
                .transform(new GlideCircleTransform(mContext, borderWidth, borderColor))
                .into(mImageView);
    }

    /**
     * View转为Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        return TextIamgeTransform.convertViewToBitmap2(view);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, String path, ImageView mImageView) {
        loadRoundImage(mContext, path, mImageView, R.drawable.ic_image);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, int res, ImageView mImageView) {
        loadRoundImage(mContext, res, mImageView, R.drawable.ic_image);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, byte[] bytes, ImageView mImageView, String name) {
        Glide.with(mContext)
                .load(bytes)
                .placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name))
                .transform(new GlideRoundTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, String path, ImageView mImageView, String name) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name))
                .transform(new GlideRoundTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, int res, ImageView mImageView, String name) {
        Glide.with(mContext)
                .load(res)
                .placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name))
                .transform(new GlideRoundTransform(mContext))
                .into(mImageView);
    }


    /**
     * @param mContext
     * @param path       网络路径
     * @param mImageView
     * @param res        站位
     */
    public static void loadRoundImage(Context mContext, String path, ImageView mImageView, int res) {
        loadRoundImage(mContext, path, mImageView, 5, res);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, String path, ImageView mImageView, int round, int res) {
        Glide.with(mContext)
                .load(glideUrl(mContext, path))
                .placeholder(res).error(res)
                .transform(new GlideRoundTransform(mContext, round))
                .into(mImageView);
    }

    /**
     * 加载圆角图
     */
    public static void loadRoundImage(Context mContext, int res, ImageView mImageView, int placeRes) {
        Glide.with(mContext)
                .load(res)
                .placeholder(placeRes).error(placeRes)
                .transform(new GlideRoundTransform(mContext))
                .into(mImageView);
    }

    /**
     * 加载指定大小
     */
    public static void loadImage(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).override(width, height).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片
     */
    public static void loadImage(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    public static void loadImage(Context mContext, ImageView mImageView, String path, int placeholder) {
        Glide.with(mContext).load(glideUrl(mContext, path)).placeholder(placeholder).into(mImageView);
    }

    public static void loadImage(Context mContext, String path, int errorImageView, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).error(errorImageView).into(mImageView);
    }

    public static void loadVideo(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {

        Glide.with(mContext).load(glideUrl(mContext, path)).asBitmap().placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片,根据文字生成
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param name       生成图片的文字
     */
    public static void loadImage(Context mContext, String path, ImageView mImageView, String name) {
        Glide.with(mContext).load(glideUrl(mContext, path)).placeholder(TextIamgeTransform.getCircleTextAvatar(mContext, name)).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小
     */
    public static void loadImage(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /**
     * 设置跳过内存缓存
     */
    public static void loadImageNoCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).skipMemoryCache(true).into(mImageView);
    }

    /**
     * 设置下载优先级
     */
    public static void loadImagePriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 设置缓存策略
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */
    public static void loadImageDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()  设置加载动画
     */
    public static void loadImageAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */
    public static void loadImageThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等  设置动态转换
     */
    public static void loadImageCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).centerCrop().into(mImageView);
    }

    /**
     * 设置动态GIF加载方式
     */
    public static void loadImageDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).asGif().into(mImageView);
    }

    /**
     * 设置静态GIF加载方式
     */
    public static void loadImageStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(glideUrl(mContext, path)).asBitmap().into(mImageView);
    }


    /**
     * 项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
     */
    public static void loadImageContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(glideUrl(mContext, path)).centerCrop().into(simpleTarget);
    }

    /**
     * 清理磁盘缓存
     */
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清理内存
     */
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }
}
