package com.hjhq.teamface.attendance.application;

import android.app.Application;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/3/13.
 */

public class AttendanceApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPHelper.init(this);
        EventBusUtils.register(this);
        AppManager.init(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        int code = event.getCode();
        switch (code) {
            case EventConstant.LOGIN_TAG:
                //CommonUtil.startActivtiyNewTask(this, MemoListActivity.class);
                break;
            default:
                break;
        }
    }
}
