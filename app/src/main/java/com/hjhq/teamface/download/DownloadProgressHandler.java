package com.hjhq.teamface.download;


import android.os.Looper;
import android.os.Message;

import com.hjhq.teamface.basis.bean.ProgressBean;

/**
 * Created by ljd on 4/12/16.
 */
public abstract class DownloadProgressHandler extends ProgressHandler {

    private static final int DOWNLOAD_PROGRESS = 1;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    protected void sendMessage(ProgressBean progressBean) {


    }

    @Override
    protected void handleMessage(Message message) {
        switch (message.what) {
            case DOWNLOAD_PROGRESS:
                ProgressBean progressBean = (ProgressBean) message.obj;
                onProgress("", progressBean.getBytesRead(), progressBean.getContentLength(), progressBean.isDone());

        }
    }


}
