package com.hjhq.teamface.memo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.view.recycler.MyGridDividerDecoration;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.view.recycler.SpaceItemDecoration;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.MemoGridAdapter;
import com.hjhq.teamface.memo.adapter.MemoListAdapter;
import com.hjhq.teamface.memo.bean.MemoListBean;
import com.hjhq.teamface.memo.view.MemoListFragmentDelegate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MemoListFragment extends FragmentPresenter<MemoListFragmentDelegate, MemoModel> {
    private int type;
    private RecyclerView mRecyclerView;
    private MemoListAdapter mListAdapter;
    private MemoGridAdapter mGridAdapter;
    private EmptyView mEmptyView1;
    private EmptyView mEmptyView2;
    private View mBlankView;
    private SmartRefreshLayout srlLayout;
    private MemoListActivity mActivity;
    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = -1;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;
    //编辑状态
    private boolean isEditState = false;
    private boolean isFirstLoad = true;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MyGridDividerDecoration mDecoration1;
    private SpaceItemDecoration mDecoration2;
    private List<MemoListItemBean> dataList = new ArrayList<>();


    static MemoListFragment newInstance(int index) {
        MemoListFragment myFragment = new MemoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MemoListActivity) getActivity();
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(Constants.DATA_TAG1, 0);
            mListAdapter = new MemoListAdapter(type, dataList);
            mGridAdapter = new MemoGridAdapter(type, dataList);
            //mDecoration1 = new MyGridDividerDecoration(getActivity(), R.color.red, 30);
            mDecoration2 = new SpaceItemDecoration((int) DeviceUtils.dpToPixel(getActivity(), 10), 2);
        }
    }

    @Override
    protected void init() {
        mRecyclerView = viewDelegate.get(R.id.rv);
        mBlankView = viewDelegate.get(R.id.blank);
        srlLayout = viewDelegate.get(R.id.refresh_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mListAdapter);
        mEmptyView1 = new EmptyView(getActivity());
        mEmptyView2 = new EmptyView(getActivity());
        mEmptyView1.showSubTitle();
        mEmptyView2.showSubTitle();
        mEmptyView1.setEmptyImage(R.drawable.memo_empty_view_img);
        mEmptyView1.setEmptyTitle("正在加载");
        mEmptyView1.setOnButtonClickListener(new EmptyView.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE1);
            }
        });
        mEmptyView2.setEmptyImage(R.drawable.memo_empty_view_img);
        mEmptyView2.setEmptyTitle("正在加载");
        mEmptyView2.setOnButtonClickListener(new EmptyView.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE1);
            }
        });
        mListAdapter.setEmptyView(mEmptyView1);
        mGridAdapter.setEmptyView(mEmptyView2);
        //监听下拉刷新
        srlLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlLayout.finishRefresh();
                    }
                }, 500);
            }
        });
        srlLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mListAdapter.getData().size() >= totalNum) {
                    srlLayout.finishLoadMore();
                    return;
                }
                state = Constants.LOAD_STATE;
                getNetData();
            }
        });
        refreshData();

    }

    public void switchLayoutmanager() {
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
            mRecyclerView.setAdapter(mGridAdapter);
            mBlankView.setVisibility(View.VISIBLE);

        } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mListAdapter);
            mBlankView.setVisibility(View.GONE);
        }
        //mRecyclerView.requestLayout();
    }

    public void setEditState(boolean editState) {
        srlLayout.setEnabled(!editState);
        isEditState = editState;
    }

    public void refreshData() {
        currentPageNo = 1;
        state = Constants.REFRESH_STATE;
        getNetData();
        srlLayout.setEnableLoadMore(true);
    }

    public void refreshIfEmpty() {
        if (mListAdapter.getData().size() <= 0) {
            state = Constants.REFRESH_STATE;
            currentPageNo = 1;
            getNetData();
        }
    }

    public MemoListAdapter getListAdapter() {
        return mListAdapter;
    }

    public MemoGridAdapter getGridAdapter() {
        return mGridAdapter;
    }

    public void getNetData() {
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.findMemoList(mActivity,
                "" + currentPageNo, "" + pageSize, type + "", "",
                new ProgressSubscriber<MemoListBean>(mActivity, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        srlLayout.finishLoadMore();
                        srlLayout.finishRefresh();
                    }

                    @Override
                    public void onNext(MemoListBean baseBean) {
                        super.onNext(baseBean);
                        showData(baseBean);


                        switch (type) {
                            case 0:
                            case 1:
                                mEmptyView1.setEmptyTitle("您还没有添加备忘录");
                                mEmptyView1.initAction("去添加备忘内容");
                                mEmptyView2.setEmptyTitle("您还没有添加备忘录");
                                mEmptyView2.initAction("去添加备忘内容");
                                break;
                            case 2:
                            case 3:
                                mEmptyView1.setEmptyTitle("");
                                mEmptyView1.initAction("");
                                mEmptyView2.setEmptyTitle("");
                                mEmptyView2.initAction("");
                                break;
                            default:

                                break;
                        }
                    }
                });
    }

    private void showData(MemoListBean fileListResBean) {

        List<MemoListItemBean> newDataList = new ArrayList<>();
        if (fileListResBean.getData() != null && fileListResBean.getData().getDataList() != null) {
            newDataList.addAll(fileListResBean.getData().getDataList());
        }
        switch (state) {
            case Constants.REFRESH_STATE:
            case Constants.NORMAL_STATE:
                dataList.clear();
                srlLayout.finishRefresh();
                break;
            case Constants.LOAD_STATE:
                srlLayout.finishLoadMore();
                break;
            default:
                break;
        }
        if (newDataList.size() > 0) {
            dataList.addAll(newDataList);
        }
        mGridAdapter.notifyDataSetChanged();
        mListAdapter.notifyDataSetChanged();
        PageInfo pageInfo = fileListResBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();
        if (totalNum == mListAdapter.getData().size()) {
            srlLayout.finishLoadMoreWithNoMoreData();
        }
        srlLayout.finishLoadMore();

    }

    /*public void setActivity(MemoListActivity activity) {
        mActivity = activity;
    }*/

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mListAdapter.isEditState()) {
                    return;
                }
                Intent intent = new Intent(mActivity, MemoDetailActivity.class);
                intent.putExtra(Constants.DATA_TAG1, mListAdapter.getData().get(position).getId());
                startActivityForResult(intent, Constants.REQUEST_CODE2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            refreshData();
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    refreshData();
                    break;
                case Constants.REQUEST_CODE2:
                    refreshData();

                    break;
                default:

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
