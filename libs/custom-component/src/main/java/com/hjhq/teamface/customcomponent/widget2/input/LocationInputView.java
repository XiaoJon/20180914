package com.hjhq.teamface.customcomponent.widget2.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.PoiItem;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.location.LocationPresenter;
import com.hjhq.teamface.common.ui.location.ViewAddressPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

import java.util.Map;


/**
 * 详细地址输入
 *
 * @author xj
 * @date 2017/8/23
 */

public class LocationInputView extends InputCommonView implements ActivityPresenter.OnActivityResult, AMapLocationListener {
    private static final String LOCATION = "location";

    //声明mlocationClient对象
    private AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private String lng;
    private String lat;
    private String address;

    public LocationInputView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        if (state == CustomConstants.ADD_STATE && "1".equals(defaultValue)) {
            location();
        }
        //可编辑时 显示定位图标
        if (state != CustomConstants.DETAIL_STATE && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            setRightImage(R.drawable.custom_icon_location);
            ivRight.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                String location = getContent();
                bundle.putString(LOCATION, location);
                ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
                CommonUtil.startActivtiyForResult(mView.getContext(), LocationPresenter.class, code, bundle);
            });
        } else if (!TextUtil.isEmpty(lng) && !TextUtil.isEmpty(lat)) {
            //不可编辑时 如果有定位信息 可点击 查看地图位置
            etInput.setTextColor(ColorUtils.resToColor(getContext(), R.color.url_color));
            etInput.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, lng);
                bundle.putString(Constants.DATA_TAG2, lat);
                CommonUtil.startActivtiy(getContext(), ViewAddressPresenter.class, bundle);
            });
        }
    }

    @Override
    public boolean formatCheck() {
        return true;
    }


    @Override
    protected void setData(Object value) {
        if (value == null) {
            return;
        }
        try {
            JSONObject jsonObject = null;
            if (value instanceof JSONObject) {
                jsonObject = (JSONObject) value;
            } else if (value instanceof Map) {
                jsonObject = JSONObject.parseObject(JSON.toJSONString(value));
            }
            if (jsonObject != null) {
                lng = jsonObject.getString("lng");
                lat = jsonObject.getString("lat");
                String address = jsonObject.getString("value");
                setAddress(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContent(String content) {
        //地图没有默认值
    }

    public void setAddress(String address) {
        TextUtil.setText(etInput, address);
    }

    @Override
    public Object getValue() {
        String content = getContent();
        if (TextUtil.isEmpty(content)) {
            return "";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", content);
        jsonObject.put("lng", lng);
        jsonObject.put("lat", lat);
        jsonObject.put("area", address);
        return jsonObject;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //详细地址
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            PoiItem poiInfo = data.getParcelableExtra(LocationPresenter.LOCATION_RESULT_CODE);
            lat = poiInfo.getLatLonPoint().getLatitude() + "";
            lng = poiInfo.getLatLonPoint().getLongitude() + "";
            String provinceName = poiInfo.getProvinceName();
            String cityName = poiInfo.getCityName();
            String adName = poiInfo.getAdName();
            address = provinceName + "#" + cityName + "#" + adName;
            setAddress(provinceName + cityName + adName + poiInfo.getTitle());
        }
    }

    private void location() {

        mLocationClient = new AMapLocationClient(getContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationListener(this);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度

                lat = amapLocation.getLatitude() + "";
                lng = amapLocation.getLongitude() + "";
                setAddress(amapLocation.getAddress());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}
