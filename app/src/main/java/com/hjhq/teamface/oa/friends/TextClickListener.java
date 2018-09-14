package com.hjhq.teamface.oa.friends;

import android.os.Bundle;
import android.view.View;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.oa.main.EmployeeInfoActivity;

/**
 * TextView文字的点击监听
 *
 * @author kin
 */
public class TextClickListener implements View.OnClickListener {

    private BaseActivity mActivity;
    private String friendId;

    public TextClickListener(String friendId, BaseActivity activity) {
        this.friendId = friendId;
        this.mActivity = activity;
    }

    @Override
    public void onClick(View arg0) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, friendId);
        CommonUtil.startActivtiy(mActivity, EmployeeInfoActivity.class, bundle);
    }

}
