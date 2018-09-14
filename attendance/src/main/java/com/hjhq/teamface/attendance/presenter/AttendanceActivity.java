package com.hjhq.teamface.attendance.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AttendanceDelegate;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.NoScrollViewPager;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/attendance_main", desc = "考勤主界面")
public class AttendanceActivity extends ActivityPresenter<AttendanceDelegate, AttendanceModel> implements ViewPager.OnPageChangeListener, View.OnClickListener {
    List<Fragment> fragments = new ArrayList<>(3);
    FragmentAdapter mAdapter;
    NoScrollViewPager mViewPager;
    private ImageView[] actionBarIvs = new ImageView[3];
    private TextView[] actionBarTvs = new TextView[3];
    private int[] normalActionBarDrawable = {R.drawable.attendance_daka_unselect, R.drawable.attendance_data_unselect, R.drawable.attendance_setting_unselect};
    private int[] clickActionBarDrawable = {R.drawable.attendance_daka_selected, R.drawable.attendance_data_selected, R.drawable.attendance_setting_selected};
    private int[] titleRes = {R.string.attendance_daka, R.string.attendance_data, R.string.attendance_setting};
    RelativeLayout rlAction1;
    RelativeLayout rlAction2;
    RelativeLayout rlAction3;


    @Override
    public void init() {
        initView();
    }

    private void initView() {
        rlAction1 = viewDelegate.get(R.id.rl1);
        rlAction2 = viewDelegate.get(R.id.rl2);
        rlAction3 = viewDelegate.get(R.id.rl3);
        rlAction1.setOnClickListener(this);
        rlAction2.setOnClickListener(this);
        rlAction3.setOnClickListener(this);
        TextView tv1 = viewDelegate.get(R.id.tv1);
        TextView tv2 = viewDelegate.get(R.id.tv2);
        TextView tv3 = viewDelegate.get(R.id.tv3);
        ImageView iv1 = viewDelegate.get(R.id.iv1);
        ImageView iv2 = viewDelegate.get(R.id.iv2);
        ImageView iv3 = viewDelegate.get(R.id.iv3);
        actionBarIvs[0] = iv1;
        actionBarIvs[1] = iv2;
        actionBarIvs[2] = iv3;
        actionBarTvs[0] = tv1;
        actionBarTvs[1] = tv2;
        actionBarTvs[2] = tv3;

        mViewPager = viewDelegate.get(R.id.main_vp);
        fragments.add(new AttendanceFragment());

        viewDelegate.getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragments.add(new DataFragment());
                fragments.add(new SettingFragment());
                mAdapter.notifyDataSetChanged();
            }
        }, 500);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
        //禁止ViewPager滑动，以免造成逻辑混乱
        mViewPager.setNoScroll(true);
    }

    private void changeActionBar(int position) {
        resetActionBar();
        viewDelegate.setTitle(titleRes[position]);
        mViewPager.setCurrentItem(position);
        actionBarIvs[position].setImageResource(clickActionBarDrawable[position]);
        actionBarTvs[position].setTextColor(ContextCompat.getColor(mContext, R.color.app_blue));

    }

    private void resetActionBar() {

        for (int i = 0; i < actionBarIvs.length; i++) {
            actionBarIvs[i].setImageResource(normalActionBarDrawable[i]);
            actionBarTvs[i].setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl1) {
            changeActionBar(0);
            viewDelegate.showMenu();
        } else if (v.getId() == R.id.rl2) {
            changeActionBar(1);
            viewDelegate.showMenu(0);
        } else if (v.getId() == R.id.rl3) {
            changeActionBar(2);
            viewDelegate.showMenu();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CommonUtil.startActivtiy(mContext, AttendanceRangeActivity.class);
        return super.onOptionsItemSelected(item);
    }
}
