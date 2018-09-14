package com.hjhq.teamface.filelib.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.FileVersionLogAdapter;
import com.hjhq.teamface.filelib.bean.FileVersionLogBean;
import com.hjhq.teamface.filelib.view.FileVerDelegate;

import java.util.ArrayList;
import java.util.List;

public class FileHistoryVersionActivity extends ActivityPresenter<FileVerDelegate, FilelibModel> {
    RecyclerView logList;
    FileVersionLogAdapter mAdapter;
    List<FileVersionLogBean.DataBean> list = new ArrayList<>();

    Bundle mBundle;
    private String fileId;
    private boolean isCreator = false;


    @Override
    public void init() {
        initView();
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            outState = mBundle;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState;
        }

    }

    protected void initView() {
        viewDelegate.setTitle(getString(R.string.filelib_version_log));
        if (mBundle == null) {
            mBundle = getIntent().getExtras();
        }
        if (mBundle != null) {
            fileId = mBundle.getString(FileConstants.FILE_ID);
            isCreator = mBundle.getBoolean(Constants.DATA_TAG1, false);
        }

        LinearLayout llTitle = (LinearLayout) findViewById(R.id.rl_title);
        llTitle.setVisibility(View.GONE);
        logList = (RecyclerView) findViewById(R.id.rv_list);
        logList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileVersionLogAdapter(list);
        logList.setAdapter(mAdapter);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        logList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isCreator) {
                    showMenu(position);
                } /*else {
                    ToastUtils.showError(mContext, "无权下载");
                }*/

            }
        });
    }

    private void showMenu(int position) {
        String[] menu = {"下载"};
        PopUtils.showBottomMenu(FileHistoryVersionActivity.this, logList.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int pos) {
                        checkLimitAndDownload(position);
                        return true;
                    }
                });

    }

    /**
     * 非WiFi网络环境时下载文件大小限制
     *
     * @param position
     */
    private void checkLimitAndDownload(int position) {
        if (FileTransmitUtils.checkLimit(TextUtil.parseLong(list.get(position).getSize()))) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续下载吗?", logList,
                    new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            ToastUtils.showToast(mContext, "正在下载文件: " + list.get(position).getName());
                            DownloadService.getInstance().downloadHistoryFile(list.get(position).getId(), list.get(position).getName());
                        }
                    });
        } else {
            ToastUtils.showToast(mContext, "正在下载文件: " + list.get(position).getName());
            DownloadService.getInstance().downloadHistoryFile(list.get(position).getId(), list.get(position).getName());
        }
    }


    protected void initData() {
        model.queryVersionList(FileHistoryVersionActivity.this, fileId,
                new ProgressSubscriber<FileVersionLogBean>(FileHistoryVersionActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);


                    }

                    @Override
                    public void onNext(FileVersionLogBean baseBean) {
                        super.onNext(baseBean);

                        list.clear();
                        list.addAll(baseBean.getData());
                        mAdapter.notifyDataSetChanged();

                    }
                });

    }

}
