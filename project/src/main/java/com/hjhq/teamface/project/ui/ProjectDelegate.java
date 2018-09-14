package com.hjhq.teamface.project.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.presenter.filter.FilterFragment;

import java.util.List;

/**
 * 项目视图
 *
 * @author Administrator
 * @date 2018/4/10
 */

public class ProjectDelegate extends AppDelegate {
    TextView tvProject;
    TextView tvTask;
    ViewPager mViewPager;
    public SearchBar mSearchBar;
    private ImageView ivSearchBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    /**
     * 这里有侧滑控件，所以布局不能用父类的实现
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        rootLayout = (ViewGroup) inflater.inflate(R.layout.drawer_layout, container, false);
        View rootView = inflater.inflate(rootLayoutId, container, false);
        rootLayout.addView(rootView, 0);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        get(R.id.fl_tool_bar).setPadding(0, statusBarHeight, 0, 0);
        tvProject = get(R.id.tv_project);
        tvTask = get(R.id.tv_task);
        tvProject.setSelected(true);
        ivSearchBar = get(R.id.iv_search_bar);

        mViewPager = get(R.id.view_pager);
        mSearchBar = get(R.id.search_bar);
        mSearchBar.setCancelColor(ColorUtils.resToColor(mContext, R.color.gray_a0));
        mSearchBar.setCancelMargin(15, 0, 0, 0);
        mSearchBar.setHintText("请输入搜索内容");
    }

    public void setAdapter(List<Fragment> fragments) {
        FragmentAdapter mAdapter = new FragmentAdapter(mContext.getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 设置当前选中
     *
     * @param item
     */
    public void setCurrentItem(int item) {
        if (item == 0) {
            tvProject.setSelected(true);
            tvProject.setTextColor(ColorUtils.resToColor(mContext, R.color.white));
            tvTask.setSelected(false);
            tvTask.setTextColor(ColorUtils.resToColor(mContext, R.color.gray_99));
            ivSearchBar.setImageResource(R.drawable.project_icon_search);
        } else {
            tvProject.setSelected(false);
            tvProject.setTextColor(ColorUtils.resToColor(mContext, R.color.gray_99));
            tvTask.setSelected(true);
            tvTask.setTextColor(ColorUtils.resToColor(mContext, R.color.white));
            ivSearchBar.setImageResource(R.drawable.project_icon_filtrate);
        }
        mViewPager.setCurrentItem(item);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    public void showSearch() {
        mSearchBar.setVisibility(View.VISIBLE);
        setVisibility(R.id.ll_title, View.GONE);
        setVisibility(R.id.iv_add_bar, View.GONE);
        setVisibility(R.id.iv_search_bar, View.GONE);
    }

    public void hideSearch() {
        mSearchBar.setVisibility(View.GONE);
        setVisibility(R.id.ll_title, View.VISIBLE);
        setVisibility(R.id.iv_add_bar, View.VISIBLE);
        setVisibility(R.id.iv_search_bar, View.VISIBLE);
    }


    /**
     * 初始化筛选控件
     */
    public void initFilter() {
        Fragment fragment = FilterFragment.newInstance(0, 0);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }

    /**
     * 打开筛选
     */
    public void openDrawer() {
        DrawerLayout mDrawerLayout = get(R.id.drawer_layout);
        mDrawerLayout.openDrawer(get(R.id.drawer_content));
    }

    /**
     * 关闭筛选
     *
     * @return
     */
    public boolean closeDrawer() {
        DrawerLayout mDrawerLayout = get(R.id.drawer_layout);
        View view = get(R.id.drawer_content);
        if (view.getVisibility() == View.VISIBLE) {
            mDrawerLayout.closeDrawer(view);
            return true;
        }
        return false;
    }
}

