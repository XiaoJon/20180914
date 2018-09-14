package com.hjhq.teamface.view.refresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/10/6.
 */

public class PlaneDrawable extends RefreshDrawable implements Runnable {

    private boolean isRunning;
    private Handler mHandler = new Handler();
    private Paint mPaint = new Paint();
    String text;

    protected int mOffset;
    protected float mPercent;
    protected int drawableMinddleWidth;
    protected List<Bitmap> bitmaps = new ArrayList<>();
    protected RectF rectF = new RectF();

    public PlaneDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
        getBitmaps(context);
    }

    private static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private void getBitmaps(Context context) {
        TextView view = new TextView(context);
        view.setText("正在加载...");
        view.setTextColor(Color.parseColor("#FF00FF"));
        Bitmap bitmap = convertViewToBitmap(view);

        drawableMinddleWidth = bitmap.getWidth() / 2;
        bitmaps.add(bitmap);
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
//        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.loading2)).getBitmap());
    }


    //正在加载...


    @Override
    public void setPercent(float percent) {
        mPercent = percent;
        int centerX = getBounds().centerX();
        rectF.left = centerX - drawableMinddleWidth * mPercent;
        rectF.right = centerX + drawableMinddleWidth * mPercent;
        rectF.top = -drawableMinddleWidth * 2 * mPercent;
        rectF.bottom = 0;
        rectF.left = centerX - drawableMinddleWidth * 2;
        rectF.right = centerX + drawableMinddleWidth * 4;
        rectF.top = -drawableMinddleWidth * 2;
        rectF.bottom = 0;
    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors) {
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mOffset += offset;
        invalidateSelf();
    }

    @Override
    public void start() {
        isRunning = true;
        mHandler.postDelayed(this, 50);
    }

    @Override
    public void run() {
        if (isRunning) {
            mHandler.postDelayed(this, 50);
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        mHandler.removeCallbacks(this);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas) {
        int num = (int) (System.currentTimeMillis() / 50 % 11);
        final int saveCount = canvas.save();
        canvas.translate(0, mOffset);
        //Bitmap bitmap = bitmaps.get(num);
        //canvas.drawBitmap(bitmap, null, rectF, null);
        mPaint.setColor(Color.parseColor("#FF00FF"));
        mPaint.setTextSize(DeviceUtils.dpToPixel(getContext(), 14));
        canvas.drawText("正在加载", ScreenUtils.getScreenWidth(getContext()) / 2 - 4 * DeviceUtils.dpToPixel(getContext(), 14), -50, mPaint);
        canvas.restoreToCount(saveCount);
    }
}
