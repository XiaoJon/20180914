package com.hjhq.teamface.email.view;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.email.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class ChooseEmailAccountDelegate extends AppDelegate {
    RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.email_choose_account_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("选择发件人");
        setRightMenuTexts(R.color.main_green, "确定");
        mRecyclerView = get(R.id.rv);
        /*rvAttachment = get(R.id.app_recycler);
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAttachment.addItemDecoration(new MyLinearDeviderDecoration(getActivity(), R.color.gray_f2, (int) DeviceUtils.dpToPixel(getActivity(), 8)));*/
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemChildClickListener(SimpleClickListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }
}
