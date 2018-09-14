package com.hjhq.teamface.custom.ui.template;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.ReferDataTempResultBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.custom.adapter.ReferenceTempAdapter;
import com.hjhq.teamface.custom.bean.RelationDataRequestBean;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 引用组件 列表
 *
 * @author xj
 * @date 2017/8/31
 */

@RouteNode(path = "/referenceTemp", desc = "引用组件 列表")
public class ReferenceTempActivity extends ActivityPresenter<ReferenceTempDelegate, DataTempModel> {

    protected ReferenceTempAdapter dataTempAdapter;
    protected String keyValue;
    protected HashMap form;

    /**
     * 所在模块的bean
     */
    protected String moduleBean;
    /**
     * 关联查询字段 name
     */
    protected String referenceField;
    /**
     * 搜索框提示文字
     */
    protected String searchHint;
    //子表单的keyName
    private String subFormName;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            referenceField = intent.getStringExtra(Constants.REFERENCE_FIELD);
            subFormName = intent.getStringExtra(Constants.SUBFORM_NAME);
            Serializable serializableExtra = intent.getSerializableExtra(Constants.DATA_TAG1);
            searchHint = intent.getStringExtra(Constants.DATA_TAG2);
            if (serializableExtra != null) {
                form = (HashMap) serializableExtra;
            } else {
                form = new HashMap();
            }
        }
    }

    @Override
    public void init() {
        initAdapter();
        form.put(referenceField, "");
        loadData();
    }

    /**
     * 初始化适配器
     */
    protected void initAdapter() {
        if (dataTempAdapter == null) {
            dataTempAdapter = new ReferenceTempAdapter(null);
            viewDelegate.setAdapter(dataTempAdapter);
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {
        RelationDataRequestBean bean = new RelationDataRequestBean();
        bean.setBean(moduleBean);
        bean.setSearchField(referenceField);
        bean.setForm(form);
        bean.setSubform(subFormName);

        model.findRelationDataList(this, bean, new ProgressSubscriber<ReferDataTempResultBean>(this) {
            @Override
            public void onNext(ReferDataTempResultBean dataTempResultBean) {
                super.onNext(dataTempResultBean);
                CollectionUtils.notifyDataSetChanged(dataTempAdapter, dataTempAdapter.getData(), dataTempResultBean.getData());
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ReferDataTempResultBean.DataBean item = dataTempAdapter.getItem(position);
                if (item == null || item.getId() == null || item.getRow() == null) {
                    ToastUtils.showError(mContext, "数据错误！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        viewDelegate.setSearchBarHint(searchHint);
        viewDelegate.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                if (TextUtils.isEmpty(keyValue)) {
                    ToastUtils.showError(mContext, "请输入搜索内容！");
                    return;
                }
                form.put(referenceField, keyValue);
                loadData();
            }

            @Override
            public void getText(String text) {
                keyValue = text;
                if (TextUtils.isEmpty(text)) {
                    List<ReferDataTempResultBean.DataBean> data = dataTempAdapter.getData();
                    data.clear();
                    dataTempAdapter.notifyDataSetChanged();
                }
            }
        });

    }

}
