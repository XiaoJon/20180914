package com.hjhq.teamface.statistic.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ZoomButtonsController;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.statistic.R;

/**
 * 报表详情UI
 *
 * @author Administrator
 * @date 2018/3/19
 */

public class ReportDetailDelegate extends AppDelegate {
    WebView mWebView;

    @Override
    public int getRootLayoutId() {
        return R.layout.statistic_activity_report_detail;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    /**
     * 这里有侧滑控件，所以布局不能用父类的实现
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        rootLayout = (ViewGroup) inflater.inflate(R.layout.drawer_layout, container, false);
        View rootView = inflater.inflate(rootLayoutId, container, false);
        LinearLayout rootLayout2 = (LinearLayout) inflater.inflate(R.layout.root_layout, container, false);
        View toolbar = inflater.inflate(R.layout.toolbar_comment, container, false);
        rootLayout2.addView(toolbar);
        rootLayout2.addView(rootView);
        rootLayout.addView(rootLayout2, 0);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        get(R.id.ll_tool_bar).setPadding(0, statusBarHeight, 0, 0);

        setTitle("报表详情");

        View view = get(R.id.drawer_content);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) / 5 * 4);
        view.setLayoutParams(layoutParams);

        mWebView = get(R.id.web_view);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);// 显示放大缩小 controler
        settings.setSupportZoom(true);// 可以缩放
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放
        settings.setUseWideViewPort(true);// 设置图片放大缩小
        int sysVersion = Build.VERSION.SDK_INT;
        // 取消放大缩小按钮显示
        if (sysVersion >= 11)
        {
            settings.setDisplayZoomControls(false);
        }
        else
        {
            ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        mWebView.loadUrl(Constants.STATISTIC_REPORT_URL);

        mWebView.setWebChromeClient(new WebChromeClient());
    }
    public void setWebClient(WebViewClient webClient){
        mWebView.setWebViewClient(webClient);
    }


    /**
     * 初始化筛选控件
     */
    public void initFilter(FragmentActivity activity, String reportId) {
        Fragment fragment = new StatisticFilterFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, reportId);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }

    /**
     * 打开筛选
     */
    public void openDrawer() {
        DrawerLayout mDrawerLayout = get(R.id.drawer_layout);
        mDrawerLayout.openDrawer(get(R.id.drawer_content));
    }

    /**
     * 关闭筛选
     *
     * @return
     */
    public boolean closeDrawer() {
        DrawerLayout mDrawerLayout = get(R.id.drawer_layout);
        View view = get(R.id.drawer_content);
        if (view.getVisibility() == View.VISIBLE) {
            mDrawerLayout.closeDrawer(view);
            return true;
        }
        return false;
    }
}
