package com.hjhq.teamface.oa.main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PasswrodSetBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.MD5;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.componentservice.login.LoginService;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.router.facade.annotation.RouteNode;

import butterknife.Bind;

/**
 * 修改密码
 *
 * @author lx
 * @date 2017/6/15
 */

@RouteNode(path = "/changePwd", desc = "修改密码")
public class ChangePasswordActivity extends BaseTitleActivity {
    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.et_affirm_password)
    EditText etAffirmPassword;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    private LoginService service;
    /**
     * 密码策略
     */
    private int pwdComplex = -1;
    /**
     * 密码长度
     */
    private int pwdLength = FormValidationUtils.MIN_PWD_LENGTH;
    /**
     * 是否修改密码后直接登录
     */
    private boolean isLogin;

    @Override
    protected int getChildView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void setListener() {
        setOnClicks(btnCommit);
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(R.string.change_password);
        isLogin = getIntent().getBooleanExtra(Constants.DATA_TAG1, false);
        TextWatcherUtil.setEdNoChinaese(etOldPassword);
        TextWatcherUtil.setEdNoChinaese(etNewPassword);
        TextWatcherUtil.setEdNoChinaese(etAffirmPassword);
    }

    @Override
    protected void initData() {
        service = (LoginService) Router.getInstance().getService(LoginService.class.getSimpleName());
        String userAccount = SPHelper.getUserAccount();
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
    public void onClick(View view) {
        changePassword();
    }

    /**
     * 修改密码
     */
    private void changePassword() {
        if (!verification()) {
            return;
        }
        String oldPwd = etOldPassword.getText().toString();
        String newPwd = etNewPassword.getText().toString();

        service.editPassWord(this, MD5.encodePasswordOrigin(oldPwd), MD5.encodePasswordOrigin(newPwd), new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "修改密码成功");
                SPHelper.setUserPassword(MD5.encodePasswordOrigin(newPwd));
                if (isLogin) {
                    service.login(ChangePasswordActivity.this, SPHelper.getUserAccount(), MD5.encodePasswordOrigin(newPwd), true);
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 校验
     */
    private boolean verification() {
        if (pwdComplex < 0) {
            ToastUtils.showError(this, "未获取到密码安全策略，请重新获取");
            return false;
        }
        String affirmPwd = etAffirmPassword.getText().toString();
        String oldPwd = etOldPassword.getText().toString();
        String newPwd = etNewPassword.getText().toString();

        if (TextUtil.isEmpty(oldPwd)) {
            ToastUtils.showError(this, "原密码不能为空");
            return false;
        }
        if (TextUtil.isEmpty(newPwd)) {
            ToastUtils.showError(this, "新密码不能为空");
            return false;
        }
        if (TextUtil.isEmpty(affirmPwd)) {
            ToastUtils.showError(this, "确认密码不能为空");
            return false;
        }
        if (!newPwd.equals(affirmPwd)) {
            ToastUtils.showError(this, "新密码和确认密码输入不一致");
            return false;
        }

        if (newPwd.length() < pwdLength) {
            ToastUtils.showError(this, "不符合密码最小长度" + pwdLength + "位");
            return false;
        }

        switch (pwdComplex) {
            case 0: {
                boolean validation = FormValidationUtils.isPassword(newPwd);
                if (!validation) {
                    ToastUtils.showError(this, "新密码只能由数字、字母或特殊字符组成");
                    return false;
                }
                break;
            }
            case 1: {
                boolean upperCase = (FormValidationUtils.isUpperCase(newPwd) || FormValidationUtils.isLowerCase(newPwd)) && FormValidationUtils.isDight(newPwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9]+$", newPwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含字母和数字字符");
                    return false;
                }
                break;
            }
            case 2: {
                boolean upperCase = (FormValidationUtils.isUpperCase(newPwd) || FormValidationUtils.isLowerCase(newPwd))
                        && FormValidationUtils.isDight(newPwd) && FormValidationUtils.isSpecialChar(newPwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$", newPwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含字母、数字和特殊字符");
                    return false;
                }
                break;
            }
            case 3: {
                boolean upperCase = FormValidationUtils.isUpperCase(newPwd) && FormValidationUtils.isLowerCase(newPwd)
                        && FormValidationUtils.isDight(newPwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9]+$", newPwd);
                if (!upperCase || !validation) {
                    ToastUtils.showError(this, "需包含数字和大小写字母");
                    return false;
                }
                break;
            }
            case 4: {
                boolean upperCase = FormValidationUtils.isUpperCase(newPwd) && FormValidationUtils.isLowerCase(newPwd)
                        && FormValidationUtils.isDight(newPwd) && FormValidationUtils.isSpecialChar(newPwd);
                boolean validation = FormValidationUtils.validation("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$", newPwd);
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
}
