package com.hjhq.teamface.project.serviceimpl;

import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.componentservice.project.ProjectService;
import com.hjhq.teamface.componentservice.project.WorkBenchService;
import com.hjhq.teamface.project.adapter.AppAdapter;
import com.hjhq.teamface.project.presenter.TimeWorkbenchFragment;

import java.util.List;


/**
 * 自定义组件对外接口实现
 *
 * @author Administrator
 * @date 2018/3/26
 */

public class ProjectServiceImpl implements ProjectService {
    @Override
    public Fragment getWorkbench() {
        return new TimeWorkbenchFragment();
    }

    @Override
    public void refreshColumn(Fragment fragment) {
        ((TimeWorkbenchFragment) fragment).refreshColumn();
    }


    @Override
    public void setWorkBenchService(Fragment fragment, WorkBenchService service) {
        ((TimeWorkbenchFragment) fragment).setWorkBenchService(service);
    }

    @Override
    public void clickIndex(Fragment fragment, int index) {
        ((TimeWorkbenchFragment) fragment).scrollToColumn(index);
    }

    @Override
    public BaseQuickAdapter getAppAdapter(List<AppModuleBean> data) {
        return new AppAdapter(data);
    }
}
