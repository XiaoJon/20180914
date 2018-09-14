package com.hjhq.teamface.memo.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.memo.R;

/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class MemoRemindModeDelegate extends AppDelegate {
    private TextView tvTime;
    private com.kyleduo.switchbutton.SwitchButton mSwitchButton;

    @Override
    public int getRootLayoutId() {
        return R.layout.memo_activity_time_and_mode;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("提醒");
        setRightMenuTexts(R.color.black, "确定");
        tvTime = get(R.id.tv_time);
        mSwitchButton = get(R.id.mode_btn);
    }

    public void setListener(View.OnClickListener listener) {
        get(R.id.rl_remind_time).setOnClickListener(listener);
    }

    public void setTime(String time) {
        TextUtil.setText(tvTime, time);
    }



    public void setMode(int remindMode) {
        if (MemoConstant.REMIND_MODE_SELF == remindMode) {
            mSwitchButton.setChecked(true);
        } else {
            mSwitchButton.setChecked(false);
        }

    }

    public int getMode() {
        if (mSwitchButton.isChecked()) {
            return MemoConstant.REMIND_MODE_SELF;
        } else {
            return MemoConstant.REMIND_MODE_ALL;
        }
    }
}
