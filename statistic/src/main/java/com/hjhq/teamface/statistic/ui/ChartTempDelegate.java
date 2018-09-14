package com.hjhq.teamface.statistic.ui;

import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.statistic.R;


/**
 * 图表 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class ChartTempDelegate extends AppDelegate {
    WebView mWebView;

    /**
     * 空数据
     */
    View rlEmpty;

    @Override
    public int getRootLayoutId() {
        return R.layout.statistic_activity_chart_temp;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        mWebView = get(R.id.web_view);
        rlEmpty = get(R.id.fl_empty);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 显示放大缩小 controler
        settings.setBuiltInZoomControls(true);
        // 可以缩放
        settings.setSupportZoom(true);
        // 默认缩放
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        // 设置图片放大缩小
        settings.setUseWideViewPort(true);
        // 取消放大缩小按钮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        mWebView.loadUrl(Constants.STATISTIC_CHART_URL);

        mWebView.setWebChromeClient(new WebChromeClient());
    }

    public void setWebClient(WebViewClient webClient) {
        mWebView.setWebViewClient(webClient);
    }

    /**
     * 设置排列显示
     */
    public void setSortInfo(String str) {
        TextView sortTitle = get(R.id.tv_sort_title);
        TextUtil.setText(sortTitle, str);
    }

    public void evaluateJavascript(String s) {
        mWebView.evaluateJavascript("javascript:getChartsVal(" + s + ")", value -> {
            //此处为 js 返回的结果
        });
    }

    public void setEmpty(boolean b) {
        rlEmpty.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
