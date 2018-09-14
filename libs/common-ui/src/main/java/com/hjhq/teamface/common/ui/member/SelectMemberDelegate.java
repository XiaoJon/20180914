package com.hjhq.teamface.common.ui.member;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.R;
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
 * 选人、选部门、选角色、动态 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class SelectMemberDelegate extends AppDelegate {
    public ViewPager mViewPager;
    MagicIndicator mMagicIndicator;


    private FragmentAdapter mAdapter;
    private View llBottomSelecter;
    private View flCompanyOrganization;
    public ImageView ivCheckAllMember;
    private TextView tvSum;

    @Override
    public int getRootLayoutId() {
        return R.layout.select_member_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void setTitle(String title) {
        if (TextUtil.isEmpty(title)) {
            setTitle(R.string.select_member);
        } else {
            TextView titleTv = get(R.id.title_tv);
            TextUtil.setText(titleTv, title);
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.text_cancel);
        setRightMenuTexts(R.color.main_green, "确定");

        mViewPager = get(R.id.m_view_pager);
        mMagicIndicator = get(R.id.m_magic_indicator);
        llBottomSelecter = get(R.id.ll_bottom_selecter);
        flCompanyOrganization = get(R.id.fl_company_organization);
        ivCheckAllMember = get(R.id.iv_check_all_member);
        tvSum = get(R.id.tv_sum);
    }

    /**
     * 设置左上角图标和事件
     *
     * @param icon
     * @param listener
     */
    public void setNavigationIcon(int icon, View.OnClickListener listener) {
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(listener);
    }

    /**
     * 初始化导航
     */
    public void initNavigator(String[] title, List<Fragment> fragments) {
        if (CollectionUtils.isEmpty(fragments)) {
            return;
        }
        if (title == null || title.length != fragments.size()) {
            return;
        }

        mMagicIndicator.setVisibility(View.VISIBLE);
        mAdapter = new FragmentAdapter(mContext.getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);

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
            scaleTransitionPagerTitleView.setTextSize(14);
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

    /**
     * 底部全选是否可见
     *
     * @param visibility
     */
    public void setBottomSelecterVisibility(int visibility) {
        llBottomSelecter.setVisibility(visibility);
    }

    public void setMagicIndicatorVisibility(int visibility) {
        mMagicIndicator.setVisibility(visibility);
    }

    /**
     * 组织架构是否可见
     *
     * @param visibility
     */
    public void setOrganizationVisibility(int visibility) {
        flCompanyOrganization.setVisibility(visibility);
    }

    public int getOrganizationVisibility() {
        return flCompanyOrganization.getVisibility();
    }

    /**
     * 成员、部门、动态。。是否可见
     *
     * @param visibility
     */
    public void setViewPager(int visibility) {
        mViewPager.setVisibility(visibility);
    }

    /**
     * 设置总共选择的数量
     *
     * @param sum
     */
    public void setSum(int sum) {
        TextUtil.setText(tvSum, "已选择: " + sum);
    }

    /**
     * 设置全选文字
     *
     * @param check
     */
    public void setAllCheckText(boolean check) {
        TextUtil.setText((TextView) get(R.id.tv_all_check), check ? "取消全选" : "全选");
    }

    /**
     * 设置全选
     *
     * @param bl
     */
    public void setAllSelected(boolean bl) {
        ivCheckAllMember.setSelected(bl);
    }

    /**
     * 获取权限状态
     *
     * @return
     */
    public boolean isAllSelected() {
        return ivCheckAllMember.isSelected();
    }
}
