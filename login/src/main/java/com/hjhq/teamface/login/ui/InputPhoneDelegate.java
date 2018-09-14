package com.hjhq.teamface.login.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;

/**
 * 注册视图
 */

public class InputPhoneDelegate extends AppDelegate {
    public EditText mPhoneEt;
    public Button mNextStepBtn;
    public TextView mTvServiceAgreement;
    public ImageView ivAnim;
    public RelativeLayout rlRoot;

    @Override
    public int getRootLayoutId() {
        return R.layout.login_activity_input_phone;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        hideTitleLine();
        getRootView().setBackgroundResource(R.color.white);
        mPhoneEt = get(R.id.phone_et);
        mNextStepBtn = get(R.id.next_step_btn);
        mTvServiceAgreement = get(R.id.tv_service_agreement);
        ivAnim = get(R.id.iv_anim);
        rlRoot = get(R.id.rl_action);
    }

    /**
     * 得到手机号
     */
    public String getPhone() {
        return mPhoneEt.getText().toString().trim();
    }

    /**
     * 隐藏服务协议
     */
    public void hideAgreement() {
        get(R.id.service_agreement_ll).setVisibility(View.GONE);
    }
}
