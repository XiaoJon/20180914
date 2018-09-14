package com.hjhq.teamface.oa.approve.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.componentservice.project.ProjectService;
import com.hjhq.teamface.view.recycler.GridDividerDecoration;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.List;

/**
 * @author lx
 * @date 2017/5/15
 */

public class ApproveTypeAdapter extends BaseQuickAdapter<ModuleResultBean.DataBean, BaseViewHolder> {

    public ApproveTypeAdapter(List<ModuleResultBean.DataBean> data) {
        super(R.layout.approve_type_app_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleResultBean.DataBean item) {
        TextView appNameTv = helper.getView(R.id.app_name_tv);
        Context context = appNameTv.getContext();
        RecyclerView moduleRecyclerView = helper.getView(R.id.module_recycler);

        TextUtil.setText(appNameTv, item.getName());

        if (moduleRecyclerView.getLayoutManager() == null) {
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 4) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            moduleRecyclerView.setLayoutManager(mGridLayoutManager);
            moduleRecyclerView.addItemDecoration(new GridDividerDecoration(context, R.color.transparent, (int) DeviceUtils.dpToPixel(context, 6)));
            moduleRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    super.onItemClick(adapter, view, position);
                    AppModuleBean item = (AppModuleBean) adapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.MODULE_BEAN, item.getEnglish_name());
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/add", bundle, CustomConstants.REQUEST_ADDCUSTOM_CODE);
                }
            });
        }
        ProjectService service = (ProjectService) Router.getInstance().getService(ProjectService.class.getSimpleName());
        moduleRecyclerView.setAdapter(service.getAppAdapter(item.getModules()));

    }
}
