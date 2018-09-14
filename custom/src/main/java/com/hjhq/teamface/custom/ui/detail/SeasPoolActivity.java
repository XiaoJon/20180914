package com.hjhq.teamface.custom.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.custom.adapter.SeasPoolAdapter;
import com.hjhq.teamface.custom.bean.SeasPoolResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 公海池列表 用于移动和退回公海池
 * Created by Administrator on 2018/1/30.
 */

public class SeasPoolActivity extends ActivityPresenter<SeasPoolDelegate, DataDetailModel> {
    private String type;
    private String moduleBean;
    private String moduleDataId;
    private String seasPoolId;
    private List<SeasPoolResponseBean.DataBean> dataList;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            type = intent.getStringExtra(Constants.DATA_TAG1);
            if (TextUtil.isEmpty(type)) {
                type = DataDetailActivity.BACK_POOL;
            }
            Serializable serializableExtra = intent.getSerializableExtra(Constants.DATA_TAG2);
            if (serializableExtra != null) {
                dataList = (List<SeasPoolResponseBean.DataBean>) serializableExtra;
            }

            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            moduleDataId = intent.getStringExtra(Constants.MODULE_ID);
            seasPoolId = intent.getStringExtra(Constants.POOL);
        }
    }

    @Override
    public void init() {
        if (DataDetailActivity.POOL_MOVE.equals(type)) {
            viewDelegate.setTitle("移动至");
        } else {
            viewDelegate.setTitle("退回到");
        }
        viewDelegate.setAdapter(new SeasPoolAdapter(dataList));
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.getmRecyclerView().addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                SeasPoolResponseBean.DataBean dataBean = (SeasPoolResponseBean.DataBean) adapter.getItem(position);
                dataBean.setCheck(!dataBean.isCheck());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (CollectionUtils.isEmpty(dataList)) {
            ToastUtils.showError(mContext, "请选择公海池");
            return super.onOptionsItemSelected(item);
        }
        String seasPoolId = null;
        for (SeasPoolResponseBean.DataBean data : dataList) {
            if (data.isCheck()) {
                seasPoolId = data.getId();
                break;
            }
        }
        if (TextUtil.isEmpty(seasPoolId)) {
            ToastUtils.showError(mContext, "请选择公海池");
            return super.onOptionsItemSelected(item);
        }
        if (DataDetailActivity.POOL_MOVE.equals(type)) {
            movePool(seasPoolId);
        } else {
            backPool(seasPoolId);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 退回公海池
     */
    private void backPool(String seasPoolId) {
        model.returnBack(mContext, moduleDataId, moduleBean, seasPoolId, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "退回成功！");
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    /**
     * 移动公海池
     */
    private void movePool(String seasPoolId) {
        if (seasPoolId.equals(this.seasPoolId)) {
            ToastUtils.showError(mContext, "请选择其他公海池");
            return;
        }
        model.moveSeapool(mContext, moduleDataId, moduleBean, seasPoolId, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "移动成功！");
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
