package com.hjhq.teamface.common.adapter;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.common.R;

import java.util.List;

/**
 * Created by lx on 2017/7/11.
 */

public class LocNearAddressAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {
    private int selectPosition;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
    public PoiItem getSelectPoiInfo(){
        if (selectPosition>=getItemCount()) {
            return null;
        }
        return getItem(selectPosition);
    }

    public LocNearAddressAdapter(List<PoiItem> data) {
        super(R.layout.custom_item_location_near, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_location, item.getProvinceName() + item.getCityName() + item.getAdName() + item.getSnippet());
        helper.setVisible(R.id.iv_select, item == getItem(selectPosition));
    }
}
