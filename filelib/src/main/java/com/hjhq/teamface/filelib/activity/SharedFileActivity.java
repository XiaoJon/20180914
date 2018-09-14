package com.hjhq.teamface.filelib.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.view.SharedFileDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@RouteNode(path = "/shared_file", desc = "分享文件")
public class SharedFileActivity extends ActivityPresenter<SharedFileDelegate, FilelibModel> {
    private WebView mWebView;
    private String docPath = "";

    @Override
    public void init() {
        initView();
    }


    protected void initView() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docPath = bundle.getString(Constants.DATA_TAG1);
        }
        //docPath = EmailConstant.EMAIL_DETAIL_URL;
        viewDelegate.setTitle("文件共享");
        mWebView = (WebView) findViewById(R.id.pdf_show_webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                LogUtil.e("js 返回的结果3" + url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(SharedFileActivity.this, "android");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(docPath);
            }
        });

    }


    private void openFile(File file) {
        if (!file.exists()) {
            ToastUtils.showToast(SharedFileActivity.this, getString(R.string.filelib_not_found));

        }
        FileUtils.browseDocument(this, file.getName(), file.getAbsolutePath());
        LogUtil.e("文件名" + file + "绝对路径" + file.getAbsolutePath());
    }

    /**
     * JS调用,下载文件
     *
     * @param url
     * @param fileName
     * @param fileSize
     */
    @JavascriptInterface
    public void download(String url, String fileName, int fileSize) {
        if (FileTransmitUtils.checkLimit(fileSize)) {
            DialogUtils.getInstance().sureOrCancel(this, "提示", "当前为非Wifi网络环境且文件大小超过10M,您要继续下载吗?", mWebView, new DialogUtils.OnClickSureListener() {
                @Override
                public void clickSure() {
                    DownloadService.getInstance().downloadFileFromUrl(System.currentTimeMillis() + "", url, fileName);
                    ToastUtils.showToast(mContext, "正在后台下载");
                }
            });
        } else {
            DownloadService.getInstance().downloadFileFromUrl(System.currentTimeMillis() + "", url, fileName);
            ToastUtils.showToast(mContext, "正在后台下载");
        }

    }

    /**
     * JS调用返回富文本内容
     *
     * @param url
     */
    @JavascriptInterface
    public void getContent(String url) {
        Log.e("url", url);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.clearCache(true);
            }
        });
    }

    String str = "'><b style=\\\"display:none\\\">jiaddsf sff 烦得很过分的话对方获得丰厚对方获得丰厚东方红回访电话sgg</b<br><a>dsgsgsdgsdgsdgsdgdsg</a>'\n";

    int type = 0;//0编辑1详情

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        JSONObject jo = new JSONObject();
        if (item.getItemId() == 0) {
            type = 1;
        }
        if (item.getItemId() == 1) {
            type = 0;
        }
        try {
            jo.put("html", str);
            jo.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT <= 19) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:sendValMobile(" + jo + ")");
                }
            });
        } else {
            LogUtil.e("javascript:sendValT0Mobile(" + jo + ")");
            mWebView.evaluateJavascript("javascript:sendValMobile(" + jo + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtil.e("js 返回的结果1" + "====" + value);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        mWebView.clearCache(true);
        super.onDestroy();

    }
}
