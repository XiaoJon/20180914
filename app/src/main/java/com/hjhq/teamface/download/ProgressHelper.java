package com.hjhq.teamface.download;

import android.content.Intent;

import com.hjhq.teamface.MyApplication;
import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by ljd on 4/12/16.
 */
public class ProgressHelper {

    private static ProgressBean progressBean = new ProgressBean();
    private static ProgressHandler mProgressHandler;
    private static String fileId;
    private static long time;

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        final ProgressListener progressListener = new ProgressListener() {


            //该方法在子线程中运行
            @Override
            public void onProgress(String id, long progress, long total, boolean done) {
                /*Log.d("ProgressHelper进度", String.format("%d%% done\n", (100 * progress) / total));
                Log.e("fileId==", fileId + "");
                Log.e("progress==", progress + "");
                Log.e("total==", total + "");
                Log.e("done==", done + "");*/


                /*if (System.currentTimeMillis() - time < 300) {
                    return;

                } else {}*/
//                Log.e("time==1    ", time + "");
//                Log.e("time==2    ", System.currentTimeMillis() - time + "");
                long t1 = System.currentTimeMillis() - time;
                if (progress < total && t1 < 50) {
//                    Log.e("time==3    ", t1 + "");
                    return;
                } else {
//                    Log.e("time==4    ", System.currentTimeMillis() - time + "");
                    time = System.currentTimeMillis();
                }
                progressBean = new ProgressBean();

                progressBean.setFileId(fileId);
                progressBean.setBytesRead(progress);
                progressBean.setContentLength(total);
                progressBean.setDone(done);
                Intent intent = new Intent();
                intent.setAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
                if (progressBean.isDone()) {
                    intent.setAction(FileConstants.FILE_DOWNLOAD_SUCCESS_ACTION);
                }
                intent.putExtra(Constants.DATA_TAG1, progressBean);
                MyApplication.getInstance().sendBroadcast(intent);


                /*if (mProgressHandler == null) {
                    return;
                }

                if (mProgressHandler != null) {
                    mProgressHandler.sendMessage(progressBean);
                }*/


            }
        };

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });

        return builder;
    }

    public static void setProgressHandler(ProgressHandler progressHandler) {
        mProgressHandler = progressHandler;
    }

    public static void setDownloadId(String id) {
        fileId = id;
    }
}
