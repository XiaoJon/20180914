package com.hjhq.teamface.login.presenter;

import android.os.Bundle;
import android.view.View;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.MD5;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.RegisterDelegate;
import com.luojilab.router.facade.annotation.RouteNode;


/**
 * 注册
 *
 * @author lx
 * @date 2017/3/22
 */
@RouteNode(path = "/register", desc = "注册")
public class RegisterActivity extends ActivityPresenter<RegisterDelegate, LoginModel> implements View.OnClickListener {
    private String phone;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            phone = getIntent().getStringExtra(Constants.DATA_TAG1);
        }
    }

    @Override
    public void init() {
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.registerBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        register();
    }

    /**
     * 注册
     */
    private void register() {
        String companyName = viewDelegate.getCompanyName();
        String name = viewDelegate.getName();
        String pwd = viewDelegate.getPwd();
        String inviteCode = viewDelegate.getInviteCode();
        if (TextUtil.isEmpty(companyName)) {
            ToastUtils.showError(this, "公司名称不能为空");
            return;
        }
        if (TextUtil.isEmpty(name)) {
            ToastUtils.showError(this, "姓名不能为空");
            return;
        }
        if (TextUtil.isEmpty(pwd)) {
            ToastUtils.showError(this, "密码不能为空");
            return;
        }
        if (!FormValidationUtils.isPassword(pwd)) {
            ToastUtils.showError(this, "密码格式错误");
            return;
        }
        model.register(this, companyName, name, MD5.encodePasswordOrigin(pwd), inviteCode, phone);

    }

    @Override
    public void onBackPressed() {
        EventBusUtils.sendEvent(new MessageBean(HttpResultFunc.LOGOUT, null, null));
    }
}
