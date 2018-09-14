package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.oa.approve.adapter.SelectApproveAdapter;
import com.hjhq.teamface.oa.approve.bean.ApproveResponseBean;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 搜索审批
 *
 * @author Administrator
 * @date 2018/6/20
 */

public class SearchApproveActivity extends ActivityPresenter<SearchApproveDelegate, ApproveModel> {
    private SelectApproveAdapter adapter;
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;


    private String keyword;
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
    }


    /**
     * 得到审批
     */
    public void getApproveList() {
        int pageNum = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;

        model.queryProjectApprovaList(mContext, moduleBean, Constants.PAGESIZE, pageNum, keyword, new ProgressSubscriber<ApproveResponseBean>(mContext) {
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

        viewDelegate.mRvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                ApproveListBean data = (ApproveListBean) adapte.getItem(position);
                data.setCheck(!data.isCheck());
                adapte.notifyItemChanged(position);
            }
        });
        //加载更更多
        adapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                adapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getApproveList();
        }, viewDelegate.mRvContacts);
        viewDelegate.mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {

            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                getApproveList();
            }

            @Override
            public void getText(String text) {
                keyword = text;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<ApproveListBean> list = adapter.getData();
        ArrayList<ApproveListBean> dataList = new ArrayList();
        Observable.from(list).filter(ApproveListBean::isCheck).subscribe(dataList::add);
        if (CollectionUtils.isEmpty(dataList)) {
            ToastUtils.showToast(mContext, "请选择数据");
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

}
