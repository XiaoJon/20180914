package com.hjhq.teamface.project.ui.task;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.QueryHierarchyResultBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import rx.Observable;

/**
 * 任务详情
 *
 * @author Administrator
 * @date 2018/6/25
 */

public class TaskDetailDelegate extends AppDelegate {
    private TextView tvProjectName;
    private TextView tvNodeName;
    private TextView tvSubNodeName;
    private TextView tvTaskName;
    private LinearLayout llContent;
    private ImageView ivSubTask;
    private ImageView ivRelevance;
    private ImageView ivBeRelevance;
    private View llAssociates;
    private View llRelevance;
    private View llBeRelevance;
    private View llSubTask;
    private ImageView ivAssociatesSpread;
    public RecyclerView subTaskRecyclerView;
    public RecyclerView relevanceRecyclerView;
    public RecyclerView beRelevanceRecyclerView;
    private View header;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvProgressSum;
    private ImageView ivLike;
    private TextView tvLikeNum;
    private TextView tvLike;
    private View ivTaskStatus;
    private ImageView ivCheckStatus;
    private View llCheck;
    public MembersView membersView;
    public SwitchButton sbtn;
    private TextView tvSubTempName;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_task_detail;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuIcons(R.drawable.icon_menu);
        showMenu();

        setTitle("详情");
        llContent = get(R.id.ll_task_layout);

        tvProjectName = get(R.id.tv_project_name);
        tvNodeName = get(R.id.tv_node_name);
        tvSubNodeName = get(R.id.tv_sub_node_name);
        tvSubTempName = get(R.id.tv_sub_temp_name);
        tvTaskName = get(R.id.tv_task_name);
        ivTaskStatus = get(R.id.iv_task_status);
        ivCheckStatus = get(R.id.iv_check_status);
        membersView = get(R.id.member_view);
        llCheck = get(R.id.ll_check);
        sbtn = get(R.id.sbtn_associates);

        llAssociates = get(R.id.ll_associates);
        ivAssociatesSpread = get(R.id.iv_associates_spread);
        subTaskRecyclerView = get(R.id.sub_task_recyler_view);
        ivSubTask = get(R.id.iv_sub_task);
        relevanceRecyclerView = get(R.id.relevance_recyler_view);
        ivRelevance = get(R.id.iv_relevance);
        beRelevanceRecyclerView = get(R.id.be_relevance_recyler_view);
        ivBeRelevance = get(R.id.iv_be_relevance);
        llRelevance = get(R.id.ll_relevance);
        llBeRelevance = get(R.id.ll_be_relevance);
        llSubTask = get(R.id.ll_sub_task);

        ivLike = get(R.id.iv_like);
        tvLike = get(R.id.tv_like);
        tvLikeNum = get(R.id.tv_like_num);

        header = View.inflate(mContext, R.layout.project_item_sub_task_header, null);
        progressBar = header.findViewById(R.id.progress_bar);
        tvProgress = header.findViewById(R.id.tv_progress);
        tvProgressSum = header.findViewById(R.id.tv_progress_sum);

        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        relevanceRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        beRelevanceRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        int naviWidth = (int) (ScreenUtils.getScreenWidth(mContext) / 4);
        tvNodeName.setMaxWidth(naviWidth);
        tvSubNodeName.setMaxWidth(naviWidth);
        tvProjectName.setMaxWidth(naviWidth);

    }


    public void setSubTaskHead(BaseQuickAdapter adapter, int progress) {
        List data = adapter.getData();
        int count = data.size();

        progressBar.setMax(count);
        progressBar.setProgress(progress);
        TextUtil.setText(tvProgress, progress + "");
        TextUtil.setText(tvProgressSum, "/" + count);

        adapter.setHeaderView(header);
    }

    /**
     * 设置子任务适配器
     *
     * @param adapter
     */
    public void setSubTaskAdapter(BaseQuickAdapter adapter) {
        subTaskRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置关联适配器
     *
     * @param adapter
     */
    public void setRelevanceAdapter(BaseQuickAdapter adapter) {
        relevanceRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置被关联适配器
     *
     * @param adapter
     */
    public void setBeRelevanceAdapter(BaseQuickAdapter adapter) {
        beRelevanceRecyclerView.setAdapter(adapter);
    }

    /**
     * 隐藏被关联
     */
    public void hideBeRelevance() {
        setVisibility(R.id.ll_be_relevance_root, false);
    }

    /**
     * 隐藏子任务
     */
    public void hideSubTask() {
        setVisibility(R.id.ll_sub_task_root, false);
    }

    public void setNavigatorLayout(QueryHierarchyResultBean.DataBean data) {
        TextUtil.setText(tvProjectName, data.getProjectname());
        TextUtil.setText(tvNodeName, data.getNodename());
        TextUtil.setText(tvSubNodeName, data.getSubnodename());

        String subTempName = data.getSubnodename2();
        TextUtil.setText(tvSubTempName, subTempName);
        setVisibility(R.id.iv_sub_temp, !TextUtil.isEmpty(subTempName));

    }

    public void hideNavigator() {
        setVisibility(R.id.ll_title, false);
    }

    public void drawLayout(TaskLayoutResultBean.DataBean.EnableLayoutBean enableLayout, String moduleBean, boolean personalTask) {
        llContent.removeAllViews();
        List<CustomBean> rows = enableLayout.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        for (CustomBean layoutBean : rows) {
            layoutBean.setModuleBean(moduleBean);
            ProjectCustomUtil.drawLayout(llContent, layoutBean, CustomConstants.DETAIL_STATE, personalTask);
        }
    }

    /**
     * 设置任务状态
     */
    public void setTaskStatus(boolean complete) {
        ivTaskStatus.setSelected(complete);
    }

    /**
     * 设置检验状态
     *
     * @param passedStatus
     */
    public void setCheckStatus(String passedStatus) {
        switch (passedStatus) {
            case ProjectConstants.CHECK_STATUS_WAIT:
                ivCheckStatus.setImageResource(R.drawable.project_icon_check_wait);
                break;
            case ProjectConstants.CHECK_STATUS_PASS:
                ivCheckStatus.setImageResource(R.drawable.project_icon_check_pass);
                break;
            case ProjectConstants.CHECK_STATUS_REJECT:
                ivCheckStatus.setImageResource(R.drawable.project_icon_check_reject);
                break;
            default:
                break;
        }
    }

    /**
     * 设置检验按钮是否显示
     *
     * @param visibility
     */
    public void setCheckBtnVis(int visibility) {
        llCheck.setVisibility(visibility);
    }

    /**
     * 设置任务标题
     *
     * @param title
     */
    public void setTaskTitle(String title) {
        Observable.just(1)
                .compose(TransformerHelper.applySchedulers())
                .subscribe(i -> TextUtil.setText(tvTaskName, title));
    }

    /**
     * 设置协作人是否可见
     */
    public void switchAssociates() {
        setVisibility(R.id.ll_associates, !isVisibili(llAssociates));
        ivAssociatesSpread.setSelected(!ivAssociatesSpread.isSelected());
    }

    /**
     * 设置子任务是否可见
     */
    public void switchSubTask() {
        setVisibility(R.id.ll_sub_task, !isVisibili(llSubTask));
        ivSubTask.setSelected(!ivSubTask.isSelected());
    }

    /**
     * 设置关联是否可见
     */
    public void switchRelevance() {
        setVisibility(R.id.ll_relevance, !isVisibili(llRelevance));
        ivRelevance.setSelected(!ivRelevance.isSelected());
    }

    /**
     * 设置被关联是否可见
     */
    public void switchBeRelevance() {
        setVisibility(R.id.ll_be_relevance, !isVisibili(llBeRelevance));
        ivBeRelevance.setSelected(!ivBeRelevance.isSelected());
    }


    /**
     * 设置协作人数据
     */
    public void setAssociates(List<Member> members) {
        membersView.setMembers(members);
    }

    /**
     * 是否仅协作人可见
     *
     * @param isLook
     */
    public void setCheckedImmediatelyNoEvent(boolean isLook) {
        sbtn.setCheckedImmediatelyNoEvent(isLook);
    }

    public boolean isVisibili(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置点赞状态
     */
    public void setLikeStatus() {
        boolean selected = ivLike.isSelected();
        setLikeStatus(!selected);
    }

    public void setLikeStatus(boolean selected) {
        ivLike.setSelected(selected);
        TextUtil.setText(tvLike, mContext.getString(selected ? R.string.project_liked : R.string.project_like));
    }

    /**
     * 是否已点赞
     *
     * @return
     */
    public boolean isLike() {
        return ivLike.isSelected();
    }

    /**
     * 设置点赞任务
     *
     * @param size
     */
    public void setLikeNum(int size) {
        TextUtil.setText(tvLikeNum, size + "个点赞");
    }


}
