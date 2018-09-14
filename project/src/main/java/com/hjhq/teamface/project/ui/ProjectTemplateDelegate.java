package com.hjhq.teamface.project.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView;
import com.hjhq.teamface.project.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

/**
 * 新增项目/项目设置
 */

public class ProjectTemplateDelegate extends AppDelegate {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_template;
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
    public void initNavigator(FragmentManager fragmentManagers, String[] title, List<Fragment> fragments) {
        if (CollectionUtils.isEmpty(fragments)) {
            return;
        }
        if (title == null || title.length != fragments.size()) {
            return;
        }

        mMagicIndicator.setVisibility(View.VISIBLE);
        FragmentAdapter mAdapter = new FragmentAdapter(fragmentManagers, fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter(title));
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
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
            scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray_99));
            scaleTransitionPagerTitleView.setGravity(Gravity.CENTER);
            scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.app_blue));
            scaleTransitionPagerTitleView.setTextSize(18);
            scaleTransitionPagerTitleView.setText(title[index]);
            scaleTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
            return scaleTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            indicator.setLineWidth(DeviceUtils.dpToPixel(mContext, 26));
            indicator.setLineHeight(DeviceUtils.dpToPixel(mContext, 2));
            indicator.setYOffset(DeviceUtils.dpToPixel(mContext, 0));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.app_blue));
            return indicator;
        }
    }
}
