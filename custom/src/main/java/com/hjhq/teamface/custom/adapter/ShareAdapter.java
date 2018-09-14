package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.ShareResultBean;
import com.hjhq.teamface.custom.ui.funcation.ShareItemView;

import java.util.List;

/**
 * 分享适配器
 * Created by lx on 2017/3/28.
 */

public class ShareAdapter extends BaseQuickAdapter<ShareResultBean.DataBean, BaseViewHolder> {


    public ShareAdapter(List<ShareResultBean.DataBean> list) {
        super(R.layout.custom_share_item_layout, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareResultBean.DataBean item) {
        ShareItemView shareItemView = helper.getView(R.id.share_item_view);

        helper.addOnClickListener(R.id.tv_edit);
        helper.addOnClickListener(R.id.tv_del);

        shareItemView.setState(CustomConstants.DETAIL_STATE);
        shareItemView.setMembers(item.getAllot_employee());
        shareItemView.setContent(TextUtil.parseInt(item.getAccess_permissions()));
    }

}
