package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.adapter.SelectTaskAdapter;
import com.hjhq.teamface.project.bean.QuoteTaskResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.SearchTaskDelegate;

import java.util.List;

import rx.Observable;

/**
 * 搜索任务
 *
 * @author Administrator
 * @date 2018/6/20
 */

public class SearchTaskActivity extends ActivityPresenter<SearchTaskDelegate, TaskModel> {
    private SelectTaskAdapter adapter;
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;


    private String keyword;
    /**
     * 项目ID
     */
    private String projectId;
    private int fromType;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            projectId = getIntent().getStringExtra(ProjectConstants.PROJECT_ID);
            fromType = projectId == null ? 1 : 0;
        }
    }

    @Override
    public void init() {
        adapter = new SelectTaskAdapter(null);
        viewDelegate.setAdapter(adapter);
    }


    /**
     * 得到审批
     */
    public void getApproveList() {
        int pageNum = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.queryQuoteTask(mContext, pageNum, Constants.PAGESIZE, keyword, fromType, projectId, new ProgressSubscriber<QuoteTaskResultBean>(mContext) {
            @Override
            public void onNext(QuoteTaskResultBean bean) {
                super.onNext(bean);
                QuoteTaskResultBean.DataBean dataBean = bean.getData();
                List<QuoteTaskResultBean.DataBean.DataListBean> dataList = dataBean.getDataList();

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
                QuoteTaskResultBean.DataBean.DataListBean data = (QuoteTaskResultBean.DataBean.DataListBean) adapte.getItem(position);
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
                keyword = "";
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
        List<QuoteTaskResultBean.DataBean.DataListBean> list = adapter.getData();
        StringBuilder sb = new StringBuilder();
        final String[] beanName = {ProjectConstants.PERSONAL_TASK_BEAN};
        Observable.from(list).filter(QuoteTaskResultBean.DataBean.DataListBean::isCheck).subscribe(dataBean ->{
            if (fromType==0){
                beanName[0] = dataBean.getBean_name();
            }
            sb.append(",").append(dataBean.getId());
        });
        if (TextUtil.isEmpty(sb)) {
            ToastUtils.showToast(mContext, "请选择任务");
            return super.onOptionsItemSelected(item);
        }
        sb.deleteCharAt(0);
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, sb.toString());
        intent.putExtra(Constants.DATA_TAG2, beanName[0]);
        intent.putExtra(Constants.DATA_TAG3, fromType);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

}
