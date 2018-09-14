package com.hjhq.teamface.basis.view.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.R;

// y=Asin(ωx+φ)+k
class Wave extends View {
    private final int DEFAULT_WAVE_HEIGHT = 15;

    private final float DEFAULT_WAVE_LENGTH_MULTIPLE = 1f;

    private final float DEFAULT_WAVE_HZ = 0.13f;

    private final float X_SPACE = 20;
    private final double PI2 = 2 * Math.PI;

    private Path mWavePath = new Path();

    private Paint mWavePaint = new Paint();

    private int mWaveColor;

    private float mWaveMultiple;
    private float mWaveLength;
    private int mWaveHeight;
    private float mMaxRight;
    private float mWaveHz;

    // wave animation
    private float mOffset = 0.0f;

    private RefreshProgressRunnable mRefreshProgressRunnable;

    private int left, right, bottom;
    // ω
    private double omega;

    public Wave(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.waveViewStyle);
    }

    public Wave(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mWavePath, mWavePaint);
    }

    public void setAboveWaveColor(int aboveWaveColor) {
        this.mWaveColor = aboveWaveColor;
    }

    public Paint getWavePaint() {
        return mWavePaint;
    }


    public void initializeWaveSize(int waveMultiple, int waveHeight, int waveHz) {
        mWaveMultiple = getWaveMultiple(waveMultiple);
        mWaveHeight = getWaveHeight(waveHeight);
        mWaveHz = getWaveHz(waveHz);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mWaveHeight * 2);
        setLayoutParams(params);
    }

    public void initializePainters() {
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setAntiAlias(true);
    }

    private float getWaveMultiple(int size) {
        return DEFAULT_WAVE_LENGTH_MULTIPLE * (size / 100f);
    }

    private int getWaveHeight(int size) {
        return (int) (DEFAULT_WAVE_HEIGHT * (size / 100f));
    }

    private float getWaveHz(int size) {
        return DEFAULT_WAVE_HZ * (size / 100f);
    }

    /**
     * calculate wave track
     */
    private void calculatePath() {
        mWavePath.reset();

        getWaveOffset();

        float y;
        mWavePath.moveTo(left, bottom);
        for (float x = 0; x <= mMaxRight; x += X_SPACE) {
            y = (float) (mWaveHeight * Math.sin(omega * x + mOffset) + mWaveHeight);
            mWavePath.lineTo(x, y);
        }
        mWavePath.lineTo(right, bottom);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (View.GONE == visibility) {
            removeCallbacks(mRefreshProgressRunnable);
        } else {
            removeCallbacks(mRefreshProgressRunnable);
            mRefreshProgressRunnable = new RefreshProgressRunnable();
            post(mRefreshProgressRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (mWaveLength == 0) {
                startWave();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mWaveLength == 0) {
            startWave();
        }
    }

    private void startWave() {
        if (getWidth() != 0) {
            int width = getWidth();
            mWaveLength = width * mWaveMultiple;
            left = getLeft();
            right = getRight();
            bottom = getBottom() + 2;
            mMaxRight = right + X_SPACE;
            omega = PI2 / mWaveLength;
        }
    }

    /**
     * 获取偏移量
     */
    private void getWaveOffset() {
        if (mOffset > Float.MAX_VALUE - 100) {
            mOffset = 0;
        } else {
            mOffset += mWaveHz;
        }
    }

    private class RefreshProgressRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (Wave.this) {
                long start = System.currentTimeMillis();

                calculatePath();

                invalidate();

                long gap = 16 - (System.currentTimeMillis() - start);
                postDelayed(this, gap < 0 ? 0 : gap);
            }
        }
    }

    public void setmWaveMultiple(int mWaveMultiple) {
        this.mWaveMultiple = getWaveMultiple(mWaveMultiple);
    }

    public void setmWaveHeight(int mWaveHeight) {
        this.mWaveHeight = getWaveHeight(mWaveHeight);
    }

    public void setmWaveHz(int mWaveHz) {
        this.mWaveHz = getWaveHz(mWaveHz);
    }
}
