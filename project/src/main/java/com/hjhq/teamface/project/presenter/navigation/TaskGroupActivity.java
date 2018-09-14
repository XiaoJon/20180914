package com.hjhq.teamface.project.presenter.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.NavigationAdapter;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.SortNodeRequestBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.navigation.TaskGroupDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

/**
 * 管理分组
 *
 * @author Administrator
 * @date 2018/4/16
 */
public class TaskGroupActivity extends ActivityPresenter<TaskGroupDelegate, ProjectModel> {
    private List<NodeBean> dataList;
    private NavigationAdapter adapter;
    private long projectId;
    /**
     * 拖拽开始位置
     */
    private int dragStartPos;
    /**
     * 是否拖拽过
     */
    private boolean isDrag;
    private String priviledgeIds;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            dataList = (List<NodeBean>) intent.getSerializableExtra(Constants.DATA_TAG1);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
        }
    }

    @Override
    public void init() {
        adapter = new NavigationAdapter(dataList);
        getProjectRoleInfo();
        viewDelegate.setAdapter(adapter);
    }

    /**
     * 开启拖拽
     */
    private void openDrag() {
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(viewDelegate.mRecyclerView);
        // 开启拖拽
        adapter.enableDragItem(itemTouchHelper);

        adapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                dragStartPos = pos;
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                if (dragStartPos != pos) {
                    nodeSort();
                }
            }
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
                priviledgeIds = queryManagerRoleResultBean.getData().getPriviledge().getPriviledge_ids();

                if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 18)) {
                    openDrag();
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
     * 节点排序
     */
    private void nodeSort() {
        SortNodeRequestBean bean = new SortNodeRequestBean();
        bean.setProjectId(projectId);
        bean.setDataList(dataList);
        model.sortMainNode(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                isDrag = true;
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            dragSetResult();
            finish();
        });
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                if (!ProjectUtil.INSTANCE.checkProjectPermission(mContext,priviledgeIds, 17)) {
                    return;
                }
                Bundle bundle = new Bundle();
                NodeBean dataListBean = dataList.get(position);
                bundle.putString(Constants.DATA_TAG2, dataListBean.getName());
                bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                bundle.putLong(ProjectConstants.NODE_ID, dataListBean.getId());
                CommonUtil.startActivtiy(mContext, EditGroupActivity.class, bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (!ProjectUtil.INSTANCE.checkProjectPermission(mContext,priviledgeIds, 19)) {
                    return;
                }
                NodeBean nodeBean = (NodeBean) adapter.getItem(position);
                String name = nodeBean.getName();
                SpannableStringBuilder ssb = new SpannableStringBuilder("确定要删除分组 “" + name + "” 吗？删除后该分组下的所有列表和任务将同时被删除。");
                ssb.setSpan(new ForegroundColorSpan(ColorUtils.hexToColor("#0F0F0F")), 8, 8 + name.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                DialogUtils.getInstance().inputDialog(TaskGroupActivity.this, getString(R.string.del), ssb, "请输入要删除的任务分组名称", viewDelegate.getRootView(), content -> {
                    if (TextUtil.isEmpty(content)) {
                        ToastUtils.showError(mContext, "请输入分组名称");
                        return false;
                    }
                    if (!name.equals(content)) {
                        ToastUtils.showError(mContext, "分组名称输入错误！");
                        return false;
                    }

                    delNote(position);
                    return true;
                });
            }
        });
    }

    /**
     * 删除节点
     */
    private void delNote(int position) {
        long id = adapter.getItem(position).getId();
        model.deleteMainNode(this, projectId, id, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(mContext, "删除成功!");
                adapter.remove(position);

                NodeBean bean = new NodeBean(projectId, id);
                EventBusUtils.sendEvent(new MessageBean(EventConstant.PROJECT_MAIN_NODE_DEL_TAG, id + "", bean));
            }
        });
    }

    @Override
    public void onBackPressed() {
        dragSetResult();
        super.onBackPressed();
    }

    private void dragSetResult() {
        if (isDrag && !CollectionUtils.isEmpty(dataList)) {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, (Serializable) dataList);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        Object object = event.getObject();
        if (!(object instanceof NodeBean)) {
            return;
        }
        NodeBean nodeBean = (NodeBean) object;
        switch (event.getCode()) {
            case EventConstant.PROJECT_MAIN_NODE_EDIT_TAG:
                //主节点编辑
                String id = event.getTag();
                int editIndex = indexOf(TextUtil.parseLong(id));
                if (editIndex < 0) {
                    return;
                }
                dataList.get(editIndex).setName(nodeBean.getName());
                adapter.notifyItemChanged(editIndex);
                break;
            default:
                break;
        }
    }

    private int indexOf(long id) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
