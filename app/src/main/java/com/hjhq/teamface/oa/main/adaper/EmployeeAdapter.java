package com.hjhq.teamface.oa.main.adaper;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.oa.login.bean.CompanyStructureBean;

import java.util.List;


public class EmployeeAdapter extends BaseQuickAdapter<CompanyStructureBean.UserBean, BaseViewHolder> {


    public EmployeeAdapter(List<CompanyStructureBean.UserBean> data) {
        super(R.layout.item_contact_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CompanyStructureBean.UserBean item) {
        helper.setVisible(R.id.tv_department_name, false);
        helper.setVisible(R.id.tv_num, false);
        helper.setVisible(R.id.tv_employee_name, true);
        helper.setVisible(R.id.tv_employee_position, true);
        helper.setText(R.id.tv_employee_name, item.getName());
        String postName = item.getPost_name();
        if (TextUtils.isEmpty(postName)) {
            postName = "";
        }
        helper.setText(R.id.tv_employee_position, postName);
        ImageView imageView = helper.getView(R.id.iv_conversation_avatar);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), imageView, item.getEmployee_name());
    }
}