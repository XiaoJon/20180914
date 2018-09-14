package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectLabelsAdapter;
import com.hjhq.teamface.common.bean.ProjectLabelBean;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.ProjectLabelsDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 项目标签
 * Created by Administrator on 2018/4/10.
 */
public class ProjectLabelsActivity extends ActivityPresenter<ProjectLabelsDelegate, ProjectModel2> {
    private ProjectLabelsAdapter mAdapter;
    private ArrayList<ProjectLabelBean> dataList = new ArrayList<>();
    private ArrayList<ProjectLabelBean> checkList = new ArrayList<>();
    private int mode;
    private long projectId;
    private String priviledgeIds;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            mode = getIntent().getIntExtra(Constants.DATA_TAG1, ProjectConstants.VIEW_MODE);
            projectId = getIntent().getLongExtra(ProjectConstants.PROJECT_ID, 0);
            Serializable serializableExtra = getIntent().getSerializableExtra(Constants.DATA_TAG2);
            if (serializableExtra != null) {
                checkList = (ArrayList<ProjectLabelBean>) serializableExtra;
            }
        }
    }

    @Override
    public void init() {
        initAdapter();
        getLabel();
        getProjectRoleInfo();
        initMenu();
    }

    private void initAdapter() {
        mAdapter = new ProjectLabelsAdapter(dataList);
        mAdapter.setMode(mode);
        viewDelegate.setAdapter(mAdapter);
    }

    /**
     * 初始化menu
     */
    private void initMenu() {
        switch (mode) {
            case ProjectConstants.VIEW_MODE:
                viewDelegate.showMenu();
                break;
            case ProjectConstants.SELECT_MODE:
                viewDelegate.setRightMenuTexts("确定");
                break;
            case ProjectConstants.EDIT_MODE:
                viewDelegate.setRightMenuTexts("添加");
                break;
        }
    }

    /**
     * 获取标签
     */
    private void getLabel() {
        if (ProjectConstants.SELECT_MODE == mode) {
            //全部标签
            model.queryAllLabel(mContext, projectId, 1, new ProgressSubscriber<ProjectLabelsListBean>(mContext, true) {
                @Override
                public void onNext(ProjectLabelsListBean baseBean) {
                    super.onNext(baseBean);
                    handleLabel(baseBean);
                }
            });
        } else {
            //项目标签
            model.getProjectLabel(mContext, projectId, 0, null, new ProgressSubscriber<ProjectLabelsListBean>(mContext, true) {
                @Override
                public void onNext(ProjectLabelsListBean baseBean) {
                    super.onNext(baseBean);
                    handleLabel(baseBean);
                }
            });
        }
    }

    /**
     * 处理标签数据
     *
     * @param baseBean
     */
    private void handleLabel(ProjectLabelsListBean baseBean) {
        dataList.clear();
        for (ProjectLabelBean bean : baseBean.getData()) {
            dataList.addAll(bean.getChildList());
        }
        for (ProjectLabelBean check : checkList) {
            Observable.from(dataList)
                    .filter(data -> check.getId().equals(data.getId()))
                    .subscribe(data -> data.setCheck(true));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 移除标签
     */
    private void removeLabel(int position) {
        List<ProjectLabelBean> data = mAdapter.getData();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (i != position) {
                if (TextUtils.isEmpty(sb)) {
                    sb.append(data.get(i).getId());
                } else {
                    sb.append(",").append(data.get(i).getId());
                }
            }
        }

        model.editProjectLabel(mContext, projectId + "", sb.toString(), new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean bean) {
                super.onNext(bean);
                getLabel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mode == ProjectConstants.SELECT_MODE) {
            ArrayList<ProjectLabelBean> list = new ArrayList<>();
            Observable.from(dataList).filter(ProjectLabelBean::isCheck).subscribe(data -> list.add(data));

            if (CollectionUtils.isEmpty(list)) {
                ToastUtils.showToast(mContext, "请选择标签");
                return true;
            }
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, list);
            setResult(RESULT_OK, intent);
            finish();
        } else if (mode == ProjectConstants.EDIT_MODE) {
            if (!ProjectUtil.INSTANCE.checkProjectPermission(mContext,priviledgeIds, 32)) {
                return super.onOptionsItemSelected(item);
            }
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, ProjectConstants.SELECT_MODE);
            bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
            bundle.putSerializable(Constants.DATA_TAG2, dataList);
            CommonUtil.startActivtiyForResult(mContext, ProjectLabelsActivity.class, Constants.REQUEST_CODE1, bundle);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setItemListener(new SimpleItemClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                int viewID = view.getId();
                if (viewID == R.id.rl_check) {
                    mAdapter.getData().get(position).switchCheck();
                    mAdapter.notifyItemChanged(position);
                }
                if (viewID == R.id.delete_icon) {
                    if (!ProjectUtil.INSTANCE.checkProjectPermission(mContext,priviledgeIds, 33)) {
                        return;
                    }
                    DialogUtils.getInstance().sureOrCancel(mContext, "", "确定要移除此标签吗?", viewDelegate.getRootView(),
                            () -> removeLabel(position));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && Constants.REQUEST_CODE1 == requestCode) {
            if (data != null) {
                ArrayList<ProjectLabelBean> list = (ArrayList<ProjectLabelBean>) data.getSerializableExtra(Constants.DATA_TAG1);
                StringBuilder sb = new StringBuilder();

                for (ProjectLabelBean bean : list) {
                    if (!TextUtils.isEmpty(sb)) {
                        sb.append(",");
                    }
                    sb.append(bean.getId());
                }

                model.editProjectLabel(mContext, projectId + "", sb.toString(), new ProgressSubscriber<BaseBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean bean) {
                        super.onNext(bean);
                        getLabel();
                    }
                });
            }
        }
    }


    /**
     * 获取项目角色信息
     */
    public void getProjectRoleInfo() {
        //查询管理员权限
        new TaskModel().queryManagementRoleInfo(this, TextUtil.parseLong(SPHelper.getEmployeeId()), projectId, new ProgressSubscriber<QueryManagerRoleResultBean>(this) {
            @Override
            public void onNext(QueryManagerRoleResultBean queryManagerRoleResultBean) {
                super.onNext(queryManagerRoleResultBean);
                priviledgeIds = queryManagerRoleResultBean.getData().getPriviledge().getPriviledge_ids();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目角色权限失败");
            }
        });
    }
}
