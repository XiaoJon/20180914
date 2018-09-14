/*
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hjhq.teamface.project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.bean.ProjectLabelBean;
import com.hjhq.teamface.common.bean.TaskInfoBean;
import com.hjhq.teamface.common.view.FlowLayout;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务看板适配器
 *
 * @author Administrator
 */
public class TaskItemAdapter extends DragItemAdapter<TaskInfoBean, TaskItemAdapter.ViewHolder> {
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(DragItemAdapter adapter, View view, int position);

        void onItemChildClick(DragItemAdapter adapter, View view, int position);

        void onItemLongClick(DragItemAdapter adapter, View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private boolean mDragOnLongPress;
    private Context mContext;

    public TaskItemAdapter(List<TaskInfoBean> list, boolean dragOnLongPress) {
        mDragOnLongPress = dragOnLongPress;
        if (list == null) list = new ArrayList<>();
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_task_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TaskInfoBean item = getItem(position);
        if (mOnItemClickListener != null) {
            holder.mGrabView.setOnClickListener(v -> mOnItemClickListener.onItemClick(this, holder.mGrabView, getPositionForItem(item)));
            holder.ivCheck.setOnClickListener(v -> mOnItemClickListener.onItemChildClick(this, holder.ivCheck, getPositionForItem(item)));
            if (!mDragOnLongPress) {
                holder.mGrabView.setOnLongClickListener(v -> {
                    mOnItemClickListener.onItemLongClick(this, holder.mGrabView, getPositionForItem(item));
                    return false;
                });
            }
        }
        switch (item.getDataType()) {
            case ProjectConstants.DATA_MEMO_TYPE:
                //备忘录
                holder.cardApprove.setVisibility(View.GONE);
                holder.cardTask.setVisibility(View.GONE);
                holder.cardCustom.setVisibility(View.GONE);
                holder.cardMemo.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString("备忘录: " + item.getTitle());
                spannableString.setSpan(new ForegroundColorSpan(ColorUtils.resToColor(mContext, R.color.black_17)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvMemoTitle.setText(spannableString);

                ImageLoader.loadCircleImage(mContext, item.getPic_url(), holder.ivMemoHead, item.getCreateObj().getName());
                break;
            case ProjectConstants.DATA_TASK_TYPE:
                //任务
                adapterTask(holder, item);
                break;
            case ProjectConstants.DATA_CUSTOM_TYPE:
                //自定义
                holder.cardApprove.setVisibility(View.GONE);
                holder.cardMemo.setVisibility(View.GONE);
                holder.cardTask.setVisibility(View.GONE);
                holder.cardCustom.setVisibility(View.VISIBLE);

                List<RowBean> rows = item.getRow();
                if (!CollectionUtils.isEmpty(rows)) {
                    holder.tvCustomTitle.setVisibility(View.VISIBLE);
                    RowBean rowBean = rows.get(0);
                    ProjectCustomUtil.setTempValue(holder.tvCustomTitle, rowBean, false);

                    String moduleName = item.getModule_name();
                    if (TextUtil.isEmpty(moduleName)) {
                        moduleName = "自定义模块";
                    }
                    SpannableString approveSpan = new SpannableString(moduleName + ": " + holder.tvCustomTitle.getText().toString());
                    approveSpan.setSpan(new ForegroundColorSpan(ColorUtils.resToColor(mContext, R.color.black_17)), 0, moduleName.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvCustomTitle.setText(approveSpan);
                } else {
                    holder.tvCustomTitle.setVisibility(View.GONE);
                }
                ImageLoader.loadCircleImage(mContext, item.getPic_url(), holder.ivCustomHead);
                break;
            case ProjectConstants.DATA_APPROVE_TYPE:
                //审批
                holder.cardMemo.setVisibility(View.GONE);
                holder.cardTask.setVisibility(View.GONE);
                holder.cardCustom.setVisibility(View.GONE);
                holder.cardApprove.setVisibility(View.VISIBLE);

                SpannableString approveSpan = new SpannableString("审批: " + item.getBegin_user_name() + "-" + item.getProcess_name());
                approveSpan.setSpan(new ForegroundColorSpan(ColorUtils.resToColor(mContext, R.color.black_17)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvApproveTitle.setText(approveSpan);

                ImageLoader.loadCircleImage(mContext, item.getPic_url(), holder.ivApproveHead, item.getBegin_user_name());
                break;
            default:
                break;
        }
        holder.itemView.setTag(getItem(position));
    }

    /**
     * 适配任务
     *
     * @param holder
     * @param item
     */
    private void adapterTask(ViewHolder holder, TaskInfoBean item) {
        holder.cardApprove.setVisibility(View.GONE);
        holder.cardTask.setVisibility(View.VISIBLE);
        holder.cardCustom.setVisibility(View.GONE);
        holder.cardMemo.setVisibility(View.GONE);

        //完成激活
        boolean completeStatus = "1".equals(item.getComplete_status());
        holder.ivCheck.setSelected(completeStatus);
        holder.tvTaskName.setTextColor(ColorUtils.resToColor(mContext,
                completeStatus ? R.color.project_task_foot_text_size : R.color.black_17));


        //任务名称
        holder.tvTaskName.setText(item.getText_name());

        //执行人
        List<Member> members = item.getPersonnel_execution();
        if (!CollectionUtils.isEmpty(members)) {
            ImageLoader.loadCircleImage(mContext, members.get(0).getPicture(), holder.ivTaskHead, members.get(0).getName());
        }

        //激活次数
        int completeNumber = TextUtil.parseInt(item.getComplete_number());
        TextUtil.setText(holder.tvCompleteNumber, completeNumber + "");
        holder.tvCompleteNumber.setVisibility(completeNumber == 0 ? View.GONE : View.VISIBLE);

        //时间
        long deadline = TextUtil.parseLong(item.getDatetime_deadline());
        long startTime = TextUtil.parseLong(item.getDatetime_starttime());

        holder.tvTaskTime.setVisibility(deadline != 0L ? View.VISIBLE : View.GONE);

        String taskTime = DateTimeUtil.AuthToTime(startTime);
        if (deadline != 0) {
            if (startTime != 0) {
                taskTime += " 至 ";
            } else {
                taskTime = "截止时间 ";
            }
            String taskDeadline = DateTimeUtil.AuthToTime(deadline);
            taskTime += taskDeadline;
            TextUtil.setText(holder.tvTaskTime, taskTime);
        }

        long overTime = System.currentTimeMillis() - deadline;
        //超时后 时间变红
        if (deadline != 0L && overTime > 0) {
            holder.tvTaskTime.setTextColor(ColorUtils.hexToColor("#FF3C26"));
        } else {
            holder.tvTaskTime.setTextColor(ColorUtils.hexToColor("#5c5c69"));
        }

        //子任务
        int subtotal = TextUtil.parseInt(item.getSub_task_count());
        int subCompleteCount = TextUtil.parseInt(item.getSub_task_complete_count());
        if (subtotal == 0) {
            holder.tvSubTask.setVisibility(View.GONE);
        } else {
            holder.tvSubTask.setVisibility(View.VISIBLE);
            TextUtil.setText(holder.tvSubTask, subCompleteCount + "/" + subtotal);
        }

        //完成激活

        //标签
        try {
            holder.flowPicklistTag.removeAllViews();
            if (CollectionUtils.isEmpty(item.getPicklist_tag())) {
                holder.flowPicklistTag.setVisibility(View.GONE);
            } else {
                holder.flowPicklistTag.setVisibility(View.VISIBLE);

                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                lp.topMargin = 5;
                lp.bottomMargin = 5;
                for (ProjectLabelBean label : item.getPicklist_tag()) {
                    if (TextUtil.isEmpty(label.getName())) {
                        continue;
                    }
                    TextView view = new TextView(mContext);
                    view.setText(label.getName());
                    view.setTextColor(Color.WHITE);
                    view.setTextSize(12f);
                    view.setMaxLines(1);
                    view.setEllipsize(TextUtils.TruncateAt.END);
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.project_task_tag_stroke);
                    GradientDrawable myGrad = (GradientDrawable) view.getBackground();
                    if ("#FFFFFF" == label.getColour()) {
                        view.setTextColor(ColorUtils.resToColor(mContext, R.color.black_4a));
                    }
                    myGrad.setColor(ColorUtils.hexToColor(label.getColour(), "#000000"));
                    holder.flowPicklistTag.addView(view, lp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getUniqueItemId(int position) {
        return getItem(position).getId();
    }

    public TaskInfoBean getItem(int position) {
        return mItemList.get(position);
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        View cardTask;
        View cardApprove;
        View cardMemo;
        View cardCustom;

        TextView tvTaskName;
        TextView tvCustomTitle;
        TextView tvMemoTitle;
        TextView tvApproveTitle;

        ImageView ivCustomHead;
        ImageView ivMemoHead;
        ImageView ivApproveHead;
        ImageView ivTaskHead;


        //任务
        TextView tvTaskTime;
        ImageView ivCheck;
        TextView tvCompleteNumber;
        TextView tvSubTask;
        FlowLayout flowPicklistTag;

        ViewHolder(final View itemView) {
            super(itemView, R.id.item_layout, mDragOnLongPress);

            tvTaskName = itemView.findViewById(R.id.tv_task_name);
            tvCustomTitle = itemView.findViewById(R.id.tv_custom_title);
            tvMemoTitle = itemView.findViewById(R.id.tv_memo_title);
            tvApproveTitle = itemView.findViewById(R.id.tv_approve_title);

            ivCustomHead = itemView.findViewById(R.id.iv_custom_head);
            ivMemoHead = itemView.findViewById(R.id.iv_memo_head);
            ivApproveHead = itemView.findViewById(R.id.iv_approve_head);
            ivTaskHead = itemView.findViewById(R.id.iv_head);

            ivCheck = itemView.findViewById(R.id.iv_check);
            tvTaskTime = itemView.findViewById(R.id.tv_time);
            tvCompleteNumber = itemView.findViewById(R.id.tv_complete_number);
            flowPicklistTag = itemView.findViewById(R.id.flow_picklist_tag);
            tvSubTask = itemView.findViewById(R.id.tv_sub_task);


            cardTask = itemView.findViewById(R.id.task_layout);
            cardApprove = itemView.findViewById(R.id.approve_layout);
            cardMemo = itemView.findViewById(R.id.memo_layout);
            cardCustom = itemView.findViewById(R.id.custom_layout);
        }

        @Override
        public void onItemClicked(View view) {
//            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
//            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
