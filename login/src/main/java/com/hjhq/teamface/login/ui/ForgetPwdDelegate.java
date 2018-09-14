package com.hjhq.teamface.login.ui;

import android.widget.Button;
import android.widget.EditText;

import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;

/**
 * 忘记密码
 * @author Administrator
 */

public class ForgetPwdDelegate extends AppDelegate {
    public Button okBtn;
    public EditText newPwdEt;
    public EditText repeatNewPwdEt;

    @Override
    public int getRootLayoutId() {
        return R.layout.login_activity_forget_pwd;
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
        setTitle("找回密码");
        okBtn = get(R.id.ok_btn);
        newPwdEt = get(R.id.new_pwd_et);
        repeatNewPwdEt = get(R.id.repeat_new_pwd_et);
        TextWatcherUtil.setEdNoChinaese(repeatNewPwdEt);
    }


    /**
     * 得到密码
     */
    public String getPassword() {
        return newPwdEt.getText().toString().trim();
    }

    /**
     * 得到重复密码
     */
    public String getRepeatPwd() {
        return repeatNewPwdEt.getText().toString().trim();
    }
}
