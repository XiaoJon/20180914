package com.hjhq.teamface.common.ui.location;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;


/**
 * 查看地址
 *
 * @author lx
 * @date 2017/7/10
 */

public class ViewAddressPresenter extends ActivityPresenter<ViewAddressDelegate, CommonModel> implements LocationSource, AMapLocationListener {
    private String lng;
    private String lat;
    private AMap aMap;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    /**
     * 是否定位
     */
    private boolean isLocation;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            lng = getIntent().getStringExtra(Constants.DATA_TAG1);
            lat = getIntent().getStringExtra(Constants.DATA_TAG2);
            isLocation = getIntent().getBooleanExtra(Constants.DATA_TAG3, false);
        }
    }

    @Override
    public void init() {
        setMap();
        try {
            if (isLocation) {
                viewDelegate.ivLocation.setVisibility(View.VISIBLE);
                viewDelegate.ivLocation.setOnClickListener(v -> onceLelocation());
            } else {
                addMarker();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showError(this, "位置信息错误！");
            finish();
        }
    }


    /**
     * 单次定位
     */
    private void onceLelocation() {
        aMap.clear();
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    /**
     * 设置地图
     */
    private void setMap() {
        if (aMap == null) {
            aMap = viewDelegate.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        if (isLocation) {
            aMap.setLocationSource(this);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

            viewDelegate.setLocationStyle();
        }
    }

    /**
     * 添加标签
     */
    private void addMarker() {
        double longitude = Double.parseDouble(lng);
        double latitude = Double.parseDouble(lat);
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latLng.latitude, latLng.longitude), 16, 30, 30));
        aMap.animateCamera(cameraUpdate);

        Marker locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed))).position(latLng));
        locationMarker.showInfoWindow();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //默认高精度定位模式
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        // Log.e("deactivate", "结束定位");
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation.getErrorCode() == 0) {
            Log.e("success", aMapLocation.toString());
            // 显示系统小蓝点
            mListener.onLocationChanged(aMapLocation);
            if (isLocation) {
                Observable.just(0).delay(1, TimeUnit.SECONDS).subscribe(d -> addMarker());
            }
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }
}
