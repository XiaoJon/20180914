package com.hjhq.teamface.project.application;


import android.app.Application;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.presenter.ProjectActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class ProjectApplication extends Application {
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
                CommonUtil.startActivtiyNewTask(this, ProjectActivity.class);
                break;
            default:
                break;
        }
    }
}
