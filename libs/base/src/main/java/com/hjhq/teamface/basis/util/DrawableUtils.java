package com.hjhq.teamface.basis.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.hjhq.teamface.basis.util.device.DeviceUtils;

/**
 * Created by lx on 2017/3/29.
 */

public class DrawableUtils {
    
    public static Bitmap getTextHeadPic(Context mContext, String text){
        Bitmap bmp = Bitmap.createBitmap((int) DeviceUtils.dpToPixel(mContext,45), (int)DeviceUtils.dpToPixel(mContext,45), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint  = new Paint();
        Typeface typeface = Typeface.DEFAULT_BOLD;
        paint.setTypeface(typeface);
        paint.setColor(Color.GREEN);
        paint.setTextSize((int)DeviceUtils.dpToPixel(mContext,16));
        canvas.drawText("Hello World", (int)DeviceUtils.dpToPixel(mContext,45),22, paint);
        return bmp;
    }

    // Drawable转换成Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
