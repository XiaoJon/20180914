package com.hjhq.teamface.common.ui.email;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.EmailAttachmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 审批 视图代理类
 * Created by lx on 2017/8/31.
 */

public class EmailDetailFragmentDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshWidget;
    private RecyclerView rvAttachment;
    private WebView mWebView;
    private EmailAttachmentAdapter mItemAdapter;
    private TextView tvTitle;
    private TextView tvSender;
    private TextView tvReceiver;
    private TextView tvCc;
    private TextView tvBcc;
    private TextView tvDate;
    private TextView tvIp;
    private LinearLayout llReceiver;
    private LinearLayout llIp;
    private LinearLayout llCc;
    private LinearLayout llBcc;
    private LinearLayout llTime;
    private EmailBean mEmailBean;
    private String labledEmailContent = "'<p></p>'";
    private RelativeLayout rlTimer;
    private RelativeLayout rlApprovalState;
    private TextView tvTimer;
    private ImageView ivState;
    /**
     * 网页加载
     */
    private boolean webLoadFinish = false;
    /**
     * 数据加载
     */
    private boolean dataLoadFinish = false;

    private String emailId = "";

    @Override
    public int getRootLayoutId() {
        return R.layout.email_fragment_detail;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initWidget() {
        super.initWidget();
        rlTimer = get(R.id.rl_timer);
        tvTimer = get(R.id.tv_timer);
        tvTitle = get(R.id.tv_email_title);
        tvSender = get(R.id.tv_email_sender_content);
        tvReceiver = get(R.id.tv_email_receiver_content);
        tvIp = get(R.id.tv_ip_content);
        tvCc = get(R.id.tv_cc_content);
        tvBcc = get(R.id.tv_bcc_content);
        tvDate = get(R.id.tv_time_content);
        llReceiver = get(R.id.rl_receiver);
        llIp = get(R.id.rl_ip);
        llCc = get(R.id.rl_cc);
        llBcc = get(R.id.rl_bcc);
        llTime = get(R.id.ll_date);
        ivState = get(R.id.iv_state);
        rlApprovalState = get(R.id.rl_state);
        mWebView = get(R.id.wv_email_content);
        rvAttachment = get(R.id.rv_attachment);
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mItemAdapter = new EmailAttachmentAdapter(EmailAttachmentAdapter.EMAIL_DETAIL_TAG, null);
        rvAttachment.setAdapter(mItemAdapter);
        mWebView.setClickable(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                DialogUtils.getInstance().sureOrCancel(getActivity(), "打开网页", url,

                        getRootView(), "打开", "取消",
                        new DialogUtils.OnClickSureOrCancelListener() {
                            @Override
                            public void clickSure() {
                                Intent intent = new Intent(Intent.ACTION_VIEW, (Uri.parse(url))).addCategory(Intent.CATEGORY_BROWSABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }

                            @Override
                            public void clickCancel() {
                            }
                        });
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webLoadFinish = true;
                if (dataLoadFinish) {
                    showWebData();
                }
                super.onPageFinished(view, url);
                LogUtil.e("js 返回的结果3" + url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        mWebView.setClickable(false);
        mWebView.setFocusable(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(getActivity(), "android");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebView.loadUrl(Constants.EMAIL_DETAIL_URL);

    }

    public void setAdapter(BaseQuickAdapter adapter) {

        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 传数据给WebView
     */
    private void showWebData() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("html", labledEmailContent);
            jo.put("type", 1);
            jo.put("device", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT <= 19) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("javascript:sendValMobile(" + jo + ")");
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
    }

    /**
     * 显示数据
     *
     * @param emailDetailBean
     */
    public void showData(EmailBean emailDetailBean) {
        mEmailBean = emailDetailBean;
        if (mEmailBean == null) {
            ToastUtils.showError(getActivity(), "数据错误");
            return;
        }
        emailId = mEmailBean.getId();
        dataLoadFinish = true;
        labledEmailContent = emailDetailBean.getMail_content();
        if (webLoadFinish) {
            showWebData();
        }
        TextUtil.setText(tvTitle, emailDetailBean.getSubject());
        TextUtil.setText(tvSender, emailDetailBean.getFrom_recipient());
        TextUtil.setText(tvReceiver, getReceiver(emailDetailBean, 1));
        if (TextUtils.isEmpty(getReceiver(emailDetailBean, 2))) {
            llCc.setVisibility(View.GONE);
        } else {
            llCc.setVisibility(View.VISIBLE);
            TextUtil.setText(tvCc, getReceiver(emailDetailBean, 2));
        }
        if (TextUtils.isEmpty(getReceiver(emailDetailBean, 3))) {
            llBcc.setVisibility(View.GONE);
        } else {
            llBcc.setVisibility(View.VISIBLE);
            TextUtil.setText(tvBcc, getReceiver(emailDetailBean, 3));
        }
        // TODO: 2018/3/19 ip和地址为空时隐藏
        //llIp.setVisibility(View.GONE);
        TextUtil.setText(tvIp, "ip(地址)");
        try {
            TextUtil.setText(tvDate, DateTimeUtil.longToStr(TextUtil.parseLong(emailDetailBean.getCreate_time()), "yyyy-MM-dd HH:mm:ss (EEEE)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mItemAdapter.setNewData(emailDetailBean.getAttachments_name());
        switch (mEmailBean.getApproval_status()) {
            //0 审批未通过 1 已通过 2待审批
            case "0":
                setApprovalState(false);
                break;
            case "1":
                //已通过
                setApprovalState(true);
                break;
            case "2":
                break;

            default:
                break;
        }
        switch (mEmailBean.getTimer_status()) {
            case "0":
                //非定时
                rlTimer.setVisibility(View.GONE);
                break;
            case "1":
                //定时
                rlTimer.setVisibility(View.VISIBLE);
                TextUtil.setText(tvTimer, "定时邮件");
                break;
            case "2":
                break;

            default:
                break;
        }

    }

    /**
     * 审批状态
     *
     * @param b
     */
    private void setApprovalState(boolean b) {
        rlApprovalState.setVisibility(View.VISIBLE);
        if (b) {
            ivState.setImageResource(R.drawable.icon_approve_pass_tag);
        } else {
            ivState.setImageResource(R.drawable.icon_approve_reject_tag);
        }

    }

    /**
     * 从详情中获取收件人,抄送人,密送人
     *
     * @param emailDetailBean
     * @param tag
     * @return
     */
    private String getReceiver(EmailBean emailDetailBean, int tag) {
        StringBuilder sb = new StringBuilder();
        List<ReceiverBean> list = new ArrayList<>();
        switch (tag) {
            case 1:
                list = emailDetailBean.getTo_recipients();
                break;
            case 2:
                list = emailDetailBean.getCc_recipients();
                break;
            case 3:
                list = emailDetailBean.getBcc_recipients();
                break;
            default:
                break;
        }

        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(sb.toString())) {
                sb.append(list.get(i).getEmployee_name() + "<" + list.get(i).getMail_account() + ">;");
            } else {
                sb.append(list.get(i).getEmployee_name() + "<" + list.get(i).getMail_account() + ">");
            }
        }

        return sb.toString();
    }
}
