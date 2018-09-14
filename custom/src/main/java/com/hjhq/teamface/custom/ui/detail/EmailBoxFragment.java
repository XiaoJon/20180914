package com.hjhq.teamface.custom.ui.detail;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.common.adapter.EmailListAdapter;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.List;

/**
 * 自定义详情跳转邮箱
 *
 * @author Administrator
 */

public class EmailBoxFragment extends FragmentPresenter<EmailBoxDelegate, DataDetailModel> {
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;
    private EmptyView mEmptyView;
    private EmailListAdapter emailAdapter;
    private String emailAccount;


    public static EmailBoxFragment newInstance(String emailAccount) {
        EmailBoxFragment fragment = new EmailBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, emailAccount);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            emailAccount = arguments.getString(Constants.DATA_TAG1);
        }
    }

    @Override
    public void init() {
        mEmptyView = new EmptyView(getActivity());
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        emailAdapter = new EmailListAdapter(EmailConstant.RECEVER_BOX, null);
        emailAdapter.setEmptyView(mEmptyView);
        viewDelegate.setAdapter(emailAdapter);
        loadTempData();
    }


    /**
     * 加载列表数据
     */
    private void loadTempData() {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.getEmailListyAccount((ActivityPresenter) getActivity(), pageNo, Constants.PAGESIZE, emailAccount, new ProgressSubscriber<EmailListBean>(getActivity()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(EmailListBean emailListBean) {
                super.onNext(emailListBean);
                List<EmailBean> newDataList = emailListBean.getData().getDataList();
                List<EmailBean> oldDataList = emailAdapter.getData();


                switch (state) {
                    case Constants.NORMAL_STATE:
                    case Constants.REFRESH_STATE:
                        oldDataList.clear();
                        emailAdapter.setEnableLoadMore(true);
                        break;
                    case Constants.LOAD_STATE:
                        emailAdapter.loadMoreEnd();
                        break;
                    default:
                        break;
                }
                if (newDataList != null && newDataList.size() > 0) {
                    oldDataList.addAll(newDataList);
                    emailAdapter.notifyDataSetChanged();
                }
                PageInfo pageInfo = emailListBean.getData().getPageInfo();
                totalPages = pageInfo.getTotalPages();
                currentPageNo = pageInfo.getPageNum();
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, emailAdapter.getItem(position).getId());
                bundle.putInt(Constants.DATA_TAG2, EmailConstant.CAN_NOT_APPROVAL);
                UIRouter.getInstance().openUri(getActivity(), "DDComp://email/detail", bundle);

            }
        });

        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        emailAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                emailAdapter.loadMoreComplete();
                emailAdapter.setEnableLoadMore(false);
                return;
            }
            state = Constants.LOAD_STATE;
            loadTempData();
        }, viewDelegate.mRecyclerView);
    }

    private void refreshData() {
        state = Constants.REFRESH_STATE;
        loadTempData();
    }
}
