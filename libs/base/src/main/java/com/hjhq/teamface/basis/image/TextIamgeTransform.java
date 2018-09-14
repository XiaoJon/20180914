package com.hjhq.teamface.basis.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;

/**
 * 文字图片转换
 *
 * @author Administrator
 * @date 2018/1/31
 */
public class TextIamgeTransform {

    /**
     * 生成圆形图片 Drawable
     *
     * @param context Any context, will not be retained.
     * @param name    文字
     * @return 文字生成的图片 Drawable
     */
    public static Drawable getCircleTextAvatar(Context context, String name) {
        Bitmap bitmap = getCircleTextAvatarBitmap(context, name);
        return bitmap2Drawable(context, bitmap);
    }

    /**
     * 生成圆形图片 Bitmap
     *
     * @param context Any context, will not be retained.
     * @param name    文字
     * @return 文字生成的图片 Bitmap
     */
    public static Bitmap getCircleTextAvatarBitmap(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            name = "未知";
        }
        String subName = name;
        int length = name.length();
        if (length > 2) {
            subName = name.substring(length - 2);
        }

        RelativeLayout rlAvatar = (RelativeLayout) View.inflate(context, R.layout.layout_name_circle_avatar, null);
        TextView tv = (TextView) rlAvatar.findViewById(R.id.text);
        //布局中的背景无效
        rlAvatar.setBackgroundResource(R.drawable.circle_avatar_bg1);
        tv.setText(subName);
        tv.setPadding(1, 10, 1, 10);
        return convertViewToBitmap(rlAvatar);
    }

    /**
     * 生成方形图片 Bitmap
     *
     * @param context Any context, will not be retained.
     * @param name    文字
     * @return 文字生成的图片 Bitmap
     */
    public static Bitmap getSquareTextAvatarBitmap(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            name = "未知";
        }
        String subName = name;
        int length = name.length();
        if (length > 2) {
            subName = name.substring(length - 2);
        }

        int screenWidth = (int) ScreenUtils.getScreenWidth(context);

        View rootView = View.inflate(context, R.layout.layout_name_square_avatar, null);
        RelativeLayout rlAvatar = rootView.findViewById(R.id.rl_layout);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlAvatar.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = screenWidth;
        rlAvatar.setLayoutParams(layoutParams);

        TextView tvText = rlAvatar.findViewById(R.id.text);
        tvText.setText(subName);
        tvText.setPadding(1, 10, 1, 10);

        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tvText.getLayoutParams();
        tvParams.width = screenWidth;
        tvParams.height = screenWidth;
        tvText.setLayoutParams(tvParams);
        tvText.setTextSize(DeviceUtils.pixelsToDp(context, screenWidth / 2 * 7 / 8));

        return convertViewToBitmap(rlAvatar);
    }


    /**
     * 获取中空圆形头像
     *
     * @param context
     * @param name
     * @return
     */
    public static Drawable getHoleTextAvatar(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            name = "*";
        } else {
            if (!check(name)) {
                name = name.substring(0, 1);
            } else {
                if (name.length() >= 2) {
                    name = name.substring(0, 2);
                    char c = name.charAt(0);
                    if (c >= 'a' && c <= 'z') {
                        name = name.substring(0, 1).toUpperCase() + name.substring(1, 2).toLowerCase();
                    }
                }
            }
        }


        String subName = name;
        int length = name.length();
        if (length > 2) {
            subName = name.substring(length - 2);
        }

        RelativeLayout rlAvatar = (RelativeLayout) View.inflate(context, R.layout.layout_name_hole_avatar, null);
        TextView tv = (TextView) rlAvatar.findViewById(R.id.text);
        //布局中的背景无效
        rlAvatar.setBackgroundResource(R.drawable.hole_avatar_bg1);
        tv.setText(subName);
        tv.setPadding(1, 10, 1, 10);

        Bitmap bitmap = convertViewToBitmap(rlAvatar);
        return bitmap2Drawable(context, bitmap);
    }


    /**
     * 将View转换成Bitmap
     *
     * @param view 目标View
     * @return 转换后的Bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap convertViewToBitmap2(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
       // view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * Bitmap转化为drawable
     *
     * @param context Any context, will not be retained.
     * @param bitmap  目标Bitmap
     * @return 转换后的Drawable
     */
    private static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static boolean check(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) || (c >= '0' && c <= '9')) {
            return true;
        } else {
            return false;
        }
    }

}
