package com.hjhq.teamface.login.view.verify;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * 计时器Button
 *
 * @author Administrator
 */

public class TimmerButton extends AppCompatButton {
    /**
     * 可以计时
     */
    public static int CAN_COUNT = 1;
    /**
     * 正在计时
     */
    public static int COUNTING = 2;
    /**
     * 错误
     */
    public static int ERROR = 3;

    private int seconds;
    private int remain;
    private int state = CAN_COUNT;

    private String normalState;
    private String errorState;


    public TimmerButton(Context context) {
        super(context);
    }

    public TimmerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimmerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getState() {
        return state;
    }


    public void initData(String normal, String error, int count) {
        this.normalState = normal;
        this.errorState = error;
        this.seconds = count;
        this.remain = count;
        setText(normal);
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void startCount() {
        if (state == CAN_COUNT || getState() == ERROR) {
            performCount();
        }
    }

    private void performCount() {
        if (getRemain() <= 0) {
            remain = seconds;
            state = CAN_COUNT;
            setText(normalState);
            setTextColor(Color.parseColor("#3689E9"));
        } else {
            state = COUNTING;
            setText(remain + "s");
            remain--;
            setTextColor(Color.parseColor("#FFAB36"));
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    performCount();
                }
            }, 1000);
        }
    }
}