package com.hjhq.teamface.custom.adapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;

import java.util.List;

/**
 * 数据列表适配器
 *
 * @author lx
 * @date 2017/3/28
 */

public class DataTempAdapter extends BaseQuickAdapter<DataTempResultBean.DataBean.DataListBean, BaseViewHolder> {

    private boolean isCheckType;

    public DataTempAdapter(List<DataTempResultBean.DataBean.DataListBean> list) {
        super(R.layout.custom_layout_data_temp, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataTempResultBean.DataBean.DataListBean item) {
        TextView tvTitle = helper.getView(R.id.tv_client_name);
        View llContent = helper.getView(R.id.ll_content);
        View llContent1 = helper.getView(R.id.ll_content1);
        TextView tvName1 = helper.getView(R.id.tv_name1);
        TextView tvValue1 = helper.getView(R.id.tv_value1);
        View llContent2 = helper.getView(R.id.ll_content2);
        TextView tvName2 = helper.getView(R.id.tv_name2);
        TextView tvValue2 = helper.getView(R.id.tv_value2);
        View llContent3 = helper.getView(R.id.ll_content3);
        TextView tvName3 = helper.getView(R.id.tv_name3);
        TextView tvValue3 = helper.getView(R.id.tv_value3);

        View llType = helper.getView(R.id.ll_type);
        TextView tvType1 = helper.getView(R.id.tv_type_1);
        TextView tvType2 = helper.getView(R.id.tv_type_2);
        TextView tvType3 = helper.getView(R.id.tv_type_3);

        ImageView ivSelect = helper.getView(R.id.iv_select);
        if (isCheckType) {
            ivSelect.setVisibility(View.VISIBLE);
            if (item.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_selected);
            } else {
                ivSelect.setImageResource(R.drawable.icon_unselect);
            }
        } else {
            ivSelect.setVisibility(View.GONE);
        }

        View itemLayout = helper.getView(R.id.ll_custom_item_layout);
        String color = item.getColor();
        LayerDrawable layerDrawable = (LayerDrawable) itemLayout.getBackground();
        Drawable drawable = layerDrawable.getDrawable(0);

        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(ColorUtils.hexToColor(color));
        }

        DataTempResultBean.DataBean.DataListBean.Row row = item.getRow();

        if (row == null) {
            return;
        }
        List<RowBean> t1 = row.getRow1();
        List<RowBean> t2 = row.getRow2();
        List<RowBean> t3 = row.getRow3();
        if (!CollectionUtils.isEmpty(t1)) {
            //第一行
            tvTitle.setVisibility(View.VISIBLE);
            RowBean rowBean = t1.get(0);
            CustomUtil.setTempValue(tvTitle, rowBean, false);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!CollectionUtils.isEmpty(t2)) {
            //第二行
            llContent.setVisibility(View.VISIBLE);
            llContent1.setVisibility(View.VISIBLE);
            RowBean rowBean1 = t2.get(0);
            TextUtil.setText(tvName1, rowBean1.getLabel());
            CustomUtil.setTempValue(tvValue1, rowBean1, false);

            if (t2.size() > 1) {
                llContent2.setVisibility(View.VISIBLE);
                RowBean rowBean2 = t2.get(1);
                TextUtil.setText(tvName2, rowBean2.getLabel());
                CustomUtil.setTempValue(tvValue2, rowBean2, false);
            } else {
                llContent2.setVisibility(View.GONE);
            }
            if (t2.size() > 2) {
                llContent3.setVisibility(View.VISIBLE);
                RowBean rowBean3 = t2.get(2);
                TextUtil.setText(tvName3, rowBean3.getLabel());
                CustomUtil.setTempValue(tvValue3, rowBean3, false);
            } else {
                llContent3.setVisibility(View.GONE);
            }
        } else {
            llContent.setVisibility(View.GONE);
        }
        if (!CollectionUtils.isEmpty(t3)) {
            //第三行
            llType.setVisibility(View.VISIBLE);
            tvType1.setVisibility(View.VISIBLE);
            RowBean rowBean1 = t3.get(0);
            CustomUtil.setTempValue(tvType1, rowBean1, true);

            if (t3.size() > 1) {
                tvType2.setVisibility(View.VISIBLE);
                RowBean rowBean2 = t3.get(1);
                CustomUtil.setTempValue(tvType2, rowBean2, true);
            } else {
                tvType2.setVisibility(View.GONE);
            }
            if (t3.size() > 2) {
                tvType3.setVisibility(View.VISIBLE);
                RowBean rowBean3 = t3.get(2);
                CustomUtil.setTempValue(tvType3, rowBean3, true);
            } else {
                tvType3.setVisibility(View.GONE);
            }
        } else {
            llType.setVisibility(View.GONE);
        }
    }

    public boolean isCheckType() {
        return isCheckType;
    }

    public void setCheckType(boolean checkType) {
        isCheckType = checkType;
    }
}
