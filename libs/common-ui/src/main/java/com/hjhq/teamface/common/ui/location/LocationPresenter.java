package com.hjhq.teamface.common.ui.location;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.utils.MapToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 地图定位
 *
 * @author lx
 * @date 2017/7/10
 */

public class LocationPresenter extends ActivityPresenter<LocationDelegate, CommonModel> implements AMap.OnMapClickListener, LocationSource, AMapLocationListener, PoiSearch.OnPoiSearchListener {
    public int radius = 5000;
    List<PoiItem> nearList = new ArrayList<>();
    private String address;
    private String city;
    public static String LOCATION_RESULT_CODE = "location_result_code";
    private AMap aMap;
    private UiSettings mUiSettings;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    /**
     * 是否首次定位
     */
    private boolean isFrist = true;
    private Marker locationMarker;
    /**
     * 是否关键字搜索
     */
    private boolean isKeySearch = false;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private int currentPage = 0;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            address = getIntent().getStringExtra(AppConst.LOCATION);
        }
    }

    @Override
    public void init() {
        setMap();

        if (TextUtils.isEmpty(address)) {
            onceLelocation();
        } else {
            keySearch(address);
        }
    }

    /**
     * 设置地图
     */
    private void setMap() {
        if (aMap == null) {
            aMap = viewDelegate.getMap();
            if (aMap != null) {
                mUiSettings = aMap.getUiSettings();
                aMap.setOnMapClickListener(this);
                aMap.setLocationSource(this);
                mUiSettings.setZoomControlsEnabled(false);
                mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
                aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

                viewDelegate.setMapViewUI();
                viewDelegate.setLocationStyle();
            }
        }

    }


    /**
     * 单次定位
     */
    private void onceLelocation() {
        if (mLocationOption == null || mlocationClient == null) {
            ToastUtils.showError(mContext, "地图定位异常！");
            return;
        }
        aMap.clear();

        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable e) {
                viewDelegate.afterTextChanged();
            }
        });
        viewDelegate.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                SoftKeyboardUtils.hide(viewDelegate.etSearch);
                String content = viewDelegate.etSearch.getText().toString();
                keySearch(content);
                return true;
            }
            return false;
        });
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                viewDelegate.setSelectPosition(position);

                PoiItem item = (PoiItem) adapter.getItem(position);
                LatLonPoint latLonPoint = item.getLatLonPoint();
                addMarker(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
            }
        });
        viewDelegate.ivLocation.setOnClickListener(v -> onceLelocation());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nearList != null && nearList.size() != 0) {
            PoiItem poiInfo = viewDelegate.getPoiInfo();
            if (poiInfo != null) {
                Intent intent = new Intent();
                intent.putExtra(LOCATION_RESULT_CODE, poiInfo);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showError(this, "请选择好位置!");
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng);
        aroundSearch(latLng.latitude, latLng.longitude);
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
            //获取最近3s内精度最高的一次定位结果：
            mLocationOption.setOnceLocationLatest(true);
            mlocationClient.setLocationOption(mLocationOption);
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
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            city = aMapLocation.getCity();
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            //移动位置
            CameraPosition cameraPosition = aMap.getCameraPosition();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(latLng.latitude, latLng.longitude), isFrist ? 16 : cameraPosition.zoom, cameraPosition.tilt, cameraPosition.bearing));
            aMap.animateCamera(cameraUpdate);

            aroundSearch(latitude, longitude);
            isFrist = false;
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }

    /**
     * 添加标签
     *
     * @param latLng 坐标
     */
    private void addMarker(LatLng latLng) {
        CameraPosition cameraPosition = aMap.getCameraPosition();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latLng.latitude, latLng.longitude), cameraPosition.zoom, cameraPosition.tilt, cameraPosition.bearing));
        aMap.animateCamera(cameraUpdate);

        if (locationMarker != null) {
            locationMarker.remove();
        }

        locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed))).position(latLng));
        locationMarker.showInfoWindow();

    }


    /**
     * 附近poi
     *
     * @param latitude
     * @param longitude
     */
    private void aroundSearch(double latitude, double longitude) {
        isKeySearch = false;
        query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,
                longitude), radius, true));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    /**
     * 开始进行poi搜索
     */
    protected void keySearch(String keyWord) {
        if (TextUtil.isEmpty(keyWord)) {
            ToastUtils.showError(this, "搜索内容为空");
            return;
        }
        isKeySearch = true;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    ArrayList<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        nearList.clear();
                        nearList.addAll(poiItems);
                        viewDelegate.setNewData(nearList);

                        if (isKeySearch) {
                            //关键字搜索需要移动中心位置
                            PoiItem poiItem = nearList.get(0);
                            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                            addMarker(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        MapToastUtil.show(this,
                                R.string.no_result);
                    } else {
                        MapToastUtil.show(this,
                                R.string.no_result);
                    }
                    return;
                }
            } else {
                MapToastUtil
                        .show(this, R.string.no_result);
            }
        } else {
            MapToastUtil.showerror(this.getApplicationContext(), rcode);
        }
        nearList.clear();
        viewDelegate.setNewData(nearList);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
