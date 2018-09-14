package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.oa.approve.adapter.SelectTypeAdapter;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * 驳回、通过方式选择
 *
 * @author lx
 * @date 2017/9/8
 */

public class SelectTypePresenter extends ActivityPresenter<SelectDelegate, ApproveModel> {

    private List<RejectTypeResponseBean.DataBean.RejectTypeBean> typeList;
    private String type;
    public SelectTypeAdapter selectAdapter;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            typeList = (List<RejectTypeResponseBean.DataBean.RejectTypeBean>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
            type = getIntent().getStringExtra(ApproveConstants.APPROVE_TYPE);
        }
    }

    @Override
    public void init() {
        if (ApproveConstants.PASS.equals(type)) {
            viewDelegate.setTitle("通过方式");
        } else if (ApproveConstants.REJECT.equals(type)) {
            viewDelegate.setTitle("驳回方式");
        }

        selectAdapter = new SelectTypeAdapter(typeList);
        viewDelegate.setAdapter(selectAdapter);
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                for (int i = 0; i < typeList.size(); i++) {
                    if (i == position) {
                        typeList.get(i).setCheck(true);
                    } else {
                        typeList.get(i).setCheck(false);
                    }
                }
                setResult();
            }
        });
    }

    /**
     * 返回结果
     */
    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, (Serializable) typeList);
        setResult(RESULT_OK, intent);
        finish();
    }

}
