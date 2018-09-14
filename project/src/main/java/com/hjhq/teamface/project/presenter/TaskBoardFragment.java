package com.hjhq.teamface.project.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.presenter.navigation.AddGroupActivity;
import com.hjhq.teamface.project.presenter.navigation.TaskGroupActivity;
import com.hjhq.teamface.project.ui.TaskBoardDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * 任务看板
 *
 * @author Administrator
 * @date 2018/4/10
 */
public class TaskBoardFragment extends FragmentPresenter<TaskBoardDelegate, ProjectModel> {
    private ProjectDetailActivity mActivity;
    private ArrayList<NodeBean> dataList;

    @Override
    public void init() {
        mActivity = (ProjectDetailActivity) getActivity();
        getAllNode();
    }

    /**
     * 获取所有节点
     */
    private void getAllNode() {
        model.getAllNode(mActivity, mActivity.projectId, new ProgressSubscriber<AllNodeResultBean>(mActivity) {
            @Override
            public void onNext(AllNodeResultBean baseBean) {
                super.onNext(baseBean);
                dataList = baseBean.getData().getDataList();
                viewDelegate.initNavigator(getChildFragmentManager(), dataList);
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v ->
                PopUtils.showBottomMenu(getActivity(), viewDelegate.getRootView(), null, new String[]{"新增分组", "管理分组"},
                        position -> {
                            Bundle bundle = new Bundle();
                            if (position == 0) {
                                if (!ProjectUtil.INSTANCE.checkProjectPermission(mActivity,mActivity.priviledgeIds, 16)) {
                                    return true;
                                }
                                bundle.putLong(ProjectConstants.PROJECT_ID, mActivity.projectId);
                                CommonUtil.startActivtiy(getContext(), AddGroupActivity.class, bundle);
                            } else {
                                bundle.putSerializable(Constants.DATA_TAG1, dataList);
                                bundle.putLong(ProjectConstants.PROJECT_ID, mActivity.projectId);
                                CommonUtil.startActivtiyForResult(getContext(), TaskGroupActivity.class, ProjectConstants.MAIN_NODE_SORT_REQUEST_CODE, bundle);
                            }
                            return true;
                        }), R.id.iv_navigation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ProjectConstants.MAIN_NODE_SORT_REQUEST_CODE) {
            List<NodeBean> nodeList = (List<NodeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            ArrayList<NodeBean> clone = (ArrayList<NodeBean>) dataList.clone();
            //循环判断位置改变
            for (int i = 0; i < clone.size(); i++) {
                NodeBean oldNode = clone.get(i);
                for (int j = 0; j < nodeList.size(); j++) {
                    NodeBean newNode = nodeList.get(j);
                    if (oldNode.getId() == newNode.getId()) {
                        dataList.set(j, clone.get(i));
                        break;
                    }
                }
            }
            refreshNode();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        //筛选
        if (ProjectConstants.PROJECT_TASK_FILTER_CODE == event.getCode()) {
            String sortField = event.getTag();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("queryType", type);
            jsonObject.put("sortField", sortField);
            jsonObject.put("queryWhere", event.getObject());
            model.queryProjectTask(mActivity, jsonObject, new ProgressSubscriber<BaseBean>(mActivity){});
            return;
        }
        Object object = event.getObject();
        if (!(object instanceof NodeBean)) {
            return;
        }
        NodeBean nodeBean = (NodeBean) object;
        switch (event.getCode()) {
            case EventConstant.PROJECT_MAIN_NODE_ADD_TAG:
                //主节点新增
                model.getAllNode(mActivity, mActivity.projectId, new ProgressSubscriber<AllNodeResultBean>(mActivity) {
                    @Override
                    public void onNext(AllNodeResultBean baseBean) {
                        super.onNext(baseBean);
                        ArrayList<NodeBean> nodeList = baseBean.getData().getDataList();
                        dataList.clear();
                        dataList.addAll(nodeList);
                        refreshNode();
                    }
                });
                break;
            case EventConstant.PROJECT_MAIN_NODE_EDIT_TAG:
                //主节点编辑
                int editIndex = indexOf(nodeBean.getMain_id());
                if (editIndex < 0) {
                    return;
                }
                dataList.get(editIndex).setName(nodeBean.getName());
                viewDelegate.refreshNode(dataList);
                break;
            case EventConstant.PROJECT_MAIN_NODE_DEL_TAG:
                //主节点删除
                int delIndex = indexOf(nodeBean.getMain_id());
                if (delIndex < 0) {
                    return;
                }
                dataList.remove(delIndex);
                refreshNode();
                break;
            default:
                break;
        }
    }

    /**
     * 刷新节点
     */
    private void refreshNode() {
        viewDelegate.refreshNode(dataList);
        viewDelegate.mAdapter.notifyDataSetChanged();
        viewDelegate.mViewPager.setCurrentItem(0);
    }

    /**
     * 判断位置
     */
    private int indexOf(long id) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

}
