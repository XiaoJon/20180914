package com.hjhq.teamface.common.ui.member;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;


/**
 * 组织架构 视图代理类
 * Created by lx on 2017/8/31.
 */

public class CompanyOrganizationDelegate extends AppDelegate {
    public RecyclerView mRvNavi;
    public RecyclerView mRvDataList;
    public RecyclerView mRvEmployee;

    @Override
    public int getRootLayoutId() {
        return R.layout.company_organization_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();

        mRvDataList = get(R.id.rv_organization);
        mRvDataList.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        mRvDataList.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvEmployee = get(R.id.rv_employee);
        mRvEmployee.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        mRvEmployee.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mRvNavi = get(R.id.rv_navi);
        LinearLayoutManager naviLm = new LinearLayoutManager(mContext);
        naviLm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNavi.setLayoutManager(naviLm);
    }


    public void setNaviAdapter(BaseQuickAdapter adapter) {
        mRvNavi.setAdapter(adapter);
    }

    public void setDataAdapter(BaseQuickAdapter adapter) {
        mRvDataList.setAdapter(adapter);
    }

    public void setEmployeeAdapter(BaseQuickAdapter adapter) {
        mRvEmployee.setAdapter(adapter);
    }

}
