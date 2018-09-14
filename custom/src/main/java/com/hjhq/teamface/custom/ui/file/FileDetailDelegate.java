package com.hjhq.teamface.custom.ui.file;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.player.AudioPlayerView;
import com.hjhq.teamface.custom.R;

import java.io.File;

/**
 * Created by Administrator on 2018/4/10.
 * 项目文件详情
 */

public class FileDetailDelegate extends AppDelegate {
    UploadFileBean mBean;
    RelativeLayout rlAudio;
    RelativeLayout rlImage;
    RelativeLayout rlOther;
    RelativeLayout rlAudioRoot;
    ImageView ivDeleteImage;
    ImageView ivDeleteAudio;
    ImageView ivImage;
    TextView tvFileName;
    TextView tvProgress;
    TextView tvFileSize;
    Button btnDownload;
    Button btnOpen;
    RelativeLayout rlProgress;
    ProgressBar progressBar;
    AudioPlayerView playerView;

    private File mFile;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_file_detail_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        rlAudio = get(R.id.rl_audio);
        rlAudioRoot = get(R.id.rl_audio_root);
        rlImage = get(R.id.rl_image);
        rlOther = get(R.id.rl_other_type);
        ivImage = get(R.id.iv_thumb);
        tvFileName = get(R.id.file_name);
        tvProgress = get(R.id.tv_progress);
        tvFileSize = get(R.id.file_info);
        btnDownload = get(R.id.btn_download);
        ivDeleteAudio = get(R.id.iv_delete);
        ivDeleteImage = get(R.id.iv_delete2);
        btnOpen = get(R.id.btn_open);
        rlProgress = get(R.id.rl_progress);
        progressBar = get(R.id.progressbar);
    }

    public void showData(UploadFileBean bean) {
        if (bean == null) {
            return;
        }
        mBean = bean;
        mFile = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD).getAbsolutePath(), mBean.getSerialNumber() + mBean.getFileName());
        String fileUrl = mBean.getFileUrl();
        String fileType = mBean.getFileType();
        if (FileTypeUtils.isImage(fileType)) {
            rlImage.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(getActivity(), fileUrl + "&width=480", ivImage);
            rlImage.setOnClickListener(v -> {

            });

        } else if (FileTypeUtils.isAudio(fileType)) {
            rlAudioRoot.setVisibility(View.VISIBLE);
            playerView = new AudioPlayerView(getActivity());
            playerView.setUrlResource(fileUrl);
            rlAudio.addView(playerView);

        } else {
            rlOther.setVisibility(View.VISIBLE);
            // progressBar.setMax(TextUtil.parseInt(mBean.getSize()));
            progressBar.setProgress(0);
            if (mFile.exists()) {
                btnOpen.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
            } else {
                btnOpen.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
            }
            TextUtil.setText(tvFileName, mBean.getFileName());
            TextUtil.setText(tvFileSize, FormatFileSizeUtil.formatFileSize(TextUtil.parseLong(mBean.getFileSize())));
        }


    }


    public void localFileNotExist() {
        btnDownload.setVisibility(View.VISIBLE);
        btnOpen.setVisibility(View.GONE);

    }

    public void isDownloading(boolean b) {
        if (b) {
            btnDownload.setVisibility(View.GONE);
            btnOpen.setVisibility(View.GONE);
            rlProgress.setVisibility(View.VISIBLE);

        } else {
            progressBar.setProgress(0);
            rlProgress.setVisibility(View.GONE);
            if (mFile.exists()) {
                btnDownload.setVisibility(View.GONE);
                btnOpen.setVisibility(View.VISIBLE);
            } else {
                btnDownload.setVisibility(View.VISIBLE);
                btnOpen.setVisibility(View.GONE);
            }
        }


    }

    /**
     * 更新进度
     *
     * @param format
     */
    public void updateProgress(String format, long current, long total) {
        TextUtil.setText(tvProgress, format);
        progressBar.setProgress((int) ((current * 100) / total));
        Log.e("进度", current + "<-当前  总数->" + total);
        Log.e("进度", (int) ((current * 100) / total) + "");
    }

    public void stopPlay() {
        if (playerView != null) {
            playerView.pause();
        }
    }

    /**
     * 控制删除按钮显示与隐藏
     *
     * @param b
     */
    public void showDeletBtn(boolean b) {
        if (b) {
            ivDeleteImage.setVisibility(View.VISIBLE);
            ivDeleteAudio.setVisibility(View.VISIBLE);
        } else {
            ivDeleteImage.setVisibility(View.GONE);
            ivDeleteAudio.setVisibility(View.GONE);
        }
    }
}
