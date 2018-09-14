package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.ViewAttendanceNumDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.location.LocationPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/view_attendance_type", desc = "打卡人数")
public class AttendanceTypeActivity extends ActivityPresenter<ViewAttendanceNumDelegate, AttendanceModel> implements View.OnClickListener {
    List<Fragment> mFragmentList = new ArrayList<>(2);
    String[] titleArray = new String[]{"地点考勤", "WiFi考勤"};

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        mFragmentList.add(AttendanceTypeFragment.newInstance(0));
        mFragmentList.add(AttendanceTypeFragment.newInstance(1));
        viewDelegate.initNavigator(titleArray, mFragmentList);
        viewDelegate.setTitle(R.string.attendance_type_title);
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.add));
    }


    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMenu();

        return super.onOptionsItemSelected(item);

    }

    private void showMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "添加考勤方式", titleArray, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (p) {
                    case 0:
                        //添加考勤地址
                        CommonUtil.startActivtiyForResult(mContext, LocationPresenter.class,
                                Constants.REQUEST_CODE_SEND_LOCATION);
                        break;
                    case 1:
                        //添加考勤wifi
                        CommonUtil.startActivtiyForResult(mContext, AddWifiActivity.class, Constants.REQUEST_CODE2);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE_SEND_LOCATION:
                //位置
                if (data != null) {
                    PoiItem poiInfo = data.getParcelableExtra(LocationPresenter.LOCATION_RESULT_CODE);
                    String address = poiInfo.getProvinceName() + poiInfo.getCityName() + poiInfo.getAdName() + poiInfo.getBusinessArea() + poiInfo.getSnippet();
                    double lat = poiInfo.getLatLonPoint().getLatitude();
                    double lon = poiInfo.getLatLonPoint().getLongitude();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DATA_TAG1, address);
                    bundle.putDouble(Constants.DATA_TAG2, lat);
                    bundle.putDouble(Constants.DATA_TAG3, lon);
                    CommonUtil.startActivtiyForResult(mContext, AddLocationActivity.class, Constants.REQUEST_CODE3, bundle);


                }
                break;
            case Constants.REQUEST_CODE2:
                refreshData(1);
                break;
            case Constants.REQUEST_CODE3:
                refreshData(0);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshData(int type) {
        ((AttendanceTypeFragment) mFragmentList.get(type)).refreshData();
    }
}
