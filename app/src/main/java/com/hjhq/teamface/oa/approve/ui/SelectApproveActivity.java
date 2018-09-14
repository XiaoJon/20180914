package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.oa.approve.adapter.SelectApproveAdapter;
import com.hjhq.teamface.oa.approve.bean.ApproveResponseBean;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 选择审批
 *
 * @author lx
 * @date 2017/616
 */
@RouteNode(path = "/approve/select", desc = "选择审批")
public class SelectApproveActivity extends ActivityPresenter<SelectApproveDelegate, ApproveModel> {
    private SelectApproveAdapter adapter;

    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;

    private String moduleBean;
    private String moduleChineseName;
    private String moduleId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            moduleBean = getIntent().getStringExtra(Constants.DATA_TAG1);
            moduleChineseName = getIntent().getStringExtra(Constants.DATA_TAG2);
            moduleId = getIntent().getStringExtra(Constants.DATA_TAG3);
        }
    }

    @Override
    public void init() {
        adapter = new SelectApproveAdapter(null);
        viewDelegate.setAdapter(adapter);
        getApproveList();
    }


    /**
     * 得到审批
     */
    public void getApproveList() {
        int pageNum = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;

        model.queryProjectApprovaList(mContext, moduleBean, Constants.PAGESIZE, pageNum, null, new ProgressSubscriber<ApproveResponseBean>(mContext) {
            @Override
            public void onNext(ApproveResponseBean baseBean) {
                super.onNext(baseBean);
                ApproveResponseBean.DataBean dataBean = baseBean.getData();
                List<ApproveListBean> data = dataBean.getDataList();

                switch (state) {
                    case Constants.NORMAL_STATE:
                    case Constants.REFRESH_STATE:
                        CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), data);
                        break;
                    case Constants.LOAD_STATE:
                        adapter.addData(data);
                        adapter.loadMoreComplete();
                        break;
                    default:
                        break;
                }

                //分页信息
                PageInfo pageInfo = dataBean.getPageInfo();
                totalPages = pageInfo.getTotalPages();
                currentPageNo = pageInfo.getPageNum();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (state == Constants.LOAD_STATE) {
                    adapter.loadMoreFail();
                }
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, moduleBean);
            CommonUtil.startActivtiyForResult(mContext, SearchApproveActivity.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.search_rl);

        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                ApproveListBean data = (ApproveListBean) adapte.getItem(position);
                data.setCheck(!data.isCheck());
                adapte.notifyItemChanged(position);
            }
        });
        viewDelegate.swipeRefreshWidget.setOnRefreshListener(() -> {
            state = Constants.REFRESH_STATE;
            getApproveList();
            viewDelegate.swipeRefreshWidget.setRefreshing(false);
        });

        //加载更更多
        adapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                adapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getApproveList();
        }, viewDelegate.mRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<ApproveListBean> list = adapter.getData();
        ArrayList<ApproveListBean> dataList = new ArrayList();
        Observable.from(list).filter(ApproveListBean::isCheck).subscribe(dataList::add);
        if (CollectionUtils.isEmpty(dataList)) {
            ToastUtils.showToast(mContext, "请选择审批");
            return super.onOptionsItemSelected(item);
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, dataList);
        intent.putExtra(Constants.DATA_TAG2, moduleChineseName);
        intent.putExtra(Constants.DATA_TAG3, moduleId);
        intent.putExtra(Constants.DATA_TAG4, moduleBean);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && Constants.REQUEST_CODE1 == requestCode) {
            setResult(RESULT_OK, data);
            finish();
        }

    }
}
