package com.hjhq.teamface.attendance.presenter;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.adapter.AttendanceAddWifiAdapter;
import com.hjhq.teamface.attendance.bean.AddTypeBean;
import com.hjhq.teamface.attendance.bean.AttendanceAddWifiBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AddWifiDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_wifi", desc = "添加WiFi")
public class AddWifiActivity extends ActivityPresenter<AddWifiDelegate, AttendanceModel> implements View.OnClickListener {
    String wifiName;
    String macAddress;
    AttendanceAddWifiAdapter mAttendanceWifiAdapter;
    List<ScanResult> dataList = new ArrayList<>();
    List<AttendanceAddWifiBean> wifiList = new ArrayList<>();
    WifiManager mWifiManager;
    LocationManager mLocationManager;
    boolean isLocationServiceOpen = false;
    boolean isReceiverRegist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
        SystemFuncUtils.requestPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION, aBoolean -> {
            if (!aBoolean) {
                ToastUtils.showError(mContext, "必须获得必要的权限才能正常使用！");
            } else {
                isLocationServiceOpen = isLocServiceEnable(mContext);
                if (!isLocationServiceOpen) {
                    openGPS(mContext);
                    /*DialogUtils.getInstance().sureOrCancel(mContext, "",
                            "必须打开定位才可以获取WiFi列表,确定打开吗?", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                                @Override
                                public void clickSure() {
                                    openGPS(mContext);
                                }
                            });*/
                }
            }
        });
    }

    @Override
    public void init() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        initView();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String resultAction = intent.getAction();
                if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(resultAction)) {
                    SystemFuncUtils.requestPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION, aBoolean -> {
                        if (!aBoolean) {
                            ToastUtils.showError(mContext, "必须获得必要的权限才能正常使用！");
                        } else {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (!isLocationEnabled()) {
                                    ToastUtils.showToast(mContext, "打开定位才可以获取wifi列表");
                                    openGPS(mContext);
                                }
                            }
                            List<ScanResult> scanResults = mWifiManager.getScanResults();
                            if (scanResults == null) {
                                return;
                            }
                            WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
                            String bssid = connectionInfo.getBSSID();
                            mAttendanceWifiAdapter.setBSSID(connectionInfo.getBSSID());
                            for (ScanResult result : scanResults) {
//                                Log.e("wifi名字=    ", result.SSID);
                                dataList.add(result);
                                AttendanceAddWifiBean bean = new AttendanceAddWifiBean();
                                bean.setName(result.SSID);
                                bean.setAddress(result.BSSID);
                                if (!TextUtils.isEmpty(bssid) && bssid.equals(bean.getAddress())) {
                                    bean.setCheck(true);
                                    bean.setCurrentWifi(true);
                                    wifiList.add(bean);
                                } else {
                                    bean.setCheck(false);
                                    bean.setCurrentWifi(false);
                                }

                            }
                            unregisterReceiver(mBroadcastReceiver);
                            isReceiverRegist = false;
                            mAttendanceWifiAdapter.notifyDataSetChanged();

                        }
                    });


                }

            }
        };
        registerBroadcast();
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        mWifiManager.startScan();


    }

    private BroadcastReceiver mBroadcastReceiver;

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        isReceiverRegist = true;
    }


    private void initView() {
        mAttendanceWifiAdapter = new AttendanceAddWifiAdapter(wifiList);
        viewDelegate.setAdapter(mAttendanceWifiAdapter);
        viewDelegate.setOnItemClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(mGpsMonitor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            if (!isReceiverRegist) {
                registerBroadcast();
            }

            mWifiManager.startScan();
            wifiList.clear();
            mAttendanceWifiAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == 1) {
            AttendanceAddWifiBean wifiBean = new AttendanceAddWifiBean();
            for (int i = 0; i < wifiList.size(); i++) {
                if (wifiList.get(i).isCheck()) {
                    wifiBean = wifiList.get(i);
                }
            }
            if (wifiList.size() <= 0) {
                return true;
            }
            if (TextUtils.isEmpty(wifiBean.getAddress())) {
                ToastUtils.showToast(mContext, "当前WiFi的mac地址为空");
                return true;
            }
            AddTypeBean bean = new AddTypeBean();
            bean.setName(wifiBean.getName());
            bean.setAddress(wifiBean.getAddress());
            bean.setAttendanceType(AttendanceConstants.TYPE_WIFI);
            bean.setEffectiveRange("");
            bean.setAttendanceStatus("0");
            bean.setId("");

            bean.setLocation(new ArrayList<>());
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        viewDelegate.setOnItemClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*for (int i = 0; i < wifiList.size(); i++) {
                    wifiList.get(i).setCheck(false);
                }
                wifiList.get(position).setCheck(true);
                mAttendanceWifiAdapter.notifyDataSetChanged();*/
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });

        super.bindEvenListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            boolean enabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println("gps enabled? " + enabled);
            if (enabled) {
                mWifiManager.startScan();
            }

        }
    };

    @Override
    public void onClick(View v) {

    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
