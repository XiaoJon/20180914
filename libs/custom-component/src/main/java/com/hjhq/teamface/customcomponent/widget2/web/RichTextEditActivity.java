package com.hjhq.teamface.customcomponent.widget2.web;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.view.TextWebView;
import com.hjhq.teamface.customcomponent.R;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2018/7/10.
 */

public class RichTextEditActivity extends ActivityPresenter<RichTextEditDelegate, CommonModel> {
    private String title;
    private TextWebView textWebView;
    private String content;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            title = getIntent().getStringExtra(Constants.DATA_TAG1);
            content = getIntent().getStringExtra(Constants.DATA_TAG2);
        }
    }

    @Override
    public void init() {
        setTitle(title);

        textWebView = findViewById(R.id.text_web_view);
        textWebView.loadUrl(0, Constants.EMAIL_EDIT_URL);
        setWebContent(content);
    }

    /**
     * 当网页加载完成后设置数据
     *
     * @param content
     */
    void setWebContent(String content) {
        textWebView.setOnStateChanListener(new TextWebView.OnStateChangeListener() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setContent(content);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            }
        });
    }

    protected void setContent(String value) {
        if (value == null) {
            value = "";
        }
        textWebView.setWebText(value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        textWebView.getWebText(text -> {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, text);
            setResult(RESULT_OK, intent);
			int childCount = textWebView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = textWebView.getChildAt(i);
                if (childAt instanceof com.tencent.smtt.sdk.WebView) {
                    SoftKeyboardUtils.hide(childAt);
                }
            }
            SoftKeyboardUtils.hide(mContext);
            finish();
        });
        return super.onOptionsItemSelected(item);
    }
}
