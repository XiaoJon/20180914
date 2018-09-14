package com.hjhq.teamface.filelib.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.FileDownloadLogAdapter;
import com.hjhq.teamface.filelib.bean.FileDownloadLogBean;
import com.hjhq.teamface.filelib.view.FileDownloadHistoryDelegate;

import java.util.ArrayList;
import java.util.List;

public class FileDownloadHistoryActivity extends ActivityPresenter<FileDownloadHistoryDelegate, FilelibModel> {

    private RecyclerView logList;
    private FileDownloadLogAdapter mAdapter;
    private List<FileDownloadLogBean.DataBean> list = new ArrayList<>();
    private Bundle mBundle;
    private String fileId;
    private EmptyView mEmptyView;

    @Override
    public void init() {
        initView();
        initData();
    }


    protected void initView() {

        viewDelegate.setTitle(getString(R.string.filelib_download_log));
        logList = (RecyclerView) findViewById(R.id.rv_list);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logList.addItemDecoration(new MyLinearDeviderDecoration(this));
        mEmptyView = new EmptyView(mContext);
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        mAdapter = new FileDownloadLogAdapter(list);
        mAdapter.setEmptyView(mEmptyView);
        logList.setAdapter(mAdapter);


        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            fileId = mBundle.getString(FileConstants.FILE_ID);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
    }

    protected void initData() {
        getNetData();
    }

    private void getNetData() {

        model.queryDownLoadList(FileDownloadHistoryActivity.this, fileId,
                new ProgressSubscriber<FileDownloadLogBean>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(FileDownloadLogBean baseBean) {
                        super.onNext(baseBean);
                        list.clear();
                        list.addAll(baseBean.getData());
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }


}
