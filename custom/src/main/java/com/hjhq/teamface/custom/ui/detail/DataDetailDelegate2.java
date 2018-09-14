package com.hjhq.teamface.custom.ui.detail;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.view.NoScrollViewPager;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.RelevanceModuleAdapter;
import com.hjhq.teamface.custom.bean.AutoModuleResultBean;
import com.hjhq.teamface.custom.bean.DataRelationResultBean;
import com.hjhq.teamface.custom.utils.AppBarStateChangeListener;
import com.hjhq.teamface.custom.view.JudgeNestedScrollView;
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

public class DataDetailDelegate2 extends AppDelegate {
    List<View> views = new ArrayList<>();

    public RecyclerView recyclerModule;
    private LinearLayout llContent;
    private RelevanceModuleAdapter mAdapter;
    private List<SubfieldView> mViewList = new ArrayList<>();

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    public NoScrollViewPager mViewPager;
    JudgeNestedScrollView scrollView;
    private TextView tvComment;
    private TextView tvText;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_detail2;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public Toolbar getToolbar() {
        return get(R.id.toolbar);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        get(R.id.rl_title).setPadding(0, statusBarHeight, 0, 0);

        getActivity().setSupportActionBar(getToolbar());
        getToolbar().setNavigationIcon(R.drawable.icon_to_back);

        mContext.getSupportActionBar().setDisplayShowTitleEnabled(true);
        getToolbar().setNavigationOnClickListener(view -> getActivity().finish());
        setRightMenuIcons(R.drawable.menu_white);


        tvComment = get(R.id.tv_comment);
        tvText = get(R.id.tv_text);
        scrollView = get(R.id.scrollView);
        collapsingToolbarLayout = get(R.id.collapsingToolbarLayout);
        appBarLayout = get(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    setVisibility(R.id.tv_text, true);
                    setVisibility(R.id.iv_company, true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    setVisibility(R.id.tv_text, false);
                    setVisibility(R.id.iv_company, false);
                } else {
                    //中间状态
                    setVisibility(R.id.iv_company, false);
                    setVisibility(R.id.tv_text, false);
                }
            }
        });

        recyclerModule = get(R.id.recycler_module);
        llContent = get(R.id.ll_content);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerModule.setLayoutManager(mLinearLayoutManager);

        mAdapter = new RelevanceModuleAdapter(null);
        recyclerModule.setAdapter(mAdapter);

        mViewPager = get(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setNoScroll(true);

    }


    /**
     * 设置详情头部信息
     *
     * @param operationInfo
     */
    public void setDetailHeadView(RowBean operationInfo) {
        CustomUtil.setDetailHeaderValue(tvText, operationInfo);
        String s = tvText.getText().toString();
        if (!TextUtil.isEmpty(s) && !"详情".equals(s)) {
            collapsingToolbarLayout.setTitle(tvText.getText().toString());
        }
        tvText.setText("详情");
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
        List<String> refModules = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            recyclerModule.setVisibility(View.VISIBLE);
            Observable.from(data).filter(refModule -> "1".equals(refModule.getShow())).subscribe(refModule -> refModules.add(refModule.getModuleLabel()));
        }
        CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), refModules);
    }

    public void setAuthModule(List<AutoModuleResultBean.DataBean.DataListBean> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            recyclerModule.setVisibility(View.VISIBLE);
            List<String> modules = new ArrayList<>();
            Observable.from(dataList).subscribe(module -> modules.add(module.getTitle()));
            mAdapter.addData(modules);
        }
    }

    List<SubfieldView> getViewList() {
        return mViewList;
    }

    void showComment() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.tv_comment).setVisibility(View.VISIBLE);
        views.add(get(R.id.tv_comment));
    }

    void showDynamic() {
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.tv_dynamic).setVisibility(View.VISIBLE);
        get(R.id.line_1).setVisibility(View.VISIBLE);
        views.add(get(R.id.tv_dynamic));
    }

    void showEmail() {
        get(R.id.line_2).setVisibility(View.VISIBLE);
        get(R.id.ll_bottom_option).setVisibility(View.VISIBLE);
        get(R.id.tv_email).setVisibility(View.VISIBLE);
        views.add(get(R.id.tv_email));
    }

    void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
        setCurrentStatus(index);
    }

    void setCurrentStatus(int index) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setSelected(i == index);
        }
    }

    void setViewPage(FragmentManager supportFragmentManager, List<Fragment> fragments) {
        mViewPager.setAdapter(new FragmentAdapter(supportFragmentManager, fragments));
    }

    public void setCommentCount(String count) {
        TextUtil.setText(tvComment, "评论(" + count + ")");
    }
}