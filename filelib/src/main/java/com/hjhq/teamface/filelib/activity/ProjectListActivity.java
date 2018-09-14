package com.hjhq.teamface.filelib.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.ProjectFolderAdapter;
import com.hjhq.teamface.filelib.bean.ProjectListBean;
import com.hjhq.teamface.filelib.view.ProjectListDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

@RouteNode(path = "/netdisk_project", desc = "文件库中的项目文件夹")
public class ProjectListActivity extends ActivityPresenter<ProjectListDelegate, FilelibModel> {
    SwipeRefreshLayout mRefreshLayout;
    private RecyclerView rvList;
    private ArrayList<FolderNaviData> naviData = new ArrayList<>();
    private ArrayList<ProjectListBean.DataBean.DataListBean> dataList = new ArrayList<>();
    private ProjectFolderAdapter mAdapter;


    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = 0;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;

    @Override
    public void init() {
        initView();
        initData();

    }

    protected void initView() {

        viewDelegate.setTitle(getString(R.string.filelib_project_file));
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProjectFolderAdapter(dataList);
        rvList.setAdapter(mAdapter);
        EmptyView emptyView = new EmptyView(mContext);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            initData();
        }, rvList);

        rvList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, dataList.get(position).getData_id());
                bundle.putBoolean(Constants.DATA_TAG2, true);
                bundle.putString(Constants.DATA_TAG3, dataList.get(position).getLibrary_type());
                bundle.putString(Constants.DATA_TAG4, dataList.get(position).getFile_name());
                bundle.putString(Constants.DATA_TAG5, dataList.get(position).getId());
                ArrayList<FolderNaviData> list = new ArrayList<FolderNaviData>();
                addRootNaviData(list);
                FolderNaviData data = new FolderNaviData();
                data.setFolderId(dataList.get(position).getId());
                data.setFolderLevel(0);
                data.setFloderType(TextUtil.parseInt(dataList.get(position).getLibrary_type()));
                data.setFolderName(dataList.get(position).getFile_name());
                list.add(data);
                bundle.putSerializable(Constants.DATA_TAG6, list);
                UIRouter.getInstance().openUri(mContext, "DDComp://project/project_group_folder", bundle);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 1) {
                    CommonUtil.startActivtiy(ProjectListActivity.this, DownloadedFileActivity.class);
                }
            }
        });
    }

    private void addRootNaviData(ArrayList<FolderNaviData> list) {
        FolderNaviData data = new FolderNaviData();
        data.setFolderId("");
        data.setFolderLevel(0);
        data.setFloderType(0);
        data.setFolderName(getString(R.string.filelib_project_file));
        list.add(data);
    }

    protected void initData() {
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.queryProjectLibraryList(ProjectListActivity.this, pageSize,
                currentPageNo, new ProgressSubscriber<ProjectListBean>(ProjectListActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ProjectListBean rootFolderResBean) {
                        super.onNext(rootFolderResBean);
                        showData(rootFolderResBean);
                    }
                });
    }

    private void showData(ProjectListBean bean) {
        List<ProjectListBean.DataBean.DataListBean> newDataList = bean.getData().getDataList();
        switch (state) {
            case Constants.REFRESH_STATE:
            case Constants.NORMAL_STATE:
                dataList.clear();
                mAdapter.setEnableLoadMore(true);
                break;
            case Constants.LOAD_STATE:
                mAdapter.loadMoreEnd();
                break;
            default:
                break;
        }
        if (newDataList != null && newDataList.size() > 0) {
            dataList.addAll(newDataList);
        }
        mAdapter.notifyDataSetChanged();
        PageInfo pageInfo = bean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();

    }

    public void refreshData() {
        state = Constants.REFRESH_STATE;
        initData();
    }

   /* @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        FolderNaviData fnd = new FolderNaviData();
        switch (view.getId()) {
            case R.id.rl1:
                //公司文件夹
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, FileMainActivity.TAG1);
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, getString(R.string.company_file));
                bundle.putString(FileConstants.FOLDER_URL, "");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                fnd.setFolderLevel(0);
                fnd.setFloderType(FileMainActivity.TAG1);
                fnd.setFolderName(getString(R.string.company_file));
                fnd.setFolderId("1");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(ProjectListActivity.this, FileMainActivity.class, bundle);

                break;
            case R.id.rl2:
                //个人文件夹
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, FileMainActivity.TAG2);
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, getString(R.string.app_file));
                bundle.putString(FileConstants.FOLDER_URL, "");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                fnd.setFolderLevel(0);
                fnd.setFloderType(FileMainActivity.TAG2);
                fnd.setFolderName(getString(R.string.app_file));
                fnd.setFolderId("1");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(ProjectListActivity.this, FileMainActivity.class, bundle);
                break;
            case R.id.rl3:
                //应用文件夹
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, FileMainActivity.TAG3);
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, getString(R.string.personal_file));
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                bundle.putString(FileConstants.FOLDER_URL, "");
                fnd.setFolderLevel(0);
                fnd.setFloderType(FileMainActivity.TAG3);
                fnd.setFolderName(getString(R.string.personal_file));
                fnd.setFolderId("1");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(ProjectListActivity.this, FileMainActivity.class, bundle);
                break;
            case R.id.rl4:
                //我共享的
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, FileMainActivity.TAG4);
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, getString(R.string.my_share_file));
                bundle.putString(FileConstants.FOLDER_URL, "");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                fnd.setFolderLevel(0);
                fnd.setFloderType(FileMainActivity.TAG4);
                fnd.setFolderName(getString(R.string.my_share_file));
                fnd.setFolderId("1");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(ProjectListActivity.this, FileMainActivity.class, bundle);
                break;
            case R.id.rl5:
                //共享给我
                bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                bundle.putInt(FileConstants.FOLDER_TYPE, FileMainActivity.TAG5);
                bundle.putString(FileConstants.FOLDER_ID, "");
                bundle.putString(FileConstants.FOLDER_NAME, getString(R.string.share_to_me_file));
                bundle.putString(FileConstants.FOLDER_URL, "");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                fnd.setFolderLevel(0);
                fnd.setFloderType(FileMainActivity.TAG5);
                fnd.setFolderName(getString(R.string.share_to_me_file));
                fnd.setFolderId("1");
                naviData.clear();
                naviData.add(fnd);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, naviData);
                CommonUtil.startActivtiy(ProjectListActivity.this, FileMainActivity.class, bundle);

                break;

            default:

                break;


        }

    }*/

    private String getRootFolderName(int id) {
        switch (id) {
            case 1:
                return "公司文件";
            case 2:
                return "应用文件";
            case 3:
                return "个人文件";
            case 4:
                return "我的共享";
            case 5:
                return "与我共享";
            default:
                break;

        }
        return "";

    }


}
