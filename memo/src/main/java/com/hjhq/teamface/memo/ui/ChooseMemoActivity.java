package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.andview.refreshview.XRefreshView;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.refresh.PullFooterView;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.ChooseMemoListAdapter;
import com.hjhq.teamface.memo.bean.MemoListBean;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.memo.view.ChooseMemoDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

@RouteNode(path = "/choose_memo", desc = "选择备忘录")
public class ChooseMemoActivity extends ActivityPresenter<ChooseMemoDelegate, MemoModel> {
    private RecyclerView mRecyclerView;
    private ChooseMemoListAdapter mAdapter;
    private List<MemoListItemBean> dataList = new ArrayList<>();
    private List<MemoListItemBean> choosedDataList = new ArrayList<>();
    private EmptyView mEmptyView;
    private XRefreshView srlLayout;
    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = -1;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;
    private String type = "0";

    @Override
    public void init() {

        mRecyclerView = viewDelegate.get(R.id.rv);
        srlLayout = viewDelegate.get(R.id.srl);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChooseMemoListAdapter(dataList);
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyImage(R.drawable.memo_empty_view_img);
        mEmptyView.setEmptyTitle("正在加载");
        mAdapter.setEmptyView(mEmptyView);
        PullFooterView footerView = new PullFooterView(this);
        srlLayout.setCustomFooterView(footerView);
        //监听下拉刷新
        //设置刷新完成以后，headerview固定的时间
        srlLayout.setPinnedTime(500);
        srlLayout.setMoveForHorizontal(true);
        srlLayout.setPullLoadEnable(true);
        srlLayout.setAutoLoadMore(false);
        srlLayout.enableReleaseToLoadMore(true);
        srlLayout.enableRecyclerViewPullUp(true);
        srlLayout.enablePullUpWhenLoadCompleted(true);
        srlLayout.enableReleaseToLoadMore(true);
        srlLayout.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                refreshData();
                srlLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlLayout.stopRefresh();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (mAdapter.getData().size() >= totalNum) {
                    srlLayout.setLoadComplete(true);
                    return;
                }
                state = Constants.LOAD_STATE;
                getNetData(false);
            }
        });
        getNetData(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<MemoListItemBean> choosedItem = getChoosedItem();
        if (choosedItem == null || choosedItem.size() <= 0) {
            ToastUtils.showToast(mContext, "未选择数据");
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, choosedItem);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.search_rl).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, type);
            bundle.putInt(Constants.DATA_TAG2, SearchMemoActivity.CHOOSE);
            CommonUtil.startActivtiyForResult(mContext, SearchMemoActivity.class, Constants.REQUEST_CODE1, bundle);
        });
    }

    /**
     * 获取选择数据
     *
     * @return
     */
    private ArrayList<MemoListItemBean> getChoosedItem() {
        return mAdapter.getChoosedItem();
    }

    public void refreshData() {

        currentPageNo = 1;
        state = Constants.REFRESH_STATE;
        getNetData(false);
    }

    public void getNetData(boolean flag) {
        choosedDataList = getChoosedItem();
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.findMemoList(ChooseMemoActivity.this,
                "" + currentPageNo, "" + pageSize, type, "",
                new ProgressSubscriber<MemoListBean>(ChooseMemoActivity.this, flag) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        srlLayout.stopLoadMore();
                    }

                    @Override
                    public void onNext(MemoListBean baseBean) {
                        super.onNext(baseBean);
                        showData(baseBean);
                        mEmptyView.setEmptyTitle("您还没有添加备忘录");
                    }
                });
    }

    private void showData(MemoListBean fileListResBean) {

        List<MemoListItemBean> newDataList = new ArrayList<>();
        if (fileListResBean.getData() != null && fileListResBean.getData().getDataList() != null) {
            newDataList.addAll(fileListResBean.getData().getDataList());
        }

        List<MemoListItemBean> oldDataList = mAdapter.getData();
        if (oldDataList == null) {
            oldDataList = new ArrayList<>();
        }


        switch (state) {
            case Constants.REFRESH_STATE:
            case Constants.NORMAL_STATE:
                oldDataList.clear();
                break;
            case Constants.LOAD_STATE:
                break;
            default:
                break;
        }
        if (newDataList.size() > 0) {
            oldDataList.addAll(newDataList);
        }
        if (choosedDataList.size() > 0) {
            for (int i = 0; i < oldDataList.size(); i++) {
                oldDataList.get(i).setChecked(false);
                for (int j = 0; j < choosedDataList.size(); j++) {
                    if (choosedDataList.get(j).getId().equals(oldDataList.get(i).getId())) {
                        oldDataList.get(i).setChecked(true);
                        break;
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        PageInfo pageInfo = fileListResBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();
        srlLayout.stopLoadMore(false);
        /*if (mAdapter.getData().size() == totalNum) {
            srlLayout.setLoadComplete(true);
        } else {
            srlLayout.stopLoadMore(false);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && Constants.REQUEST_CODE1 == requestCode) {
            if (data != null) {
                MemoListItemBean bean = (MemoListItemBean) data.getSerializableExtra(Constants.DATA_TAG1);
                ArrayList<MemoListItemBean> choosedItem = getChoosedItem();
                choosedItem.add(bean);
                if (choosedItem == null || choosedItem.size() <= 0) {
                    ToastUtils.showToast(mContext, "未选择数据");
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, choosedItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
