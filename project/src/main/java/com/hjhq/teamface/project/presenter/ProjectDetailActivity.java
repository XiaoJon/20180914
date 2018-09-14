package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.ui.dynamic.DynamicActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.QuerySettingResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.presenter.add.ProjectAddFolderActivity;
import com.hjhq.teamface.project.presenter.add.ProjectAddShareActivity;
import com.hjhq.teamface.project.presenter.filter.FilterFragment;
import com.hjhq.teamface.project.ui.ProjectDetailDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.hjhq.teamface.common.utils.CommonUtil.startActivtiy;

/**
 * 项目详情
 *
 * @author Administrator
 * @date 2018/4/12
 */

public class ProjectDetailActivity extends ActivityPresenter<ProjectDetailDelegate, ProjectModel> implements View.OnClickListener {
    static final String[] MORE_FUNCTION = {"项目设置", "成员管理", "项目标签", "项目动态"};
    private FragmentAdapter mAdapter;
    private List<Fragment> fragments;
    public long projectId;
    public String priviledgeIds;
    public String projectTimeStatus;
    public String projectCompleteStatus;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
        }
        if (projectId == 0) {
            ToastUtils.showError(this, "项目ID为空");
            finish();
        }
    }

    @Override
    public void init() {
        getProjectInfo();
        initFilter();

        fragments = new ArrayList<>(4);
        fragments.add(new TaskBoardFragment());
        fragments.add(ProjectShareListFragment.newInstance(projectId));
        fragments.add(new ProjectFolderListFragment(projectId));
        fragments.add(new ProjectFileListFragment());

        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewDelegate.setAdapter(mAdapter);
        getProjectRoleInfo();
    }

    /**
     * 获取项目信息
     */
    private void getProjectInfo() {
        model.querySettingById(mContext, projectId, new ProgressSubscriber<QuerySettingResultBean>(mContext) {
            @Override
            public void onNext(QuerySettingResultBean querySettingResultBean) {
                super.onNext(querySettingResultBean);
                QuerySettingResultBean.DataBean data = querySettingResultBean.getData();
                if (data.getStar_level() == 1) {
                    viewDelegate.showMenu(0, 2, 3);
                }
                viewDelegate.setToolTitle(data.getName());
                projectTimeStatus = data.getProject_time_status();
                projectCompleteStatus = data.getProject_complete_status();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目信息失败");
            }
        });
    }

    /**
     * 初始化筛选控件
     */
    public void initFilter() {
        Fragment fragment = FilterFragment.newInstance(1, projectId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.rl_task, R.id.rl_share, R.id.rl_statistic, R.id.rl_library);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.rl_task) {
            viewDelegate.showMenu(1, 2, 3);
            viewDelegate.setCurrentItem(0);
        } else if (viewId == R.id.rl_share) {
            viewDelegate.showMenu(5);
            viewDelegate.setCurrentItem(1);
        } else if (viewId == R.id.rl_library) {
           /* if (taskAuths != null && taskAuths[1]) {
                viewDelegate.showMenu(4);
            } else {
                viewDelegate.showMenu();
            }*/
            if (TextUtils.isEmpty(priviledgeIds)) {
                viewDelegate.showMenu();
            } else {
                if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 31)) {
                    viewDelegate.showMenu(4);
                } else {
                    viewDelegate.showMenu();
                }
            }

            viewDelegate.setCurrentItem(2);
        } else if (viewId == R.id.rl_statistic) {
            viewDelegate.showMenu(1, 2, 3);
            viewDelegate.setCurrentItem(3);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                updateStar(0);
                break;
            case 1:
                updateStar(1);
                break;
            case 2:
                viewDelegate.openDrawer();
                break;
            case 3:
                moreFunction();
                break;
            case 4:
                //添加文件夹
                // TODO: 2018/7/5 判断权限
                addFolder();
                break;
            case 5:
                //添加分享
                // TODO: 2018/7/5 判断权限
                addShare();
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

    private void addShare() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG2, ProjectConstants.TYPE_ADD_SHARE);
        bundle.putString(Constants.DATA_TAG3, projectId + "");
        CommonUtil.startActivtiyForResult(mContext, ProjectAddShareActivity.class, Constants.REQUEST_CODE9, bundle);
    }

    /**
     * 新建文件夹
     */
    private void addFolder() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.DATA_TAG1, projectId);
        bundle.putInt(ProjectConstants.EDIT_FOLDER_TYPE, ProjectConstants.ADD_FOLDER);
        CommonUtil.startActivtiyForResult(mContext, ProjectAddFolderActivity.class, Constants.REQUEST_CODE2, bundle);

    }

    /**
     * 更改星标
     *
     * @param starLevel 星标 0为否，1为是
     */
    private void updateStar(int starLevel) {
        model.updateStar(mContext, projectId, starLevel, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean bean) {
                super.onNext(bean);
                if (starLevel == 0) {
                    viewDelegate.showMenu(1, 2, 3);
                } else {
                    viewDelegate.showMenu(0, 2, 3);
                }
                EventBusUtils.sendEvent(new MessageBean(ProjectConstants.PROJECT_INFO_EDIT_EVENT_CODE, null, null));
            }
        });
    }

    /**
     * 更多功能
     */
    private void moreFunction() {
        PopUtils.showBottomMenu(this, viewDelegate.getRootView(), null, MORE_FUNCTION, p -> {
            Bundle bundle = new Bundle();
            bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
            switch (p) {
                case 0:
                    startActivtiy(mContext, EditProjectActivity.class, bundle);
                    break;
                case 1:
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    startActivtiy(mContext, ProjectMemberActivity.class, bundle);
                    break;
                case 2:
                    bundle.putInt(Constants.DATA_TAG1, ProjectConstants.EDIT_MODE);
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    startActivtiy(mContext, ProjectLabelsActivity.class, bundle);
                    break;
                case 3:
                    //动态
                    bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_DYNAMIC_BEAN_NAME);
                    bundle.putLong(Constants.DATA_TAG1, projectId);
                    CommonUtil.startActivtiy(mContext, DynamicActivity.class, bundle);
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    /**
     * 获取项目角色信息
     */
    public void getProjectRoleInfo() {
        //查询管理员权限
        new TaskModel().queryManagementRoleInfo(mContext, TextUtil.parseLong(SPHelper.getEmployeeId()), projectId, new ProgressSubscriber<QueryManagerRoleResultBean>(mContext) {
            @Override
            public void onNext(QueryManagerRoleResultBean queryManagerRoleResultBean) {
                super.onNext(queryManagerRoleResultBean);
                QueryManagerRoleResultBean.DataBean.PriviledgeBean priviledge = queryManagerRoleResultBean.getData().getPriviledge();
                priviledgeIds = priviledge.getPriviledge_ids();
                RxManager.$(ProjectDetailActivity.this.hashCode()).post(ProjectConstants.PROJECT_ROLE_TAG, priviledgeIds);
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目角色权限失败");
            }
        });
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean event) {
        if (ProjectConstants.PROJECT_TASK_FILTER_CODE == event.getCode()) {
            viewDelegate.closeDrawer();
        }
    }
}
