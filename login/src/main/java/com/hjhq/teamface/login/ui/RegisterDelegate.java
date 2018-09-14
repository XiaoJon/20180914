package com.hjhq.teamface.login.ui;

import android.widget.Button;
import android.widget.EditText;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.basis.util.TextWatcherUtil;

/**
 * 注册视图
 */

public class RegisterDelegate extends AppDelegate {
    public Button registerBtn;
    public EditText companyEt;
    public EditText nameEt;
    public EditText pwdEt;
    public EditText inviteEt;

    @Override
    public int getRootLayoutId() {
        return R.layout.login_activity_register;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("注册");
        hideTitleLine();

        registerBtn = get(R.id.register_btn);
        companyEt = get(R.id.company_et);
        nameEt = get(R.id.name_et);
        pwdEt = get(R.id.pwd_et);
        inviteEt = get(R.id.invite_et);
        TextWatcherUtil.setEdNoChinaese(pwdEt);
    }

    /**
     * 获取公司名称
     */
    public String getCompanyName() {
        return companyEt.getText().toString().trim();
    }

    /**
     * 获取姓名
     */
    public String getName() {
        return nameEt.getText().toString().trim();
    }

    /**
     * 获取密码
     */
    public String getPwd() {
        return pwdEt.getText().toString().trim();
    }

    /**
     * 获取邀请码
     */
    public String getInviteCode() {
        return inviteEt.getText().toString().trim();
    }
}
