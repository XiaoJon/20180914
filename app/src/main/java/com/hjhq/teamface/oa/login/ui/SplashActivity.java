package com.hjhq.teamface.oa.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.activity.ViewUserProtocolActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.componentservice.login.LoginService;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.im.IMService;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * App启动界面
 *
 * @author Administrator
 */
@RouteNode(path = "/splash", desc = "欢迎界面")
public class SplashActivity extends ActivityPresenter<SplashDelegate, CommonModel> {
    private TextView tvCopyRight;
    private Button btnReg;
    private Button btnLogin;
    private ImageView ivLogo;
    private RelativeLayout rlRoot;
    //能否点击返回键
    private boolean canBack = false;
    private int logoutType = -1;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            logoutType = bundle.getInt(Constants.DATA_TAG1, -1);
        }
        overridePendingTransition(0, 0);
        btnReg = (Button) findViewById(R.id.reg_btn);
        btnLogin = (Button) findViewById(R.id.login_btn);
        ivLogo = (ImageView) findViewById(R.id.login_logo);
        rlRoot = (RelativeLayout) findViewById(R.id.activity_splash);
        ViewCompat.setTransitionName(btnLogin, "login_btn");
        Calendar c = Calendar.getInstance();
        tvCopyRight = findViewById(R.id.copyright);
        TextUtil.setText(tvCopyRight, String.format(getString(R.string.app_copy_right), c.get(Calendar.YEAR) + ""));
        StatusBarUtil.hideFakeStatusBarView(this);
        IMState.setImOnlineState(false);
        //避免快速切换导致闪屏，延迟500毫秒跳转
        playAnim();
        if (logoutType <= 0) {
            try {
                if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                    Intent intent = new Intent(mContext, DownloadService.class);
                    mContext.startService(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            rlRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLogoutDialog();
                }
            }, 500);

        }

    }

    private void showLogoutDialog() {
        String subTitle = "您已离线!";
        switch (logoutType) {
            case MsgConstant.TYPE_NEED_RELOGIN:
                subTitle = " 请重新登录!";
                break;
            case MsgConstant.TYPE_FORCE_OFFLINE_BY_SYSTEM:
                subTitle = "当前会话被后台退出，请重新登录!";
                break;
            case MsgConstant.TYPE_FORCE_OFFLINE_BY_PC:
                subTitle = "当前会话被PC端退出，请重新登录!";
                break;
            case MsgConstant.TYPE_LOGIN_ON_OTHER_DEVICE:
                subTitle = "当前账号已在其他手机登录，如不是本人操作，请修改密码!";
                break;
            case MsgConstant.TYPE_NEED_CHANGE_PASSWORD:
                subTitle = "请修改密码!";
                break;
            case MsgConstant.TYPE_IM_ACCOUNT_ERROR:
                subTitle = "账户异常,请尝试重新登录!";
                break;
        }
        DialogUtils.getInstance().sureOrCancel(mContext, "提示", subTitle, viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {
                if (MsgConstant.TYPE_NEED_CHANGE_PASSWORD == logoutType) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.DATA_TAG1, Constants.VERIFY_CODE_FORGET);
                    UIRouter.getInstance().openUri(mContext, "DDComp://login/input_phone", bundle);
                }
            }
        });


    }

    private void playAnim() {
        Observable.just(0).delay(350, TimeUnit.MILLISECONDS).subscribe(i -> {
            if (SPHelper.isLoginBefore()) {
                login();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLogin.setVisibility(View.VISIBLE);
                        btnReg.setVisibility(View.VISIBLE);
                        PropertyValuesHolder animatorScaleX1 = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
                        PropertyValuesHolder animatorScaleY1 = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
                        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(btnLogin, animatorScaleX1, animatorScaleY1);
                        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(btnReg, animatorScaleX1, animatorScaleY1);
                        animator1.setDuration(500);
                        animator2.setDuration(500);
                        animator1.start();
                        animator2.start();
                        canBack = true;
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        overridePendingTransition(0, 0);
        super.finish();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        btnReg.setOnLongClickListener(v -> {
            AppManager.restartApp();
            return true;
        });
        // 这里指定了共享的视图元素
        btnLogin.setOnClickListener(v -> {
            // getWindow().setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.login_start_bg));
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(mContext, btnLogin, "login_btn");
            UIRouter.getInstance().openUri(mContext, "DDComp://login/login", options.toBundle());
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        });
        btnReg.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            //mBitmap = ImageLoader.getBitmapFromView(rlRoot);
            bundle.putInt(Constants.DATA_TAG1, Constants.VERIFY_CODE_REG);
            bundle.putBoolean(Constants.DATA_TAG2, false);
            UIRouter.getInstance().openUri(mContext, "DDComp://login/input_phone", bundle);

            finish();
            overridePendingTransition(R.anim.in_from_with_alpha, R.anim.out_from_top_left);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (btnLogin.getVisibility() != View.VISIBLE) {
            btnLogin.clearAnimation();
            btnReg.clearAnimation();
        }
        if (btnReg != null && !SPHelper.isLoginBefore()) {
            btnReg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnLogin.setVisibility(View.VISIBLE);
                    btnReg.setVisibility(View.VISIBLE);
                }
            }, 500);
        }

    }

    /**
     * 登录
     */
    private void login() {
        String account = SPHelper.getUserAccount();
        String pwd = SPHelper.getUserPassword();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            LoginService service = (LoginService) Router.getInstance().getService(LoginService.class.getSimpleName());
            service.login(this, account, pwd, false);
        } else {
            UIRouter.getInstance().openUri(mContext, "DDComp://login/login", null);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (canBack) {
            finish();
        } else {
            return;
        }
    }
}
