package com.hjhq.teamface.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.common.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Administrator
 */
public class RecordVoiceButton extends android.support.v7.widget.AppCompatButton {

    private File myRecAudioFile;

    private static final int MIN_INTERVAL_TIME = 1000;// 1s
    private final static int CANCEL_RECORD = 5;
    private final static int START_RECORD = 7;
    private final static int RECORD_DENIED_STATUS = 1000;
    //依次为按下录音键坐标、手指离开屏幕坐标、手指移动坐标
    float mTouchY1, mTouchY2, mTouchY;
    private final float MIN_CANCEL_DISTANCE = 300f;
    //依次为开始录音时刻，按下录音时刻，松开录音按钮时刻
    public long startTime, time1, time2;

    private Dialog recordIndicator;

    private ImageView mVolumeIv;
    private TextView mRecordHintTv;

    private MediaRecorder mRecorder;

    private ObtainDecibelThread mThread;

    private Handler mVolumeHandler;
    public static boolean mIsPressed = false;
    private Context mContext;

    private Timer timer = new Timer();
    private Timer mCountTimer;
    private boolean isTimerCanceled = false;
    private boolean mTimeUp = false;
    private static int[] res;


    public RecordVoiceButton(Context context) {
        super(context);
        init();
    }

    public RecordVoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public RecordVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mVolumeHandler = new ShowVolumeHandler(this);
        res = new int[]{R.drawable.jmui_mic_1, R.drawable.jmui_mic_2,
                R.drawable.jmui_mic_3, R.drawable.jmui_mic_4,
                R.drawable.jmui_mic_5, R.drawable.jmui_cancel_record};
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SystemFuncUtils.requestPermissions((Activity) mContext, android.Manifest.permission.RECORD_AUDIO, aBoolean -> {
            if (aBoolean) {
                touchEvent(event);
            } else {
                ToastUtils.showError(mContext, "必须获得必要的权限才能录音！");
            }
        });
        return true;
    }

    private void touchEvent(MotionEvent event) {
        this.setPressed(true);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.setText(mContext.getString(R.string.send_voice_hint));
                mIsPressed = true;
                time1 = System.currentTimeMillis();
                mTouchY1 = event.getY();
                //检查sd卡是否存在
                if (JYFileHelper.existSDCard()) {
                    if (isTimerCanceled) {
                        timer = createTimer();
                    }
                    try {
                        initDialogAndStartRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showError(mContext, R.string.sdcard_not_exist_toast);
                    this.setPressed(false);
                    this.setText(R.string.record_voice_hint);
                    mIsPressed = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                this.setText(R.string.record_voice_hint);
                mIsPressed = false;
                this.setPressed(false);
                mTouchY2 = event.getY();
                time2 = System.currentTimeMillis();
                if (time2 - time1 < 500) {
                    cancelTimer();
                } else if (time2 - time1 < 1000) {
                    cancelRecord();
                } else if (mTouchY1 - mTouchY2 > MIN_CANCEL_DISTANCE) {
                    cancelRecord();
                } else if (time2 - time1 < 60000) {
                    finishRecord();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchY = event.getY();
                //手指上滑到超出限定后，显示松开取消发送提示
                if (mTouchY1 - mTouchY > MIN_CANCEL_DISTANCE) {
                    this.setText(R.string.cancel_record_voice_hint);
                    mVolumeHandler.sendEmptyMessage(CANCEL_RECORD);
                    if (mThread != null) {
                        mThread.exit();
                    }
                    mThread = null;
                } else {
                    this.setText(R.string.send_voice_hint);
                    if (mThread == null) {
                        mThread = new ObtainDecibelThread();
                        mThread.start();
                    }
                }
                break;
            // 当手指移动到view外面，会cancel
            case MotionEvent.ACTION_CANCEL:
                this.setText(R.string.record_voice_hint);
                cancelRecord();
                break;
            default:
                break;
        }
    }

    /**
     * 清楚定时任务
     */
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            isTimerCanceled = true;
        }
        if (mCountTimer != null) {
            mCountTimer.cancel();
            mCountTimer.purge();
        }
    }

    private Timer createTimer() {
        timer = new Timer();
        isTimerCanceled = false;
        return timer;
    }

    /**
     * 初始化录音文件
     *
     * @throws IOException
     */
    private void initDialogAndStartRecord() throws IOException {
        EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.RECORDING_TAG, null));
        //存放录音文件目录
        String fileDir = JYFileHelper.getFileDir(getContext(), Constants.PATH_AUDIO).getAbsolutePath();
        //录音文件的命名格式
        myRecAudioFile = new File(fileDir,
                DateTimeUtil.getCurrentStr("yyyyMMddHHmmssSSS") + ".mp3");

        myRecAudioFile.createNewFile();
        if (myRecAudioFile == null) {
            cancelTimer();
            stopRecording();
            releaseRecorder();
            ToastUtils.showError(mContext, R.string.create_file_failed);
        }
        Log.i("FileCreate", "Create file success file path: " + myRecAudioFile.getAbsolutePath());
        recordIndicator = new Dialog(getContext(), R.style.record_voice_dialog);
        recordIndicator.setContentView(R.layout.dialog_record_voice);
        mVolumeIv = recordIndicator.findViewById(R.id.jmui_volume_hint_iv);
        mRecordHintTv = recordIndicator.findViewById(R.id.jmui_record_voice_tv);
        mRecordHintTv.setText(R.string.move_to_cancel_hint);
        startRecording();
        recordIndicator.show();
    }

    //录音完毕加载 ListView item
    private void finishRecord() {
        cancelTimer();
        stopRecording();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }

        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        long intervalTime = time2 - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            ToastUtils.showToast(mContext, R.string.time_too_short_toast);
            myRecAudioFile.delete();
            return;
        }
        EventBusUtils.sendEvent(new MessageBean((int) intervalTime, Constants.DATA_TAG1, myRecAudioFile));
        mRecorder = null;
    }


    //取消录音，清除计时
    private void cancelRecord() {
        //可能在消息队列中还存在HandlerMessage，移除剩余消息
        mVolumeHandler.removeMessages(56, null);
        mVolumeHandler.removeMessages(57, null);
        mVolumeHandler.removeMessages(58, null);
        mVolumeHandler.removeMessages(59, null);
        mTimeUp = false;
        cancelTimer();
        stopRecording();
        releaseRecorder();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
        }
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(192000);
            mRecorder.prepare();
            mRecorder.start();

            startTime = System.currentTimeMillis();
            mCountTimer = new Timer();
            mCountTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mTimeUp = true;
                    android.os.Message msg = mVolumeHandler.obtainMessage();
                    msg.what = 55;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", 5);
                    msg.setData(bundle);
                    msg.sendToTarget();
                    mCountTimer.cancel();
                }
            }, 55000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            cancelTimer();
            dismissDialog();
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
            if (myRecAudioFile != null) {
                myRecAudioFile.delete();
            }
        }
        mThread = new ObtainDecibelThread();
        mThread.start();
    }

    //停止录音，隐藏录音动画
    private void stopRecording() {
        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
    }

    /**
     * 释放资源
     */
    public void releaseRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder = null;
        }
    }

    /**
     * 分贝
     */
    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mRecorder == null || !running) {
                    break;
                }
                int x = mRecorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (20 * Math.log10(x));
                    int i = f / 10 - 4;
                    int volume = i < 0 ? 0 : (i > 4 ? 4 : i);
                    mVolumeHandler.sendEmptyMessage(volume);
                }
            }
        }
    }

    public void dismissDialog() {
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
        this.setText(R.string.record_voice_hint);
    }

    /**
     * 录音动画控制
     * 倒计时控制
     */
    private static class ShowVolumeHandler extends Handler {

        private final WeakReference<RecordVoiceButton> mButton;

        public ShowVolumeHandler(RecordVoiceButton button) {
            mButton = new WeakReference<>(button);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            RecordVoiceButton controller = mButton.get();
            if (controller != null) {
                int restTime = msg.getData().getInt("restTime", -1);
                // 若restTime>0, 进入倒计时
                if (restTime > 0) {
                    LogUtil.d("进入倒计时：" + restTime);
                    controller.mTimeUp = true;
                    android.os.Message msg1 = controller.mVolumeHandler.obtainMessage();
                    msg1.what = 60 - restTime + 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", restTime - 1);
                    msg1.setData(bundle);
                    //创建一个延迟一秒执行的HandlerMessage，用于倒计时
                    controller.mVolumeHandler.sendMessageDelayed(msg1, 1000);
                    controller.mRecordHintTv.setText(String.format(controller.mContext.getString(R.string.rest_record_time_hint), restTime + ""));
                    // 倒计时结束，发送语音, 重置状态
                } else if (restTime == 0) {
                    LogUtil.d("倒计时结束");
                    controller.time2 = System.currentTimeMillis();
                    controller.finishRecord();
                    controller.setPressed(false);
                    controller.mTimeUp = false;
                    // restTime = -1, 一般情况
                } else {
                    // 没有进入倒计时状态
                    if (!controller.mTimeUp) {
                        if (msg.what < CANCEL_RECORD) {
                            controller.mRecordHintTv.setText(R.string.move_to_cancel_hint);
                        } else {
                            controller.mRecordHintTv.setText(R.string.cancel_record_voice_hint);
                        }
                        // 进入倒计时
                    } else {
                        if (msg.what == CANCEL_RECORD) {
                            controller.mRecordHintTv.setText(R.string.cancel_record_voice_hint);
                            if (!mIsPressed) {
                                controller.cancelRecord();
                            }
                        }
                    }
                    controller.mVolumeIv.setImageResource(res[msg.what]);
                }
            }
        }
    }

}
