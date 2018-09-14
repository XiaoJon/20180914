package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.adapter.FileNaviAdapter;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectFolderListAdapter;
import com.hjhq.teamface.project.bean.ProjectFolderListBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.presenter.add.ProjectAddFolderActivity;
import com.hjhq.teamface.project.ui.ProjectFolderListActivityDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目文件
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/project_group_folder", desc = "项目文件(分组列表)")
public class ProjectGroupFolderListActivity extends ActivityPresenter<ProjectFolderListActivityDelegate, ProjectModel2> {
    private BaseQuickAdapter mAdapter;
    String[] menu;
    private String mProjectId;
    private String mFolderName;
    private boolean mIsCreator;
    private String mFolderType;
    private String priviledgeIds;
    private String mDataId;
    private List<ProjectFolderListBean.DataBean.DataListBean> mDataList = new ArrayList<>();
    private FileNaviAdapter naviAdapter;
    private List<FolderNaviData> naviDataList = new ArrayList<>();
    /**
     * 总页数
     */
    private int totalPages = 1;
    private int currentPageNo = 1;
    private int state = Constants.NORMAL_STATE;


    @Override
    public void init() {
        //项目数据id
        mDataId = getIntent().getStringExtra(Constants.DATA_TAG1);
        mIsCreator = getIntent().getBooleanExtra(Constants.DATA_TAG2, false);
        mFolderType = getIntent().getStringExtra(Constants.DATA_TAG3);
        mFolderName = getIntent().getStringExtra(Constants.DATA_TAG4);
        mProjectId = getIntent().getStringExtra(Constants.DATA_TAG5);
        naviDataList = (List<FolderNaviData>) getIntent().getSerializableExtra(Constants.DATA_TAG6);
        viewDelegate.setTitle(mFolderName);
        //初始导航数据
        initNaviData();
        //权限控制
        if (ProjectConstants.USER_FOLDER.equals(mFolderType)) {
            viewDelegate.setRightMenuIcons(R.drawable.icon_plus_gray, R.drawable.icon_menu);
            viewDelegate.showMenu();
            getProjectRoleInfo();
        }
        mAdapter = new ProjectFolderListAdapter(mDataList);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mDataList.get(position).getData_id());
                bundle.putBoolean(Constants.DATA_TAG2, true);
                bundle.putString(Constants.DATA_TAG3, mDataList.get(position).getLibrary_type());
                bundle.putString(Constants.DATA_TAG4, mDataList.get(position).getFile_name());
                bundle.putString(Constants.DATA_TAG5, mProjectId);
                ArrayList<FolderNaviData> list = new ArrayList<FolderNaviData>();
                List<FolderNaviData> data = naviAdapter.getData();
                list.addAll(data);
                FolderNaviData data2 = new FolderNaviData();
                data2.setFloderType(TextUtil.parseInt(mDataList.get(position).getLibrary_type()));
                data2.setFolderId(mProjectId);
                data2.setFolderName(mDataList.get(position).getFile_name());
                data2.setFolderLevel(2);
                list.add(data2);
                bundle.putSerializable(Constants.DATA_TAG6, list);
                CommonUtil.startActivtiyForResult(mContext, ProjectFolderListActivity.class, Constants.REQUEST_CODE2, bundle);

            }
        });
        getNetData(true);
    }

    private void initNaviData() {
        if (naviDataList != null && naviDataList.size() > 0) {
            viewDelegate.get(R.id.rv_navi).setVisibility(View.VISIBLE);
            naviAdapter = new FileNaviAdapter(naviDataList);
            viewDelegate.setNaviAdapter(naviAdapter);
        } else {
            viewDelegate.get(R.id.rv_navi).setVisibility(View.GONE);
            naviDataList = new ArrayList<>();
            naviAdapter = new FileNaviAdapter(naviDataList);
            viewDelegate.setNaviAdapter(naviAdapter);
        }
    }

    /**
     * 添加文件夹"22, "编辑文件夹", 23, "删除文件" :24
     */
    public void getProjectRoleInfo() {
        //查询管理员权限
        new TaskModel().queryManagementRoleInfo(mContext, TextUtil.parseLong(SPHelper.getEmployeeId()), TextUtil.parseLong(mProjectId), new ProgressSubscriber<QueryManagerRoleResultBean>(mContext) {
            @Override
            public void onNext(QueryManagerRoleResultBean queryManagerRoleResultBean) {
                super.onNext(queryManagerRoleResultBean);
                QueryManagerRoleResultBean.DataBean.PriviledgeBean priviledge = queryManagerRoleResultBean.getData().getPriviledge();
                priviledgeIds = priviledge.getPriviledge_ids();
                if (TextUtils.isEmpty(priviledgeIds)) {
                    viewDelegate.showMenu();
                } else {
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 29) && ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 30)) {
                        menu = new String[]{"编辑", "删除"};
                        viewDelegate.showMenu(0, 1);
                        return;
                    }
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 29)) {
                        menu = new String[]{"编辑"};
                        viewDelegate.showMenu(0);
                        return;
                    }
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 30)) {
                        menu = new String[]{"删除"};
                        viewDelegate.showMenu(1);
                        return;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目角色权限失败");
            }
        });
    }

    /**
     * 获取文件列表
     */
    private void getNetData(boolean showLoading) {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.getProjectFolderList(mContext, TextUtil.parseLong(mDataId), Constants.PAGESIZE, pageNo,
                new ProgressSubscriber<ProjectFolderListBean>(mContext, showLoading) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (state == Constants.LOAD_STATE) {
                            mAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onNext(ProjectFolderListBean baseBean) {
                        super.onNext(baseBean);
                        showDataResult(baseBean);
                    }
                });

    }

    private void showDataResult(ProjectFolderListBean projectListBean) {
        List<ProjectFolderListBean.DataBean.DataListBean> dataList = projectListBean.getData().getDataList();

        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), dataList);
                break;
            case Constants.LOAD_STATE:
                mAdapter.addData(dataList);
                mAdapter.loadMoreComplete();
                break;
            default:
                break;
        }
        PageInfo pageInfo = projectListBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //导航栏的点击事件
        viewDelegate.setNaviClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                EventBusUtils.sendEvent(new MessageBean(position, Constants.JUMP_FOLDER, null));
            }
        });
        viewDelegate.get(R.id.search_layout).setVisibility(View.GONE);
        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        mAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getNetData(true);
        }, viewDelegate.mRecyclerView);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        state = Constants.REFRESH_STATE;
        getNetData(false);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeFolder(MessageBean bean) {
        if (bean != null && Constants.JUMP_FOLDER.equals(bean.getTag()) && bean.getCode() < 1) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                /*Bundle bundle = new Bundle();
                bundle.putLong(Constants.DATA_TAG1, TextUtil.parseLong(mProjectId));
                bundle.putString(Constants.DATA_TAG2, mFolderId);
                bundle.putInt(ProjectConstants.EDIT_FOLDER_TYPE, ProjectConstants.ADD_SUB_FOLDER);
                CommonUtil.startActivtiyForResult(mContext, ProjectAddFolderActivity.class, Constants.REQUEST_CODE2, bundle);
*/
                break;
            case 1:
                showMenu();
                break;
            default:

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * 更多菜单
     */
    private void showMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "", menu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (menu[p]) {
                    case "编辑":
                        Bundle bundle = new Bundle();
                        bundle.putInt(ProjectConstants.EDIT_FOLDER_TYPE, ProjectConstants.EDIT_FOLDER);
                        bundle.putLong(Constants.DATA_TAG1, TextUtil.parseLong(mProjectId));
                        bundle.putString(Constants.DATA_TAG2, mDataId);
                        bundle.putString(Constants.DATA_TAG3, mFolderName);
                        CommonUtil.startActivtiyForResult(mContext, ProjectAddFolderActivity.class, Constants.REQUEST_CODE3, bundle);
                        break;
                    case "删除":
                        DialogUtils.getInstance().sureOrCancel(mContext, "", "确定要删除该文件夹吗？", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                deleteProjectFolder();
                            }
                        });
                        break;
                    default:

                        break;
                }

                return false;
            }
        });


    }

    /**
     * 删除项目文件夹
     */
    private void deleteProjectFolder() {
        /*model.deleteProjectFolder(mContext, mFolderId, mProjectId, mFolderName,
                new ProgressSubscriber<BaseBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        setResult(RESULT_OK);
                        finish();
                    }
                });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Constants.REQUEST_CODE3:
                    if (data != null) {
                        String name = data.getStringExtra(Constants.DATA_TAG1);
                        viewDelegate.setTitle(name);
                        setResult(RESULT_OK);
                    }
                    break;
                case Constants.REQUEST_CODE2:
                    refreshData();
                    break;
                case Constants.REQUEST_CODE1:

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
