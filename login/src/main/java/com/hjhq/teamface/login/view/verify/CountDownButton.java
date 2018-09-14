package com.hjhq.teamface.login.view.verify;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.login.R;

import java.util.Locale;


/**
 * Author: Wh1te
 * Date: 2016/10/18
 */

public class CountDownButton extends AppCompatButton {

    /**
     * 默认时间间隔1000ms
     */
    private static final long DEFAULT_INTERVAL = 1000;
    /**
     * 默认时长120s
     */
    private static final long DEFAULT_COUNT = 120 * 1000;
    /**
     * 默认倒计时文字格式(显示秒数)
     */
    private static final String DEFAULT_COUNT_FORMAT = "%d";
    /**
     * 默认按钮文字 {@link #getText()}
     */
    private String mDefaultText;
    /**
     * 倒计时时长，单位为毫秒
     */
    private long mCount;
    /**
     * 时间间隔，单位为毫秒
     */
    private long mInterval;
    /**
     * 倒计时文字格式
     */
    private String mCountDownFormat = DEFAULT_COUNT_FORMAT;
    /**
     * 倒计时是否可用
     */
    private boolean mEnableCountDown = true;
    /**
     * 点击事件监听器
     */
    private OnClickListener onClickListener;

    /**
     * 倒计时
     */
    private CountDownTimer mCountDownTimer;

    /**
     * 是否正在执行倒计时
     */
    private boolean isCountDownNow;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.login_CountDownButton);
        mCountDownFormat = typedArray.getString(R.styleable.login_CountDownButton_login_countDownFormat);
        if (typedArray.hasValue(R.styleable.login_CountDownButton_login_countDown)) {
            mCount = (int) typedArray.getFloat(R.styleable.login_CountDownButton_login_countDown, DEFAULT_COUNT);
        }
        mInterval = (int) typedArray.getFloat(R.styleable.login_CountDownButton_login_countDownInterval, DEFAULT_INTERVAL);
        mEnableCountDown = (mCount > mInterval) && typedArray.getBoolean(R.styleable.login_CountDownButton_login_enableCountDown, true);
        typedArray.recycle();
        // 初始化倒计时Timer
        refreshCountDownTimer();
        mDefaultText = getText().toString();
    }

    private void refreshCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mCount, mInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    String format = String.format(Locale.CHINA, mCountDownFormat, millisUntilFinished / 1000);
                    int matcherStart = format.indexOf("重新发送验证码");
                    SpannableString spannable = new SpannableString(format);

                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ColorUtils.resToColor(getContext(), R.color.app_blue));
                    spannable.setSpan(foregroundColorSpan, matcherStart, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    setText(spannable);
                }
            }

            @Override
            public void onFinish() {
                isCountDownNow = false;
                setEnabled(true);
                setClickable(true);
                setTextColor(ContextCompat.getColor(getContext(), R.color.main_green));
                setText(mDefaultText);
                setEnableCountDown(false);
            }
        };
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Rect rect = new Rect();
                this.getGlobalVisibleRect(rect);
                if (onClickListener != null && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    onClickListener.onClick(this);
                }
                if (mEnableCountDown
                    // && rect.contains((int) event.getRawX(), (int) event.getRawY())
                        ) {
                    //mDefaultText = getText().toString();
                    // 设置按钮不可点击
                    setEnabled(false);
                    setClickable(false);
                    // 开始倒计时
                    mCountDownTimer.start();
                    setTextColor(ContextCompat.getColor(getContext(), R.color.orange));
                    isCountDownNow = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void startCount() {
        setEnabled(false);
        setClickable(false);
        // 开始倒计时
        mCountDownTimer.start();
        setTextColor(ContextCompat.getColor(getContext(), R.color.orange));
        isCountDownNow = true;
    }

    public void setEnableCountDown(boolean enableCountDown) {
        this.mEnableCountDown = (mCount > mInterval) && enableCountDown;
    }

    public boolean getEnableCountDown() {
        return this.mEnableCountDown;
    }

    public void setCountDownFormat(String countDownFormat) {
        this.mCountDownFormat = countDownFormat;
    }

    public void setCount(long count) {
        this.mCount = count;
        refreshCountDownTimer();

    }

    public void setInterval(long interval) {
        mInterval = interval;
        refreshCountDownTimer();
    }

    /**
     * 是否正在执行倒计时
     *
     * @return 倒计时期间返回true否则返回false
     */
    public boolean isCountDownNow() {
        return isCountDownNow;
    }

    /**
     * 设置倒计时数据
     *
     * @param count           时长
     * @param interval        间隔
     * @param countDownFormat 文字格式
     */
    public void setCountDown(long count, long interval, String countDownFormat) {
        this.mCount = count;
        this.mCountDownFormat = countDownFormat;
        this.mInterval = interval;
        setEnableCountDown(true);
        refreshCountDownTimer();
    }

    /**
     * 移除倒计时
     */
    public void removeCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        isCountDownNow = false;
        setTextColor(ContextCompat.getColor(getContext(), R.color.main_green));
        setText(mDefaultText);
        setEnabled(true);
        setClickable(true);
    }
}
