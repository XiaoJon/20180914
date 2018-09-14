package com.hjhq.teamface.custom.ui.template;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.DataTempAdapter;
import com.hjhq.teamface.custom.bean.FindAutoByBean;
import com.hjhq.teamface.custom.ui.detail.DataDetailActivity;
import com.hjhq.teamface.custom.ui.detail.DataDetailModel;
import com.hjhq.teamface.custom.utils.CustomPopUtil;

import java.util.ArrayList;
import java.util.List;

import static com.hjhq.teamface.basis.constants.CustomConstants.REQUEST_DETAIL_CODE;


public class AutoModuleActivity extends ActivityPresenter<AutoModuleDelegate, DataTempModel> {

    private DataTempAdapter dataTempAdapter;
    private String title;
    //menu数据
    private List<FindAutoByBean.DataBean.DataListBean> menuList;
    //选中的menu 值
    protected long ruleId;

    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;
    private String sorceBean;
    private String targetBean;
    private String dataId;
    private String moduleBean;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            dataId = intent.getStringExtra(Constants.DATA_ID);
            sorceBean = intent.getStringExtra(Constants.DATA_TAG1);
            targetBean = intent.getStringExtra(Constants.DATA_TAG2);
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
        if (dataTempAdapter == null) {
            dataTempAdapter = new DataTempAdapter(null);
            viewDelegate.setAdapter(dataTempAdapter);
        }
    }

    /**
     * 加载无筛选的数据
     */
    public void loadData() {
        new DataDetailModel().findAutoByBean(this, sorceBean, targetBean, new ProgressSubscriber<FindAutoByBean>(this) {
            @Override
            public void onNext(FindAutoByBean baseBean) {
                super.onNext(baseBean);
                menuList = baseBean.getData().getDataList();
                if (!CollectionUtils.isEmpty(menuList)) {
                    FindAutoByBean.DataBean.DataListBean dataBean = menuList.get(0);
                    String menuLabel = dataBean.getTitle();
                    ruleId = dataBean.getId();
                    moduleBean = dataBean.getEmployee_name();
                    viewDelegate.setSortInfo(menuLabel);
                    loadTempData();
                }
            }
        });
    }

    /**
     * 加载列表数据
     */
    private void loadTempData() {
        new DataDetailModel().findAutoList(this, dataId, ruleId, sorceBean, targetBean, state == Constants.LOAD_STATE ? currentPageNo + 1 : 1, Constants.PAGESIZE, new ProgressSubscriber<DataTempResultBean>(this) {
            @Override
            public void onNext(DataTempResultBean baseBean) {
                super.onNext(baseBean);
                showDataResult(baseBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (state == Constants.LOAD_STATE) {
                    dataTempAdapter.loadMoreFail();
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
                CollectionUtils.notifyDataSetChanged(dataTempAdapter, dataTempAdapter.getData(), dataList);
                break;
            case Constants.LOAD_STATE:
                dataTempAdapter.addData(dataList);
                dataTempAdapter.loadMoreComplete();
                break;
            default:
                break;
        }

        PageInfo pageInfo = data.getPageInfo();
        if (pageInfo != null) {
            totalPages = pageInfo.getTotalPages();
            currentPageNo = pageInfo.getPageNum();

            viewDelegate.setSortInfo(pageInfo.getTotalRows());
        }
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> sortClick(), R.id.ll_sort);
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tempItemClick(adapter, view, position);
            }
        });
        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        dataTempAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                dataTempAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            loadData();
        }, viewDelegate.mRecyclerView);
    }

    /**
     * 数据点击事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    protected void tempItemClick(BaseQuickAdapter adapter, View view, int position) {
        DataTempResultBean.DataBean.DataListBean item = (DataTempResultBean.DataBean.DataListBean) adapter.getItem(position);
        String id = item.getId().getValue();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.DATA_ID, id);
        CommonUtil.startActivtiyForResult(this, DataDetailActivity.class, REQUEST_DETAIL_CODE, bundle);
    }


    /**
     * 分类选择 点击
     */
    protected void sortClick() {
        if (menuList != null && menuList.size() > 0) {
            List<String> list = new ArrayList<>();
            for (FindAutoByBean.DataBean.DataListBean dataBean : menuList) {
                list.add(dataBean.getTitle());
            }
            CustomPopUtil.showSortWindow(this,
                    viewDelegate.get(R.id.ll_sort),
                    list,
                    position -> {
                        sortLoadData(position);
                        return true;
                    });
        }
    }

    /**
     * 排序点击加载数据
     *
     * @param position
     */
    protected void sortLoadData(int position) {
        FindAutoByBean.DataBean.DataListBean dataBean = menuList.get(position);
        ruleId = dataBean.getId();
        moduleBean = dataBean.getEnglish_name();
        viewDelegate.setSortInfo(dataBean.getTitle());
        refreshData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CustomConstants.REQUEST_DETAIL_CODE) {
            refreshData();
        }
    }

    private void refreshData() {
        state = Constants.REFRESH_STATE;
        loadData();
    }
}
