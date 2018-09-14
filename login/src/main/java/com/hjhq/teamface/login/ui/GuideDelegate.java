package com.hjhq.teamface.login.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.login.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/11/9
 * Describe：登录注册
 */

public class GuideDelegate extends AppDelegate {
    private static final int[] GUIDE_PAGES = {R.drawable.login_guide_page_1, R.drawable.login_guide_page_2, R.drawable.login_guide_page_3};

    public ViewPager mViewPager;
    public LinearLayout llPoint;
    public Button loginBtn;
    public Button registerBtn;
    public LinearLayout buttonLl;
    public TextView copyright;


    @Override
    public int getRootLayoutId() {
        return R.layout.login_acitivity_guide;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mViewPager = get(R.id.vp);
        llPoint = get(R.id.llPoint);
        loginBtn = get(R.id.login_btn);
        registerBtn = get(R.id.register_btn);
        buttonLl = get(R.id.button_ll);
        copyright = get(R.id.copyright);

        //全屏
        getRootView().setMinimumHeight((int) ScreenUtils.getScreenHeight(mContext));
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenHeight(mContext) * 7 / 10);
        mViewPager.setLayoutParams(layoutParams);

        // 初始化引导页视图列表
        initGuideView();
        //初始化小圆点
        initPoint();
    }

    /**
     * 初始化引导页
     */
    private void initGuideView() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < GUIDE_PAGES.length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.login_view_guide, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            ImageLoader.loadImage(mContext, GUIDE_PAGES[i], iv);
            views.add(view);
        }
        mViewPager.setAdapter(new GuideViewpagerAdapter(views));
    }

    /**
     * 添加小圆点
     */
    private void initPoint() {
        int pointSize = (int) DeviceUtils.dpToPixel(mContext, 10);
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < GUIDE_PAGES.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(pointSize, pointSize);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(pointSize, 0, 0, 0);
            }
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.login_circle_guide_normal);
            llPoint.addView(iv);
        }
        llPoint.getChildAt(0).setBackgroundResource(R.drawable.login_circle_guide_select);
    }

    /**
     * 监听引导界面变化来切换小圆点
     *
     * @param position
     */
    public void monitorPoint(int position) {
        position = position % GUIDE_PAGES.length;
        for (int i = 0; i < GUIDE_PAGES.length; i++) {
            if (i == position) {
                llPoint.getChildAt(position).setBackgroundResource(
                        R.drawable.login_circle_guide_select);
            } else {
                llPoint.getChildAt(i).setBackgroundResource(
                        R.drawable.login_circle_guide_normal);
            }
        }
    }


    /**
     * 引导页面适配器
     */
    private class GuideViewpagerAdapter extends PagerAdapter {
        private List<View> viewList;

        public GuideViewpagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return GUIDE_PAGES.length;
        }

        public View getItem(int position) {
            return viewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(getItem(position % GUIDE_PAGES.length));
            return getItem(position % GUIDE_PAGES.length);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(getItem(position % GUIDE_PAGES.length));
        }
    }
}
