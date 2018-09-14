package com.hjhq.teamface.im.view;

import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.im.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class SendMessageDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_send_message;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("登录");
        getRootView().setBackgroundColor(getActivity().getResources().getColor(R.color.white));
    }
}
