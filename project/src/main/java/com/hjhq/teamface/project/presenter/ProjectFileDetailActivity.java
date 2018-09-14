package com.hjhq.teamface.project.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectFileListBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.ui.ProjectFileDetailDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.File;
import java.util.List;

/**
 * 项目任务
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/project_file_detail", desc = "项目文件详情")
public class ProjectFileDetailActivity extends ActivityPresenter<ProjectFileDetailDelegate, ProjectModel2> {
    private String mFileId;
    private String mProjectId;
    private String mDataId;
    private String mFolderType;
    //权限
    private String priviledgeIds;
    private MyReceiver mReceiver;
    private boolean hasInitReceiver = false;
    ProjectFileListBean.DataBean.DataListBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    private void initReceiver() {
        if (hasInitReceiver) {
            return;
        }
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
        registerReceiver(mReceiver, intentFilter);
        hasInitReceiver = true;
    }

    @Override
    public void init() {
        mFileId = getIntent().getStringExtra(Constants.DATA_TAG1);
        mProjectId = getIntent().getStringExtra(Constants.DATA_TAG2);
        mDataId = getIntent().getStringExtra(Constants.DATA_TAG3);
        mBean = (ProjectFileListBean.DataBean.DataListBean) getIntent().getSerializableExtra(Constants.DATA_TAG4);
        mFolderType = getIntent().getStringExtra(Constants.DATA_TAG5);
        priviledgeIds = getIntent().getStringExtra(Constants.DATA_TAG6);
        //0系统文件夹,1创建的文件夹
        controlDelete();
        if (mBean == null) {
            ToastUtils.showError(mContext, "文件错误");
            finish();
        }
        viewDelegate.setTitle(mBean.getFile_name());
        viewDelegate.setProjectId(mProjectId);
        viewDelegate.showData(mBean);
        if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 31)) {
            viewDelegate.showDeletBtn(true);
        } else {
            viewDelegate.showDeletBtn(false);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.btn_delete).setOnClickListener(v -> {
            //删除文件
            deleteFile();
        });
        viewDelegate.get(R.id.iv_delete).setOnClickListener(v -> {
            //删除文件
            deleteFile();
        });
        viewDelegate.get(R.id.iv_delete2).setOnClickListener(v -> {
            //删除文件
            deleteFile();
        });
        viewDelegate.get(R.id.btn_open).setOnClickListener(v -> {
            //打开文件
            File file = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD).getAbsolutePath(), mBean.getId() + mBean.getFile_name());
            if (file.exists()) {
                openFile(file);
            } else {
                ToastUtils.showToast(mContext, "本地文件不存在");
                viewDelegate.isDownloading(false);
            }

        });
        viewDelegate.get(R.id.btn_download).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getSize()));
        });
        viewDelegate.get(R.id.iv_download).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getSize()));
        });
        viewDelegate.get(R.id.iv_download2).setOnClickListener(v -> {
            //下载文件
            checkFileSizeAndDownload(TextUtil.parseLong(mBean.getSize()));
        });
        viewDelegate.get(R.id.iv_thumb).setOnClickListener(v -> {
            //全屏查看图片
        });
        viewDelegate.get(R.id.tv_view_online).setOnClickListener(v -> {
            //在线预览
        });
        viewDelegate.get(R.id.cancel_download).setOnClickListener(v -> {
            //取消下载
            FileTransmitUtils.cancelDownload(mBean.getId());
            viewDelegate.isDownloading(false);
        });

    }

    /**
     * 删除文件
     */
    private void deleteFile() {

        DialogUtils.getInstance().sureOrCancel(mContext, "", "确认删除文件吗?", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {
                model.deleteProjectFolder(mContext, mFileId, mProjectId, mBean.getFile_name(), new ProgressSubscriber<BaseBean>(mContext) {
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
        });

    }

    /**
     * 检查文件大小并下载
     *
     * @param size
     */
    public void checkFileSizeAndDownload(long size) {
        if (FileTransmitUtils.checkLimit(size)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续下载吗?",
                    viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            downloadFile();
                        }
                    });
        } else {
            downloadFile();
        }
    }

    private void downloadFile() {
        viewDelegate.isDownloading(true);
        controlDelete();
        String url = String.format(ProjectConstants.PROJECT_FILE_DOWNLOAD_URL, mBean.getId(), mProjectId);
        DownloadService.getInstance().downloadFileFromUrl(mBean.getId(), url, mBean.getFile_name());
    }

    private void openFile(File file) {
        if (!file.exists()) {
            ToastUtils.showToast(mContext, "本地文件不存在");
            viewDelegate.localFileNotExist();
            return;
        }
        FileUtils.browseDocument(this, viewDelegate.getRootView(), file.getName(), file.getAbsolutePath());
        LogUtil.e("文件名" + file + "绝对路径" + file.getAbsolutePath());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, Constants.REQUEST_CODE1, null);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDelegate.stopPlay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                if (data != null) {
                    List<Member> list = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (list != null && list.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < list.size(); i++) {
                            if (TextUtils.isEmpty(sb)) {
                                sb.append(list.get(i).getId() + "");
                            } else {
                                sb.append("," + list.get(i).getId() + "");
                            }

                        }
                        if (TextUtils.isEmpty(sb)) {
                            return;
                        } else {
                            model.shareProjectFile(mContext, mFileId, sb.toString(), new ProgressSubscriber<BaseBean>(mContext) {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }

                                @Override
                                public void onNext(BaseBean baseBean) {
                                    super.onNext(baseBean);
                                    ToastUtils.showSuccess(mContext, "分享成功");
                                }
                            });
                        }

                    }
                }
                break;
            case Constants.REQUEST_CODE2:

                break;
            case Constants.REQUEST_CODE3:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (mBean == null) {
                return;
            }

            if (FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION.equals(action)) {
                //文件下载进度
                ProgressBean bean = (ProgressBean) intent.getSerializableExtra(Constants.DATA_TAG1);
                if (bean.getBytesRead() <= 0 || bean.getContentLength() <= 0) {
                    return;
                }
                if (bean.getFileId().equals(mBean.getId())) {
                    if (bean.getStatus() == 8) {
                        viewDelegate.isDownloading(false);
                    } else if (bean.getStatus() == 16) {
                        viewDelegate.isDownloading(false);
                        ToastUtils.showError(mContext, "下载失败");
                    }
                    controlDelete();
                    if (bean.getBytesRead() <= 0 || bean.getContentLength() <= 0) {
                        return;
                    }
                    viewDelegate.updateProgress(
                            String.format(getString(R.string.project_progress_num), FormatFileSizeUtil.formatFileSize(bean.getBytesRead()) + "/" + FormatFileSizeUtil.formatFileSize(bean.getContentLength())),
                            bean.getBytesRead(), bean.getContentLength());
                }


            }


        }
    }

    /**
     * 任务文件不能删除
     */
    private void controlDelete() {
        if ("0".equals(mFolderType)) {
            viewDelegate.get(R.id.btn_delete).setVisibility(View.INVISIBLE);
            viewDelegate.get(R.id.iv_delete).setVisibility(View.INVISIBLE);
            viewDelegate.get(R.id.iv_delete2).setVisibility(View.INVISIBLE);
        }
    }


}
