package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.oa.approve.adapter.ApproveTypeAdapter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;


/**
 * 发起审批
 *
 * @author Administrator
 */
@RouteNode(path = "/choose_approve_module", desc = "选择添加关联审批模块")
public class ApproveTypeActivity extends ActivityPresenter<ApproveTypeDelegate, ApproveModel> {
    private ApproveTypeAdapter appAdapter;
    private List<ModuleResultBean.DataBean> dataBeanList = new ArrayList<>();

    @Override
    public void init() {
        initData();
    }


    protected void initData() {
        viewDelegate.setAdapter(appAdapter = new ApproveTypeAdapter(dataBeanList));
        viewDelegate.showMenu(0);
        model.getApproveModule(ApproveTypeActivity.this, new ProgressSubscriber<ModuleResultBean>(ApproveTypeActivity.this) {
            @Override
            public void onNext(ModuleResultBean moduleResultBean) {
                super.onNext(moduleResultBean);
                List<ModuleResultBean.DataBean> data1 = moduleResultBean.getData();
                dataBeanList.clear();
                dataBeanList.addAll(data1);
                appAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CustomConstants.REQUEST_ADDCUSTOM_CODE && resultCode == RESULT_OK) {
            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, null, null));
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
