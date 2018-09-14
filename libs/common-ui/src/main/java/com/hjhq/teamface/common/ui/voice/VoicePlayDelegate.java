package com.hjhq.teamface.common.ui.voice;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 语音播放
 * Created by lx on 2017/9/4.
 */

public class VoicePlayDelegate extends AppDelegate {

    protected ImageView ivPlayPause;
    protected SeekBar mSeekBar;
    protected TextView tvCurrent;
    protected TextView tvTotal;

    @Override
    public int getRootLayoutId() {
        return R.layout.voice_play_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("语音播放");
        ivPlayPause = get(R.id.play_pause);
        mSeekBar = get(R.id.seekbar);
        tvCurrent = get(R.id.current);
        tvTotal = get(R.id.total);

    }


    /**
     * 设置播放
     */
    public void setPlayState() {
        ivPlayPause.setImageResource(R.drawable.icon_audio_pause);
    }

    /**
     * 设置暂停
     */
    public void setPauseState() {
        ivPlayPause.setImageResource(R.drawable.icon_audio_play);
    }

    /**
     * 设置当前播放时间
     *
     * @param durationString
     */
    public void setCurrentTime(String durationString) {
        TextUtil.setText(tvCurrent, durationString);
    }

    /**
     * 设置总时长
     *
     * @param durationString
     */
    public void setTotalTime(String durationString) {
        TextUtil.setText(tvTotal, durationString);
    }

    /**
     * 设置进度
     *
     * @param currentProgress
     */
    public void setProgress(int currentProgress) {
        mSeekBar.setProgress(currentProgress);
    }

    /**
     * 设置进度
     *
     * @param currentProgress
     * @param totalProgress
     */
    public void setProgress(int currentProgress, int totalProgress) {
        setProgress(currentProgress);
        mSeekBar.setMax(totalProgress);
    }

   
}

