package com.hjhq.teamface.project.presenter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectFileListAdapter;
import com.hjhq.teamface.project.adapter.ProjectShareListAdapter;
import com.hjhq.teamface.project.bean.ProjectFileListBean;
import com.hjhq.teamface.project.bean.ProjectShareListBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.presenter.add.ProjectAddShareActivity;
import com.hjhq.teamface.project.ui.ProjectSearchDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/project_search", desc = "项目搜索界面(或为通用)")
public class ProjectSearchActivity extends ActivityPresenter<ProjectSearchDelegate, ProjectModel2> {
    private BaseQuickAdapter mAdapter;
    private String keyword;
    private int type;
    private String projectId;
    private String folderType;
    private String folderId;
    private RecyclerView mRecyclerView;

    @Override
    public void init() {
        type = getIntent().getIntExtra(Constants.DATA_TAG1, -1);
        projectId = getIntent().getStringExtra(Constants.DATA_TAG2);
        folderType = getIntent().getStringExtra(Constants.DATA_TAG3);
        folderId = getIntent().getStringExtra(Constants.DATA_TAG4);
        mRecyclerView = viewDelegate.get(R.id.search_result_recycler_view);
        if (-1 == type) {
            finish();
        }

        switch (type) {
            case ProjectConstants.SEARCH_PROJECT_SHARE:
                //分享
                mAdapter = new ProjectShareListAdapter(new ArrayList<>());
                break;
            case ProjectConstants.SEARCH_PROJECT_FILE:
                //文件
                mAdapter = new ProjectFileListAdapter(projectId, "", new ArrayList<>());
                break;
            case ProjectConstants.SEARCH_PROJECT_TASK:
                //任务

                break;
            case ProjectConstants.SEARCH_PROJECT_SELF:
                //项目

                break;
        }

        viewDelegate.setAdapter(mAdapter);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchData(keyword);
            }

            @Override
            public void getText(String text) {
                keyword = text;
            }
        });
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (type) {
                    case ProjectConstants.SEARCH_PROJECT_SHARE:
                        //分享
                        viewShareDetail(position);

                        break;
                    case ProjectConstants.SEARCH_PROJECT_FILE:
                        //文件详情
                        viewFileDetail(position);
                        break;
                    case ProjectConstants.SEARCH_PROJECT_TASK:
                        //任务

                        break;
                    case ProjectConstants.SEARCH_PROJECT_SELF:
                        //项目

                        break;
                }

                super.onItemClick(adapter, view, position);
            }
        });
    }

    /**
     * 查看文件详情
     *
     * @param position
     */
    private void viewFileDetail(int position) {
        List<ProjectFileListBean.DataBean.DataListBean> data = mAdapter.getData();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, data.get(position).getId());
        bundle.putString(Constants.DATA_TAG2, projectId);
        bundle.putString(Constants.DATA_TAG3, data.get(position).getData_id());
        bundle.putSerializable(Constants.DATA_TAG4, data.get(position));
        bundle.putString(Constants.DATA_TAG5, folderType);
        CommonUtil.startActivtiyForResult(mContext, ProjectFileDetailActivity.class, Constants.REQUEST_CODE4, bundle);
    }

    /**
     * 分享详情
     *
     * @param position
     */
    private void viewShareDetail(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, ((ProjectShareListAdapter) mAdapter).getData().get(position).getId());
        bundle.putInt(Constants.DATA_TAG2, ProjectConstants.TYPE_SHARE_DETAIL);
        bundle.putString(Constants.DATA_TAG3, projectId);
        CommonUtil.startActivtiyForResult(mContext, ProjectAddShareActivity.class, Constants.REQUEST_CODE9, bundle);
    }

    public void searchData(final String word) {

        if (TextUtils.isEmpty(word)) {
            return;
        }
        switch (type) {
            case ProjectConstants.SEARCH_PROJECT_SHARE:
                //搜索分享
                searchShare(word);
                break;
            case ProjectConstants.SEARCH_PROJECT_FILE:
                //文件
                searchFile(word);
                break;
            case ProjectConstants.SEARCH_PROJECT_TASK:
                //任务

                break;
            case ProjectConstants.SEARCH_PROJECT_SELF:
                //项目

                break;
        }
    }

    /**
     * 搜索文件
     *
     * @param word
     */
    private void searchFile(String word) {
        model.searchProjectFile(mContext, folderId, folderType, word, new ProgressSubscriber<ProjectFileListBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ProjectFileListBean projectFileListBean) {
                super.onNext(projectFileListBean);
                mAdapter.getData().clear();
                mAdapter.getData().addAll(projectFileListBean.getData().getDataList());
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 搜索分享
     *
     * @param word
     */
    private void searchShare(final String word) {
       /* model.searchProjectShareList(mContext, TextUtil.parseLong(projectId), word, 0, new ProgressSubscriber<ProjectShareListBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ProjectShareListBean baseBean) {
                super.onNext(baseBean);
                if (baseBean.getData() != null && baseBean.getData().getDataList() != null) {
                    if (TextUtils.isEmpty(keyword) || !word.equals(keyword)) {
                        mAdapter.getData().clear();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ((ProjectShareListAdapter) mAdapter).setKeyword(keyword);
                        mAdapter.getData().clear();
                        mAdapter.getData().addAll(baseBean.getData().getDataList());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mAdapter.getData().clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });*/
        model.getProjectShareList(mContext, 1, 100, word,  0,TextUtil.parseLong(projectId), new ProgressSubscriber<ProjectShareListBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ProjectShareListBean baseBean) {
                super.onNext(baseBean);
                if (baseBean.getData() != null && baseBean.getData().getDataList() != null) {
                    if (TextUtils.isEmpty(keyword) || !word.equals(keyword)) {
                        mAdapter.getData().clear();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ((ProjectShareListAdapter) mAdapter).setKeyword(keyword);
                        mAdapter.getData().clear();
                        mAdapter.getData().addAll(baseBean.getData().getDataList());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mAdapter.getData().clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}
