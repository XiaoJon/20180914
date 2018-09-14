package com.hjhq.teamface.project.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.adapter.ProjectFolderListAdapter;
import com.hjhq.teamface.project.bean.ProjectFolderListBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.ui.ProjectFolderListFragmentDelegate;

import java.util.ArrayList;
import java.util.List;


/**
 * 任务列表
 * Created by Administrator on 2018/4/10.
 */
public class ProjectFolderListFragment extends FragmentPresenter<ProjectFolderListFragmentDelegate, ProjectModel2> {
    private int index;
    BaseQuickAdapter adapter;
    private long projectId;
    /**
     * 总页数
     */
    private int totalPages = 1;
    private int currentPageNo = 1;
    private int state = Constants.NORMAL_STATE;

    List<ProjectFolderListBean.DataBean.DataListBean> mList = new ArrayList<>();

    static ProjectFolderListFragment newInstance(int index) {
        ProjectFolderListFragment myFragment = new ProjectFolderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    public ProjectFolderListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ProjectFolderListFragment(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            index = arguments.getInt(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {

        adapter = new ProjectFolderListAdapter(mList);
        viewDelegate.setAdapter(adapter);

        getNetData();
    }

    /**
     * 获取文件夹列表
     */
    public void getNetData() {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.getProjectFolderList(((ActivityPresenter) getActivity()), projectId, Constants.PAGESIZE, pageNo,
                new ProgressSubscriber<ProjectFolderListBean>(getActivity(), false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (state == Constants.LOAD_STATE) {
                            adapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onNext(ProjectFolderListBean baseBean) {
                        super.onNext(baseBean);
                        showDataResult(baseBean);
                    }
                });
    }

    private void showDataResult(ProjectFolderListBean projectListBean) {
        List<ProjectFolderListBean.DataBean.DataListBean> dataList = projectListBean.getData().getDataList();

        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), dataList);
                break;
            case Constants.LOAD_STATE:
                adapter.addData(dataList);
                adapter.loadMoreComplete();
                break;
            default:
                break;
        }
        PageInfo pageInfo = projectListBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mList.get(position).getData_id());
                bundle.putBoolean(Constants.DATA_TAG2, true);
                bundle.putString(Constants.DATA_TAG3, mList.get(position).getLibrary_type());
                bundle.putString(Constants.DATA_TAG4, mList.get(position).getFile_name());
                bundle.putString(Constants.DATA_TAG5, projectId + "");
                bundle.putSerializable(Constants.DATA_TAG6, new ArrayList<>());
                bundle.putString(Constants.DATA_TAG7, ((ProjectDetailActivity) getActivity()).priviledgeIds);
                CommonUtil.startActivtiyForResult(getActivity(), ProjectFolderListActivity.class, Constants.REQUEST_CODE2, bundle);
            }
        });
        //刷新
        viewDelegate.mRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        adapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                adapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getNetData();
        }, viewDelegate.mRecyclerView);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        state = Constants.REFRESH_STATE;
        getNetData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:

                    break;
                case Constants.REQUEST_CODE2:
                    getNetData();
                    break;
                case Constants.REQUEST_CODE3:

                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
