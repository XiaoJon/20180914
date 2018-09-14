package com.hjhq.teamface.login.presenter;

import android.os.Bundle;

import com.hjhq.teamface.basis.bean.PasswrodSetBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.MD5;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.componentservice.login.LoginService;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.ForgetPwdDelegate;
import com.luojilab.component.componentlib.router.Router;


/**
 * 忘记密码
 *
 * @author lx
 * @date 2017/3/22
 */

public class ForgetPwdActivity extends ActivityPresenter<ForgetPwdDelegate, LoginModel> {
    private String userAccount;
    /**
     * 密码策略
     */
    private int pwdComplex = -1;
    /**
     * 密码长度
     */
    private int pwdLength = FormValidationUtils.MIN_PWD_LENGTH;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            userAccount = getIntent().getStringExtra(Constants.DATA_TAG1);
        }
    }

    @Override
    public void init() {
        LoginService service = (LoginService) Router.getInstance().getService(LoginService.class.getSimpleName());
        service.getCompanySet(this, userAccount, new ProgressSubscriber<PasswrodSetBean>(this) {
            @Override
            public void onNext(PasswrodSetBean baseBean) {
                super.onNext(baseBean);
                PasswrodSetBean.DataBean pwdSet = baseBean.getData();
                if (pwdSet != null) {
                    pwdComplex = pwdSet.getPwd_complex();
                    pwdLength = pwdSet.getPwd_length();
                }
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.okBtn.setOnClickListener(view -> resetPwd());
    }

    /**
     * 格式校验
     */
    private boolean formValidation() {
        if (pwdComplex < 0) {
            ToastUtils.showError(this, "未获取到密码安全策略，请重新获取");
            return false;
        }
        String repeatPwd = viewDelegate.getRepeatPwd();
        String pwd = viewDelegate.getPassword();

        if (TextUtil.isEmpty(pwd)) {
            ToastUtils.showError(this, "新密码不能为空");
            return false;
        }
        if (TextUtil.isEmpty(repeatPwd)) {
            ToastUtils.showError(this, "重复密码不能为空");
            return false;
        }
        if (!pwd.equals(repeatPwd)) {
            ToastUtils.showError(this, "新密码和确认密码输入不一致");
            return false;
        }

        if (pwd.length() < pwdLength) {
            ToastUtils.showError(this, "不符合密码最小长度" + pwdLength + "位");
            return false;
        }

        switch (pwdComplex) {
            case 0: {
                boolean validation = FormValidationUtils.isPassword(pwd);
                if (!validation) {
                    ToastUtils.showError(this, "新密码只能由数字、字母或特殊字符组成");
                    return false;
                }
                break;
            }
            case 1: {
                boolean upperCase = (FormValidationUtils.isUpperCase(pwd) || FormValidationUtils.isLowerCase(pwd))
                        && FormValidationUtils.isDight(pwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9]+$", pwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含字母和数字字符");
                    return false;
                }
                break;
            }
            case 2: {
                boolean upperCase = (FormValidationUtils.isUpperCase(pwd) || FormValidationUtils.isLowerCase(pwd))
                        && FormValidationUtils.isDight(pwd) && FormValidationUtils.isSpecialChar(pwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$", pwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含字母、数字和特殊字符");
                    return false;
                }
                break;
            }
            case 3: {
                boolean upperCase = FormValidationUtils.isUpperCase(pwd) && FormValidationUtils.isLowerCase(pwd)
                        && FormValidationUtils.isDight(pwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9]+$", pwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含数字和大小写字母");
                    return false;
                }
                break;
            }
            case 4: {
                boolean upperCase = FormValidationUtils.isUpperCase(pwd) && FormValidationUtils.isLowerCase(pwd)
                        && FormValidationUtils.isDight(pwd) && FormValidationUtils.isSpecialChar(pwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$", pwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含数字、大小写字母和特殊字符");
                    return false;
                }
                break;
            }
            default:
                break;
        }
        return true;
    }


    /**
     * 重新设置密码
     */
    private void resetPwd() {
        if (!formValidation()) {
            return;
        }

        //重置密码
        model.resetPwd(this, userAccount, MD5.encodePasswordOrigin(viewDelegate.getPassword()));
    }

}
