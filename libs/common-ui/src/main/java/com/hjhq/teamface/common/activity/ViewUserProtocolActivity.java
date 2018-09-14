package com.hjhq.teamface.common.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

import java.io.File;

public class ViewUserProtocolActivity extends ActivityPresenter<WebviewDelegate, CommonModel> {
    private WebView mWebView;
    private String protocolUrl = "https://app.teamface.cn/#/termsService";

    @Override
    public void init() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            protocolUrl = bundle.getString(Constants.DATA_TAG1);
        }
        initView();
    }


    protected void initView() {

        viewDelegate.setTitle("Teamface");
        mWebView = (WebView) findViewById(R.id.pdf_show_webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

        });
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                viewDelegate.setTitle(title);
            }

        };
        mWebView.setWebChromeClient(wvcc);
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(protocolUrl);


    }


    protected void initData() {
        File file = JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {

            }
        }
    }


    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
