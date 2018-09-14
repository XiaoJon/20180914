package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.activity.FullscreenViewImageActivity;
import com.hjhq.teamface.common.adapter.EmailAttachmentAdapter;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.bean.EmailDetailBean;
import com.hjhq.teamface.email.bean.NewEmailBean;
import com.hjhq.teamface.email.view.EmailDetailDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RouteNode(path = "/detail", desc = "邮件详情")
public class EmailDetailActivity extends ActivityPresenter<EmailDetailDelegate, EmailModel> {
    private String emailId;
    private int boxTag = 0;
    private EmailDetailBean mEmailDetailBean;
    private Calendar mCalendar;
    private String datetimeType;

    @Override
    public void init() {
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sendEmail();
        return super.onOptionsItemSelected(item);

    }

    protected void initData() {
        mCalendar = Calendar.getInstance();
        datetimeType = "yyyy-MM-dd HH:mm";
        initIntent();
        initListener();

    }

    private void initListener() {
        viewDelegate.setListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                List<AttachmentBean> data = ((EmailAttachmentAdapter) adapter).getData();
                if (FileTypeUtils.isImage(data.get(position).getFileType())) {
                    ArrayList<Photo> photoList = new ArrayList<Photo>();
                    Photo p = new Photo(data.get(position).getFileUrl());
                    photoList.add(p);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, photoList);
                     CommonUtil.startActivtiy(EmailDetailActivity.this, FullscreenViewImageActivity.class, bundle);
                } else {
                    ToastUtils.showToast(mContext, "暂不支持预览");
                }

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                List<AttachmentBean> data = ((EmailAttachmentAdapter) adapter).getData();
                if (FileTransmitUtils.checkLimit(TextUtil.parseLong(data.get(position).getFileSize()))) {
                    DialogUtils.getInstance().sureOrCancel(mContext, "",
                            "当前为移动网络且文件大小超过10M,继续下载吗?", viewDelegate.getRootView(),
                            new DialogUtils.OnClickSureListener() {
                                @Override
                                public void clickSure() {
                                    FileTransmitUtils.downloadFileFromUrl(data.get(position).getFileUrl(), data.get(position).getFileName());
                                }
                            });
                } else {
                    FileTransmitUtils.downloadFileFromUrl(data.get(position).getFileUrl(), data.get(position).getFileName());
                }

            }
        });
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            emailId = bundle.getString(Constants.DATA_TAG1);
            boxTag = bundle.getInt(Constants.DATA_TAG2);
            //  viewDelegate.initAction(boxTag);
            viewDelegate.setEmailId(emailId);
            getNetData();
        }

    }

    private void getNetData() {
        model.getEmailDetail(EmailDetailActivity.this, emailId, "1", new ProgressSubscriber<EmailDetailBean>(EmailDetailActivity.this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(EmailDetailBean emailDetailBean) {
                super.onNext(emailDetailBean);
                mEmailDetailBean = emailDetailBean;
                // markEmailReaded(emailDetailBean.getData());
                showData(emailDetailBean.getData());
            }
        });

    }

    private void showData(EmailBean emailDetailBean) {
        viewDelegate.showData(emailDetailBean);
        viewDelegate.initAction(boxTag);


    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 删除邮件
     */
    public void deleteEmail() {
        model.deleteDraft(EmailDetailActivity.this, emailId, boxTag + "", new ProgressSubscriber<BaseBean>(EmailDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtils.showError(mContext, "删除失败");
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

    /**
     * 彻底删除
     */
    public void clearMail() {
        model.clearMail(EmailDetailActivity.this, emailId, new ProgressSubscriber<BaseBean>(EmailDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtils.showError(mContext, "删除失败");
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

    /**
     * 标记邮件已读
     */
    private void markEmailReaded(EmailBean bean) {
        if (EmailConstant.EMAIL_READ_TAG.equals(bean.getRead_status())) {
            return;
        }
        model.markMailReadOrUnread(this,
                bean.getId(),
                EmailConstant.EMAIL_READ_TAG, boxTag + "", new ProgressSubscriber<BaseBean>(this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                    }
                });


    }

    /**
     * 标记不是垃圾邮件
     */
    public void thisIsNotAJunkEmail() {
        model.markNotTrash(mContext, emailId, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                boxTag = EmailConstant.RECEVER_BOX;
                viewDelegate.initAction(boxTag);
                viewDelegate.setEmailId(emailId);
                setResult(RESULT_OK);
                getNetData();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtils.showError(mContext, "执行失败");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                getNetData();
                break;
            case Constants.REQUEST_CODE2:
                if (data != null) {
                    mCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                    if (mCalendar != null) {
                        if (mCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                            ToastUtils.showToast(mContext, "请选择未来的时间");
                            return;
                        }
                        if (mEmailDetailBean != null && mEmailDetailBean.getData() != null) {
                            EmailBean emailBean = mEmailDetailBean.getData();
                            NewEmailBean bean = new NewEmailBean();
                            bean.setTimer_task_time(mCalendar.getTimeInMillis() + "");
                            bean.setPersonnel_ccTo(emailBean.getPersonnel_ccTo());
                            bean.setId(emailBean.getId());
                            bean.setSubject(emailBean.getSubject());
                            bean.setAccount_id(TextUtil.parseLong(emailBean.getAccount_id()));
                            bean.setFrom_recipient(emailBean.getFrom_recipient());
                            bean.setMail_content(emailBean.getMail_content());
                            bean.setPersonnel_approverBy(emailBean.getPersonnel_approverBy());
                            bean.setPersonnel_ccTo(emailBean.getPersonnel_ccTo());
                            bean.setCc_setting(0);
                            bean.setBcc_setting(0);
                            bean.setSingle_show(0);
                            bean.setIs_emergent(0);
                            bean.setIs_notification(0);
                            bean.setIs_plain(0);
                            bean.setIs_track(0);
                            bean.setIs_encryption(0);
                            bean.setIs_signature(0);
                           // bean.setSignature_id(0);
                            bean.setMail_source(0);
                            bean.setAttachments_name(emailBean.getAttachments_name());
                            bean.setCc_recipients(emailBean.getCc_recipients());
                            bean.setBcc_recipients(emailBean.getBcc_recipients());
                            bean.setTo_recipients(emailBean.getTo_recipients());


                            model.editDraft(EmailDetailActivity.this, bean, new ProgressSubscriber<BaseBean>(mContext) {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }

                                @Override
                                public void onNext(BaseBean baseBean) {
                                    super.onNext(baseBean);
                                    getNetData();
                                }
                            });
                        }

                    }
                }
                break;
            default:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 定时
     */
    public void changeSendTime() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, mCalendar);
        bundle.putString(DateTimeSelectPresenter.FORMAT, datetimeType);
        CommonUtil.startActivtiyForResult(EmailDetailActivity.this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE2, bundle);
    }

    /**
     * 手动发送邮件
     */
    public void sendEmail() {
        model.manualSend(EmailDetailActivity.this, emailId, new ProgressSubscriber<BaseBean>(EmailDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
