package com.hjhq.teamface.componentservice.project;

import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AppModuleBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/3/26
 */

public interface ProjectService {
    /**
     * 获取工作台
     *
     * @return 工作台Fragment 可拖拽
     */
    Fragment getWorkbench();

    /**
     * 刷新工作台数据
     *
     * @param fragment
     */
    void refreshColumn(Fragment fragment);

    void setWorkBenchService(Fragment fragment, WorkBenchService service);

    void clickIndex(Fragment fragment, int index);

    BaseQuickAdapter getAppAdapter(List<AppModuleBean> data);
}
