package com.hjhq.teamface.project.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.MyOpenFragmentStatePagerAdapter;
import com.hjhq.teamface.project.bean.NodeBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/4/12
 */

public class TaskBoardDelegate extends AppDelegate {
    private MagicIndicator mMagicIndicator;
    public ViewPager mViewPager;
    public MyOpenFragmentStatePagerAdapter mAdapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_fragment_task_board;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mMagicIndicator = get(R.id.magic_indicator);
        mViewPager = get(R.id.view_pager);
    }

    /**
     * 初始化导航
     */
    public void initNavigator(FragmentManager fragmentManagers, List<NodeBean> dataList) {
        mMagicIndicator.setVisibility(View.VISIBLE);
        mAdapter = new MyOpenFragmentStatePagerAdapter(fragmentManagers, dataList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter(dataList));
        commonNavigator.setFollowTouch(true);
        commonNavigator.setPadding(15, 0, 15, 0);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    /**
     * 刷新节点导航
     *
     * @param dataList
     */
    public void refreshNode(List<NodeBean> dataList) {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setFollowTouch(true);
        commonNavigator.setPadding(15, 0, 15, 0);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter(dataList));
        mMagicIndicator.setNavigator(commonNavigator);
    }

    /**
     * 导航适配器
     */
    private class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

        public final List<NodeBean> nodeList;

        public MyCommonNavigatorAdapter(List<NodeBean> nodeList) {
            this.nodeList = nodeList;
        }

        @Override
        public int getCount() {
            return nodeList == null ? 0 : nodeList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
            scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray_99));
            scaleTransitionPagerTitleView.setGravity(Gravity.CENTER);
            scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.app_blue));
            scaleTransitionPagerTitleView.setTextSize(14);
            scaleTransitionPagerTitleView.setText(nodeList.get(index).getName());

            scaleTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
            int padding = (int) DeviceUtils.dpToPixel(context, 15);
            scaleTransitionPagerTitleView.setPadding(padding, 0, padding, 0);
            scaleTransitionPagerTitleView.setMaxEms(25);
            return scaleTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            indicator.setLineWidth(DeviceUtils.dpToPixel(mContext, 56));
            indicator.setLineHeight(DeviceUtils.dpToPixel(mContext, 2));
            indicator.setYOffset(DeviceUtils.dpToPixel(mContext, 0));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.app_blue));
            return indicator;
        }
    }
}
