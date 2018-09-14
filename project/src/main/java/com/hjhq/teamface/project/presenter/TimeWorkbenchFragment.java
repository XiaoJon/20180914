package com.hjhq.teamface.project.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.bean.TaskInfoBean;
import com.hjhq.teamface.componentservice.project.WorkBenchService;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.TaskItemAdapter;
import com.hjhq.teamface.project.bean.PersonalTaskRoleResultBan;
import com.hjhq.teamface.project.bean.QueryTaskCompleteAuthResultBean;
import com.hjhq.teamface.project.bean.TimeWorkbenchResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.WorkbenchDelegate;
import com.hjhq.teamface.project.util.ProjectDialog;
import com.hjhq.teamface.project.util.TaskHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragItemRecyclerView;

import java.util.List;

/**
 * 时间工作台
 *
 * @author Administrator
 * @date 2018/5/14
 */

public class TimeWorkbenchFragment extends FragmentPresenter<WorkbenchDelegate, ProjectModel> {
    private WorkBenchService mWorkBenchService;
    private RxAppCompatActivity mContext;

    @Override
    protected void init() {
        mContext = (RxAppCompatActivity) getActivity();
        initData();
    }

    private void initData() {
        for (int workbenchType = 1; workbenchType <= ProjectConstants.WORK_BENCH_INDICATOR_COUNT; workbenchType++) {
            addTaskList();
        }
    }

    /**
     * 添加任务列表
     */
    private void addTaskList() {
        TaskItemAdapter listAdapter = new TaskItemAdapter(null, true);
        DragItemRecyclerView dragItemRecyclerView = viewDelegate.mBoardView.addColumnList(listAdapter, null, null, null, false);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        int recyclerHeight = (int) (ScreenUtils.getScreenHeight(getContext()) - DeviceUtils.dpToPixel(getContext(), 135) - statusBarHeight);
        ViewGroup.LayoutParams layoutParams = dragItemRecyclerView.getLayoutParams();
        layoutParams.height = recyclerHeight;
        dragItemRecyclerView.setLayoutParams(layoutParams);

        listAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DragItemAdapter adapter, View view, int position) {
                TaskInfoBean item = listAdapter.getItem(position);
                TaskHelper.INSTANCE.clickTaskItem(mContext, item);
            }

            @Override
            public void onItemChildClick(DragItemAdapter adapter, View view, int position) {
                TaskInfoBean item = listAdapter.getItem(position);
                boolean completeStatus = "1".equals(item.getComplete_status());

                if (item.getProject_id() == 0) {
                    //个人任务
                    new TaskModel().queryPersonalTaskRole(mContext, item.getId(), item.getFromType(), new ProgressSubscriber<PersonalTaskRoleResultBan>(mContext) {
                        @Override
                        public void onNext(PersonalTaskRoleResultBan baseBean) {
                            super.onNext(baseBean);
                            String taskRole = baseBean.getData().getRole();
                            if ("2".equals(taskRole)) {
                                ToastUtils.showError(mContext, "没有权限");
                            } else {
                                ProjectDialog.updatePersonalTaskStatus(item.getFromType() == 1, mContext, viewDelegate.getRootView()
                                        , item.getId(), false, new ProgressSubscriber<BaseBean>(mContext, 1) {
                                            @Override
                                            public void onNext(BaseBean baseBean) {
                                                super.onNext(baseBean);
                                                listAdapter.removeItem(position);
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    //项目任务
                    new TaskModel().queryTaskCompleteAuth(mContext, item.getProject_id(), "".equals(item.getTask_id()) ? 1 : 2, item.getTaskInfoId(), new ProgressSubscriber<QueryTaskCompleteAuthResultBean>(mContext) {
                        @Override
                        public void onNext(QueryTaskCompleteAuthResultBean queryTaskAuthResultBean) {
                            super.onNext(queryTaskAuthResultBean);
                            QueryTaskCompleteAuthResultBean.DataBean data = queryTaskAuthResultBean.getData();
                            if (TextUtil.isEmpty(data.getFinish_task_role()) || "0".equals(data.getFinish_task_role())) {
                                ToastUtils.showError(mContext, "没有权限");
                                return;
                            }
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", item.getTaskInfoId());
                            jsonObject.put("completeStatus", completeStatus ? 0 : 1);
                            //更新状态
                            ProjectDialog.updateTaskStatus(item.getBeanType() == 3, mContext, viewDelegate.getRootView()
                                    , jsonObject, false, null, new ProgressSubscriber<BaseBean>(mContext, 1) {
                                        @Override
                                        public void onNext(BaseBean baseBean) {
                                            super.onNext(baseBean);
                                            listAdapter.removeItem(position);
                                        }
                                    });
                        }
                    });
                }
            }

            @Override
            public void onItemLongClick(DragItemAdapter adapter, View view, int position) {
            }
        });
    }

    /**
     * 获取工作台下的任务列表
     */
    public void refreshColumn() {
        for (int workbenchType = 1; workbenchType <= ProjectConstants.WORK_BENCH_INDICATOR_COUNT; workbenchType++) {
            int finalWorkbenchType = workbenchType;
            model.queryTimeWorkbench(mContext, workbenchType, new ProgressSubscriber<TimeWorkbenchResultBean>(mContext, false) {
                @Override
                public void onNext(TimeWorkbenchResultBean baseBean) {
                    super.onNext(baseBean);
                    List<TaskInfoBean> dataList = baseBean.getData().getDataList();
                    viewDelegate.mBoardView.getAdapter(finalWorkbenchType - 1).setItemList(dataList);
                }
            });
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
//                Toast.makeText(getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
//                    Toast.makeText(getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
                JSONObject jsonObject = new JSONObject();

                DragItemAdapter adapter = viewDelegate.mBoardView.getAdapter(toColumn);
                List<TaskInfoBean> itemList = adapter.getItemList();
                jsonObject.put("dataList", itemList);
                if (fromColumn == toColumn) {
                    //同一列中拖拽
                    if (fromRow != toRow) {
                        model.sortTimeWorkbench(mContext, jsonObject, new ProgressSubscriber<BaseBean>(mContext) {
                            @Override
                            public void onError(Throwable e) {
                                dismissWindowView();
                                e.printStackTrace();
                                ToastUtils.showError(mContext, R.string.project_task_move_failed);
                            }
                        });
                    }
                } else {
                    jsonObject.put("timeId", itemList.get(toRow).getId());
                    jsonObject.put("workbenchTag", toColumn + 1);
                    model.moveTimeWorkbench(mContext, jsonObject, new ProgressSubscriber<BaseBean>(mContext) {
                        @Override
                        public void onError(Throwable e) {
                            dismissWindowView();
                            e.printStackTrace();
                            ToastUtils.showError(mContext, R.string.project_task_move_failed);
                        }
                    });
                }
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
//                Toast.makeText(getContext(), "Position changed - column: " + newColumn + " row: " + newRow, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
            }

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
//                Toast.makeText(getContext(), "Focused column changed from " + oldColumn + " to " + newColumn, Toast.LENGTH_SHORT).show();
                if (mWorkBenchService != null) {
                    mWorkBenchService.setTextIndex(newColumn);
                }
            }

            @Override
            public void onColumnDragStarted(int position) {
//                Toast.makeText(getContext(), "Column drag started from " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
//                Toast.makeText(getContext(), "Column changed from " + oldPosition + " to " + newPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragEnded(int position) {
//                Toast.makeText(getContext(), "Column drag ended at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        viewDelegate.mBoardView.setBoardCallback(new BoardView.BoardCallback() {
            @Override
            public boolean canDragItemAtPosition(int column, int dragPosition) {
                if (column == 0) {
                    return false;
                }
                if (mWorkBenchService != null) {
                    mWorkBenchService.canDragItemAtPosition(column, dragPosition);
                }
                return true;
            }

            @Override
            public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                if (oldColumn == 0 || newColumn == 0) {
                    return false;
                }
                if (mWorkBenchService != null) {
                    mWorkBenchService.canDropItemAtPosition(oldColumn, oldRow, newColumn, newRow);
                }
                return true;
            }
        });
    }


    public void setWorkBenchService(WorkBenchService service) {
        this.mWorkBenchService = service;
    }

    /**
     * 看板滚动
     *
     * @param index
     */
    public void scrollToColumn(int index) {
        viewDelegate.mBoardView.scrollToColumn(index, true);
    }
}
