package com.hjhq.teamface.download;

import android.os.Looper;
import android.os.Message;

import com.hjhq.teamface.basis.bean.ProgressBean;

/**
 * Created by ljd on 4/18/16.
 */
public abstract class UploadProgressHandler extends ProgressHandler{

    private static final int UPLOAD_PROGRESS = 0;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    protected void sendMessage(ProgressBean progressBean) {
        mHandler.obtainMessage(UPLOAD_PROGRESS,progressBean).sendToTarget();

    }

    @Override
    protected void handleMessage(Message message){
        switch (message.what){
            case UPLOAD_PROGRESS:
                ProgressBean progressBean = (ProgressBean)message.obj;
                onProgress("",progressBean.getBytesRead(),progressBean.getContentLength(),progressBean.isDone());
        }
    }

}
