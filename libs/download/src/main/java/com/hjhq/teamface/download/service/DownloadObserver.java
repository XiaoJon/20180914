package com.hjhq.teamface.download.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.log.LogUtil;

public class DownloadObserver extends ContentObserver {
    private Handler mHandler;
    private Context mContext;
    private int progress;
    private DownloadManager mDownloadManager;
    private DownloadManager.Query query;
    private Cursor cursor;
    private long downloadProgress = -1L;
    private long downloadId;
    private Uri mUri;

    @SuppressLint("NewApi")
    public DownloadObserver(Handler handler, Context context, long downId) {
        super(handler);
        // TODO Auto-generated constructor stub  
        this.mHandler = handler;
        this.mContext = context;
        this.downloadId = downId;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mUri = mDownloadManager.getUriForDownloadedFile(downId);
        query = new DownloadManager.Query().setFilterById(downId);


    }

    @SuppressLint("NewApi")
    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询  
        super.onChange(selfChange);
        //发送消息
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_TAG1, getBytesAndStatus(downloadId));
        bundle.putLong(Constants.DATA_TAG2, downloadId);
        msg.setData(bundle);
        msg.what = 100;
        mHandler.sendMessage(msg);


        boolean downloading = true;
        while (downloading) {
            cursor = mDownloadManager.query(query);
            if (cursor != null) {
                cursor.moveToFirst();
            } else {
                cursor.close();
                downloading = false;
            }
            int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            progress = (bytesDownloaded * 100) / bytesTotal;
            // mHandler.sendEmptyMessageDelayed(progress, 100);
            if (bytesTotal <= 0) {
                continue;
            }
            if (bytesDownloaded == downloadProgress) {
                continue;
            } else {
                downloadProgress = bytesDownloaded;
            }
            Message msg2 = Message.obtain();
            Bundle bundle2 = new Bundle();
            bundle2.putInt(Constants.DATA_TAG1, bytesDownloaded);
            bundle2.putInt(Constants.DATA_TAG2, bytesTotal);
            msg2.setData(bundle2);
            msg2.what = progress;
            LogUtil.e("进度===" + progress);
            LogUtil.e(bytesDownloaded + " / " + bytesTotal);
            // TODO: 2018/1/6 使用广播发送
            if (cursor == null) {
                downloading = false;
            }
            //发送下载错误广播
            mHandler.sendMessageDelayed(msg2, 100);
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
//                下载完成
                cursor.close();
                msg2 = Message.obtain();
                bundle2 = new Bundle();
                bundle2.putInt(Constants.DATA_TAG1, bytesDownloaded);
                bundle2.putInt(Constants.DATA_TAG2, bytesTotal);
                msg2.setData(bundle2);
                msg2.what = progress;
                LogUtil.e("进度2===" + progress);
                LogUtil.e("进度2===" + bytesDownloaded + " / " + bytesTotal);
                //发送下载错误广播
                mHandler.sendMessageDelayed(msg2, 100);
            }
            if (bytesDownloaded == bytesTotal) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

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
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

} 