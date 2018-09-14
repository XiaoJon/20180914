package com.hjhq.teamface.oa.main.adaper;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.oa.login.bean.CompanyStructureBean;

import java.util.List;


public class DepartmentAdapter extends BaseQuickAdapter<CompanyStructureBean.DataBean, BaseViewHolder> {


    public DepartmentAdapter(List<CompanyStructureBean.DataBean> data) {
        super(R.layout.item_contact_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CompanyStructureBean.DataBean item) {
        helper.setText(R.id.tv_department_name, item.getName());

     
        helper.setText(R.id.tv_num, "(" + item.getCount() + ")");

        if (!TextUtil.isEmpty(item.getCompany_count()) && !"0".equals(item.getCompany_count())) {
            helper.setText(R.id.tv_num, "(" + item.getCompany_count() + ")");
        }
        ImageView imageView = helper.getView(R.id.iv_conversation_avatar);
        ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.icon_organization, imageView);
    }
}