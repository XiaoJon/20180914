package com.hjhq.teamface.custom.ui.template;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.ui.filter.FilterFragment;


/**
 * 数据列表 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class DataTempDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_data_temp;
    }

    @Override
    public boolean isToolBar() {
        return true;
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
        LinearLayout rootLayout2 = (LinearLayout) inflater.inflate(R.layout.root_layout, container, false);
        View toolbar = inflater.inflate(R.layout.toolbar_comment, container, false);
        rootLayout2.addView(toolbar);
        rootLayout2.addView(rootView);
        rootLayout.addView(rootLayout2, 0);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        get(R.id.ll_tool_bar).setPadding(0, statusBarHeight, 0, 0);
        setRightMenuIcons(R.drawable.search_icon, R.drawable.add_company_icon);
        showMenu(0);

        View view = get(R.id.drawer_content);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) / 5 * 4);
        view.setLayoutParams(layoutParams);

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_green);

    }

    /**
     * 初始化筛选控件
     */
    public void initFilter(FragmentActivity activity, String moduleId) {
        Fragment fragment = new FilterFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, moduleId);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }

    public void setSortInfo(String str) {
        setSortInfo(str, 0);
    }

    public void setSortInfo(int count) {
        setSortInfo(null, count);
    }

    /**
     * 设置排列显示
     */
    public void setSortInfo(String str, int count) {
        TextView sortTitle = get(R.id.tv_sort_title);
        TextUtil.setText(sortTitle, str);
        TextView sortCount = get(R.id.tv_sort_count);
        if (count == 0) {
            sortCount.setVisibility(View.GONE);
        } else {
            sortCount.setVisibility(View.VISIBLE);
            TextUtil.setText(sortCount, count + "条");
        }
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
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
