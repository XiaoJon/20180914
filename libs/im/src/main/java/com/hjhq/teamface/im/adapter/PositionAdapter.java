package com.hjhq.teamface.im.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.activity.PositionManageActivity;
import com.hjhq.teamface.im.bean.PositionBean;

import java.util.List;

/**
 * Created by lx on 2017/5/15.
 */

public class PositionAdapter extends BaseQuickAdapter<PositionBean, BaseViewHolder> {

    private int currentMode = PositionManageActivity.NORMAL_MODE;

    public void setMode(int mode) {
        this.currentMode = mode;
    }

    public PositionAdapter(List<PositionBean> data) {
        super(R.layout.item_position, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PositionBean item) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        ImageView ivEdit = helper.getView(R.id.iv_edit);
        ImageView ivDel = helper.getView(R.id.iv_del);
        LinearLayout llManage = helper.getView(R.id.ll_manage);

        tvTitle.setText(item.getPosition());
        if (currentMode == PositionManageActivity.NORMAL_MODE) {//正常模式
            llManage.setVisibility(View.VISIBLE);
        } else {//选择模式
            llManage.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.iv_edit).addOnClickListener(R.id.iv_del);
    }
}
