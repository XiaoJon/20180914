package com.hjhq.teamface.filelib.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.RootFolderAdapter;
import com.hjhq.teamface.filelib.bean.RootFolderResBean;
import com.hjhq.teamface.filelib.view.FilelibDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;

@RouteNode(path = "/netdisk", desc = "文件库")
public class FileLibActivity extends ActivityPresenter<FilelibDelegate, FilelibModel> {
    private RecyclerView rvList;
    private ArrayList<FolderNaviData> naviData = new ArrayList<>();
    private ArrayList<RootFolderResBean.DataBean> dataList = new ArrayList<>();
    private RootFolderAdapter mAdapter;

    @Override
    public void init() {
        initView();
        initData();
    }

    protected void initView() {
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RootFolderAdapter(dataList);
        rvList.setAdapter(mAdapter);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        rvList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("6".equals(dataList.get(position).getId())) {
                    CommonUtil.startActivtiy(mContext, ProjectListActivity.class);
                    return;
                }
                Bundle bundle = new Bundle();
                FolderNaviData fnd = new FolderNaviData();
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, Integer.parseInt(dataList.get(position).getId()));
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, dataList.get(position).getName());
                bundle.putString(FileConstants.FOLDER_URL, "");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                fnd.setFolderLevel(0);
                fnd.setFloderType(TextUtil.parseInt(dataList.get(position).getId()));
                fnd.setFolderName(dataList.get(position).getName());
                fnd.setFolderId("");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(FileLibActivity.this, FileMainActivity.class, bundle);

            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 1 && Constants.IS_DEBUG) {
                    //CommonUtil.startActivtiy(NetDiskActivity.this, FileOnlinePreviewActivity.class);
                    CommonUtil.startActivtiy(FileLibActivity.this, DownloadedFileActivity.class);
                    overridePendingTransition(0, 0);
                }
                if (position == 2 && Constants.IS_DEBUG) {
                    //CommonUtil.startActivtiy(NetDiskActivity.this, FileOnlinePreviewActivity.class);
                    CommonUtil.startActivtiy(FileLibActivity.this, ProjectListActivity.class);
                    overridePendingTransition(0, 0);
                }
            }
        });
    }


    protected void initData() {
        model.queryfileCatalog(FileLibActivity.this, new ProgressSubscriber<RootFolderResBean>(FileLibActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(RootFolderResBean rootFolderResBean) {
                super.onNext(rootFolderResBean);
                dataList.clear();
                dataList.addAll(rootFolderResBean.getData());
                mAdapter.notifyDataSetChanged();

            }
        });
    }

}
