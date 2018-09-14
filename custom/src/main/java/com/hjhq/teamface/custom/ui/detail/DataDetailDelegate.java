package com.hjhq.teamface.custom.ui.detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.RelevanceModuleAdapter;
import com.hjhq.teamface.custom.bean.DataRelationResultBean;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * @author lx
 * @date 2017/9/4
 */

public class DataDetailDelegate extends AppDelegate {

    private RecyclerView recyclerModule;
    private LinearLayout llContent;
    private RelevanceModuleAdapter mAdapter;
    private TextView tvTitleValue;
    private List<SubfieldView> mViewList = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_detail;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        get(R.id.mine_top_rl).setPadding(0, statusBarHeight, 0, 0);

        recyclerModule = get(R.id.recycler_module);
        llContent = get(R.id.ll_content);
        tvTitleValue = get(R.id.tv_title_value);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerModule.setLayoutManager(mLinearLayoutManager);

        mAdapter = new RelevanceModuleAdapter(null);
        recyclerModule.setAdapter(mAdapter);
    }


    /**
     * 设置详情头部信息
     *
     * @param operationInfo
     */
    public void setDetailHeadView(RowBean operationInfo) {
        CustomUtil.setDetailHeaderValue(tvTitleValue, operationInfo);
    }


    /**
     * 绘制布局
     *
     * @param layoutBeanList 布局
     * @param detailMap      数据
     * @param state          0新增 1详情 2编辑
     * @param moduleId       模块id
     */
    public void drawLayout(CustomLayoutResultBean.DataBean layoutBeanList, Map detailMap, int state, String moduleId) {
        if (layoutBeanList == null) {
            return;
        }

        llContent.removeAllViews();
        mViewList.clear();
        String title = layoutBeanList.getTitle();
        setTitle(title + "详情");
        List<CustomLayoutResultBean.DataBean.LayoutBean> layout = layoutBeanList.getLayout();
        if (layout == null) {
            return;
        }
        for (CustomLayoutResultBean.DataBean.LayoutBean layoutBean : layout) {
            boolean isTerminalApp = "1".equals(layoutBean.getTerminalApp());
            boolean isHideInDetail = "1".equals(layoutBean.getIsHideInDetail());
            boolean isSpread = "0".equals(layoutBean.getIsSpread());
//            boolean isHideColumnName = "1".equals(layoutBean.getIsHideColumnName());
            //详情分栏一定显示
            boolean isHideColumnName = false;
            if (!isTerminalApp || isHideInDetail) {
                //不是app端或新建时隐藏
                continue;
            }

            List<CustomBean> list = layoutBean.getRows();
            for (CustomBean customBean : list) {
                Object o = detailMap.get(customBean.getName());
                customBean.setValue(o);
            }
            SubfieldView subfieldView = new SubfieldView(list, state, layoutBean.getTitle(), isSpread, moduleId);
            subfieldView.setHideColumnName(isHideColumnName);
            subfieldView.addView(llContent);
            mViewList.add(subfieldView);
        }
    }

    /**
     * 设置关联模块数据
     *
     * @param data
     */
    public void setAdapterData(List<DataRelationResultBean.DataRelation.RefModule> data) {
        if (!CollectionUtils.isEmpty(data)) {
            recyclerModule.setVisibility(View.VISIBLE);
            List<DataRelationResultBean.DataRelation.RefModule> refModules = new ArrayList<>();
            Observable.from(data).filter(refModule -> "1".equals(refModule.getShow())).subscribe(refModule -> refModules.add(refModule));
            CollectionUtils.notifyDataSetChanged(mAdapter,mAdapter.getData(),refModules);
        }
    }

    public void addOnItemTouchListener(SimpleItemClickListener listener) {
        recyclerModule.addOnItemTouchListener(listener);
    }

    public void showComment() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.ll_comment).setVisibility(View.VISIBLE);
    }

    public void showDynamic() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.ll_dynamic).setVisibility(View.VISIBLE);
    }

    public void showApprove() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
    }

    public void showEmail() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.ll_email).setVisibility(View.VISIBLE);
    }

    public List<SubfieldView> getViewList() {
        return mViewList;
    }
}