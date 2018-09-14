package com.hjhq.teamface.project.presenter;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectAdapter;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ListDelegate;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * 项目列表
 *
 * @author Administrator
 * @date 2018/4/10
 */
public class ProjectListFragment extends FragmentPresenter<ListDelegate, ProjectModel> {
    ProjectAdapter projectAdapter;
    /**
     * 0全部  1我负责 2我参与 3我创建 4进行中 5已完成
     */
    private int type;
    private RxAppCompatActivity mActivity;
    /**
     * 总页数
     */
    private int totalPages = 1;
    private int currentPageNo = 1;
    private int state = Constants.NORMAL_STATE;
    private String keyword;


    public static ProjectListFragment newInstance(int index) {
        ProjectListFragment myFragment = new ProjectListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {
        mActivity = (RxAppCompatActivity) getActivity();
        projectAdapter = new ProjectAdapter(null);
        viewDelegate.setAdapter(projectAdapter);
        loadData();
    }


    /**
     * 加载数据
     */
    private void loadData() {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.queryAllList(mActivity, type, pageNo, Constants.PAGESIZE, keyword, new ProgressSubscriber<ProjectListBean>(mActivity, false) {
            @Override
            public void onNext(ProjectListBean baseBean) {
                super.onNext(baseBean);
                showDataResult(baseBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (state == Constants.LOAD_STATE) {
                    projectAdapter.loadMoreFail();
                }
            }
        });
    }

    /**
     * 显示数据
     */
    private void showDataResult(ProjectListBean projectListBean) {
        ProjectListBean.DataBean data = projectListBean.getData();

        List<ProjectListBean.DataBean.DataListBean> dataList = data.getDataList();
        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                CollectionUtils.notifyDataSetChanged(projectAdapter, projectAdapter.getData(), dataList);
                break;
            case Constants.LOAD_STATE:
                projectAdapter.addData(dataList);
                projectAdapter.loadMoreComplete();
                break;
            default:
                break;
        }
        PageInfo pageInfo = data.getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
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
                ProjectListBean.DataBean.DataListBean item = projectAdapter.getItem(position);
                bundle.putLong(ProjectConstants.PROJECT_ID, item.getId());
                CommonUtil.startActivtiy(getContext(), ProjectDetailActivity.class, bundle);
            }
        });
        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        projectAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                projectAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            loadData();
        }, viewDelegate.mRecyclerView);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        state = Constants.REFRESH_STATE;
        keyword = null;
        loadData();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        if (ProjectConstants.PROJECT_INFO_EDIT_EVENT_CODE == event.getCode()) {
            refreshData();
        }
    }

    public void search(String key) {
        keyword = key;
        state = Constants.REFRESH_STATE;
        loadData();
    }
}
