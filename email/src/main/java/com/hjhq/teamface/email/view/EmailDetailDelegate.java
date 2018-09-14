package com.hjhq.teamface.email.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.common.adapter.EmailAttachmentAdapter;
import com.hjhq.teamface.email.presenter.EmailDetailActivity;
import com.hjhq.teamface.email.presenter.NewEmailActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/11/9.
 * Describe：邮件详情展示
 */

public class EmailDetailDelegate extends AppDelegate {
    private RecyclerView rvAttachment;
    private WebView mWebView;
    private EmailAttachmentAdapter mItemAdapter;
    private FrameLayout flContainer;
    private View mActionView;
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
    private LinearLayout llInfo;
    private EmailBean mEmailBean;
    private String labledEmailContent = "<p></p>";
    private RelativeLayout rlTimer;
    private RelativeLayout rlApprovalState;
    private TextView tvTimer;
    private TextView tvHide;
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
        return R.layout.email_detail_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuIcons(R.drawable.email_send_icon);
        getToolbar().setNavigationIcon(R.drawable.icon_back_with_text);
        showMenu();
        rlTimer = get(R.id.rl_timer);
        tvTimer = get(R.id.tv_timer);
        flContainer = get(R.id.ll_action);
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
        tvHide = get(R.id.tv_hide);
        llInfo = get(R.id.ll_info);
        tvHide.setText("详情");
        llInfo.setVisibility(View.GONE);
        flContainer.setVisibility(View.GONE);
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
               /* DialogUtils.getInstance().sureOrCancel(getActivity(), "打开网页", url,

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
                        });*/
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
        /*if (Constants.IS_DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }*/
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
        mWebView.addJavascriptInterface(getActivity(), "android");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }*/
        mWebView.loadUrl(Constants.EMAIL_DETAIL_URL);
        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llInfo.getVisibility() == View.VISIBLE) {
                    llInfo.setVisibility(View.GONE);
                    tvHide.setText("详情");
                } else {
                    llInfo.setVisibility(View.VISIBLE);
                    tvHide.setText("隐藏");
                }
            }
        });
    }

    /**
     * 传数据给WebView
     */
    private void showWebData() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("head", new JSONObject());
            jo.put("html", labledEmailContent);
            jo.put("type", EmailConstant.DETAIL);
            jo.put("device", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.evaluateJavascript("javascript:getValHtml(" + jo + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.e("js 返回的结果1", "====" + value);
            }
        });
       /* if (Build.VERSION.SDK_INT <= 19) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("getValHtml", jo.toString());
                    mWebView.loadUrl("javascript:getValHtml(" + jo + ")");
                }
            });
        } else {
            mWebView.evaluateJavascript("javascript:getValHtml(" + jo + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtil.e("js 返回的结果1" + "====" + value);
                }
            });
        }*/
    }


    public void setAdapter(BaseQuickAdapter adapter) {
        rvAttachment.setAdapter(adapter);
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
        LogUtil.e("url", url);
        LogUtil.e("fileName", fileName);
        LogUtil.e("fileSize", fileSize + " ");
        DownloadService.getInstance().downloadFileFromUrl(System.currentTimeMillis() + "", url, fileName);
    }

    /**
     * 初始底部按钮
     */
    public void initAction(int type) {
        flContainer.setVisibility(View.VISIBLE);
        flContainer.removeAllViews();
        initTimer();
        hideApprovalState();
        switch (type) {
            case EmailConstant.RECEVER_BOX:
            case EmailConstant.UNREAD_BOX:
                //未读
                //收件箱
                mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_hzp, null);
                mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                    //回复
                    replyEmail(EmailConstant.REPLY_EAMAIL);
                });

                mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                    //转发
                    replyEmail(EmailConstant.TRANSMIT_EMAIL);
                });
                mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> {
                    //评论
                    commentEmail();

                });
                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }
                break;

            case EmailConstant.SENDED_BOX:

                //已发送

                if (EmailConstant.APPROVAL_STATE_NO.equals(mEmailBean.getApproval_status())) {
                    mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zzp, null);
                    mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> editEmail(EmailConstant.EDIT_AGAIN));
                    mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> replyEmail(EmailConstant.TRANSMIT_EMAIL));
                    mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> commentEmail());
                } else {
                    mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zzpg, null);
                    mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> editEmail(EmailConstant.EDIT_AGAIN));
                    mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> replyEmail(EmailConstant.TRANSMIT_EMAIL));
                    mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> commentEmail());
                    mActionView.findViewById(R.id.rl_action4).setOnClickListener(v -> {
                        showMenu2();
                    });
                }

                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }
                break;
            case EmailConstant.DRAFT_BOX:
                //草稿箱
                switch (mEmailBean.getApproval_status()) {
                    // 2 审批通过 3 审批驳回 4 已撤销 10 没有审批
                    case EmailConstant.APPROVAL_STATE_NO:
                        if ("1".equals(mEmailBean.getTimer_status())) {
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_xpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                ((EmailDetailActivity) getActivity()).changeSendTime();
                            });
                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu3());
                            initTimer();
                        } else {
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_pg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> showMenu3());
                            initTimer();
                            showMenu(0);
                        }
                        break;
                    case EmailConstant.APPROVAL_STATE_PASS:
                        //已通过
                        if ("1".equals(mEmailBean.getTimer_status())) {
                            setApprovalState(true);
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_xpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                ((EmailDetailActivity) getActivity()).changeSendTime();
                            });
                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            setApprovalState(true);
                        } else {
                            setApprovalState(true);
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_fpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                ((EmailDetailActivity) getActivity()).sendEmail();
                            });

                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            setApprovalState(true);
                            showMenu(0);
                        }
                        break;
                    case EmailConstant.APPROVAL_STATE_REJECTED:
                        //被驳回
                        if ("1".equals(mEmailBean.getTimer_status())) {
                            setApprovalState(true);
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                editEmail(EmailConstant.EDIT_AGAIN);
                            });
                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            setApprovalState(false);
                            showMenu();
                        } else {
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                editEmail(EmailConstant.EDIT_AGAIN);
                            });

                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            setApprovalState(false);
                            showMenu();
                        }
                        break;
                    case EmailConstant.APPROVAL_STATE_CANCEL:
                        //撤销
                        if ("1".equals(mEmailBean.getTimer_status())) {
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                editEmail(EmailConstant.EDIT_AGAIN);
                            });
                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            setApprovalState(false);
                        } else {
                            setApprovalState(true);
                            mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_zpg, null);
                            mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                                editEmail(EmailConstant.EDIT_AGAIN);
                            });

                            mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                            mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu4());
                            initTimer();
                            showMenu();
                        }
                        rlApprovalState.setVisibility(View.GONE);
                        break;
                    default:
                        flContainer.setVisibility(View.GONE);
                        break;
                }
                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }
                break;
            case EmailConstant.DELETED_BOX:
                //已删除
                mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_hzpg, null);
                mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                    //回复
                    replyEmail(EmailConstant.REPLY_EAMAIL);
                });

                mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                    //转发
                    replyEmail(EmailConstant.TRANSMIT_EMAIL);
                });
                mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> {
                    //评论
                    commentEmail();

                });
                mActionView.findViewById(R.id.rl_action4).setOnClickListener(v -> {
                    showMenu3();
                });
                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }

                break;
            case EmailConstant.TRASH_BOX:
                //垃圾箱
                mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_lpg, null);
                mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> ((EmailDetailActivity) getActivity()).thisIsNotAJunkEmail());
                mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> commentEmail());
                mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> showMenu3());
                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }
                break;
            case EmailConstant.NO_OPERATION:
            case EmailConstant.CAN_NOT_APPROVAL:
            case EmailConstant.CAN_APPROVAL:
                //从小助手进入
                mActionView = getActivity().getLayoutInflater().inflate(R.layout.email_action_p, null);
                mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> commentEmail());
                if (mActionView != null) {
                    flContainer.addView(mActionView);
                }
                break;
            default:
                flContainer.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 审批状态
     *
     * @param b
     */
    private void setApprovalState(boolean b) {

        if (b) {
            rlApprovalState.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.icon_approve_pass_tag);
        } else {
            rlApprovalState.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.icon_approve_reject_tag);
        }

    }

    /**
     * 隐藏审批状态
     */
    private void hideApprovalState() {
        rlApprovalState.setVisibility(View.VISIBLE);
    }

    /**
     * 定时显示
     */
    private void initTimer() {
        if ("1".equals(mEmailBean.getTimer_status())
                && !TextUtils.isEmpty(mEmailBean.getTimer_task_time())
            //&& System.currentTimeMillis() < TextUtil.parseLong(mEmailBean.getTimer_task_time())
                ) {
            rlTimer.setVisibility(View.VISIBLE);
            TextUtil.setText(tvTimer,
                    String.format(getActivity().getString(R.string.email_timed_hint),
                            DateTimeUtil.longToStr(TextUtil.parseLong(mEmailBean.getTimer_task_time()), "yyyy-MM-dd HH:mm")));
        } else {
            rlTimer.setVisibility(View.GONE);
        }

    }

    /**
     * 删除
     */
    private void showMenu1() {

        PopUtils.showBottomMenu(getActivity(), getRootView(), "更多操作", new String[]{"删除"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                ((EmailDetailActivity) getActivity()).deleteEmail();
                return false;
            }
        });

    }

    /**
     * 查看流程
     */
    private void showMenu2() {

        PopUtils.showBottomMenu(getActivity(), getRootView(), "更多操作", new String[]{"查看流程"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                if ("10".equals(mEmailBean.getApproval_status())) {
                    ToastUtils.showToast(getActivity(), "暂未开启流程");
                    return true;
                }
                Bundle bundle = new Bundle();
                bundle.putString(ApproveConstants.MODULE_BEAN, EmailConstant.MAIL_BOX_SCOPE);
                bundle.putString(ApproveConstants.APPROVAL_DATA_ID, emailId);
                bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, mEmailBean.getProcess_instance_id());
                UIRouter.getInstance().openUri(mContext, "DDComp://approve/task", bundle);
                //CommonUtil.startActivtiy(getActivity(), ApproveTaskActivity.class, bundle);

/*
                Map<String, String> map = new HashMap<>(4);
                map.put("moduleBean", EmailConstant.BEAN_NAME);
                map.put("dataId", emailId);
                map.put("type", MsgConstant.TYPE_EMAIL);
                map.put("processInstanceId", mEmailBean.getProcess_instance_id());
                new ApproveModel()
                        .queryApprovalData(getActivity(), map, new ProgressSubscriber<QueryApprovalDataResultBean>(mContext) {
                            @Override
                            public void onNext(QueryApprovalDataResultBean approvalBean) {
                                super.onNext(approvalBean);
                                QueryApprovalDataResultBean.ModuleInfoBean data = approvalBean.getData();

                                Bundle bundle = new Bundle();
                                bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, data.getProcess_definition_id());
                                bundle.putString(ApproveConstants.PROCESS_FIELD_V, data.getProcess_field_v());
                                bundle.putString(ApproveConstants.MODULE_BEAN, EmailConstant.BEAN_NAME);
                                bundle.putString(ApproveConstants.APPROVAL_DATA_ID, data.getApproval_data_id());
                                bundle.putString(ApproveConstants.TASK_KEY, data.getTask_key());
                                bundle.putString(ApproveConstants.TASK_NAME, data.getTask_name());
                                bundle.putString(ApproveConstants.TASK_ID, data.getTask_id());
                                bundle.putString(Constants.DATA_ID, data.getId());
                                //0我发起的审批
                                bundle.putInt(ApproveConstants.APPROVE_TYPE, 0);
                                CommonUtil.startActivtiy(getActivity(), ApproveDetailActivity.class, bundle);

                            }
                        });*/
                return true;
            }
        });

    }

    /**
     * 彻底删除
     */
    private void showMenu3() {

        PopUtils.showBottomMenu(getActivity(), getRootView(), "更多操作", new String[]{"彻底删除"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                ((EmailDetailActivity) getActivity()).clearMail();
                return false;
            }
        });

    }

    private void showMenu4() {

        PopUtils.showBottomMenu(getActivity(), getRootView(), "更多操作", new String[]{"查看流程", "彻底删除"}, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (p) {
                    case 0:
                        if (EmailConstant.APPROVAL_STATE_NO.equals(mEmailBean.getApproval_status())) {
                            ToastUtils.showToast(getActivity(), "暂未开启流程");
                            return true;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(ApproveConstants.MODULE_BEAN, EmailConstant.MAIL_BOX_SCOPE);
                        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, emailId);
                        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, mEmailBean.getProcess_instance_id());
                        UIRouter.getInstance().openUri(mContext, "DDComp://approve/task", bundle);
                        //CommonUtil.startActivtiy(getActivity(), ApproveTaskActivity.class, bundle);
                        break;
                    case 1:
                        ((EmailDetailActivity) getActivity()).clearMail();

                        break;
                }

                return false;
            }
        });

    }

    /**
     * 邮件评论
     */
    private void commentEmail() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, EmailConstant.BEAN_NAME);
        bundle.putString(Constants.DATA_ID, emailId);
        CommonUtil.startActivtiy(getActivity(), CommentActivity.class, bundle);
    }

    /**
     * 回复/转发
     *
     * @param tag
     */
    private void replyEmail(int tag) {
        if (mEmailBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        Member m = new Member();
        m.setEmail(mEmailBean.getFrom_recipient());
        ArrayList<Member> list = new ArrayList<>();
        list.add(m);
        ArrayList<AttachmentBean> attList = mEmailBean.getAttachments_name();
        if (attList != null && attList.size() > 0) {
            for (int i = 0; i < attList.size(); i++) {
                attList.get(i).setFromWhere(EmailConstant.FROM_UPLOAD);
            }
        }
        bundle.putSerializable(Constants.DATA_TAG1, list);
        bundle.putSerializable(Constants.DATA_TAG2, attList);
        bundle.putInt(Constants.DATA_TAG3, tag);
        bundle.putString(Constants.DATA_TAG4, labledEmailContent);
        bundle.putString(Constants.DATA_TAG5, emailId);
        bundle.putString(Constants.DATA_TAG6, mEmailBean.getSubject());
        bundle.putSerializable(Constants.DATA_TAG7, mEmailBean);
        CommonUtil.startActivtiyForResult(getActivity(), NewEmailActivity.class, Constants.REQUEST_CODE1, bundle);

    }

    /**
     * 再次编辑
     *
     * @param tag
     */
    private void editEmail(int tag) {
        if (mEmailBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG3, tag);
        bundle.putSerializable(Constants.DATA_TAG5, mEmailBean);
        CommonUtil.startActivtiyForResult(getActivity(), NewEmailActivity.class, Constants.REQUEST_CODE1, bundle);

    }

    /**
     * 隐藏显示底部按钮
     */
    public void hideAction() {
        if (flContainer.getVisibility() == View.GONE || flContainer.getVisibility() == View.INVISIBLE) {
            flContainer.setVisibility(View.VISIBLE);

        } else {
            flContainer.setVisibility(View.GONE);
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
        flContainer.setVisibility(View.VISIBLE);
        emailId = mEmailBean.getId();
        dataLoadFinish = true;
        labledEmailContent = emailDetailBean.getMail_content();
        if (webLoadFinish) {
            showWebData();
        }
        TextUtil.setText(tvTitle, emailDetailBean.getSubject());
        TextUtil.setText(tvSender, emailDetailBean.getFrom_recipient_name() +
                "<" + emailDetailBean.getFrom_recipient() + ">");
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
        if (TextUtils.isEmpty(emailDetailBean.getIp_address())) {
            llIp.setVisibility(View.GONE);
        } else {
            TextUtil.setText(tvIp, emailDetailBean.getIp_address());
        }
        try {
            TextUtil.setText(tvDate, DateTimeUtil.longToStr(TextUtil.parseLong(emailDetailBean.getCreate_time()), "yyyy-MM-dd HH:mm:ss (EEEE)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mItemAdapter.setNewData(emailDetailBean.getAttachments_name());


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
                if (TextUtil.isEmpty(list.get(i).getEmployee_name())) {
                    sb.append("<" + list.get(i).getMail_account() + ">");
                } else {
                    sb.append(list.get(i).getEmployee_name() + "<" + list.get(i).getMail_account() + ">");
                }

            } else {
                if (TextUtil.isEmpty(list.get(i).getEmployee_name())) {
                    sb.append(";<" + list.get(i).getMail_account() + ">");
                } else {
                    sb.append(";" + list.get(i).getEmployee_name() + "<" + list.get(i).getMail_account() + ">");
                }
            }
        }

        return sb.toString();
    }

    public void setListener(SimpleItemClickListener simpleItemClickListener) {
        rvAttachment.addOnItemTouchListener(simpleItemClickListener);

    }

    public void setEmailId(String emailId) {

        this.emailId = emailId;

    }


}
