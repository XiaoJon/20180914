package com.hjhq.teamface.download.service;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.download.utils.FileTransmitUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DownloadChangeObserver extends ContentObserver {
    private Handler mHandler;
    private DownloadManager mDownloadManager;
    private ScheduledExecutorService scheduledExecutorService;
    private Runnable progressRunnable;
    private long mDownloadId;
    private boolean downloadFinish = false;

    public DownloadChangeObserver(Handler handler, DownloadManager downloadManager, final long downloadId) {
        super(handler);
        mHandler = handler;
        mDownloadManager = downloadManager;
        mDownloadId = downloadId;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                int[] bytesAndStatus = getBytesAndStatus(mDownloadId);
                Message msg2 = Message.obtain();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(Constants.DATA_TAG1, bytesAndStatus);
                bundle2.putLong(Constants.DATA_TAG2, mDownloadId);
                msg2.setData(bundle2);
                msg2.what = bytesAndStatus[2];
                Log.e("当前===", "" + bytesAndStatus[0]);
                Log.e("全部===", "" + bytesAndStatus[1]);
                Log.e("状态===", "" + bytesAndStatus[2]);
                /*if (bytesAndStatus[0] == -1 && bytesAndStatus[1] == -1) {
                    FileTransmitUtils.cancelDownload(mDownloadId);
                    scheduledExecutorService.shutdown();
                    mHandler.sendMessage(msg2);
                    return;
                }*/

                if (16 == bytesAndStatus[2]) {
                    Log.e("状态===", "下载失败  错误码" + bytesAndStatus[2]);
                    scheduledExecutorService.shutdown();
                    mHandler.sendMessage(msg2);
                    FileTransmitUtils.cancelDownload(mDownloadId);
                    return;
                }
                /*if (bytesAndStatus[1] == -1) {
                    // FileTransmitUtils.cancelDownload(mDownloadId);
                    return;
                }*/
                if (bytesAndStatus[0] > 0 && bytesAndStatus[1] > 0) {
                    //发送下载广播
                    mHandler.sendMessage(msg2);
                    int state = bytesAndStatus[2];
                    switch (state) {
                        case 1 << 0:
                            //等待下载

                            break;
                        case 1 << 1:
                            //正在下载

                            break;
                        case 1 << 2:
                            //下载正在等待或恢复

                            break;
                        case 1 << 3:
                            //下载成功
                            if (bytesAndStatus[2] == 8) {
                                //下载成功
                                scheduledExecutorService.shutdown();
                                downloadFinish = true;

                            }
                            break;

                        case 1 << 4:
                            //下载失败
                            scheduledExecutorService.shutdown();
                            downloadFinish = true;
                            break;
                    }
                }
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     *
     * @param selfChange 此值意义不大, 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
        if (downloadFinish) {
            return;
        }

    }


    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int status = cursor.getInt(bytesAndStatus[2]);
                int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                int reason = cursor.getInt(columnReason);
                switch (status) {
                    case DownloadManager.STATUS_FAILED:
                        switch (reason) {
                            case DownloadManager.ERROR_CANNOT_RESUME:
                                //some possibly transient error occurred but we can't resume the download
                                break;
                            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                //no external storage device was found. Typically, this is because the SD card is not mounted
                                break;
                            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                //the requested destination file already exists (the download manager will not overwrite an existing file)
                                break;
                            case DownloadManager.ERROR_FILE_ERROR:
                                //a storage issue arises which doesn't fit under any other error code
                                break;
                            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                //an error receiving or processing data occurred at the HTTP level
                                break;
                            case DownloadManager.ERROR_INSUFFICIENT_SPACE://sd卡满了
                                //here was insufficient storage space. Typically, this is because the SD card is full
                                break;
                            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                //there were too many redirects
                                break;
                            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                //an HTTP code was received that download manager can't handle
                                break;
                            case DownloadManager.ERROR_UNKNOWN:
                                //he download has completed with an error that doesn't fit under any other error code
                                break;
                        }
                        // isNeedDownloadAgain = true;

                        // AlertUtil.alert("开始重新下载更新!", mContext);
                        break;
                    case DownloadManager.STATUS_PAUSED:

                        switch (reason) {
                            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                //the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed

                                break;
                            case DownloadManager.PAUSED_UNKNOWN:
                                //the download is paused for some other reason
                                break;
                            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                //the download is waiting for network connectivity to proceed
                                break;
                            case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                //the download is paused because some network error occurred and the download manager is waiting before retrying the request
                                break;
                        }
                        // isNeedDownloadAgain = false;

                        // AlertUtil.alert("下载已暂停，请继续下载！", mContext);
                        break;
                    case DownloadManager.STATUS_PENDING:
                        //the download is waiting to start
                        //isNeedDownloadAgain = false;
                        // AlertUtil.alert("更新正在下载！", mContext);
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        //the download is currently running
                        // isNeedDownloadAgain = false;
                        // AlertUtil.alert("更新正在下载！", mContext);
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        //the download has successfully completed
                        //isNeedDownloadAgain = false;
                        // installApk(id, downloadManager, mContext);
                        break;
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }
}