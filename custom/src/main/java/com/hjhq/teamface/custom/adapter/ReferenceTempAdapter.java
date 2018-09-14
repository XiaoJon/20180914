package com.hjhq.teamface.custom.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.basis.bean.ReferDataTempResultBean;

import java.util.List;

/**
 * 关联列表适配器
 *
 * @author xj
 * @date 2017/3/28
 */

public class ReferenceTempAdapter extends BaseQuickAdapter<ReferDataTempResultBean.DataBean, BaseViewHolder> {

    public ReferenceTempAdapter(List<ReferDataTempResultBean.DataBean> list) {
        super(R.layout.custom_item_reference_temp, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReferDataTempResultBean.DataBean item) {
        List<RowBean> row = item.getRow();
        if (!CollectionUtils.isEmpty(row)) {
            row.get(0).setIsLock("1");
            RecyclerView mRecyclerView = helper.getView(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            mRecyclerView.setAdapter(new ReferenceItemAdapter(row));
        }
    }

}
