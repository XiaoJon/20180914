package com.hjhq.teamface.oa.approve.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.view.line.DashView;

import java.util.List;

/**
 * 审批流程适配器
 *
 * @author lx
 * @date 2017/5/15
 */

public class ApproveTaskItemAdapter extends BaseQuickAdapter<ProcessFlowResponseBean.DataBean, BaseViewHolder> {
    //颜色
    private final int RED_COLOR = ColorUtils.hexToColor("#F94C4A");
    private final int GRAY_COLOR = ColorUtils.hexToColor("#A0A0AE");
    private final int GREEN_COLOR = ColorUtils.hexToColor("#29AB91");

    public ApproveTaskItemAdapter(List<ProcessFlowResponseBean.DataBean> data) {
        super(R.layout.item_approve_task_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProcessFlowResponseBean.DataBean item) {
        View rlLayout = helper.getView(R.id.rl_layout);
        rlLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        //当前下标
        int position = helper.getAdapterPosition();
        DashView dashLineTop = helper.getView(R.id.dash_line_top);
        DashView dashLineBottom = helper.getView(R.id.dash_line_bottom);
        View overView = helper.getView(R.id.view_over);

        ImageView ivHead = helper.getView(R.id.iv_head_portrait);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvPosition = helper.getView(R.id.tv_position);
        TextView tvState = helper.getView(R.id.tv_state);
        TextView tvDesc = helper.getView(R.id.tv_desc);
        TextView tvApproveing = helper.getView(R.id.tv_approveing);
        TextView tvTime = helper.getView(R.id.tv_time);

        String taskKey = item.getTask_key();
        String approvalMessage = item.getApproval_message();
        //当前节点状态ID
        String taskStateId = item.getTask_status_id();
        //节点是否正常 0异常 不显示头像
        String normal = item.getNormal();

        //逻辑控制
        if ("endEvent".equals(taskKey)) {
            if (position != getItemCount() - 1) {
                //只要不是最后一个节点 的endEvent 都隐藏
                ViewGroup.LayoutParams layoutParams = rlLayout.getLayoutParams();
                layoutParams.height = 0;
                rlLayout.setLayoutParams(layoutParams);
                return;
            }

            dashLineBottom.setVisibility(View.GONE);
            tvPosition.setVisibility(View.GONE);
            tvState.setVisibility(View.GONE);
            tvDesc.setVisibility(View.GONE);
            tvApproveing.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            setText(tvName, approvalMessage);
            overView.setVisibility(View.VISIBLE);

            //审批结束节点
            switch (taskStateId) {
                case ApproveConstants.APPROVE_PASS:
                    setHeadImage(ivHead, R.drawable.icon_approve_pass, normal);
                    overView.setBackgroundResource(R.drawable.approve_task_flow_green_over_bg);
                    setDashLine(dashLineTop, false, GREEN_COLOR);
                    break;
                case ApproveConstants.APPROVE_REVOKED:
                    setHeadImage(ivHead, R.drawable.icon_approve_revoked, normal);
                    overView.setBackgroundResource(R.drawable.approve_task_flow_gray_over_bg);

                    setDashLine(dashLineTop, false, GRAY_COLOR);
                    break;
                case ApproveConstants.APPROVE_REJECT:
                    setHeadImage(ivHead, R.drawable.icon_approve_reject, normal);
                    overView.setBackgroundResource(R.drawable.approve_task_flow_red_over_bg);
                    setDashLine(dashLineTop, false, RED_COLOR);
                    break;
                default:
                    setDashLine(dashLineTop, true, GRAY_COLOR);
                    setHeadImage(ivHead, R.drawable.icon_approving, normal);
                    overView.setBackgroundResource(R.drawable.approve_task_flow_gray_over_bg);
                    break;
            }
        } else {
            // 1会签  2获签 3从角色中指定审批人
            String taskApprovalType = item.getTask_approval_type();
            if ("1".equals(taskApprovalType) || "2".equals(taskApprovalType) || "3".equals(taskApprovalType)) {
                setHeadImage(ivHead, R.drawable.icon_approve_unknow, normal);
                tvPosition.setVisibility(View.GONE);
                tvDesc.setVisibility(View.GONE);
                tvApproveing.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
            } else {
                ImageLoader.loadCircleImage(mContext, item.getApproval_employee_picture(), ivHead, item.getApproval_employee_name());
                if (!TextUtil.isEmpty(item.getApproval_employee_post())) {
                    TextUtil.setTextorVisibility(tvPosition, "(" + item.getApproval_employee_post() + ")");
                }

                //根据节点状态控制内容
                switch (taskStateId) {
                    case ApproveConstants.APPROVE_PENDING:
                        tvApproveing.setVisibility(View.GONE);
                        tvDesc.setVisibility(View.GONE);
                        tvTime.setVisibility(View.GONE);
                        break;
                    case ApproveConstants.APPROVING:
                        setText(tvApproveing, approvalMessage);
                        tvDesc.setVisibility(View.GONE);
                        tvTime.setVisibility(View.GONE);
                        break;
                    default:
                        setText(tvDesc, approvalMessage);
                        tvApproveing.setVisibility(View.GONE);
                        String approvalTime = item.getApproval_time();
                        setTime(tvTime, approvalTime);
                        break;
                }
            }
            //设置名称
            setText(tvName, item.getApproval_employee_name());


            //根据节点状态设置线条
            setDashLineByTaskState(dashLineBottom, taskStateId, overView, false);
            if (position > 0) {
                //上一节点ID
                ProcessFlowResponseBean.DataBean previousData = getItem(position - 1);
                String previousTaskKey = previousData.getTask_key();

                //当前节点和上一节点相同，自由节点和撤销、firstTask除外
                boolean isSame = taskKey.equals(previousTaskKey)
                        && "0".equals(previousData.getProcess_type())
                        && !ApproveConstants.APPROVE_REVOKED.equals(taskStateId)
                        && !"firstTask".equals(taskKey);

                String lastTaskStatusId = previousData.getTask_status_id();
                setDashLineByTaskState(dashLineTop, lastTaskStatusId, overView, isSame);
            } else {
                dashLineTop.setVisibility(View.INVISIBLE);
            }

            //审批状态设置
            switch (taskStateId) {
                case ApproveConstants.APPROVING:
                    tvState.setTextColor(ColorUtils.hexToColor("#FFBC5F"));
                    Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_approving_status);
                    dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
                    tvState.setCompoundDrawables(dwLeft, null, null, null);
                    tvState.setCompoundDrawablePadding((int) DeviceUtils.dpToPixel(mContext, 5));

                    setText(tvState, item.getTask_status_name());
                    break;
                case ApproveConstants.APPROVE_REJECT:
                    tvState.setTextColor(RED_COLOR);
                    tvState.setCompoundDrawables(null, null, null, null);
                    setText(tvState, item.getTask_status_name());
                    break;
                default:
                    tvState.setTextColor(ColorUtils.hexToColor("#BBBBC3"));
                    tvState.setCompoundDrawables(null, null, null, null);
                    setText(tvState, item.getTask_status_name());
                    break;
            }

        }
        dashLineTop.invalidate();
        dashLineBottom.invalidate();
    }

    /**
     * 根据节点状态设置线条
     *
     * @param dashView
     * @param taskStateId
     */
    private void setDashLineByTaskState(DashView dashView, String taskStateId, View overView, boolean hideOver) {
        int overViewBg = R.drawable.approve_task_flow_gray_over_bg;
        switch (taskStateId) {
            case ApproveConstants.APPROVING:
            case ApproveConstants.APPROVE_PENDING:
            case ApproveConstants.APPROVE_PEND_SUBMIT:
                setDashLine(dashView, true, GRAY_COLOR);
                break;
            case ApproveConstants.APPROVE_SUBMITTED:
            case ApproveConstants.APPROVE_PASS:
            case ApproveConstants.APPROVE_TRANSFER:
                setDashLine(dashView, false, GREEN_COLOR);
                overViewBg = R.drawable.approve_task_flow_green_over_bg;
                break;
            case ApproveConstants.APPROVE_REJECT:
                setDashLine(dashView, false, RED_COLOR);
                overViewBg = R.drawable.approve_task_flow_red_over_bg;
                break;
            case ApproveConstants.APPROVE_REVOKED:
                setDashLine(dashView, false, GRAY_COLOR);
                break;
            default:
                break;
        }
        if (hideOver) {
            overView.setVisibility(View.GONE);
        } else {
            overView.setVisibility(View.VISIBLE);
            overView.setBackgroundResource(overViewBg);
        }
    }

    /**
     * 设置线条
     *
     * @param dashView
     * @param isDash
     * @param color
     */
    private void setDashLine(DashView dashView, boolean isDash, int color) {
        dashView.setVisibility(View.VISIBLE);
        dashView.setIsDashLine(isDash);
        dashView.setColor(color);
    }

    /**
     * 设置时间
     *
     * @param tvTime
     * @param approvalTime
     */
    private void setTime(TextView tvTime, String approvalTime) {
        long timeLong = TextUtil.parseLong(approvalTime);
        if (timeLong != 0) {
            String timeStr = DateTimeUtil.longToStr(timeLong, "yyyy-MM-dd HH:mm");
            setText(tvTime, timeStr);
        }
    }

    /**
     * 设置文本内容
     *
     * @param view
     * @param str
     */
    private void setText(TextView view, String str) {
        view.setVisibility(View.VISIBLE);
        TextUtil.setText(view, str);
    }

    /**
     * 设置图片
     *
     * @param iv  图片控件
     * @param res 图片资源
     */
    private void setImageResource(ImageView iv, int res) {
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(res);
    }

    /**
     * 设置头像
     *
     * @param iv     图片控件
     * @param res    图片资源
     * @param normal 是否正常
     */
    private void setHeadImage(ImageView iv, int res, String normal) {
        if ("0".equals(normal)) {
            iv.setVisibility(View.GONE);
        } else {
            setImageResource(iv, res);
        }
    }

}
