package com.hjhq.teamface.oa.approve.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.oa.approve.bean.ApproveUnReadCountResponseBean;
import com.hjhq.teamface.util.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批
 *
 * @author lx
 * @date 2017/616
 */


@RouteNode(path = "/approve/main", desc = "审批首页")
public class ApproveActivity extends ActivityPresenter<ApproveDelegate, ApproveModel> {
    private ApproveFragment approveFragment1;
    private ApproveFragment approveFragment2;
    private ApproveFragment approveFragment3;
    private ApproveFragment approveFragment4;
    private ApproveFilterFragment filterFragment;

    private int currentType = ApproveFragment.TAG1;

    @Override
    public void init() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(approveFragment1 = ApproveFragment.newInstance(ApproveFragment.TAG1));
        fragments.add(approveFragment2 = ApproveFragment.newInstance(ApproveFragment.TAG2));
        fragments.add(approveFragment3 = ApproveFragment.newInstance(ApproveFragment.TAG3));
        fragments.add(approveFragment4 = ApproveFragment.newInstance(ApproveFragment.TAG4));
        viewDelegate.setFragments(fragments);

        initFilter();
        getUnReadCount();
    }

    /**
     * 得到未读数量
     */
    private void getUnReadCount() {
        model.queryApprovalCount(this, new ProgressSubscriber<ApproveUnReadCountResponseBean>(this) {
            @Override
            public void onNext(ApproveUnReadCountResponseBean unReadCountResponseBean) {
                super.onNext(unReadCountResponseBean);
                ApproveUnReadCountResponseBean.DataBean data = unReadCountResponseBean.getData();
                viewDelegate.setUnReadCount(data);
            }
        });
    }

    /**
     * 初始化筛选控件
     */
    public void initFilter() {
        filterFragment = new ApproveFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, currentType);
        filterFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, filterFragment).commit();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentType = position;
                filterFragment.setType(currentType);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                viewDelegate.openDrawer();
                break;
            case 1:
                CommonUtil.startActivtiy(this, ApproveTypeActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!viewDelegate.closeDrawer()) {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean messageBean) {
        int responseCode = messageBean.getCode();
        if (responseCode == ApproveConstants.REFRESH) {
            getUnReadCount();
        }
    }

}
