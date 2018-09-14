package com.hjhq.teamface.login.ui;

import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.basis.util.TextWatcherUtil;

/**
 * 登录视图
 *
 * @author Administrator
 */

public class LoginDelegate extends AppDelegate {
    public EditText phoneEt;
    public EditText pwdEt;
    public Button loginBtn;
    public ImageView showPwdSelectIv;
    public TextView mForgetPassword;
    public TextView registerTv;

    @Override
    public int getRootLayoutId() {
        return R.layout.login_activity_login;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.login);
        phoneEt = get(R.id.phone_et);
        pwdEt = get(R.id.pwd_et);
        loginBtn = get(R.id.login_btn);
        registerTv = get(R.id.tv_register);
        showPwdSelectIv = get(R.id.show_pwd_select_iv);
        mForgetPassword = get(R.id.tv_froget_password);
        TextWatcherUtil.setEdNoChinaese(pwdEt);

    }

    /**
     * 得到手机号
     */
    public String getPhone() {
        return phoneEt.getText().toString().trim();
    }

    /**
     * 设置手机号
     *
     * @param userAccount 帐号
     */
    public void setPhone(String userAccount) {
        TextUtil.setText(phoneEt, userAccount);
        if (!TextUtil.isEmpty(userAccount)) {
            phoneEt.setSelection(userAccount.length());
        }
    }

    /**
     * 得到密码
     */
    public String getPassword() {
        return pwdEt.getText().toString().trim();
    }

    /**
     * 设置密码隐藏显示
     */
    public void setPwdVisible() {
        if (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == pwdEt.getInputType()) {
            showPwdSelectIv.setImageResource(R.drawable.login_show_pwd_no);
            pwdEt.setInputType((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
        } else {
            showPwdSelectIv.setImageResource(R.drawable.login_show_pwd_yes);
            pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        pwdEt.setSelection(getPassword().length());
    }
}
