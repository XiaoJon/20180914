package com.hjhq.teamface.view.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.basis.util.log.LogUtil;


/**
 * 发送语音提示控件
 */
public class VoiceSendingView extends RelativeLayout {
    /**
     * 最小录音时长
     */
    public static int mixVoiceTime = 1;
    public static int maxVoiceTime = 60;

    private AnimationDrawable frameAnimation;
    private long startTime;
    private ImageView img;
    private TextView tvTimer;
    private CountDownTimer timer;
    private int startNum = 0;

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    private boolean isRecording = false;

    public VoiceSendingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_sending, this);
        img = (ImageView) findViewById(R.id.microphone);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        img.setBackgroundResource(R.drawable.animation_voice);
        frameAnimation = (AnimationDrawable) img.getBackground();

    }

    public void showRecording() {
        isRecording = true;
        startTime = System.currentTimeMillis();
        frameAnimation.start();
        startCountDownTime(maxVoiceTime);

    }

    public boolean hideRecording() {
        startNum = 0;
        isRecording = false;
       if(tvTimer!=null){
           tvTimer.setText("");
       }
        if(timer!=null){
            timer.cancel();
        }
        if(frameAnimation!=null){
        frameAnimation.stop();
        }
        long endTime = System.currentTimeMillis();
        if (endTime - startTime < mixVoiceTime * 1000) {
            CommonUtil.showToast("录音时间不能小于" + mixVoiceTime + "秒");
            return false;
        }
        return true;
    }

    public void setTime(String time) {
        tvTimer.setText(time);
    }

    public void release() {
        startNum = 0;
        isRecording = false;
        tvTimer.setText("");
        if (timer!=null)
            timer.cancel();
        frameAnimation.stop();
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                long endNum = millisUntilFinished / 1000;
                LogUtil.e("计时器=" + endNum + "");
                if (endNum <= 5) {
                    tvTimer.setText("还可以说" + endNum + "秒");
                } else {
                    tvTimer.setText(startNum + "s");
                }
                startNum++;
            }

            @Override
            public void onFinish() {
                if (listner != null)
                    listner.overTime(maxVoiceTime);
            }
        };
        timer.start();// 开始计时
    }

    public interface OverTimeListner {
        public void overTime(long maxTime);
    }

    OverTimeListner listner;

    public void setOverTimeListner(OverTimeListner listner) {
        this.listner = listner;
    }
}
