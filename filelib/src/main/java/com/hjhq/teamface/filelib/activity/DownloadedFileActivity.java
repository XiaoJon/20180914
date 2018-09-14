package com.hjhq.teamface.filelib.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.DownloadFileListAdapter;
import com.hjhq.teamface.filelib.view.DownloadedFileDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadedFileActivity extends ActivityPresenter<DownloadedFileDelegate, FilelibModel> {
    RecyclerView rvFileList;
    List<File> mFileList = new ArrayList<>();
    DownloadFileListAdapter mAdapter;

    @Override
    public void init() {
        initView();
        initData();
        setListener();
    }


    protected void initView() {
        rvFileList = (RecyclerView) findViewById(R.id.rv_file_list);
        rvFileList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DownloadFileListAdapter(mFileList);
        rvFileList.setAdapter(mAdapter);


    }

    protected void initData() {
        File file = JYFileHelper.getFileDir(this, Constants.PATH_DOWNLOAD);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                mFileList.add(files[i]);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void setListener() {
        rvFileList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                openFile(mFileList.get(position));
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                mFileList.get(position).delete();
                mFileList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void openFile(File file) {
        if (!file.exists()) {
            ToastUtils.showToast(mContext, getString(R.string.filelib_file_not_found));

        }
        FileUtils.browseDocument(this, file.getName(), file.getAbsolutePath());
        LogUtil.e("文件名" + file + "绝对路径" + file.getAbsolutePath());
    }

}
