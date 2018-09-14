package com.hjhq.teamface.custom.ui.select;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.custom.adapter.DataTempAdapter;
import com.hjhq.teamface.custom.ui.template.DataTempModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 搜索自定义列表数据
 *
 * @author Administrator
 * @date 2018/6/20
 */

public class SearchDataTempActivity extends ActivityPresenter<SearchDataTempDelegate, DataTempModel> {
    private DataTempAdapter adapter;
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;


    private String keyword;

    private String title;
    private String moduleBean;
    private String searchKey;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            searchKey = getIntent().getStringExtra(Constants.DATA_TAG1);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            title = intent.getStringExtra(Constants.NAME);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle(title);
        initAdapter();
        loadData();
    }

    /**
     * 初始化适配器
     */
    protected void initAdapter() {
        adapter = new DataTempAdapter(null);
        adapter.setCheckType(true);
        viewDelegate.setAdapter(adapter);
    }


    /**
     * 得到审批
     */
    public void loadData() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(state == Constants.LOAD_STATE ? currentPageNo + 1 : 1);
        pageInfo.setPageSize(Constants.PAGESIZE);

        DataListRequestBean bean = new DataListRequestBean();
        bean.setBean(moduleBean);
        bean.setPageInfo(pageInfo);

        Map<String, Object> map = new HashMap<>(1);
        if (!TextUtils.isEmpty(keyword)) {
            map.put(searchKey, keyword);
        }
        bean.setQueryWhere(map);


        model.getDataTemp(this, bean, new ProgressSubscriber<DataTempResultBean>(this) {
            @Override
            public void onNext(DataTempResultBean baseBean) {
                super.onNext(baseBean);
                showDataResult(baseBean);
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

    /**
     * 显示数据结果
     *
     * @param baseBean
     */
    protected void showDataResult(DataTempResultBean baseBean) {
        DataTempResultBean.DataBean data = baseBean.getData();

        List<DataTempResultBean.DataBean.DataListBean> dataList = data.getDataList();
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

        PageInfo pageInfo = data.getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

        viewDelegate.mRvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                DataTempResultBean.DataBean.DataListBean data = (DataTempResultBean.DataBean.DataListBean) adapte.getItem(position);
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
            loadData();
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
                loadData();
            }

            @Override
            public void getText(String text) {
                keyword = text;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setCustomResult();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置自定义选择结果
     */
    private void setCustomResult() {
        List<DataTempResultBean.DataBean.DataListBean> list = adapter.getData();
        ArrayList<DataTempResultBean.DataBean.DataListBean> dataList = new ArrayList();
        Observable.from(list).filter(DataTempResultBean.DataBean.DataListBean::isCheck).subscribe(dataList::add);
        if (CollectionUtils.isEmpty(dataList)) {
            ToastUtils.showToast(mContext, "请选择数据");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, dataList);
        intent.putExtra(Constants.MODULE_BEAN, moduleBean);
        setResult(RESULT_OK, intent);
        finish();
    }

}
