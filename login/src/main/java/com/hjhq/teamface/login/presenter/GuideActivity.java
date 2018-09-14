package com.hjhq.teamface.login.presenter;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.model.LoginModel;
import com.hjhq.teamface.login.ui.GuideDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导
 * Created by lx on 2017/3/22.
 */

public class GuideActivity extends ActivityPresenter<GuideDelegate, LoginModel>
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    String BASE_URL1 = "http://192.168.1.188:8081/custom-gateway/";//开发
    String BASE_URL2 = "https://121.201.107.6:81/custom-gateway/";//外网
    String BASE_URL3 = "http://192.168.1.58:8281/custom-gateway/";//莫凡
    String BASE_URL4 = "http://192.168.1.9:8080/custom-gateway/";//开发
    String BASE_URL5 = "http://192.168.1.172:8080/custom-gateway/";//张工
    String BASE_URL6 = "http://192.168.1.202:8080/custom-gateway/";//建华
    String BASE_URL7 = "http://192.168.1.48:8080/custom-gateway/";//邓顺利
    String BASE_URL8 = "https://file.teamface.cn/custom-gateway/";
    List<String> list = new ArrayList<>();


    @Override
    public void init() {
        initData();
        list.add(BASE_URL1);
        list.add(BASE_URL2);
        list.add(BASE_URL3);
        list.add(BASE_URL4);
        list.add(BASE_URL5);
        list.add(BASE_URL6);
        list.add(BASE_URL7);
        list.add(BASE_URL8);
    }


    protected void initData() {
        initDir();
        reQuestPermission();
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mViewPager.addOnPageChangeListener(this);
        viewDelegate.registerBtn.setOnClickListener(this);
        viewDelegate.loginBtn.setOnClickListener(this);
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

    /**
     * 请求权限
     */
    private void reQuestPermission() {
        getRxPermissions().request(
                Manifest.permission.CAMERA
//                , Manifest.permission.READ_PHONE_STATE
//                , Manifest.permission.READ_CONTACTS
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.RECORD_AUDIO
//                , Manifest.permission.CALL_PHONE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(aBoolean -> {
            if (aBoolean) {

            } else {
                ToastUtils.showToast(mContext, "必须获得必要的权限才能运行！");
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.register_btn){
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, Constants.VERIFY_CODE_REG);
            CommonUtil.startActivtiy(this, InputPhoneActivity.class, bundle);
        }else if (id == R.id.login_btn){
            CommonUtil.startActivtiy(this, LoginActivity.class);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        viewDelegate.monitorPoint(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishAllActivity();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
