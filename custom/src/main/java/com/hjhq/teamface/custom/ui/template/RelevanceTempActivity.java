package com.hjhq.teamface.custom.ui.template;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.AuthHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.DataTempAdapter;
import com.hjhq.teamface.custom.ui.add.AddCustomActivity;
import com.hjhq.teamface.custom.ui.detail.DataDetailActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hjhq.teamface.basis.constants.CustomConstants.REQUEST_DETAIL_CODE;

/**
 * 关联模块列表控制器
 * Created by lx on 2017/8/31.
 */

public class RelevanceTempActivity extends ActivityPresenter<DataTempDelegate, DataTempModel> {

    private DataTempAdapter dataTempAdapter;
    private String title;
    private String moduleBean;
    /**
     * 引用对象ID
     */
    private String relevanceObjectId;
    //关联组件的key
    private String fieldName;
    private String moduleId;
    //关联字段的值
    private String referenceFieldValue;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            moduleId = intent.getStringExtra(Constants.MODULE_ID);
            title = intent.getStringExtra(Constants.NAME);
            relevanceObjectId = intent.getStringExtra(Constants.DATA_ID);
            fieldName = intent.getStringExtra(Constants.FIELD_NAME);
            referenceFieldValue = intent.getStringExtra(CustomConstants.FIELD_VALUE_TAG);
        } else {
            moduleBean = savedInstanceState.getString(Constants.MODULE_BEAN);
            moduleId = savedInstanceState.getString(Constants.MODULE_ID);
            title = savedInstanceState.getString(Constants.NAME);
            relevanceObjectId = savedInstanceState.getString(Constants.DATA_ID);
            fieldName = savedInstanceState.getString(Constants.FIELD_NAME);
            referenceFieldValue = savedInstanceState.getString(CustomConstants.FIELD_VALUE_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.MODULE_BEAN, moduleBean);
        outState.putString(Constants.MODULE_ID, moduleId);
        outState.putString(Constants.NAME, title);
        outState.putString(Constants.DATA_ID, relevanceObjectId);
        outState.putString(Constants.FIELD_NAME, fieldName);
        outState.putString(CustomConstants.FIELD_VALUE_TAG, referenceFieldValue);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void init() {
        viewDelegate.setTitle(title);
        viewDelegate.setAdapter(dataTempAdapter = new DataTempAdapter(null));
        loadTempData();

        viewDelegate.setRightMenuIcons(R.drawable.add_company_icon);
        viewDelegate.showMenu(0, 1);
    }

    /**
     * 加载列表数据
     */
    private void loadTempData() {
        Map<String, Object> map = new HashMap<>();
        map.put(fieldName, relevanceObjectId);
        DataListRequestBean bean = new DataListRequestBean();
        bean.setBean(moduleBean);

        bean.setQueryWhere(map);
        model.getDataTemp(this, bean, new ProgressSubscriber<DataTempResultBean>(this) {
            @Override
            public void onNext(DataTempResultBean baseBean) {
                super.onNext(baseBean);
                showDataResult(baseBean);
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
        if (data != null) {
            List<DataTempResultBean.DataBean.DataListBean> dataList = data.getDataList();
            List<DataTempResultBean.DataBean.DataListBean> data1 = dataTempAdapter.getData();
            data1.clear();
            if (!CollectionUtils.isEmpty(dataList)) {
                data1.addAll(dataList);
            }
            dataTempAdapter.notifyDataSetChanged();
            viewDelegate.setSortInfo(data1.size());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addCustom();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 新增自定义
     */
    private void addCustom() {
        AuthHelper.getInstance().getModuleFunctionAuth(this, moduleBean, new AuthHelper.InitialDataCompleteListener() {
            @Override
            public void complete(ModuleFunctionBean moduleFunctionBean) {
                boolean b = AuthHelper.getInstance().checkFunctionAuth(moduleBean, CustomConstants.ADD_NEW);
                if (b) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    bundle.putString(Constants.MODULE_ID, moduleId);

                    HashMap<String, Object> map = new HashMap<>(1);
                    JSONArray jsonArray = new JSONArray();
                    Map<String, Object> map1 = new HashMap<>(2);
                    map1.put("id", relevanceObjectId);
                    map1.put("name", referenceFieldValue);
                    jsonArray.add(map1);
                    map.put(fieldName, jsonArray);
                    bundle.putSerializable(Constants.DATA_TAG1, map);
                    CommonUtil.startActivtiyForResult(RelevanceTempActivity.this, AddCustomActivity.class, CustomConstants.REQUEST_ADDCUSTOM_CODE, bundle);
                } else {
                    ToastUtils.showError(RelevanceTempActivity.this, "没有权限进行新增");
                    finish();
                }
            }

            @Override
            public void error() {
                ToastUtils.showError(RelevanceTempActivity.this, "获取权限失败！");
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tempItemClick(adapter, view, position);
            }
        });
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadTempData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });
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

        //模块权限判断
        AuthHelper.getInstance().getAuthByBean(this,
                moduleBean, id,
                new ProgressSubscriber<ViewDataAuthBean>(this, false) {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError(RelevanceTempActivity.this, "权限请求失败!");
                    }

                    @Override
                    public void onNext(ViewDataAuthBean baseBean) {
                        if ("1".equals(baseBean.getData().getDel_status())) {
                            ToastUtils.showError(RelevanceTempActivity.this, "数据已删除!");
                        } else if ("1".equals(baseBean.getData().getReadAuth())) {
                            ToastUtils.showError(RelevanceTempActivity.this, "无权限查看!");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.MODULE_BEAN, moduleBean);
                            bundle.putString(Constants.MODULE_ID, moduleId);
                            bundle.putString(Constants.DATA_ID, id);
                            CommonUtil.startActivtiyForResult(RelevanceTempActivity.this, DataDetailActivity.class, CustomConstants.REQUEST_DETAIL_CODE, bundle);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CustomConstants.REQUEST_ADDCUSTOM_CODE && resultCode == RESULT_OK) {
            init();
        } else if (requestCode == REQUEST_DETAIL_CODE && resultCode == RESULT_OK) {
            init();
        }
    }
}
