package com.hjhq.teamface.common.ui.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 语音播放器
 *
 * @author Administrator
 * @date 2018/1/24
 */

public class VoicePlayActivity extends ActivityPresenter<VoicePlayDelegate, CommonModel> implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private MyReceiver mReceiver;
    private boolean mFromUser;
    private UpdateProgress mUpdateprogress;
    //音频地址
    private String fileUrl;
    //总进度
    private int totalProgress;
    //当前进度
    private int currentProgress = 0;
    private Map<String, String> headerMap = new HashMap<>();
    //语音时长
    private int voiceTime;
    private Handler handler;
    private boolean isPrepared;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            fileUrl = getIntent().getStringExtra(Constants.DATA_TAG1);
            voiceTime = getIntent().getIntExtra(Constants.DATA_TAG2, 0);
        }
    }

    @Override
    public void init() {
        handler = new Handler();
        headerMap.put("TOKEN", SPUtils.getString(this, AppConst.TOKEN_KEY));
        mUpdateprogress = new UpdateProgress();
        try {
            initAudioPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initReceiver();
    }


    /**
     * 初始化播放器
     *
     * @throws IOException
     */
    private void initAudioPlayer() throws IOException {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mediaPlayer.isPlaying()) {
            return;
        }

        File file = new File(fileUrl);
        if (file.exists()) {
            Uri audioUri = Uri.fromFile(file);
            mediaPlayer.setDataSource(mContext, audioUri);
        } else {
            Uri audioUri = Uri.parse(fileUrl);
            mediaPlayer.setDataSource(VoicePlayActivity.this, audioUri, headerMap);
        }
        mediaPlayer.prepareAsync();
        if (voiceTime == 0) {
            mediaPlayer.getDuration();
        } else {
            totalProgress = voiceTime;
        }

        viewDelegate.setProgress(currentProgress, totalProgress);
        viewDelegate.setCurrentTime(DateTimeUtil.getDurationString(currentProgress));
        viewDelegate.setTotalTime(DateTimeUtil.getDurationString(totalProgress));
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
        //有线耳机拔出或无线耳机中断
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        viewDelegate.setPauseState();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.play_pause);
        viewDelegate.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mFromUser = true;
                    viewDelegate.setCurrentTime(DateTimeUtil.getDurationString(progress));
                    viewDelegate.setPlayState();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            viewDelegate.setProgress(totalProgress);
            handler.removeCallbacks(mUpdateprogress);
            new Handler().postDelayed(() -> {
                currentProgress = 0;
                viewDelegate.setProgress(currentProgress);
                viewDelegate.setCurrentTime(DateTimeUtil.getDurationString(currentProgress));
                viewDelegate.setPauseState();
            }, 100);
        });
        //SeekTo 拖动监听
        mediaPlayer.setOnSeekCompleteListener(mp -> {
            if (!isPrepared) {
                ToastUtils.showError(mContext, "正在加载音频");
                return;
            }
            mediaPlayer.start();
            mFromUser = false;
            handler.post(mUpdateprogress);
        });
        //播放器准备好
        mediaPlayer.setOnPreparedListener(mp -> {
            // ToastUtils.showToast(mContext, "准备就绪");
            isPrepared = true;
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.play_pause) {
            playOrPause();
        }
    }

    /**
     * 播放或暂停
     */
    private void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            currentProgress = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            viewDelegate.setPauseState();
        } else {
            try {
                if (!isPrepared) {
                    ToastUtils.showError(mContext, "正在加载音频");
                    return;
                }
                requestAudioFocus();
                mediaPlayer.start();
                viewDelegate.setPlayState();
                handler.post(mUpdateprogress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新进度
     */
    private class UpdateProgress implements Runnable {
        @Override
        public void run() {
            if (mFromUser || mediaPlayer == null) {
                return;
            }
            int currentPosition = mediaPlayer.getCurrentPosition();
            viewDelegate.setProgress(currentPosition);
            viewDelegate.setCurrentTime(DateTimeUtil.getDurationString(currentPosition));
            int time = totalProgress - currentPosition;
            if (time > 0) {
                handler.postDelayed(mUpdateprogress, Math.min(time, 500));
            }
        }
    }

    /**
     * 请求音频焦点，开始播放时候调用
     *
     * @return
     */
    public boolean requestAudioFocus() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /**
     * 放弃音频焦点，销毁播放时候调用
     */
    public void abandonAudioFocus() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 当音频焦点发生变化的时候调用这个方法，在这里可以处理逻辑
     * 欢迎访问我的GitHub：https://github.com/yangchong211
     * 如果可以的话，请star吧
     *
     * @param focusChange 焦点改变
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
    }

    /**
     * 耳机拔出或无线耳机中断广播
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    viewDelegate.setPlayState();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mUpdateprogress);
        mediaPlayer.stop();
        mediaPlayer.release();
        abandonAudioFocus();
        mediaPlayer = null;
    }
}
