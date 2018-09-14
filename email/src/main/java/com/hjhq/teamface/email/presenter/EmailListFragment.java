package com.hjhq.teamface.email.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.EmailListAdapter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.view.EmailListFragmentDelegate;

import java.util.ArrayList;
import java.util.List;

public class EmailListFragment extends FragmentPresenter<EmailListFragmentDelegate, EmailModel> {
    private String boxTag;
    private RecyclerView mRecyclerView;
    private EmailListAdapter mAdapter;
    private EmptyView mEmptyView;
    private SwipeRefreshLayout srlLayout;
    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = 0;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;

    //编辑状态
    private boolean isEditState = false;
    private boolean isFirstLoad = true;


    static EmailListFragment newInstance(String tag) {
        EmailListFragment myFragment = new EmailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, tag);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            boxTag = arguments.getString(Constants.DATA_TAG1);
        }
        mAdapter = new EmailListAdapter(Integer.parseInt(boxTag), new ArrayList<>());

    }

    @Override
    protected void init() {
        mRecyclerView = viewDelegate.get(R.id.rv);
        srlLayout = viewDelegate.get(R.id.srl);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView = new EmptyView(getActivity());
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        // mEmptyView.setEmptyTitle("正在加载");
        mAdapter.setEmptyView(mEmptyView);
        srlLayout.setOnRefreshListener(() -> {
            if (isEditState) {
                srlLayout.setRefreshing(false);
                return;
            }
            if (boxTag.equals(EmailConstant.INBOX) || boxTag.equals(EmailConstant.TRASH)) {
                receiveEmail();
            } else {
                refreshData();
            }
            srlLayout.postDelayed(() -> srlLayout.setRefreshing(false), 100);
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mAdapter.loadMoreComplete();
                mAdapter.loadMoreEnd(false);
                return;
            }
            state = Constants.LOAD_STATE;
            getNetData();
        }, mRecyclerView);
        refreshData();
        closeDefaultAnimator();


    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator() {
        mRecyclerView.getItemAnimator().setAddDuration(0);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mRecyclerView.getItemAnimator().setMoveDuration(0);
        mRecyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        state = Constants.REFRESH_STATE;
        getNetData();
        ((EmailActivity3) getActivity()).getUnreadNum();
    }

    public EmailListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 获取数据
     */
    public void getNetData() {
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.getEmailList(((ActivityPresenter) getActivity()), currentPageNo, pageSize, "", boxTag, new ProgressSubscriber<EmailListBean>(getActivity(), isFirstLoad) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isFirstLoad = false;
            }

            @Override
            public void onNext(EmailListBean emailListBean) {
                super.onNext(emailListBean);
                // mEmptyView.setEmptyTitle("无数据");
                showData(emailListBean);
                isFirstLoad = false;
            }
        });
    }

    /**
     * 收信
     */
    private void receiveEmail() {
        model.receive(((ActivityPresenter) getActivity()), new ProgressSubscriber<EmailListBean>(getContext(), true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(EmailListBean emailListBean) {
                super.onNext(emailListBean);
                refreshData();
            }
        });
    }

    private void showData(EmailListBean bean) {

        List<EmailBean> newDataList = bean.getData().getDataList();
        List<EmailBean> oldDataList = mAdapter.getData();


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
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } else {
            mAdapter.setNewData(oldDataList);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        PageInfo pageInfo = bean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();
        if (mAdapter.getData().size() == totalNum) {
            mAdapter.loadMoreComplete();
        }
        try {
            ((EmailActivity3) getActivity()).setTotalNum(pageInfo.getTotalRows());
        } catch (Exception e) {
            e.printStackTrace();
            ((EmailActivity3) getActivity()).setTotalNum(0);
        }

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("0".equals(mAdapter.getData().get(position).getRead_status())) {
                    //未读
                    if (EmailConstant.DRAFTBOX.equals(boxTag) && ("1".equals(mAdapter.getData().get(position).getDraft_status()))
                            && (EmailConstant.APPROVAL_STATE_NO.equals(mAdapter.getData().get(position).getApproval_status()) || EmailConstant.APPROVAL_STATE_CANCEL.equals(mAdapter.getData().get(position).getApproval_status()))) {
                        //草稿箱中的草稿和撤销的审批进入编辑界面
                        ((EmailActivity3) getActivity()).editEmail(mAdapter.getData().get(position));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG1, mAdapter.getData().get(position).getId());
                        bundle.putInt(Constants.DATA_TAG2, TextUtil.parseInt(boxTag));
                        CommonUtil.startActivtiyForResult(getActivity(), EmailDetailActivity.class, Constants.REQUEST_CODE1, bundle);
                    }
                    mAdapter.getData().get(position).setRead_status("1");
                    mAdapter.notifyDataSetChanged();
                    ((EmailActivity3) getActivity()).markEmailReaded(mAdapter.getData().get(position).getId(), position);
                } else {
                    //已读
                    if (EmailConstant.DRAFTBOX.equals(boxTag) && ("1".equals(mAdapter.getData().get(position).getDraft_status()))
                            && (EmailConstant.APPROVAL_STATE_NO.equals(mAdapter.getData().get(position).getApproval_status()) || EmailConstant.APPROVAL_STATE_CANCEL.equals(mAdapter.getData().get(position).getApproval_status()))) {
                        //草稿箱中的草稿和撤销的审批进入编辑界面
                        ((EmailActivity3) getActivity()).editEmail(mAdapter.getData().get(position));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG1, mAdapter.getData().get(position).getId());
                        bundle.putInt(Constants.DATA_TAG2, TextUtil.parseInt(boxTag));
                        CommonUtil.startActivtiyForResult(getActivity(), EmailDetailActivity.class, Constants.REQUEST_CODE2, bundle);
                    }
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
