package com.hjhq.teamface.login.presenter;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.MD5;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.wrapper.ViewWrapper;
import com.hjhq.teamface.login.BuildConfig;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.LoginDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;


/**
 * 登录
 *
 * @author lx
 * @date 2017/3/22
 */
@RouteNode(path = "/login", desc = "登录")
public class LoginActivity extends ActivityPresenter<LoginDelegate, LoginModel> implements View.OnClickListener {
    Button btnReg;
    Button btnLogin1;
    Button btnLogin2;
    RelativeLayout rlLogin;
    ImageView ivLogo;
    LinearLayout llInputArea;
    FrameLayout flRoot;
    boolean isAnimated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        btnReg = (Button) findViewById(R.id.reg_btn);
        btnLogin1 = (Button) findViewById(R.id.login_btn);
        btnLogin2 = (Button) findViewById(R.id.login_btn2);
        rlLogin = (RelativeLayout) findViewById(R.id.rl_login);
        ivLogo = findViewById(R.id.login_logo);
        llInputArea = findViewById(R.id.ll_input_area);
        flRoot = findViewById(R.id.fl_root);
        //结束所有Activity
        AppManager.getAppManager().finishNotLastActivity();

        reQuestPermission();
        initData();
    }

    protected void initData() {
        String userAccount = SPHelper.getUserAccount();
        viewDelegate.setPhone(userAccount);
    }


    /**
     * 请求权限
     */
    private void reQuestPermission() {
        getRxPermissions().request(
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(aBoolean -> {
            if (aBoolean) {
                initDir();
            } else {
                ToastUtils.showError(mContext, "必须获得必要的权限才能运行！");
                finish();
            }
        });
    }

    /**
     * 初始化应用目录
     */
    private void initDir() {
        JYFileHelper.creatDir(this, Constants.PATH_ROOT);
        JYFileHelper.creatDir(this, Constants.PATH_AUDIO);
        JYFileHelper.creatDir(this, Constants.PATH_CACHE);
        JYFileHelper.creatDir(this, Constants.PATH_DOWNLOAD);
        JYFileHelper.creatDir(this, Constants.PATH_IMAGE);
        JYFileHelper.creatDir(this, Constants.LOGER_CACHE);
        JYFileHelper.creatDir(this, Constants.PATH_RECEIVE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String hint = getIntent().getStringExtra(Constants.DATA_TAG1);
        if (!TextUtil.isEmpty(hint)) {
            Observable.just(0).delay(300, TimeUnit.MILLISECONDS)
                    .compose(bindToLifecycle())
                    .compose(TransformerHelper.applySchedulers())
                    .subscribe(i ->
                            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_failed), hint, viewDelegate.getRootView()));
        }
        btnReg.post(new Runnable() {
            @Override
            public void run() {
                if (!isAnimated) {
                    isAnimated = true;
                    llInputArea.setVisibility(View.VISIBLE);
                    //隐藏注册按钮
                    btnReg.setVisibility(View.GONE);
                    int[] l1 = new int[2];
                    btnLogin2.getLocationOnScreen(l1);
                    int[] l2 = new int[2];
                    btnLogin1.getLocationOnScreen(l2);
                    final float y0 = l2[1] - l1[1];
                    //移动登录按钮
                    final int x0 = l2[0] + btnReg.getWidth() / 2 - l1[0] - btnLogin2.getWidth() / 2;

                    ViewWrapper viewWrapper1 = new ViewWrapper(btnLogin1);
                    AnimatorSet set = new AnimatorSet();
                    //登录按钮中心距离屏幕中心的距离
                    btnLogin1.setPivotX(btnLogin1.getWidth() / 2);
                    final ObjectAnimator anim1 = ObjectAnimator.ofInt(viewWrapper1, "width", btnLogin1.getWidth(), btnLogin2.getWidth());
                    final ObjectAnimator anim2 = ObjectAnimator.ofFloat(btnLogin1, "translationX", 0f, (btnLogin2.getRight() - btnLogin1.getRight()) / DeviceUtils.getDensity(mContext));

                    // final ObjectAnimator anim2 = ObjectAnimator.ofFloat(btnLogin1, "translationX", 0f, x0);
                    // final ObjectAnimator anim3 = ObjectAnimator.ofFloat(btnLogin1, "translationY", 0f, -DeviceUtils.dpToPixel(mContext, 227));
                    final ObjectAnimator anim3 = ObjectAnimator.ofFloat(btnLogin1, "translationY", 0f, -y0);
                    final ObjectAnimator anim4 = ObjectAnimator.ofFloat(llInputArea, "alpha", 0f, 0.2f, 0.5f, 1f);
                    //输入区域
                    final float x = ScreenUtils.getScreenWidth(mContext) / 2 - DeviceUtils.dpToPixel(mContext, 100);
                    final float y = ScreenUtils.getScreenHeight(mContext) / 2 - DeviceUtils.dpToPixel(mContext, 100);
                    PropertyValuesHolder animatorScaleX1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
                    PropertyValuesHolder animatorScaleY1 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
                    PropertyValuesHolder animatorTranslationX1 = PropertyValuesHolder.ofFloat("translationX", 0f, -x);
                    PropertyValuesHolder animatorTranslationY1 = PropertyValuesHolder.ofFloat("translationY", 0f, -y);
                    ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(ivLogo, animatorScaleX1, animatorScaleY1, animatorTranslationX1, animatorTranslationY1);
                    animator2.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnLogin2.setVisibility(View.VISIBLE);
                            btnLogin1.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    set.playTogether(anim1, anim2, anim3, anim4, animator2);
                    set.setDuration((long) (300 * DeviceUtils.getDensity(mContext))).start();
                    rlLogin.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.showPwdSelectIv.setOnClickListener(this);
        viewDelegate.loginBtn.setOnClickListener(this);
        viewDelegate.mForgetPassword.setOnClickListener(this);
        viewDelegate.registerTv.setOnClickListener(this);

        viewDelegate.get(R.id.login_logo).setOnLongClickListener(v -> {
            switchUrl();
            return false;
        });

        btnLogin2.setOnClickListener(v -> {
            login();
        });
    }


    @Override
    protected void translucentstatus() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_froget_password) {
            frogetPwd();
        } else if (id == R.id.show_pwd_select_iv) {
            viewDelegate.setPwdVisible();
        } else if (id == R.id.login_btn) {
            login();
        } else if (id == R.id.tv_register) {
            register();
        }
    }

    /**
     * 注册
     */
    private void register() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, Constants.VERIFY_CODE_REG);
        bundle.putBoolean(Constants.DATA_TAG2, true);
        CommonUtil.startActivtiy(this, InputPhoneActivity.class, bundle);
    }

    /**
     * 忘记密码
     */
    private void frogetPwd() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, Constants.VERIFY_CODE_FORGET);
        CommonUtil.startActivtiy(this, InputPhoneActivity.class, bundle);
    }

    /**
     * 登录
     */
    private void login() {
        String phone = viewDelegate.getPhone();
        String pwd = viewDelegate.getPassword();
        if (TextUtil.isEmpty(phone)) {
            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_failed), getString(R.string.login_please_input_phone), viewDelegate.getRootView());
            return;
        }
        if (TextUtil.isEmpty(pwd)) {
            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_failed), getString(R.string.login_please_input_password), viewDelegate.getRootView());
            return;
        }
        if (!FormValidationUtils.isMobile(phone)) {
            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_failed), getString(R.string.login_please_enter_correct_phone), viewDelegate.getRootView());
            return;
        }
        if (!FormValidationUtils.isPassword(pwd)) {
            DialogUtils.getInstance().sureDialog(this, getString(R.string.login_failed), getString(R.string.login_please_enter_correct_password), viewDelegate.getRootView());
            return;
        }
        model.login(this, phone, MD5.encodePasswordOrigin(pwd));
    }


    private void switchUrl() {
        if (!Constants.IS_DEBUG && !BuildConfig.DEBUG) {
            return;
        }

        List<String> list = new ArrayList<>();
        list.add("http://192.168.1.180:8080/custom-gateway/");
        list.add("http://192.168.1.183:8081/custom-gateway/");
        list.add("https://file.teamface.cn/custom-gateway/");
        //莫凡
        list.add("http://192.168.1.58:8281/custom-gateway/");
        //罗军
        list.add("http://192.168.1.60:8083/custom-gateway/");
        //建华
        list.add("http://192.168.1.202:8080/custom-gateway/");
        //徐冰
        list.add("http://192.168.1.57:8080/custom-gateway/");

        final String[] menu = new String[]{"开发180", "测试183", "外网", "莫帆58",  "罗军60", "建华202",  "徐冰"};
        final int[] resId = new int[menu.length];
        for (int i = 0; i < menu.length; i++) {
            resId[i] = R.drawable.icon_default;
        }

        DialogUtils.getInstance().bottomDialog(LoginActivity.this, viewDelegate.getRootView(), "选择环境", menu, resId, (parent, view, position, id) -> {
            Constants.BASE_URL = list.get(position);
        });
    }

    @Override
    public void onBackPressed() {
        if (SoftKeyboardUtils.isShown(viewDelegate.pwdEt)) {
            SoftKeyboardUtils.hide(viewDelegate.pwdEt);
        } else {
            UIRouter.getInstance().openUri(mContext, "DDComp://app/splash", null);
            finish();
        }
    }
}
