package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.adapter.ProjectAdapter;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.MemoChooseProjectDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

@RouteNode(path = "/memo/choose", desc = "备忘录选择")
public class ChooseProjectActivity extends ActivityPresenter<MemoChooseProjectDelegate, ProjectModel> {
    private int totalPages = 1;
    private int currentPageNo = 1;
    private int state = Constants.NORMAL_STATE;
    private String keyword = "";
    private int type = 0;
    private ProjectAdapter mProjectAdapter;

    @Override
    public void init() {
        viewDelegate.showMenu();
        initData();
        initAdapter();
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.DATA_TAG1, mProjectAdapter.getData().get(position).getId());
                CommonUtil.startActivtiyForResult(mContext, ChooseProjectActivity.class, Constants.REQUEST_CODE1, bundle);
                super.onItemClick(adapter, view, position);
            }
        });
        mProjectAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mProjectAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            initData();
        }, viewDelegate.mRecyclerView);
        viewDelegate.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                viewDelegate.getRootView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(keyword)) {
                            initData();
                        }
                    }
                }, 1000);

            }
        });
        viewDelegate.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(keyword)) {
                        return true;
                    } else {
                        initData();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected void initData() {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.queryAllList(mContext, type, pageNo, Constants.PAGESIZE, keyword,
                new ProgressSubscriber<ProjectListBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ProjectListBean projectListBean) {
                        super.onNext(projectListBean);
                        showDataResult(projectListBean);
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
                CollectionUtils.notifyDataSetChanged(mProjectAdapter, mProjectAdapter.getData(), dataList);
                break;
            case Constants.LOAD_STATE:
                mProjectAdapter.addData(dataList);
                mProjectAdapter.loadMoreComplete();
                break;
            default:
                break;
        }
        PageInfo pageInfo = data.getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }

    protected void initAdapter() {
        viewDelegate.rlSearchLayout.setVisibility(View.VISIBLE);
        viewDelegate.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mProjectAdapter = new ProjectAdapter(new ArrayList<>());
        viewDelegate.setAdapter(mProjectAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
