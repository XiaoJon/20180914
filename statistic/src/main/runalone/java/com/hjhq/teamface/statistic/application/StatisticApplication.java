package com.hjhq.teamface.statistic.application;

import android.support.multidex.MultiDexApplication;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.statistic.ui.StatisticsActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *
 * @author Administrator
 * @date 2018/3/13
 */

public class StatisticApplication extends MultiDexApplication {

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
                CommonUtil.startActivtiyNewTask(this, StatisticsActivity.class);
                break;
            default:
                break;
        }
    }
}
