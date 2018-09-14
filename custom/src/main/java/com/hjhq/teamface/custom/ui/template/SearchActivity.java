package com.hjhq.teamface.custom.ui.template;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.custom.adapter.DataTempAdapter;
import com.hjhq.teamface.custom.ui.detail.DataDetailActivity;

import java.util.ArrayList;


/**
 * @author Administrator
 * @date 2017/9/5
 * Describe：搜索页
 */

public class SearchActivity extends ActivityPresenter<SearchDelegate, DataTempModel> {

    private String moduleBean = "commodity";
    private String keyword = "";
    private BaseQuickAdapter mAdapter;
    private String menuId;
    private boolean isChange;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            moduleBean = getIntent().getStringExtra(Constants.MODULE_BEAN);
            menuId = getIntent().getStringExtra(Constants.DATA_TAG1);
        }
    }

    @Override
    public void init() {
        mAdapter = new DataTempAdapter(null);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setHintText("请输入关键字");
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDelegate.getHistoryData(model.getSearchHistory(mContext, moduleBean));
    }

    @Override
    protected void getDelegateView() {
        viewDelegate = new SearchDelegate();
    }

    /**
     * 销售模块搜索
     */
    public void searchCustom() {
        DataListRequestBean bean = new DataListRequestBean();
        bean.setBean(moduleBean);
        bean.setFuzzyMatching(keyword);
        bean.setMenuId(menuId);
        model.getDataTemp(this, bean, new ProgressSubscriber<DataTempResultBean>(this) {
            @Override
            public void onNext(DataTempResultBean baseBean) {
                super.onNext(baseBean);
                viewDelegate.changeView(true);
                CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), baseBean.getData().getDataList());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void cancel() {
                if (isChange) {
                    setResult(RESULT_OK);
                }
                finish();
            }

            @Override
            public void clear() {
                viewDelegate.getHistoryData(model.getSearchHistory(mContext, moduleBean));
                viewDelegate.showHistoryItem();
            }

            @Override
            public void getText(String text) {
                if (!TextUtils.isEmpty(text)) {
                    keyword = text;
                }
            }

            @Override
            public void search() {
                model.saveSearchHistory(mContext, viewDelegate.saveSearchHistory(keyword), moduleBean);
                searchCustom();
            }
        });

        viewDelegate.setRecyclerListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                viewDelegate.setText(position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                viewDelegate.removeHistoryItem(position);
                model.saveSearchHistory(mContext, viewDelegate.saveSearchHistory(""), moduleBean);
            }
        });
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                DataTempResultBean.DataBean.DataListBean item = (DataTempResultBean.DataBean.DataListBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                String id = item.getId().getValue();
                bundle.putString(Constants.MODULE_BEAN, moduleBean);
                bundle.putString(Constants.DATA_ID, id);
                CommonUtil.startActivtiy(SearchActivity.this, DataDetailActivity.class, bundle);
            }
        });
        //清空搜索记录
        viewDelegate.setCleanButtonClickListener(v -> {
            viewDelegate.removeAllHistoryItem();
            model.saveSearchHistory(mContext, new ArrayList<>(), moduleBean);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CustomConstants.REQUEST_DETAIL_CODE) {
            searchCustom();
            isChange = true;
        }
    }
}
