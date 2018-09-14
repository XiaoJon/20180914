package com.hjhq.teamface.common.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.common.R;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 富文本webView
 * Created by Administrator on 2018/7/10.
 */

public class TextWebView extends FrameLayout {
    private com.tencent.smtt.sdk.WebView mWebView;
    private TextWebInterface textWebInterface;
    private OnStateChangeListener mListener;
    private int type;

    public interface TextWebInterface {
        void getWebText(String text);
    }

    public TextWebView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public TextWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mWebView = (com.tencent.smtt.sdk.WebView) View.inflate(context, R.layout.layout_text_web_view, null);
        addView(mWebView);

        if (Constants.IS_DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        mWebView.setClickable(true);
        mWebView.setFocusable(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(this, "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (mListener != null) {
                    return mListener.shouldOverrideUrlLoading(view, url);
                } else {

                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getWebText(null);
                if (mListener != null) {
                    mListener.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

        });

    }

    /**
     * 加载url,支持查看和编辑两种形式
     *
     * @param type 0编辑,1详情
     * @param url
     */
    public void loadUrl(int type, String url) {
        mWebView.loadUrl(url);
        this.type = type;
    }

    /**
     * 设置值
     */
    public void setWebText(String text) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("head", new JSONObject());
            jo.put("html", text);
            jo.put("type", type);
            jo.put("device", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.evaluateJavascript("javascript:getValHtml(" + jo + ")", value -> {
        });
    }

    /**
     * 得到web值
     */
    public void getWebText(TextWebInterface textWebInterface) {
        this.textWebInterface = textWebInterface;
        mWebView.evaluateJavascript("javascript:sendValMobile(1)", value -> {
        });
    }

    /**
     * 获取内容的回调
     *
     * @param value
     */
    @JavascriptInterface
    public void getContent2(String value) {
        if (textWebInterface != null) {
            textWebInterface.getWebText(value);
        }
    }

    public interface OnStateChangeListener {

        boolean shouldOverrideUrlLoading(WebView view, String url);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
    }

    public void setOnStateChanListener(OnStateChangeListener listener) {
        this.mListener = listener;
    }
}
