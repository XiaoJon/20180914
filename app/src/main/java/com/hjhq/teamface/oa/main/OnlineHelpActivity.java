package com.hjhq.teamface.oa.main;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;

public class OnlineHelpActivity extends BaseTitleActivity {
    private WebView pdfShowWebView;

    @Override
    protected int getChildView() {
        return R.layout.online_file_preview_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("teamface.cn");
        pdfShowWebView = findViewById(R.id.pdf_show_webview);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        pdfShowWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = pdfShowWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        pdfShowWebView.setWebChromeClient(new WebChromeClient());
        pdfShowWebView
                .loadUrl("http://www.teamface.cn");
    }


    @Override
    public void onClick(View view) {
    }
}
