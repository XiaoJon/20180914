package com.hjhq.teamface.customcomponent.widget2.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.TextWebView;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;

/**
 * 富文本
 * Created by Administrator on 2018/7/10.
 */

public class RichTextWebView extends BaseView implements ActivityPresenter.OnActivityResult {
    private TextView tvTitle;
    private TextWebView textWebView;
    private String content = "";
    private View flEdit;

    public RichTextWebView(CustomBean bean) {
        super(bean);
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        mView = View.inflate(activity, R.layout.custom_item_vertical_web_view, null);

        tvTitle = mView.findViewById(R.id.tv_title);
        flEdit = mView.findViewById(R.id.fl_edit);
        textWebView = mView.findViewById(R.id.text_web_view);

        parent.addView(mView);
        initView();
    }

    private void initView() {
        setTitle(tvTitle, title);
        if (isDetailState()) {
            setWebContent(bean.getValue());
        } else {
            if (state == CustomConstants.ADD_STATE) {
                CustomBean.FieldBean field = bean.getField();
                if (field != null) {
                    String defaultValue = field.getDefaultValue();
                    setWebContent(defaultValue);
                }
            } else {
                setWebContent(bean.getValue());
            }

            if (CustomConstants.FIELD_READ.equals(fieldControl)) {
                flEdit.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
            } else {
                flEdit.setOnClickListener(v -> {
                    //清除焦点
                    RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DATA_TAG1, title);
                    bundle.putString(Constants.DATA_TAG2, content);

                    ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
                    CommonUtil.startActivtiyForResult(getContext(), RichTextEditActivity.class, code, bundle);
                });
            }
        }
        textWebView.loadUrl(1, Constants.EMAIL_DETAIL_URL);
    }

    /**
     * 当网页加载完成后设置数据
     *
     * @param content
     */
    void setWebContent(Object content) {
        if (content == null) {
            content = "";
        }
        this.content = content + "";
        textWebView.setOnStateChanListener(new TextWebView.OnStateChangeListener() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setData(RichTextWebView.this.content);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            }
        });
    }

    @Override
    protected void setData(Object value) {
        if (value == null) {
            value = "";
        }
        textWebView.setWebText(value + "");
    }

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, content);
    }

    @Override
    public boolean checkNull() {
        return TextUtil.isEmpty(content);
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            content = data.getStringExtra(Constants.DATA_TAG1);
            textWebView.setWebText(content);
        }
    }
}
