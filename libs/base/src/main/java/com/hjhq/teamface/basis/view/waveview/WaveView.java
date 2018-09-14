package com.hjhq.teamface.basis.view.waveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hjhq.teamface.basis.R;

/**
 * 波浪控件
 *
 * @author John
 * @date 2014/10/15
 */
public class WaveView extends LinearLayout {

    private int mWaveColor;
    private int mProgress;
    private int mWaveHeight;
    private int mWaveMultiple;
    private int mWaveHz;

    private int mWaveToTop;

    private Wave mWave;
    private Solid mSolid;

    /**
     * 默认颜色
     */
    private final int DEFAULT_WAVE_COLOR = Color.WHITE;
    /**
     * 默认位置
     */
    private final int DEFAULT_PROGRESS = 100;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveView, R.attr.waveViewStyle, 0);
        mWaveColor = attributes.getColor(R.styleable.WaveView_wave_color, DEFAULT_WAVE_COLOR);
        mProgress = attributes.getInt(R.styleable.WaveView_progress, DEFAULT_PROGRESS);
        mWaveHeight = attributes.getInt(R.styleable.WaveView_wave_height, 100);
        mWaveMultiple = attributes.getInt(R.styleable.WaveView_wave_length, 100);
        mWaveHz = attributes.getInt(R.styleable.WaveView_wave_hz, 100);
        attributes.recycle();

        mWave = new Wave(context, null);
        mWave.initializeWaveSize(mWaveMultiple, mWaveHeight, mWaveHz);
        mWave.setAboveWaveColor(mWaveColor);
        mWave.initializePainters();

        mSolid = new Solid(context, null);
        mSolid.setWavePaint(mWave.getWavePaint());

        addView(mWave);
        addView(mSolid);

        setProgress(mProgress);
    }

    public void setProgress(int progress) {
        this.mProgress = progress > 100 ? 100 : progress;
        computeWaveToTop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToTop();
        }
    }

    private void computeWaveToTop() {
        mWaveToTop = (int) (getHeight() * (1f - mProgress / 100f));
        ViewGroup.LayoutParams params = mWave.getLayoutParams();
        if (params != null) {
            ((LayoutParams) params).topMargin = mWaveToTop;
        }
        mWave.setLayoutParams(params);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        /**
         * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public int getmWaveHeight() {
        return mWaveHeight;
    }

    public void setmWaveHeight(int mWaveHeight) {
        this.mWaveHeight = mWaveHeight;
        mWave.setmWaveHeight(mWaveHeight);
    }

    public int getmWaveMultiple() {
        return mWaveMultiple;
    }

    public void setmWaveMultiple(int mWaveMultiple) {
        this.mWaveMultiple = mWaveMultiple;
        mWave.setmWaveMultiple(mWaveMultiple);
    }

    public int getmWaveHz() {
        return mWaveHz;
    }

    public void setmWaveHz(int mWaveHz) {
        this.mWaveHz = mWaveHz;
        mWave.setmWaveHz(mWaveHz);
    }
}
