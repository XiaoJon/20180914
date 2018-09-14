package com.hjhq.teamface.login.presenter;

import android.os.Bundle;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.InputVerifyDelegate;
import com.hjhq.teamface.login.view.verify.SecurityCodeView;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;


/**
 * 输入验证码
 *
 * @author lx
 * @date 2017/3/22
 */
@RouteNode(path = "/verify", desc = "输入验证码")
public class InputVerifyActivity extends ActivityPresenter<InputVerifyDelegate, LoginModel> implements View.OnClickListener {
    private String telephone;
    private int verifyType;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            telephone = getIntent().getStringExtra(Constants.DATA_TAG1);
            verifyType = getIntent().getIntExtra(Constants.DATA_TAG2, Constants.VERIFY_CODE_FORGET);
        } else {
            telephone = savedInstanceState.getString(Constants.DATA_TAG1);
            verifyType = savedInstanceState.getInt(Constants.DATA_TAG1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DATA_TAG1, telephone);
        outState.putInt(Constants.DATA_TAG2, verifyType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void init() {
        getVerifyCode();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mNextStepBtn.setOnClickListener(this);
        viewDelegate.verifyCodeBtn.setOnClickListener(this);
        viewDelegate.mVerifyCodeEt.setInputCompleteListener(new SecurityCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                viewDelegate.mNextStepBtn.setEnabled(true);
            }

            @Override
            public void deleteContent(boolean isDelete) {
                viewDelegate.mNextStepBtn.setEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.next_step_btn) {
            //下一步
            nextStep();
        } else if (id == R.id.timmer_btn) {
            getVerifyCode();
        }
    }

    /**
     * 下一步
     */
    private void nextStep() {
        String code = viewDelegate.getVerifyCode();
        if (!FormValidationUtils.isVerificationCode(code)) {
            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_please_enter_correct_verify_code), null, viewDelegate.getRootView());
            return;
        }

        //修改手机号
        if (Constants.VERIFY_CODE_PHONE == verifyType) {
            model.editPhone(this, telephone, code, new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    //保存账号
                    SPHelper.setUserAccount(telephone);
                    ToastUtils.showSuccess(mContext, "修改成功！");
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/setting", null);
                }
            });
            return;
        }

        model.verifySmsCode(this, telephone,
                verifyType, code, new ProgressSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG1, telephone);
                        if (Constants.VERIFY_CODE_FORGET == verifyType) {
                            CommonUtil.startActivtiy(InputVerifyActivity.this, ForgetPwdActivity.class, bundle);
                        } else if (Constants.VERIFY_CODE_REG == verifyType) {
                            CommonUtil.startActivtiy(InputVerifyActivity.this, RegisterActivity.class, bundle);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissWindowView();
                        DialogUtils.getInstance().sureDialog(mContext, e.getMessage(), null, viewDelegate.getRootView());
                    }
                });
    }


    /**
     * 获取验证码
     */
    public void getVerifyCode() {
        //是否可用 倒计时是否在使用 是否重复点击
        if (viewDelegate.getEnable()) {
            return;
        }
        viewDelegate.sendVerify();

        //验证码类型：0通用1注册2修改密码
        model.sendSmsCode(this, telephone, verifyType, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                viewDelegate.verifyCodeFailed();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                viewDelegate.verifyCodeSuccess();
            }
        });
    }

    @Override
    protected void onDestroy() {
        viewDelegate.removeCountDown();
        super.onDestroy();
    }
}
