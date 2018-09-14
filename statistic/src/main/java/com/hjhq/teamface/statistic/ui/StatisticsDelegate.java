package com.hjhq.teamface.statistic.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.statistic.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.List;


/**
 * 统计 视图代理类
 * Created by lx on 2017/8/31.
 */

public class StatisticsDelegate extends AppDelegate {
    ViewPager mViewPager;
    MagicIndicator mMagicIndicator;

    @Override
    public int getRootLayoutId() {
        return R.layout.statistic_activity_statistics;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("数据分析");
        mViewPager = get(R.id.m_view_pager);
        mMagicIndicator = get(R.id.m_magic_indicator);
    }

    /**
     * 初始化导航
     */
    public void initNavigator(String[] title, List<Fragment> fragments) {
        if (CollectionUtils.isEmpty(fragments)) {
            return;
        }
        FragmentAdapter mAdapter = new FragmentAdapter(mContext.getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter());
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    /**
     * 导航适配器
     */
    private class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

        final String[] title = {"仪表盘", "报表"};

        @Override
        public int getCount() {
            return title == null ? 0 : title.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {

            final int mSelectedColor = ColorUtils.resToColor(mContext, R.color.white);
            final int mNormalColor = ColorUtils.resToColor(mContext, R.color.app_blue);

            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
            commonPagerTitleView.setContentView(R.layout.statistic_simple_pager_title_layout);

            final TextView titleText = commonPagerTitleView.findViewById(R.id.tv_title);
            titleText.setText(title[index]);

            // 初始化
            final View llNavigatorLayout = commonPagerTitleView.findViewById(R.id.ll_navigator_layout);
            if (index == 0) {
                llNavigatorLayout.setBackgroundResource(R.drawable.chart_navigator_selector);
                llNavigatorLayout.setSelected(true);
                titleText.setTextColor(mSelectedColor);
            } else {
                llNavigatorLayout.setBackgroundResource(R.drawable.report_navigator_selector);
                llNavigatorLayout.setSelected(false);
                titleText.setTextColor(mNormalColor);
            }

            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                @Override
                public void onSelected(int i, int i1) {
                    titleText.setTextColor(mSelectedColor);
                    llNavigatorLayout.setSelected(true);
                }

                @Override
                public void onDeselected(int i, int i1) {
                    titleText.setTextColor(mNormalColor);
                    llNavigatorLayout.setSelected(false);
                }

                @Override
                public void onLeave(int i, int i1, float v, boolean b) {
                }

                @Override
                public void onEnter(int i, int i1, float v, boolean b) {
                }
            });

            commonPagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return commonPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return null;
        }
    }

}
