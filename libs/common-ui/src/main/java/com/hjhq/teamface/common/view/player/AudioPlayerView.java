package com.hjhq.teamface.common.view.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.common.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/18.
 * Describe：
 */

public class AudioPlayerView extends RelativeLayout {
    private Context mContext;
    private Map<String, String> headerMap = new HashMap<>();
    private int currentProgress = 0;
    private int totalProgress;
    private UpdateProgress mUpdateprogress;
    private int mProgress;
    private boolean mFromUser = false;
    private MediaPlayer mediaPlayer;
    private ImageView ivImage;
    private ImageView ivLike;
    private ImageView playOrPause;
    private ProgressBar progressbar;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar sbSeekbar;

    private File audioFile;
    private Uri audioUri;
    private AudioManager mAudioManager;

    public AudioPlayerView(Context context) {
        super(context, null);
        this.mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.audio_player_view, null);
        addView(view);
        initView();
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setResource(File file) {
        this.audioFile = file;
        try {
            initAudioPlayer();
        } catch (IOException e) {

        }
    }

    public void setLocalPathResource(String fileLocalPath) {
        this.audioFile = new File(fileLocalPath);
        try {
            initAudioPlayer();
        } catch (IOException e) {

        }
    }

    public void setUriResource(Uri fileUri) {
        this.audioUri = fileUri;
        try {
            initAudioPlayer();
        } catch (IOException e) {

        }
    }

    public void setUrlResource(String fileUrl) {
        this.audioUri = Uri.parse(fileUrl);
        try {
            initAudioPlayer();
        } catch (IOException e) {

        }
    }


    private void initView() {
        headerMap.put("TOKEN", SPUtils.getString(getContext(), AppConst.TOKEN_KEY));
        mediaPlayer = new MediaPlayer();
        mUpdateprogress = new UpdateProgress();
        playOrPause = (ImageView) findViewById(R.id.play_pause);
        tvCurrentTime = (TextView) findViewById(R.id.current);
        tvTotalTime = (TextView) findViewById(R.id.total);
        sbSeekbar = (SeekBar) findViewById(R.id.seekbar);
        playOrPause.setOnClickListener(v -> {
            playOrPause();
        });
        sbSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtil.e("进度=  " + progress);
                if (fromUser) {
                    mFromUser = true;
                    mProgress = progress;
                    TextUtil.setText(tvCurrentTime, DateTimeUtil.getDurationString(progress));
                    playOrPause.setImageResource(R.drawable.icon_audio_pause);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /*if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }*/

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mediaPlayer.isPlaying()) {

                }
                mediaPlayer.start();
                mediaPlayer.seekTo(mProgress);
                playOrPause.post(mUpdateprogress);
                mProgress = 0;
                mFromUser = false;
            }
        });

    }

    private void initAudioPlayer() throws IOException {
        if (audioFile == null && audioUri == null) {
            ToastUtils.showError(getContext(), "未获取可播放资源!");
            return;
        }
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            return;
        }

        if (audioFile != null && audioFile.exists()) {
            audioUri = Uri.fromFile(audioFile);
            mediaPlayer.setDataSource(audioUri.getPath());

        } else {
            mediaPlayer.setDataSource(mContext, audioUri, headerMap);
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(mp -> {
            //ToastUtils.showSuccess(getContext(),"准备就绪");
            totalProgress = mediaPlayer.getDuration();
            sbSeekbar.setMax(totalProgress);
            sbSeekbar.setProgress(currentProgress);
            TextUtil.setText(tvTotalTime, DateTimeUtil.getDurationString(totalProgress));
            TextUtil.setText(tvCurrentTime, DateTimeUtil.getDurationString(currentProgress));
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            currentProgress = 0;
            sbSeekbar.setProgress(0);
            TextUtil.setText(tvCurrentTime, DateTimeUtil.getDurationString(0));
            playOrPause.setImageResource(R.drawable.icon_audio_paly);

        });
        //mediaPlayer.prepare();


    }

    private void playOrPause() {
        if (audioFile == null && audioUri == null) {
            ToastUtils.showError(getContext(), "未获取可播放资源!");
            return;
        }

        if (mediaPlayer.isPlaying()) {
            currentProgress = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playOrPause.setImageResource(R.drawable.icon_audio_paly);
        } else {
            try {
                requestAudioFocus();
                mediaPlayer.start();
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "pause");
                mContext.sendBroadcast(i);
                playOrPause.postDelayed(new UpdateProgress(), 500);
                playOrPause.setImageResource(R.drawable.icon_audio_pause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;
        }
        return false;

    }

    /**
     * 释放资源
     */
    public void release() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.stop();
                playOrPause.setImageResource(R.drawable.icon_audio_play);
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    public void pause() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.pause();
                playOrPause.setImageResource(R.drawable.icon_audio_play);
                abandonAudioFocus();
            }
        }

    }

    private class UpdateProgress implements Runnable {
        @Override
        public void run() {
            if (mFromUser) {
                return;
            }
            sbSeekbar.setProgress(mediaPlayer.getCurrentPosition());
            TextUtil.setText(tvCurrentTime, DateTimeUtil.getDurationString(mediaPlayer.getCurrentPosition()));
            if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() < 500) {
                sbSeekbar.setProgress(0);
                TextUtil.setText(tvCurrentTime, DateTimeUtil.getDurationString(0));
            } else if (mediaPlayer.isPlaying()) {
                playOrPause.postDelayed(new UpdateProgress(), 500);
            }

        }
    }

    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };

    /**
     * 获取音频焦点
     */
    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (mAudioManager == null)
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager != null) {
            int ret = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (ret != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            }
        }

    }

    /**
     * 放弃音频焦点
     */
    private void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (mAudioManager != null) {

            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);

            mAudioManager = null;
        }
    }
}
