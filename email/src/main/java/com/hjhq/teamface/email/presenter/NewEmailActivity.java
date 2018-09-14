package com.hjhq.teamface.email.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ClickUtil;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.adapter.EmailAttachmentAdapter;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.EmailContactsAdapter;
import com.hjhq.teamface.email.bean.CheckNextApprovalResultBean;
import com.hjhq.teamface.email.bean.EmailAccountListBean;
import com.hjhq.teamface.email.bean.EmailDetailBean;
import com.hjhq.teamface.email.bean.NewEmailBean;
import com.hjhq.teamface.email.view.NewEmailDelegate;
import com.hjhq.teamface.email.widget.EmailAddressView;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;


/**
 * 新增/编辑/转发/回复邮件
 */
@RouteNode(path = "/new_email", desc = "新增,编辑,回复,转发邮件")
public class NewEmailActivity extends ActivityPresenter<NewEmailDelegate, EmailModel> {
    public static final int REQUEST_SELECT_MEMBER_CODE = 0x211;
    public static final int REQUEST_SELECT_CC_CODE = 0x212;
    private int currentType = EmailConstant.ADD_NEW_EMAIL;
    private LinearLayout llInput;
    private FrameLayout flContainer;
    private LinearLayout rlWidgetContainer;
    private RecyclerView rvContacts;
    private EditText mEtTheme;
    private com.tencent.smtt.sdk.WebView mWebView;
    private EmailContactsAdapter mEmailContactsAdapter;
    private EmailAttachmentAdapter mAttachmentAdapter;
    private EmailAttachmentAdapter mOrginAttachmentAdapter;
    private EmailAddressView currentAddressView;
    private EmailAddressView eav1;
    private EmailAddressView eav2;
    private EmailAddressView eav3;
    private int eavIndex;
    private File imageFromCamera;
    private EmailBean mEmailean;
    private NewEmailBean bean;
    private EmailBean mEmailDetailBean;
    private EmailDetailBean mDetailBean;
    private ArrayList<EmailAccountListBean.DataBean> accountList = new ArrayList<>();
    private List<AttachmentBean> choosedFileList = new ArrayList<>();
    private List<File> localFileList = new ArrayList<>();

    private String defaultAccount = "";
    private String emailId = "";
    private String defaultAccountId = "";

    private boolean showRelevant = true;


    private String htmlContent = "<p></p>";
    private String orginContent = "";
    private String emailSubject = "";
    private boolean sendEmailFlag = true;
    private boolean isContentChanged = false;
    private int getContentTimes = 0;
    //审批类型
    private String mProcessType;
    //审批相关数据
    private CheckNextApprovalResultBean.CheckNextApproval mProcessData;
    //获取审批数据状态
    private boolean approvalDataFlag = false;

    private String approvalId = "";
    private String ccIds = "";
    //内容是否发生变化
    private boolean isAnythingChanged = false;
    private RelativeLayout mRootLayout;

    private int keyHeight = 0;

    //防止连击
    private boolean isClickable = true;


    @Override
    public void init() {
        mRootLayout = viewDelegate.get(R.id.app_recycler);
        keyHeight = (getWindowManager().getDefaultDisplay().getHeight()) / 3;
        mEtTheme = viewDelegate.get(R.id.text53);
        mEtTheme.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    eav1.releaseFocus();
                    eav2.releaseFocus();
                    eav3.releaseFocus();
                }
            }
        });
        mEtTheme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isAnythingChanged = true;
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initWebView();
        initData();
        viewDelegate.chooseEmailAccount(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, defaultAccount);
            bundle.putSerializable(Constants.DATA_TAG2, accountList);
            CommonUtil.startActivtiyForResult(NewEmailActivity.this, ChooseEmailAccountActivity.class, Constants.REQUEST_CODE1, bundle);

        });
        getNetData();
        checkChooseNextApproval();
        mAttachmentAdapter = new EmailAttachmentAdapter(EmailAttachmentAdapter.ADD_EMAIL_TAG, choosedFileList);
        viewDelegate.setAttachmentAdapter(mAttachmentAdapter);
        viewDelegate.setRvListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                mAttachmentAdapter.getData().remove(position);
                mAttachmentAdapter.notifyDataSetChanged();
            }
        });
        initIntent();
        eav1.setDataChanged(false);
        eav2.setDataChanged(false);
        eav3.setDataChanged(false);
        isAnythingChanged = false;
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentType = intent.getIntExtra(Constants.DATA_TAG3, EmailConstant.ADD_NEW_EMAIL);
            switch (currentType) {
                case EmailConstant.ADD_NEW_EMAIL:
                    //添加邮件/新建邮件
                    break;
                case EmailConstant.EDIT_DRAFT:
                    //编辑草稿
                    mEmailDetailBean = (EmailBean) intent.getSerializableExtra(Constants.DATA_TAG5);
                    viewDelegate.initAction(EmailConstant.DRAFT_BOX);
                    viewDelegate.setListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentEmail();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moreAction();
                        }
                    });
                    setData();
                    break;
                case EmailConstant.EDIT_AGAIN:
                    // 再次编辑
                    mEmailDetailBean = (EmailBean) intent.getSerializableExtra(Constants.DATA_TAG5);
                    setData();
                    break;
                case EmailConstant.REPLY_EAMAIL:
                    setAttAndTo(intent);
                    break;
                case EmailConstant.TRANSMIT_EMAIL:
                    setAttAndTo(intent);
                    eav1.clear();
                    break;
                default:
                    break;
            }
        }
    }

    private void setAttAndTo(Intent intent) {
        mEmailDetailBean = (EmailBean) intent.getSerializableExtra(Constants.DATA_TAG7);
        emailSubject = intent.getStringExtra(Constants.DATA_TAG6);
        if (currentType == EmailConstant.REPLY_EAMAIL) {
            viewDelegate.setEmailTheme(getString(R.string.email_reply_add) + emailSubject);
        } else if (currentType == EmailConstant.TRANSMIT_EMAIL) {
            viewDelegate.setEmailTheme(getString(R.string.email_transmit_add) + emailSubject);
        } else {
            viewDelegate.setEmailTheme(emailSubject);
        }

        htmlContent = intent.getStringExtra(Constants.DATA_TAG4);
        ArrayList<Member> list = (ArrayList<Member>) intent.getSerializableExtra(Constants.DATA_TAG1);
        eav1.addMember(list);
        ArrayList<AttachmentBean> attList = (ArrayList<AttachmentBean>) intent.getSerializableExtra(Constants.DATA_TAG2);
        if (attList != null && attList.size() > 0) {
            choosedFileList.addAll(attList);
            mAttachmentAdapter.notifyDataSetChanged();
        }
        if (currentType == EmailConstant.EDIT_AGAIN || currentType == EmailConstant.EDIT_DRAFT) {
            ArrayList<ReceiverBean> cc_recipients = mEmailDetailBean.getCc_recipients();
            List<Member> ccList = new ArrayList<>();
            for (int i = 0; i < cc_recipients.size(); i++) {
                Member m = new Member();
                m.setEmployee_name(cc_recipients.get(i).getEmployee_name());
                m.setEmail(cc_recipients.get(i).getMail_account());
                ccList.add(m);
            }
            eav2.addMember(ccList);
            ArrayList<ReceiverBean> bcc_recipients = mEmailDetailBean.getBcc_recipients();
            List<Member> bccList = new ArrayList<>();
            for (int i = 0; i < bcc_recipients.size(); i++) {
                Member m = new Member();
                m.setEmployee_name(bcc_recipients.get(i).getEmployee_name());
                m.setEmail(bcc_recipients.get(i).getMail_account());
                bccList.add(m);
            }
            eav3.addMember(bccList);
        }

    }

    private void setData() {
        if (mEmailDetailBean != null) {
            //邮件id
            try {
                emailId = mEmailDetailBean.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                viewDelegate.setData(mEmailDetailBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //发件人
            try {
                defaultAccount = mEmailDetailBean.getFrom_recipient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                defaultAccountId = mEmailDetailBean.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewDelegate.setDefaultAccount(defaultAccount);
            //附件
            try {
                choosedFileList.addAll(mEmailDetailBean.getAttachments_name());
                mAttachmentAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //收件人
            try {
                ArrayList<ReceiverBean> to_recipients = mEmailDetailBean.getTo_recipients();
                List<Member> toList = new ArrayList<>();
                for (int i = 0; i < to_recipients.size(); i++) {
                    Member m = new Member();
                    m.setEmployee_name(to_recipients.get(i).getEmployee_name());
                    m.setEmail(to_recipients.get(i).getMail_account());
                    toList.add(m);
                }
                if (toList.size() > 0) {
                    eav1.addMember(toList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ArrayList<ReceiverBean> cc_recipients = mEmailDetailBean.getCc_recipients();
                List<Member> ccList = new ArrayList<>();
                for (int i = 0; i < cc_recipients.size(); i++) {
                    Member m = new Member();
                    m.setEmployee_name(cc_recipients.get(i).getEmployee_name());
                    m.setEmail(cc_recipients.get(i).getMail_account());
                    ccList.add(m);
                }
                if (ccList.size() > 0) {
                    eav2.addMember(ccList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ArrayList<ReceiverBean> bcc_recipients = mEmailDetailBean.getBcc_recipients();
                List<Member> bccList = new ArrayList<>();
                for (int i = 0; i < bcc_recipients.size(); i++) {
                    Member m = new Member();
                    m.setEmployee_name(bcc_recipients.get(i).getEmployee_name());
                    m.setEmail(bcc_recipients.get(i).getMail_account());
                    bccList.add(m);
                }
                if (bccList.size() > 0) {
                    eav3.addMember(bccList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //邮件主题
            try {
                viewDelegate.setEmailTheme(mEmailDetailBean.getSubject());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //邮件内容


        }


    }

    /**
     * JS调用返回富文本内容
     *
     * @param value
     */
    @JavascriptInterface
    public void getContent(String value) {

        if (getContentTimes == 0) {
            orginContent = value;
            getContentTimes++;
        } else {
            if (!TextUtils.isEmpty(orginContent)) {
                if (orginContent.equals(value)) {
                    isContentChanged = false;
                } else {
                    isContentChanged = true;
                }
            } else {
                if (TextUtils.isEmpty(value)) {
                    isContentChanged = false;
                } else {
                    isContentChanged = true;
                }
            }
        }
        Log.i("内容1=    ", value);

    }

    @JavascriptInterface
    public void getContent2(String value) {
        Log.i("内容2=    ", value);
        sendEmail(value);
    }

    /**
     * 邮件内容发生变化js回调
     *
     * @param flag
     */
    @JavascriptInterface
    public void contentChanged(String flag) {
        if ("1".equals(flag)) {
            isAnythingChanged = true;
        }
    }

    /**
     * @return
     */
    private void sendEmail() {
        bean = new NewEmailBean();
        bean.setPersonnel_approverBy(approvalId);
        bean.setPersonnel_ccTo(ccIds);
        bean.setTimer_task_time("");

        if (!TextUtils.isEmpty(emailId)) {
            bean.setId(emailId);
        }

        if (TextUtils.isEmpty(defaultAccount) && sendEmailFlag) {
            ToastUtils.showToast(NewEmailActivity.this, "发件人不能为空");
            return;
        }
        if (TextUtils.isEmpty(defaultAccountId) && sendEmailFlag) {
            ToastUtils.showToast(NewEmailActivity.this, "账户id为空");
            return;
        }
        bean.setAccount_id(TextUtil.parseLong(defaultAccountId));
        bean.setFrom_recipient(defaultAccount);
        bean.setIs_plain(0);
        bean.setMail_content("");
        //收件人
        ArrayList<ReceiverBean> to_recipients = new ArrayList<>();
        ArrayList<ReceiverBean> cc_recipients = new ArrayList<>();
        ArrayList<ReceiverBean> bcc_recipients = new ArrayList<>();
        ArrayList<Member> list1 = eav1.getMember();
        if (list1.size() <= 0 && sendEmailFlag) {
            ToastUtils.showToast(NewEmailActivity.this, "收件人不能为空");
            return;
        }
        List<Member> list2 = eav2.getMember();
        List<Member> list3 = eav3.getMember();
        for (int i = 0; i < list1.size(); i++) {
            ReceiverBean bean1 = new ReceiverBean();
            bean1.setEmployee_name(list1.get(i).getEmployee_name());
            bean1.setMail_account(list1.get(i).getEmail());
            to_recipients.add(bean1);

        }
        for (int i = 0; i < list2.size(); i++) {
            ReceiverBean bean1 = new ReceiverBean();
            bean1.setEmployee_name(list2.get(i).getEmployee_name());
            bean1.setMail_account(list2.get(i).getEmail());
            cc_recipients.add(bean1);

        }
        for (int i = 0; i < list3.size(); i++) {
            ReceiverBean bean1 = new ReceiverBean();
            bean1.setEmployee_name(list3.get(i).getEmployee_name());
            bean1.setMail_account(list3.get(i).getEmail());
            bcc_recipients.add(bean1);
        }
        bean.setTo_recipients(to_recipients);
        bean.setCc_recipients(cc_recipients);
        bean.setBcc_recipients(bcc_recipients);
        bean.setSubject(viewDelegate.getEmailTheme());
        if (TextUtils.isEmpty(bean.getSubject()) && sendEmailFlag) {
            ToastUtils.showToast(NewEmailActivity.this, "邮件主题不能为空");
            return;
        }
        //检查附件总大小,将已上传,文件库文件筛选出并设置到bean
        bean.setAttachments_name(new ArrayList<AttachmentBean>());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.evaluateJavascript("javascript:sendValMobile(1)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        });


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRootLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

                    //监听到软键盘弹起
                    if (currentType == EmailConstant.EDIT_DRAFT) {
                        viewDelegate.showActionBar(false);
                    }
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    //监听到软件盘关闭
                    if (currentType == EmailConstant.EDIT_DRAFT) {
                        viewDelegate.showActionBar(true);
                    }

                }
            }
        });
    }

    /**
     * 邮件内容与附件
     *
     * @param content
     */
    private void sendEmail(String content) {
        /*if (!TextUtils.isEmpty(content)) {
            if (content.startsWith("\"")) {
                content = content.substring(1, content.length());
            }
        }
        if (!TextUtils.isEmpty(content)) {
            if (content.endsWith("\"")) {
                content = content.substring(0, content.length() - 1);
            }
        }*/
        bean.setMail_content(content);
        if (!sendEmailFlag || (currentType == EmailConstant.TRANSMIT_EMAIL || currentType == EmailConstant.REPLY_EAMAIL)) {
            reallySend();
        } else {
            chooseApproval();
        }
    }

    private void reallySend() {
        bean.setPersonnel_approverBy(approvalId);
        bean.setPersonnel_ccTo(ccIds);
        List<File> selectedFile = getFileList(bean);
        boolean limit = FileTransmitUtils.checkLimit(selectedFile);
        if (limit) {
            DialogUtils.getInstance().sureOrCancel(NewEmailActivity.this,
                    getString(R.string.email_operation_hint),
                    getString(R.string.email_file_size_10m_limit),
                    viewDelegate.getRootView(),
                    new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            sendEmail(bean, selectedFile, sendEmailFlag);
                        }

                    });
        } else {
            sendEmail(bean, selectedFile, sendEmailFlag);
        }
    }

    private String getLabelText(String emailContent) {

        return "'<p>" + emailContent + "</p>'";
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        mWebView = viewDelegate.get(R.id.wv);
        mWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    eav1.releaseFocus();
                    eav2.releaseFocus();
                    eav3.releaseFocus();
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //  ToastUtils.showToast(NewEmailActivity.this, url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switch (currentType) {
                    case EmailConstant.REPLY_EAMAIL:
                    case EmailConstant.TRANSMIT_EMAIL:
                        showWebDataWithHead(true);
                        break;
                    case EmailConstant.EDIT_AGAIN:
                    case EmailConstant.EDIT_DRAFT:
                        showWebDataWithHead(false);
                        break;
                    case EmailConstant.ADD_NEW_EMAIL:
                        initEdit();
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("WebView error =", error.toString());
                // mWebView.setVisibility(View.GONE);
            }
        });
        if (Constants.IS_DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
       /* WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(false);
        //支持缩放
        settings.setSupportZoom(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(false);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        mWebView.addJavascriptInterface(NewEmailActivity.this, "android");
        mWebView.loadUrl(EmailConstant.EMAIL_EDIT_URL);*/
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
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
        mWebView.loadUrl(Constants.EMAIL_EDIT_URL);
    }

    /**
     * 初始空的输入控件
     */
    private void initEdit() {
        JSONObject jo = new JSONObject();
        try {

            jo.put("head", new JSONObject());
            jo.put("html", "");
            jo.put("type", EmailConstant.EDIT);
            jo.put("device", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.evaluateJavascript("javascript:getValHtml(" + jo + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("javascript:sendValMobile(0)", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 传数据到富文本编辑器
     */
    private void showWebDataWithHead(boolean flag) {
        JSONObject jo = new JSONObject();
        try {
            if (flag) {
                jo.put("head", getHeadJson());

            } else {
                jo.put("head", new JSONObject());
            }
            jo.put("html", mEmailDetailBean.getMail_content());
            jo.put("type", EmailConstant.EDIT);
            jo.put("device", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("getValHtml", jo.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.evaluateJavascript("javascript:getValHtml(" + jo + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("javascript:sendValMobile(0)", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    /**
     * 邮件头部详情
     *
     * @return
     */
    private JSONObject getHeadJson() {
        JSONObject jo = new JSONObject();
        if (mEmailDetailBean != null) {
            try {
                jo.put("subject", mEmailDetailBean.getSubject());
                jo.put("ip_address", mEmailDetailBean.getIp_address());
                jo.put("time", DateTimeUtil.longToStr(TextUtil.parseLong(mEmailDetailBean.getCreate_time()), "yyyy-MM-dd HH:mm:ss (EEEE)"));
                jo.put("from_recipient_name", mEmailDetailBean.getFrom_recipient_name() + "");
                jo.put("from_recipient", mEmailDetailBean.getFrom_recipient());
                JSONArray cc_recipients = new JSONArray();
                JSONArray bcc_recipients = new JSONArray();
                JSONArray to_recipients = new JSONArray();

                for (int i = 0; i < mEmailDetailBean.getTo_recipients().size(); i++) {
                    JSONObject j = new JSONObject();
                    if (TextUtils.isEmpty(mEmailDetailBean.getTo_recipients().get(i).getEmployee_name())) {
                        j.put("employee_name", "");
                    } else {
                        j.put("employee_name", mEmailDetailBean.getTo_recipients().get(i).getEmployee_name());
                    }

                    j.put("mail_account", mEmailDetailBean.getTo_recipients().get(i).getMail_account());
                    to_recipients.put(j);
                }
                for (int i = 0; i < mEmailDetailBean.getCc_recipients().size(); i++) {
                    JSONObject j = new JSONObject();
                    if (TextUtils.isEmpty(mEmailDetailBean.getCc_recipients().get(i).getEmployee_name())) {
                        j.put("employee_name", "");
                    } else {
                        j.put("employee_name", mEmailDetailBean.getCc_recipients().get(i).getEmployee_name());
                    }
                    j.put("mail_account", mEmailDetailBean.getCc_recipients().get(i).getMail_account());
                    cc_recipients.put(j);
                }
                for (int i = 0; i < mEmailDetailBean.getBcc_recipients().size(); i++) {
                    JSONObject j = new JSONObject();
                    if (TextUtils.isEmpty(mEmailDetailBean.getBcc_recipients().get(i).getEmployee_name())) {
                        j.put("employee_name", "");
                    } else {
                        j.put("employee_name", mEmailDetailBean.getBcc_recipients().get(i).getEmployee_name());
                    }
                    j.put("mail_account", mEmailDetailBean.getBcc_recipients().get(i).getMail_account());
                    bcc_recipients.put(j);
                }
                jo.put("cc_recipients", cc_recipients);
                jo.put("bcc_recipients", bcc_recipients);
                jo.put("to_recipients", to_recipients);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jo;

    }


    protected void initData() {
        flContainer = viewDelegate.get(R.id.fl_input_widget);
        rlWidgetContainer = viewDelegate.get(R.id.ll_widget_container);
        rvContacts = viewDelegate.get(R.id.rv_email);


        llInput = viewDelegate.get(R.id.ll_input);
        eav1 = new EmailAddressView(this, "收件人:", true);
        eav2 = new EmailAddressView(this, "抄送人:", false);
        eav3 = new EmailAddressView(this, "密送人:", false);
        eav1.setOnAddClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eav1.clearFocus();
                chooseContacts(Constants.REQUEST_CODE3, eav1.getMember());
            }
        });
        eav2.setOnAddClickListener(v -> {
            eav2.clearFocus();
            chooseContacts(Constants.REQUEST_CODE4, eav2.getMember());
        });
        eav3.setOnAddClickListener(v -> {
            eav3.clearFocus();
            chooseContacts(Constants.REQUEST_CODE5, eav3.getMember());
        });
        eav1.setOnFocusListener(new EmailAddressView.OnFocusListener() {
            @Override
            public void onFocus() {
                eav1.showList();
                eav2.releaseFocus();
                eav3.releaseFocus();
            }
        });
        eav2.setOnFocusListener(new EmailAddressView.OnFocusListener() {
            @Override
            public void onFocus() {
                eav2.showList();
                eav1.releaseFocus();
                eav3.releaseFocus();
            }
        });
        eav3.setOnFocusListener(new EmailAddressView.OnFocusListener() {
            @Override
            public void onFocus() {
                eav3.showList();
                eav1.releaseFocus();
                eav2.releaseFocus();
            }
        });
        llInput.addView(eav1);
        llInput.addView(eav2);
        llInput.addView(eav3);
        viewDelegate.get(R.id.rl_attachment).setOnClickListener(v -> {
            showAttachmentOption();
        });
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            if (flContainer.getVisibility() == View.VISIBLE) {
                hideRelevant(((EmailAddressView) rlWidgetContainer.getChildAt(0)), "");
                return;
            }
            isEmailContentEmpty();
        });
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        mEmailContactsAdapter = new EmailContactsAdapter(null);
        rvContacts.setAdapter(mEmailContactsAdapter);
        rvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean flag = true;
                for (int i = 0; i < currentAddressView.getMember().size(); i++) {
                    if (!TextUtils.isEmpty(currentAddressView.getMember().get(i).getEmail())
                            && currentAddressView.getMember().get(i).getEmail().equals(((EmailContactsAdapter) adapter).getData().get(position).getEmail())) {
                        flag = false;
                    }
                }
                if (flag) {
                    currentAddressView.addMember(((EmailContactsAdapter) adapter).getData().get(position));
                }
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(currentAddressView.getWidth(), currentAddressView.getHeight());
                rlWidgetContainer.setLayoutParams(layoutParams);
                rlWidgetContainer.requestLayout();
                // rlWidgetContainer.invalidate();
                hideRelevant(currentAddressView, "");

            }
        });


    }

    private void isEmailContentEmpty() {

        if (!isAnythingChanged && !eav1.isDataChanged() && !eav2.isDataChanged() && !eav3.isDataChanged()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.evaluateJavascript("javascript:sendValMobile(0)", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            if (isContentChanged) {
                                quitAlertMenu();
                            } else {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
                }
            });
        } else {
            quitAlertMenu();
        }


    }

    /**
     * 选择收件人/抄送人/密送人
     *
     * @param requestCode3
     * @param member
     */
    private void chooseContacts(int requestCode3, ArrayList<Member> member) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, requestCode3);
        bundle.putSerializable(Constants.DATA_TAG2, member);
        CommonUtil.startActivtiyForResult(NewEmailActivity.this, ChooseEmailContactsActivity.class, requestCode3, bundle);
    }

    /**
     * 弹出添加附件菜单
     */
    private void showAttachmentOption() {


        String[] menu = {"拍照", "从相册中选择", "从文件库中选择"};
        PopUtils.showBottomMenu(NewEmailActivity.this, llInput.getRootView(), "上传附件", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        Bundle bundle = new Bundle();
                        switch (position) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                //从相册中选择
                                CommonUtil.getImageFromAlbumByMultiple(NewEmailActivity.this, 1);
                                break;
                            case 2:
                                //从文件夹选择
                                // FileHelper.showFileChooser(NewEmailActivity.this);
                                UIRouter.getInstance().openUri(mContext, "DDComp://filelib/select_file", bundle, Constants.REQUEST_CODE6);
                                //CommonUtil.startActivtiyForResult(mContext, SelectRootFileActivity.class, Constants.REQUEST_CODE6);
                                break;

                            default:

                                break;
                        }
                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sendEmailFlag = true;
//        ClickUtils.INSTANCE.click(() -> sendEmail());
        ClickUtil.click(() -> sendEmail());
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发送邮件
     *
     * @param bean
     * @param selectedFile
     */
    private void sendEmail(NewEmailBean bean, List<File> selectedFile, boolean sendOrSaveDraft) {
        if (selectedFile.size() > 0) {
            DownloadService.getInstance().uploadFile(selectedFile, new ProgressSubscriber<UpLoadFileResponseBean>(NewEmailActivity.this) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    // TODO: 2018/3/9 上传附件失败

                }

                @Override
                public void onNext(UpLoadFileResponseBean upLoadFileResponseBean) {
                    super.onNext(upLoadFileResponseBean);
                    List<UploadFileBean> data = upLoadFileResponseBean.getData();
                    List<AttachmentBean> list = new ArrayList<AttachmentBean>();
                    for (int i = 0; i < data.size(); i++) {
                        AttachmentBean attachmentBean = new AttachmentBean();
                        attachmentBean.setFileName(data.get(i).getFileName());
                        attachmentBean.setFileSize(data.get(i).getFileSize());
                        attachmentBean.setFileType(data.get(i).getFileType());
                        attachmentBean.setFileUrl(data.get(i).getFileUrl());
                        attachmentBean.setFromWhere(EmailConstant.FROM_UPLOAD);
                        list.add(attachmentBean);
                    }
                    bean.getAttachments_name().addAll(list);
                    NewEmailActivity.this.bean = bean;
                    sendOrSaveEmail();
                }
            });
        } else {
            sendOrSaveEmail();
        }
    }

    /**
     * 发送,保存草稿,回复,转发
     */
    private void sendOrSaveEmail() {
        if (sendEmailFlag) {
            //发送
            switch (currentType) {
                case EmailConstant.ADD_NEW_EMAIL:
                case EmailConstant.EDIT_AGAIN:
                case EmailConstant.EDIT_DRAFT:
                    send();
                    break;
                case EmailConstant.REPLY_EAMAIL:
                    reply();
                    break;
                case EmailConstant.TRANSMIT_EMAIL:
                    transmit();
                    break;
                default:
                    break;
            }
        } else {
            //保存草稿
            switch (currentType) {
                case EmailConstant.ADD_NEW_EMAIL:
                    save();
                    break;
                case EmailConstant.EDIT_AGAIN:
                    save();
                    break;
                case EmailConstant.EDIT_DRAFT:
                    if ("1".equals(mEmailDetailBean.getDraft_status())) {
                        saveEdit();
                    } else {
                        save();
                    }
                    break;
                case EmailConstant.REPLY_EAMAIL:
                case EmailConstant.TRANSMIT_EMAIL:
                    // TODO: 2018/4/1 草稿箱中的未通过审批和撤销审批的邮件编辑后如何保存?
                    save();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 编辑后再次保存
     */
    private void saveEdit() {
        bean.setIs_emergent(0);
        bean.setIs_track(0);
        model.editDraft(NewEmailActivity.this, bean, new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                // ToastUtils.showToast(NewEmailActivity.this, "保存失败");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                // ToastUtils.showToast(NewEmailActivity.this, "保存成功");
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    /**
     * 转发
     */
    private void transmit() {
        if (mEmailDetailBean != null) {
            bean.setId(mEmailDetailBean.getId());
            model.mailForward(NewEmailActivity.this, bean, new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    e.printStackTrace();
                    //ToastUtils.showToast(NewEmailActivity.this, "转发失败");
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    // ToastUtils.showToast(NewEmailActivity.this, "转发成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }


    }

    /**
     * 回复
     */
    private void reply() {
        if (mEmailDetailBean != null) {
            bean.setId(mEmailDetailBean.getId());
            model.mailReply(NewEmailActivity.this, bean, new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    e.printStackTrace();
                    // ToastUtils.showToast(NewEmailActivity.this, "回复失败");
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showToast(NewEmailActivity.this, "回复成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

    }

    private List<File> getFileList(NewEmailBean bean) {
        List<File> list = new ArrayList<>();
        bean.getAttachments_name().clear();
        List<AttachmentBean> attList = mAttachmentAdapter.getData();
        if (attList != null) {
            for (int i = 0; i < attList.size(); i++) {
                if (attList.get(i).getFromWhere() == EmailConstant.FROM_LOCAL_FILE) {
                    File file = new File(attList.get(i).getFileUrl());
                    if (file.exists()) {
                        list.add(file);
                    }
                } else {
                    bean.getAttachments_name().add(attList.get(i));
                }

            }
        }
        return list;
    }

    /**
     * 保存草稿
     */
    private void save() {
        if (currentType == EmailConstant.EDIT_AGAIN) {
            //避免原邮件被删除
            bean.setId("");
        }
        model.saveToDraft(NewEmailActivity.this, bean, new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                // ToastUtils.showToast(NewEmailActivity.this, "保存失败");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(NewEmailActivity.this, "保存成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 发送
     */
    private void send() {
        if (mEmailDetailBean != null) {
            bean.setId(mEmailDetailBean.getId());
        }
        if (currentType == EmailConstant.EDIT_AGAIN) {
            //避免原邮件被删除
            bean.setId("");
        }
        model.sendEmail(NewEmailActivity.this, bean, new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                // ToastUtils.showToast(NewEmailActivity.this, "发送失败");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                if (mProcessData != null && !"2".equals(mProcessData.getProcess_type())) {
                    ToastUtils.showToast(NewEmailActivity.this, "您的邮件已进入审批环节，请在审批模块查看。");
                } else {
                    ToastUtils.showToast(NewEmailActivity.this, "发送成功");
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (FileHelper.isSdCardExist()) {
            SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                if (aBoolean) {
                    imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                } else {
                    ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                }
            });
        } else {
            ToastUtils.showToast(NewEmailActivity.this, R.string.email_sdcard_not_exist_toast);
        }

    }

    @Override
    public void onBackPressed() {
        if (flContainer.getVisibility() == View.VISIBLE) {
            hideRelevant(((EmailAddressView) rlWidgetContainer.getChildAt(0)), "");
            return;
        }
        if (getContentTimes == 0) {
            finish();
        } else {
            isEmailContentEmpty();
        }

    }

    private String[] menu = {"放弃修改", "保存草稿"};

    /**
     * 退出提醒
     */
    private void quitAlertMenu() {

        PopUtils.showBottomMenu(NewEmailActivity.this, llInput.getRootView(), "提示", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        switch (position) {
                            case 0:
                                setResult(RESULT_OK);
                                finish();
                                break;
                            case 1:
                                sendEmailFlag = false;
                                sendEmail();
                                break;
                            case 2:
                                setResult(RESULT_OK);
                                finish();
                                break;

                            default:

                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * 删除草稿
     */
    private void deleteDraft() {
        if (TextUtils.isEmpty(emailId)) {
            finish();
            return;
        }
        // TODO: 2018/4/1 草稿和审批未通过/撤销的也使用这个接口?
        model.deleteDraft(mContext, emailId, EmailConstant.DRAFT_BOX + "", new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //ToastUtils.showError(mContext, "删除失败");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "删除成功");
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    public void showRelevant(EmailAddressView view, String keyword) {
        if (!showRelevant) {
            return;
        }
        if (flContainer.getVisibility() == View.GONE || flContainer.getVisibility() == View.INVISIBLE) {
            this.currentAddressView = view;
            List<Member> memberList = new ArrayList<>();
            // TODO: 2018/7/31 需要使用到im模块 
            // List<Member> memberList = DBManager.getInstance().queryMemberByKeyword(keyword);
            if (memberList != null && memberList.size() > 0) {
                flContainer.setVisibility(View.VISIBLE);
                rlWidgetContainer.removeAllViews();
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(view.getWidth(), view.getHeight());
                if (view.equals(eav1)) {
                    llInput.removeView(eav1);
                    rlWidgetContainer.addView(view);
                    rlWidgetContainer.setLayoutParams(layoutParams);
                    view.hideAddButton();
                    eavIndex = 0;
                }
                if (view.equals(eav2)) {
                    llInput.removeView(eav2);
                    rlWidgetContainer.addView(view);
                    rlWidgetContainer.setLayoutParams(layoutParams);
                    view.hideAddButton();
                    eavIndex = 1;
                }
                if (view.equals(eav3)) {
                    llInput.removeView(eav3);
                    rlWidgetContainer.addView(view);
                    rlWidgetContainer.setLayoutParams(layoutParams);
                    view.hideAddButton();
                    eavIndex = 2;
                }

                mEmailContactsAdapter.setNewData(memberList);
                mEmailContactsAdapter.notifyDataSetChanged();
                mEmailContactsAdapter.setKeyword(keyword);
                // if (memberList.size() <= 0 && (TextUtils.isEmpty(keyword) || keyword.length() >= 1)) {
                if (memberList.size() <= 0 && TextUtils.isEmpty(keyword)) {
                    hideRelevant(view, keyword);
                }

            }
        } else {
            List<Member> memberList = new ArrayList<>();
            // TODO: 2018/7/31 需要使用im模块 
            // List<Member> memberList = DBManager.getInstance().queryMemberByKeyword(keyword);
            if (memberList != null && memberList.size() > 0) {

                mEmailContactsAdapter.setNewData(memberList);
                mEmailContactsAdapter.notifyDataSetChanged();
                mEmailContactsAdapter.setKeyword(keyword);

            } else {
                mEmailContactsAdapter.setNewData(new ArrayList<>());
                mEmailContactsAdapter.notifyDataSetChanged();
                mEmailContactsAdapter.setKeyword(keyword);
            }
        }
    }

    public void hideRelevant(EmailAddressView view, String trim) {
        if (!showRelevant) {
            return;
        }
        if (flContainer.getVisibility() == View.VISIBLE) {
            flContainer.setVisibility(View.GONE);
            rlWidgetContainer.removeView(view);
            llInput.addView(view, eavIndex);
            view.showAddButton();
            mEmailContactsAdapter.getData().clear();
            mEmailContactsAdapter.notifyDataSetChanged();
            mEmailContactsAdapter.setKeyword("");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    isAnythingChanged = true;
                    //更改发送邮件账号
                    if (data != null) {
                        accountList = (ArrayList<EmailAccountListBean.DataBean>) data.getSerializableExtra(Constants.DATA_TAG2);
                        if (accountList != null && accountList.size() > 0) {
                            for (int i = 0; i < accountList.size(); i++) {
                                if (accountList.get(i).isChecked()) {
                                    defaultAccount = accountList.get(i).getAccount();
                                    defaultAccountId = accountList.get(i).getId();
                                    accountList.get(i).setChecked(true);
                                }
                                viewDelegate.setDefaultAccount(defaultAccount);
                            }
                        }
                    }
                    break;
                case Constants.REQUEST_CODE3:
                    isAnythingChanged = true;
                    ArrayList<Member> list1 = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (list1 != null) {
                        eav1.addMember(list1);
                    }
                    break;
                case Constants.REQUEST_CODE4:
                    isAnythingChanged = true;
                    ArrayList<Member> list2 = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (list2 != null) {
                        eav2.addMember(list2);
                    }
                    break;
                case Constants.REQUEST_CODE5:
                    isAnythingChanged = true;
                    ArrayList<Member> list3 = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (list3 != null) {
                        eav3.addMember(list3);
                    }
                    break;
                case Constants.REQUEST_CODE6:
                    isAnythingChanged = true;
                    //文件库选择的文件
                    if (data != null) {
                        AttachmentBean bean = (AttachmentBean) data.getSerializableExtra(Constants.DATA_TAG1);
                        bean.setFromWhere(EmailConstant.FROM_UPLOAD);

                        choosedFileList.add(bean);
                        mAttachmentAdapter.notifyDataSetChanged();
                    }
                    break;
                case PhotoPicker.REQUEST_CODE:
                    isAnythingChanged = true;
                    //相册
                    ArrayList<String> photos =
                            data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if (photos != null) {
                        for (int i = 0; i < photos.size(); i++) {
                            File file = new File(photos.get(i));
                            if (file.exists()) {
                                localFileList.add(file);
                                choosedFileList.add(new AttachmentBean(file));

                                mAttachmentAdapter.notifyDataSetChanged();
                                viewDelegate.getRootView().requestLayout();
                                viewDelegate.getRootView().invalidate();

                            }
                        }
                    }
                    break;
                case Constants.CHOOSE_LOCAL_FILE:
                    isAnythingChanged = true;
                    //本地文件
                    Uri uri = data.getData();
                    String path = UriUtil.getPhotoPathFromContentUri(NewEmailActivity.this, uri);
                    File file = new File(path);
                    if (file.exists()) {
                        localFileList.add(file);
                        choosedFileList.add(new AttachmentBean(file));
                        mAttachmentAdapter.notifyDataSetChanged();
                        viewDelegate.getRootView().requestLayout();
                        viewDelegate.getRootView().invalidate();

                    }
                    break;
                case Constants.TAKE_PHOTO_NEW_REQUEST_CODE:
                    isAnythingChanged = true;
                    //拍照
                    if (imageFromCamera != null && imageFromCamera.exists()) {
                        localFileList.add(imageFromCamera);
                        choosedFileList.add(new AttachmentBean(imageFromCamera));
                        mAttachmentAdapter.notifyDataSetChanged();
                        viewDelegate.getRootView().requestLayout();
                        viewDelegate.getRootView().invalidate();
                    }
                    break;
                case REQUEST_SELECT_MEMBER_CODE:
                    isAnythingChanged = true;
                    //审批人与抄送人
                    if (data != null) {
                        List<Member> aList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                        List<Member> cList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG2);
                        if (aList != null && aList.size() > 0) {
                            approvalId = aList.get(0).getId() + "";
                        } else {
                            ToastUtils.showToast(mContext, "请再次发送并选择审批人");
                            return;
                        }
                        if (cList != null && cList.size() > 0) {
                            StringBuilder sb = new StringBuilder();

                            for (int i = 0; i < cList.size(); i++) {
                                if (i == cList.size() - 1) {
                                    sb.append(cList.get(i).getId() + "");
                                } else {
                                    sb.append(cList.get(i).getId() + ",");
                                }
                            }
                            ccIds = sb.toString();
                        }
                        reallySend();
                    }


                    break;
                default:

                    break;


            }

        }

    }

    /**
     * 获取有效邮箱账户
     */
    private void getNetData() {
        model.queryPersonnelAccount(NewEmailActivity.this,
                new ProgressSubscriber<EmailAccountListBean>(NewEmailActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(EmailAccountListBean emailAccountListBean) {
                        super.onNext(emailAccountListBean);
                        List<EmailAccountListBean.DataBean> data = emailAccountListBean.getData();
                        accountList.clear();
                        accountList.addAll(data);

                        if (mEmailDetailBean != null && (currentType == EmailConstant.TRANSMIT_EMAIL || currentType == EmailConstant.REPLY_EAMAIL)) {
                            ArrayList<ReceiverBean> to_recipients = mEmailDetailBean.getTo_recipients();
                            for (int i = 0; i < accountList.size(); i++) {
                                String account = accountList.get(i).getAccount();
                                if (TextUtils.isEmpty(account)) {
                                    continue;
                                } else {
                                    for (int j = 0; j < to_recipients.size(); j++) {
                                        if (account.equals(to_recipients.get(j).getMail_account())) {
                                            defaultAccount = accountList.get(i).getAccount();
                                            defaultAccountId = accountList.get(i).getId();
                                            accountList.get(i).setDefault(true);
                                            accountList.get(i).setChecked(true);
                                            viewDelegate.setDefaultAccount(defaultAccount);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (TextUtils.isEmpty(defaultAccount)) {
                                findDefaultAccount();
                            }
                        } else {
                            findDefaultAccount();
                        }

                    }
                });

    }

    private void findDefaultAccount() {
        for (int i = 0; i < accountList.size(); i++) {
            if ("1".equals(accountList.get(i).getAccount_default())) {
                defaultAccount = accountList.get(i).getAccount();
                defaultAccountId = accountList.get(i).getId();
                accountList.get(i).setDefault(true);
                accountList.get(i).setChecked(true);
                viewDelegate.setDefaultAccount(defaultAccount);
                break;
            }
        }
        if (TextUtils.isEmpty(defaultAccount) || TextUtils.isEmpty(defaultAccountId)) {
            if (accountList.size() > 0) {
                String id = accountList.get(0).getId();
                String account = accountList.get(0).getAccount();
                if (!TextUtil.isEmpty(id) && !TextUtils.isEmpty(account)) {
                    defaultAccount = account;
                    defaultAccountId = id;
                    accountList.get(0).setDefault(true);
                    accountList.get(0).setChecked(true);
                    viewDelegate.setDefaultAccount(defaultAccount);
                }
            }
        }
    }

    /**
     * 判断是否需要选择下一审批人
     */
    private void checkChooseNextApproval() {
        model.checkChooseNextApproval(this, new ProgressSubscriber<CheckNextApprovalResultBean>(this) {
            @Override
            public void onNext(CheckNextApprovalResultBean baseBean) {
                super.onNext(baseBean);
                mProcessData = baseBean.getData();
                if (approvalDataFlag) {
                    chooseApproval();
                }
                approvalDataFlag = true;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtils.showToast(mContext, "获取审批流程异常!请联系管理员.");
            }
        });
    }

    private void chooseApproval() {
        if (approvalDataFlag) {
            if (mProcessData == null) {
                ToastUtils.showToast(mContext, "获取审批流程异常!请联系管理员.");
                return;
            }
            mProcessType = mProcessData.getProcess_type();
            if (TextUtil.isEmpty(mProcessType)) {
                sendEmailFlag = true;
                reallySend();
                return;
            }
            //0固定流程,1自由流程,2无流程
            if ("0".equals(mProcessType)) {
                sendEmailFlag = true;
                reallySend();
                return;
            }

            if ("1".equals(mProcessType)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DATA_TAG1, mProcessData);
                CommonUtil.startActivtiyForResult(mContext, ChooseApprovalActivity.class, REQUEST_SELECT_MEMBER_CODE, bundle);
                return;
            }
            if ("2".equals(mProcessType)) {
                sendEmailFlag = true;
                reallySend();
                return;
            }
        } else {
            checkChooseNextApproval();
        }


    }

    /**
     * 更多
     */
    private void moreAction() {

        PopUtils.showBottomMenu(NewEmailActivity.this, viewDelegate.getRootView(), "更多操作", new String[]{"彻底删除"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                model.deleteDraft(NewEmailActivity.this, emailId, EmailConstant.DRAFT_BOX + "", new ProgressSubscriber<BaseBean>(NewEmailActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // ToastUtils.showError(mContext, "删除失败");
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showSuccess(mContext, "删除成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                return false;
            }
        });

    }

    /**
     * 评论
     */
    private void commentEmail() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, EmailConstant.BEAN_NAME);
        bundle.putString(Constants.DATA_ID, emailId);
        CommonUtil.startActivtiy(NewEmailActivity.this, CommentActivity.class, bundle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {

        }
    }


}
