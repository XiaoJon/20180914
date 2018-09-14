package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddTypeBean;
import com.hjhq.teamface.attendance.bean.AttendanceLocationBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AddLocationDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
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
@RouteNode(path = "/add_location", desc = "添加地址")
public class AddLocationActivity extends ActivityPresenter<AddLocationDelegate, AttendanceModel> implements View.OnClickListener {
    String id = "";
    String name;
    String address;
    double lat = 0.0d;
    double lng = 0.0d;
    int range = 100;
    String[] rangeMenu;
    int[] rangeValue = new int[]{50, 100, 150, 200, 300, 500, 1000};


    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            range = TextUtil.parseInt(bundle.getString(Constants.DATA_TAG6), 100);
            id = bundle.getString(Constants.DATA_TAG5);
            name = bundle.getString(Constants.DATA_TAG4);
            address = bundle.getString(Constants.DATA_TAG1);
            lat = bundle.getDouble(Constants.DATA_TAG2);
            lng = bundle.getDouble(Constants.DATA_TAG3);
            viewDelegate.setAddress(address);
            viewDelegate.setRangeText(range + "米");
            if (!TextUtils.isEmpty(name)) {
                viewDelegate.setName(name);
            }
        }
        rangeMenu = getResources().getStringArray(R.array.attendance_location_range_menu);

    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

        viewDelegate.get(R.id.rl2).setOnClickListener(v -> {
            //位置
            CommonUtil.startActivtiyForResult(mContext, LocationPresenter.class,
                    Constants.REQUEST_CODE_SEND_LOCATION);
        });
        viewDelegate.get(R.id.set).setOnClickListener(v -> {
            showMenu();
        });
    }

    private void showMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(),
                getResources().getString(R.string.attendance_add_approval_label),
                rangeMenu, new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        range = rangeValue[p];
                        viewDelegate.setRangeText(rangeMenu[p]);
                        return false;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        name = viewDelegate.getName();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(mContext, "名字不能为空");
            return true;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showToast(mContext, "地址不能为空");
            return true;
        }
        if (lat == 0.0d || lng == 0.0d) {
            ToastUtils.showToast(mContext, "经纬度数据错误");
            return true;
        }
        AddTypeBean bean = new AddTypeBean();
        bean.setName(name);
        bean.setAddress(address);
        bean.setAttendanceType(AttendanceConstants.TYPE_LOCATION);
        bean.setEffectiveRange("" + range);
        AttendanceLocationBean locationBean = new AttendanceLocationBean();
        locationBean.setAddress(address);
        locationBean.setLat(lat);
        locationBean.setLng(lng);
        bean.setAttendanceStatus("0");
        bean.setId(id);
        List<AttendanceLocationBean> list = new ArrayList<>();
        list.add(locationBean);
        bean.setLocation(list);
        if (TextUtils.isEmpty(id)) {
            model.addAttendanceType(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    setResult(RESULT_OK);
                    finish();

                }
            });
        } else {
            model.updateAttendanceType(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    setResult(RESULT_OK);
                    finish();

                }
            });
        }

        return super.onOptionsItemSelected(item);
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
                    address = poiInfo.getProvinceName() + poiInfo.getCityName() + poiInfo.getAdName() + poiInfo.getBusinessArea() + poiInfo.getSnippet();
                    lat = poiInfo.getLatLonPoint().getLatitude();
                    lng = poiInfo.getLatLonPoint().getLongitude();
                    viewDelegate.setAddress(address);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
