package com.hjhq.teamface.login.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.activity.ViewUserProtocolActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.wrapper.ViewWrapper;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.InputPhoneDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;


/**
 * 注册
 *
 * @author lx
 * @date 2017/3/22
 */
@RouteNode(path = "/input_phone", desc = "输入号码")
public class InputPhoneActivity extends ActivityPresenter<InputPhoneDelegate, LoginModel> implements View.OnClickListener {

    private int verifyCode;
    private boolean backToLogin = false;
    private boolean isRegist = false;
    private Bitmap mBitmap;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            verifyCode = getIntent().getIntExtra(Constants.DATA_TAG1, Constants.VERIFY_CODE_FORGET);
            backToLogin = getIntent().getBooleanExtra(Constants.DATA_TAG2, false);

        }
    }

    @Override
    public void init() {

        if (verifyCode != Constants.VERIFY_CODE_REG) {
            viewDelegate.hideAgreement();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBitmap == null) {
            viewDelegate.get(R.id.rl_action).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.iv_anim).setVisibility(View.GONE);
        } else {
            playAnim();
        }
    }

    /**
     * 播放动画
     */
    private void playAnim() {
        //viewDelegate.ivAnim.setImageBitmap(mBitmap);
        viewDelegate.ivAnim.setImageResource(R.drawable.login_start_bg);
        viewDelegate.ivAnim.setVisibility(View.VISIBLE);
        ViewWrapper v = new ViewWrapper(viewDelegate.ivAnim);
        PropertyValuesHolder animatorScaleX1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder animatorScaleY1 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        PropertyValuesHolder animatorTranslationX1 = PropertyValuesHolder.ofFloat("translationX", 0f, -ScreenUtils.getScreenWidth(mContext));
        PropertyValuesHolder animatorTranslationY1 = PropertyValuesHolder.ofFloat("translationY", 0f, -ScreenUtils.getScreenHeight(mContext));
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(v, animatorScaleX1, animatorScaleY1, animatorTranslationX1, animatorTranslationY1);
        final ObjectAnimator alpha = ObjectAnimator.ofInt(viewDelegate.rlRoot, "alpha", 100, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(10000);
        set.playTogether(animator2, alpha);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        viewDelegate.mNextStepBtn.setOnClickListener(this);
        viewDelegate.mTvServiceAgreement.setOnClickListener(this);
        viewDelegate.mPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean mobile = FormValidationUtils.isMobile(s.toString());
                viewDelegate.mNextStepBtn.setEnabled(mobile);
            }
        });
    }

    @Override
    public void onClick(View view) {
        String telephone = viewDelegate.getPhone();
        int id = view.getId();
        if (id == R.id.next_step_btn) {
            //下一步
            nextStep(telephone);
        } else if (id == R.id.tv_service_agreement) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, Constants.USER_AGGREMENT_URL);

            CommonUtil.startActivtiy(mContext, ViewUserProtocolActivity.class, bundle);
            UIRouter.getInstance().openUri(mContext, "DDComp://login/view_user_protocol", null);
        }
    }

    /**
     * 下一步
     *
     * @param telephone 手机
     */
    private void nextStep(String telephone) {
        if (!FormValidationUtils.isMobile(telephone)) {
            ToastUtils.showError(this, R.string.login_please_enter_correct_phone);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, telephone);
        bundle.putInt(Constants.DATA_TAG2, verifyCode);
        CommonUtil.startActivtiy(this, InputVerifyActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        if (backToLogin) {
            finish();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, 0);
        UIRouter.getInstance().openUri(mContext, "DDComp://app/splash", bundle);
        finish();
    }
}
