package com.hjhq.teamface.attendance.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class DataFragmentDelegate extends AppDelegate {
    final String[] title = {"日统计", "月统计", "我的"};
    ViewPager mViewPager;
    MagicIndicator mMagicIndicator;
    FragmentAdapter mAdapter;


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_data_fragment_main_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mViewPager = get(R.id.view_pager);
        mMagicIndicator = get(R.id.magic_indicator);
    }

    /**
     * 初始化导航
     */
    public void initNavigator(List<Fragment> fragments) {
        if (CollectionUtils.isEmpty(fragments)) {
            return;
        }
        if (title == null || title.length != fragments.size()) {
            return;
        }
        mViewPager.setOffscreenPageLimit(2);
        mMagicIndicator.setVisibility(View.VISIBLE);
        mAdapter = new FragmentAdapter(mContext.getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);


        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter(title));
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    /**
     * 单个fragment显示不需要导航
     *
     * @param fragments
     */
    public void setFragment(List<Fragment> fragments) {
        if (fragments == null || fragments.size() != 1) {
            return;
        }
        mAdapter = new FragmentAdapter(mContext.getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 导航适配器
     */
    private class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

        public final String[] title;

        public MyCommonNavigatorAdapter(String[] title) {
            this.title = title;
        }

        @Override
        public int getCount() {
            return title == null ? 0 : title.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
            scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray_69));
            scaleTransitionPagerTitleView.setGravity(Gravity.CENTER);
            scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_green));
            scaleTransitionPagerTitleView.setTextSize(16);
            scaleTransitionPagerTitleView.setText(title[index]);
            scaleTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
            return scaleTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            indicator.setLineWidth(DeviceUtils.dpToPixel(mContext, 90));
            indicator.setLineHeight(DeviceUtils.dpToPixel(mContext, 2));
            indicator.setYOffset(DeviceUtils.dpToPixel(mContext, 0));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.main_green));
            return indicator;
        }
    }

}
