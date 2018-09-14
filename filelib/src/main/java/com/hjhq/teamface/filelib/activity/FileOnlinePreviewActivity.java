package com.hjhq.teamface.filelib.activity;

import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.view.OnlinePreviewDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.File;

@RouteNode(path = "/online_preview", desc = "文件在线预览")
public class FileOnlinePreviewActivity extends ActivityPresenter<OnlinePreviewDelegate, FilelibModel> {
    private WebView pdfShowWebView;
    private String docPath = "http://192.168.1.42:8888/#/pdfPreview";

    @Override
    public void init() {
        initView();
        initData();
    }


    protected void initView() {

        viewDelegate.setTitle("在线预览");
        pdfShowWebView = (WebView) findViewById(R.id.pdf_show_webview);
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
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        pdfShowWebView.setWebChromeClient(new WebChromeClient());
        /*if (!"".equals(docPath)) {
            byte[] bytes = null;
            try {// 获取以字符编码为utf-8的字符
                bytes = docPath.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            if (bytes != null) {
                //  docPath = new BASE64Encoder().encode(bytes);// BASE64转码
                docPath = getFromBASE64(bytes.toString());// BASE64转码
            }
        }*/
        docPath = "https://blog.csdn.net/superherowupan/article/details/54235997";
        pdfShowWebView.loadUrl(docPath);


    }

    public String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }

        try {
            byte[] b = Base64.decode(s, Base64.DEFAULT);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }


    protected void initData() {
        File file = JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {

            }
        }
    }


    private void openFile(File file) {
        if (!file.exists()) {
            ToastUtils.showToast(mContext, getString(R.string.filelib_not_found));

        }
        FileUtils.browseDocument(this, file.getName(), file.getAbsolutePath());
        LogUtil.e("文件名" + file + "绝对路径" + file.getAbsolutePath());
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
