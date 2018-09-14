package com.hjhq.teamface.common.ui.location;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.view.search.SearchEditText;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.LocNearAddressAdapter;

import java.util.List;

/**
 *
 * @author lx
 * @date 2017/9/4
 */

public class LocationDelegate extends AppDelegate {
    public SearchEditText etSearch;
    public MapView mapView;
    public ImageView ivLocation;
    public RecyclerView mRecyclerView;
    private LocNearAddressAdapter locAdapter;
    public static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    public static final int FILL_COLOR = Color.argb(90, 3, 145, 255);

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.create(inflater, container, savedInstanceState);
        mapView = get(R.id.mapview_location);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_location;
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

        setRightMenuTexts(R.color.main_green, "确定");
        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("定位");

        float mapHeight = (ScreenUtils.getScreenHeight(mContext) - DeviceUtils.dpToPixel(mContext, 50)) / 2;
        View rlMap = get(R.id.rl_map);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlMap.getLayoutParams();
        layoutParams.height = (int) mapHeight;
        rlMap.setLayoutParams(layoutParams);

        etSearch = get(R.id.et_search_in_searchbar);
        mRecyclerView = get(R.id.lv_location_nearby);
        ivLocation = get(R.id.iv_location);

        locAdapter = new LocNearAddressAdapter(null);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(locAdapter);
    }

    /**
     * 设置选中
     *
     * @param selectPosition
     */
    public void setSelectPosition(int selectPosition) {
        locAdapter.setSelectPosition(selectPosition);
        locAdapter.notifyDataSetChanged();
    }

    /**
     * 得到选中的地址
     *
     * @return
     */
    public PoiItem getPoiInfo() {
        return locAdapter.getSelectPoiInfo();
    }

    /**
     * 设置新值
     *
     * @param nearList
     */
    public void setNewData(List<PoiItem> nearList) {
        locAdapter.setNewData(nearList);
        setSelectPosition(0);
    }

    /**
     * 搜索框文字改变后返回值
     *
     * @return
     */
    public void afterTextChanged() {
        etSearch.drawableDel();
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
    public void setMapViewUI() {
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
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);

        // 将自定义的 myLocationStyle 对象添加到地图上
        getMap().setMyLocationStyle(myLocationStyle);
    }

    public AMap getMap() {
        return mapView.getMap();
    }
}