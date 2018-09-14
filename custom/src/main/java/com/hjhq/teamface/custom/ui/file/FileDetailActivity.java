package com.hjhq.teamface.custom.ui.file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.ui.add.AddCustomModel;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.File;

/**
 * 文件详情
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/file", desc = "文件详情")
public class FileDetailActivity extends ActivityPresenter<FileDetailDelegate, AddCustomModel> {
    private MyReceiver mReceiver;
    private boolean hasInitReceiver = false;
    UploadFileBean mBean;
    private boolean isDel;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        initReceiver();
        if (savedInstanceState == null) {
            mBean = (UploadFileBean) getIntent().getSerializableExtra(Constants.DATA_TAG1);
            isDel = getIntent().getBooleanExtra(Constants.DATA_TAG2, false);
        }
    }

    private void initReceiver() {
        if (hasInitReceiver) {
            return;
        }
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
        registerReceiver(mReceiver, intentFilter);
        hasInitReceiver = true;
    }

    @Override
    public void init() {
        if (mBean == null) {
            ToastUtils.showError(mContext, "文件错误");
            finish();
        }
        viewDelegate.setTitle(mBean.getFileName());
        viewDelegate.showData(mBean);
        if (isDel) {
            viewDelegate.showDeletBtn(true);
        } else {
            viewDelegate.showDeletBtn(false);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.iv_delete).setOnClickListener(v -> {
            //删除文件
            deleteFile();
        });
        viewDelegate.get(R.id.iv_delete2).setOnClickListener(v -> {
            //删除文件
            deleteFile();
        });
        viewDelegate.get(R.id.btn_open).setOnClickListener(v -> {
            //打开文件
            File file = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD).getAbsolutePath(), mBean.getSerialNumber() + mBean.getFileName());
            if (file.exists()) {
                openFile(file);
            } else {
                ToastUtils.showToast(mContext, "本地文件不存在");
                viewDelegate.isDownloading(false);
            }

        });
        viewDelegate.get(R.id.btn_download).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getFileSize()));
        });
        viewDelegate.get(R.id.iv_download).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getFileSize()));
        });
        viewDelegate.get(R.id.iv_download2).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getFileSize()));
        });
        viewDelegate.get(R.id.iv_thumb).setOnClickListener(v -> {
            //全屏查看图片
        });
        viewDelegate.get(R.id.tv_view_online).setOnClickListener(v -> {
            //在线预览
        });
        viewDelegate.get(R.id.cancel_download).setOnClickListener(v -> {
            //取消下载
            FileTransmitUtils.cancelDownload(mBean.getSerialNumber());
            viewDelegate.isDownloading(false);
        });

    }

    /**
     * 删除文件
     */
    private void deleteFile() {
        DialogUtils.getInstance().sureOrCancel(mContext, "", "确认删除文件吗?", viewDelegate.getRootView(), () -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    /**
     * 检查文件大小并下载
     *
     * @param size
     */
    public void checkFileSizeAndDownload(long size) {
        if (FileTransmitUtils.checkLimit(size)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续下载吗?",
                    viewDelegate.getRootView(), () -> downloadFile());
        } else {
            downloadFile();
        }
    }

    private void downloadFile() {
        viewDelegate.isDownloading(true);
        DownloadService.getInstance().downloadFileFromUrl(mBean.getSerialNumber(), mBean.getFileUrl(), mBean.getFileName());
    }

    private void openFile(File file) {
        if (!file.exists()) {
            ToastUtils.showToast(mContext, "本地文件不存在");
            viewDelegate.localFileNotExist();
            return;
        }
        FileUtils.browseDocument(this, viewDelegate.getRootView(), file.getName(), file.getAbsolutePath());
        LogUtil.e("文件名" + file + "绝对路径" + file.getAbsolutePath());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDelegate.stopPlay();
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (mBean == null) {
                return;
            }

            if (FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION.equals(action)) {
                //文件下载进度
                ProgressBean bean = (ProgressBean) intent.getSerializableExtra(Constants.DATA_TAG1);
                if (bean.getBytesRead() <= 0 || bean.getContentLength() <= 0) {
                    return;
                }
                if (bean.getFileId().equals(mBean.getSerialNumber())) {
                    if (bean.getStatus() == 8) {
                        viewDelegate.isDownloading(false);
                    } else if (bean.getStatus() == 16) {
                        viewDelegate.isDownloading(false);
                        ToastUtils.showError(mContext, "下载失败");
                    }
                    if (bean.getBytesRead() <= 0 || bean.getContentLength() <= 0) {
                        return;
                    }
                    viewDelegate.updateProgress(
                            String.format("下载中(%s)", FormatFileSizeUtil.formatFileSize(bean.getBytesRead()) + "/" + FormatFileSizeUtil.formatFileSize(bean.getContentLength())),
                            bean.getBytesRead(), bean.getContentLength());
                }


            }


        }
    }


}
