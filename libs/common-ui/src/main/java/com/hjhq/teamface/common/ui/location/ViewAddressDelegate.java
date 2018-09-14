package com.hjhq.teamface.common.ui.location;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;


/**
 * 查看地址 视图类
 * @author lx
 * @date 2017/9/4
 */

public class ViewAddressDelegate extends AppDelegate {
    public MapView mapView;
    public ImageView ivLocation;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.create(inflater, container, savedInstanceState);
        mapView = get(R.id.mapview_location);
        ivLocation = get(R.id.iv_location);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_view_address;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.text_cancel);

        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("地址");

        setMapViewUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void close() {
        super.close();
        mapView.onDestroy();
    }

    /**
     * 设置地图UI样式
     */
    void setMapViewUI() {
        //隐藏LOGO
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    public void setLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(LocationDelegate.STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(LocationDelegate.FILL_COLOR);

        // 将自定义的 myLocationStyle 对象添加到地图上
        getMap().setMyLocationStyle(myLocationStyle);
    }

    public AMap getMap() {
        return mapView.getMap();
    }
}