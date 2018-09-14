package com.hjhq.teamface.login.ui;

import android.widget.Button;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.view.verify.CountDownButton;
import com.hjhq.teamface.login.view.verify.SecurityCodeView;

/**
 * 输入验证码视图
 */

public class InputVerifyDelegate extends AppDelegate {
    public SecurityCodeView mVerifyCodeEt;
    public CountDownButton verifyCodeBtn;
    public Button mNextStepBtn;

    @Override
    public int getRootLayoutId() {
        return R.layout.login_activity_input_verify;
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
        mVerifyCodeEt = get(R.id.verification_code_et);
        verifyCodeBtn = get(R.id.timmer_btn);
        verifyCodeBtn.setCount(Constants.VERIFY_CODE_TIME);
        verifyCodeBtn.setEnableCountDown(false);
        mNextStepBtn = get(R.id.next_step_btn);
    }


    /**
     * 得到验证码
     */
    public String getVerifyCode() {
        return mVerifyCodeEt.getEditContent().trim();
    }


    /**
     * 验证码失败
     */
    public void verifyCodeFailed() {
        verifyCodeBtn.setEnabled(true);
        verifyCodeBtn.setEnableCountDown(false);
        verifyCodeBtn.setText("重新获取");
    }

    /**
     * 验证码成功
     */
    public void verifyCodeSuccess() {
        verifyCodeBtn.removeCountDown();
        verifyCodeBtn.setEnableCountDown(true);
        verifyCodeBtn.startCount();
    }

    /**
     * 移除倒计时
     */
    public void removeCountDown() {
        if (verifyCodeBtn.isCountDownNow()) {
            verifyCodeBtn.removeCountDown();
        }
    }

    /**
     * 是否可用
     */
    public boolean getEnable() {
        return verifyCodeBtn.getEnableCountDown() || !verifyCodeBtn.isEnabled();
    }

    /**
     * 发送验证码中
     */
    public void sendVerify() {
        verifyCodeBtn.setEnabled(false);
        verifyCodeBtn.setText("正在获取...");
    }
}
