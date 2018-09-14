package com.hjhq.teamface.email.view;


import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.email.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class ChooseEmailContactsDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.email_choose_contacts_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.main_green, getActivity().getString(R.string.confirm));
        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("联系人");
    }
}
