package com.hjhq.teamface.view.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by SidHu on 2016/10/18.
 */

public class PlaneLoadDrawable extends PlaneDrawable {
    Paint mPaint = new Paint();

    public PlaneLoadDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
    }

    @Override
    public void setPercent(float percent) {
        mPercent = percent;
        int centerX = getBounds().centerX();
        int bottom = getBounds().bottom;
        // rectF.left = centerX - drawableMinddleWidth * mPercent;
        rectF.left = centerX;
        //rectF.right = centerX + drawableMinddleWidth * mPercent;
        rectF.right = centerX;
        rectF.top = bottom;
        // rectF.bottom = bottom + drawableMinddleWidth * 2 * mPercent;
        rectF.bottom = bottom;
    }

    @Override
    public void draw(Canvas canvas) {
        int num = (int) (System.currentTimeMillis() / 50 % 11);
        final int saveCount = canvas.save();
        canvas.translate(0, mOffset);
        // Bitmap bitmap = bitmaps.get(num);
        //canvas.drawBitmap(bitmap, null, rectF, null);
        mPaint.setColor(Color.parseColor("#FF00FF"));
        mPaint.setTextSize(100F);
        canvas.drawText("啦啦啦1", 400, 50, mPaint);
//        canvas.drawBitmap();
        canvas.restoreToCount(saveCount);
    }
}
