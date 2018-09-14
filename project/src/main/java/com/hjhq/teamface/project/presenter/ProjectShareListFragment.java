package com.hjhq.teamface.project.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectShareListAdapter;
import com.hjhq.teamface.project.bean.ProjectShareListBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.presenter.add.ProjectAddShareActivity;
import com.hjhq.teamface.project.ui.ProjectFolderListFragmentDelegate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 任务列表
 * Created by Administrator on 2018/4/10.
 */
public class ProjectShareListFragment extends FragmentPresenter<ProjectFolderListFragmentDelegate, ProjectModel2> {
    private ProjectShareListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ProjectShareListBean.DataBean.DataListBean> dataList = new ArrayList<>();
    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = 0;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;
    private int type = 0;

    //项目id
    private long mProjectId;

    static ProjectShareListFragment newInstance(long projectId) {
        ProjectShareListFragment myFragment = new ProjectShareListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.DATA_TAG1, projectId);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mProjectId = arguments.getLong(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {
        mSwipeRefreshLayout = viewDelegate.get(R.id.swipe_refresh_layout);
        mRecyclerView = viewDelegate.get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectShareListAdapter(dataList);
        mRecyclerView.setAdapter(mAdapter);
        getNetData();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean bean) {
        if (!TextUtils.isEmpty(bean.getTag()) && ProjectConstants.PUT_ON_TOP_FLAG.equals(bean.getTag())) {
            int position = bean.getCode();
            putOnTop(position);
        }
    }

    /**
     * 置顶
     */
    private void putOnTop(int position) {
        String status = "1".equals(mAdapter.getData().get(position).getShare_top_status()) ? "0" : "1";
        model.editShareStickStatus(((ActivityPresenter) getActivity()), mAdapter.getData().get(position).getId(), status,
                new ProgressSubscriber<BaseBean>(getContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        refreshData();

                    }
                });
    }

    public void refreshData() {
        state = Constants.REFRESH_STATE;
        getNetData();
    }

    private void getNetData() {
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.getProjectShareList(((ProjectDetailActivity) getActivity()),
                currentPageNo, pageSize, "", type, mProjectId, new ProgressSubscriber<ProjectShareListBean>(getActivity()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ProjectShareListBean projectShareListBean) {
                        super.onNext(projectShareListBean);
                        showData(projectShareListBean);
                    }
                });

    }

    private void showData(ProjectShareListBean projectShareListBean) {

        List<ProjectShareListBean.DataBean.DataListBean> newDataList = projectShareListBean.getData().getDataList();
        List<ProjectShareListBean.DataBean.DataListBean> oldDataList = mAdapter.getData();
        switch (state) {
            case Constants.REFRESH_STATE:
                oldDataList.clear();
                mAdapter.setEnableLoadMore(true);
                break;
            case Constants.LOAD_STATE:
                mAdapter.loadMoreEnd();
                break;
            default:
                break;
        }
        if (newDataList != null && newDataList.size() > 0) {
            oldDataList.addAll(newDataList);
            mAdapter.setNewData(oldDataList);

        } else {
            mAdapter.setNewData(oldDataList);

        }
        List<ProjectShareListBean.DataBean.DataListBean> data = mAdapter.getData();
        // Collections.reverse(data);
        mAdapter.notifyDataSetChanged();
        PageInfo pageInfo = projectShareListBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();
        if (mAdapter.getData().size() == totalNum) {
            mAdapter.loadMoreComplete();
        }
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.iv_check) {
                    view.setSelected(!view.isSelected());
                }
            }

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mAdapter.getData().get(position).getId());
                bundle.putInt(Constants.DATA_TAG2, ProjectConstants.TYPE_SHARE_DETAIL);
                bundle.putString(Constants.DATA_TAG3, getProjectId());
                CommonUtil.startActivtiyForResult(getActivity(), ProjectAddShareActivity.class, Constants.REQUEST_CODE9, bundle);


            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                refreshData();
            }
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mAdapter.loadMoreComplete();
                //mAdapter.setEnableLoadMore(false);
                mAdapter.loadMoreEnd(false);
                return;
            }
            state = Constants.LOAD_STATE;
            getNetData();
        }, mRecyclerView);
        viewDelegate.get(R.id.search_layout).setVisibility(View.VISIBLE);
        viewDelegate.get(R.id.search_rl).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, ProjectConstants.SEARCH_PROJECT_SHARE);
            bundle.putString(Constants.DATA_TAG2, getProjectId());
            CommonUtil.startActivtiyForResult(getActivity(), ProjectSearchActivity.class, Constants.REQUEST_CODE1, bundle);
        });

    }

    private String getProjectId() {
        return ((ProjectDetailActivity) getActivity()).projectId + "";
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE9) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
