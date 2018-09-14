package com.hjhq.teamface.custom.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;

import java.util.List;

/**
 * 关联列表子项适配器
 *
 * @author xj
 * @date 2017/3/28
 */

public class ReferenceItemAdapter extends BaseQuickAdapter<RowBean, BaseViewHolder> {

    public ReferenceItemAdapter(List<RowBean> list) {
        super(R.layout.custom_item_reference_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RowBean item) {
        if ("1".equals(item.getHidden())) {
            helper.setVisible(R.id.fl_item, false);
            return;
        } else {
            helper.setVisible(R.id.fl_item, true);
        }
        if ("1".equals(item.getIsLock())) {
            //头部
            TextView tvTitle = helper.getView(R.id.tv_title);
            helper.setVisible(R.id.tv_title, true);
            helper.setVisible(R.id.ll_content, false);
            CustomUtil.setReferenceTempValue(tvTitle, item);
        } else {
            helper.setVisible(R.id.tv_title, false);
            helper.setVisible(R.id.ll_content, true);

            TextView contentName = helper.getView(R.id.content_name);
            TextView contentVame = helper.getView(R.id.content_value);

            TextUtil.setText(contentName, item.getLabel());
            CustomUtil.setReferenceTempValue(contentVame, item);
        }

    }

}
