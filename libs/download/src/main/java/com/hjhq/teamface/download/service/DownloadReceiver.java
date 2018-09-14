package com.hjhq.teamface.download.service;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.log.LogUtil;

import java.io.File;

public class DownloadReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        LogUtil.e("=====", "下载的IDonReceive: " + completeDownloadId);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if (c.moveToFirst()) {
                //获取文件下载路径
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if (filename != null) {
                    LogUtil.e("=====", "下载完成的文件名为：" + filename);
                    //     /storage/emulated/0/zhnet/T台魅影.apk

                    //执行安装
//                    Intent intent_ins = new Intent(Intent.ACTION_VIEW);
//                    intent_ins.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_ins.setDataAndType(Uri.parse("file://" + filename), "application/vnd.android.package-archive");
//                    context.getApplicationContext().startActivity(intent_ins);
//                    filename = filename.substring(filename.lastIndexOf("/")+1, filename.lastIndexOf("."));
//                    Log.d("=====", "截取后的文件名onReceive: "+filename);
                    String path = Environment.getExternalStorageDirectory().getPath() + "//" + Constants.PATH_AUDIO;
                    File file = new File(path, filename);
                    Intent intent1 = new Intent();
                    intent1.setAction(Intent.ACTION_VIEW);
                    intent1.setDataAndType(Uri.parse("file://" + filename), "text/plain");
                    intent1.setDataAndType(Uri.fromFile(file), "text/plain");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent1);
                }
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
//            manager.remove(ids);
//            Toast.makeText(context, "已经取消下载", Toast.LENGTH_SHORT).show();

        }
    }
}