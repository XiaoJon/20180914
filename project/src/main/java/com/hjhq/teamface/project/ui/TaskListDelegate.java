package com.hjhq.teamface.project.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

/**
 * @author Administrator
 * @date 2018/4/10
 */

public class TaskListDelegate extends AppDelegate {
    public BoardView mBoardView;
    private LinearLayout llPoint;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_fragment_task_group;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mBoardView = get(R.id.board_view);
        llPoint = get(R.id.ll_point);

        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.project_task_list_item));
        mBoardView.setCustomColumnDragItem(new MyColumnDragItem(getActivity(), R.layout.project_column_drag_layout));
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);

        mBoardView.setColumnDragEnabled(false);
        mBoardView.setDragEnabled(false);
    }

    public void addPoint() {
        int pointSize = (int) DeviceUtils.dpToPixel(mContext, 5);
        // 1.根据图片多少，添加多少小圆点
        LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(pointSize, pointSize);
        pointParams.setMargins(0, 0, pointSize, 0);
        ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(pointParams);
        iv.setBackgroundResource(R.drawable.icon_gray_point);
        llPoint.addView(iv, 0);
    }

    public void setPointIndex(int i) {
        int childCount = llPoint.getChildCount();
        for (int j = 0; j < childCount; j++) {
            View child = llPoint.getChildAt(j);
            if (j == i) {
                if (j == childCount - 1) {
                    child.setBackgroundResource(R.drawable.project_task_icon_blue_add);
                } else {
                    child.setBackgroundResource(R.drawable.icon_blue_point);
                }
            } else {
                if (j == childCount - 1) {
                    child.setBackgroundResource(R.drawable.project_task_icon_gray_add);
                } else {
                    child.setBackgroundResource(R.drawable.icon_gray_point);
                }
            }
        }
    }

    public void removePoint() {
        int childCount = llPoint.getChildCount();
        for (int i = childCount - 2; i >= 0; i--) {
            llPoint.removeViewAt(i);
        }
    }

    public void setAddPointVisibility(boolean bl) {
        setVisibility(R.id.add_point, bl);
    }


    private class MyColumnDragItem extends DragItem {

        MyColumnDragItem(Context context, int layoutId) {
            super(context, layoutId);
            setSnapToTouch(false);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            LinearLayout clickedLayout = (LinearLayout) clickedView;
            View clickedHeader = clickedLayout.getChildAt(0);
            RecyclerView clickedRecyclerView = (RecyclerView) clickedLayout.getChildAt(1);

            TextView tvClickTaskTempName = clickedHeader.findViewById(R.id.tv_task_temp_name);

            TextView tvTaskTempName = dragView.findViewById(R.id.tv_task_temp_name);
            ScrollView dragScrollView = dragView.findViewById(R.id.drag_scroll_view);
            LinearLayout dragLayout = dragView.findViewById(R.id.drag_list);
            dragLayout.removeAllViews();

            tvTaskTempName.setText(tvClickTaskTempName.getText());
            for (int i = 0; i < clickedRecyclerView.getChildCount(); i++) {
                View view = View.inflate(dragView.getContext(), R.layout.project_task_list_item, null);
                ((TextView) view.findViewById(R.id.tv_task_name)).setText(((TextView) clickedRecyclerView.getChildAt(i).findViewById(R.id.tv_task_name)).getText());
                ((TextView) view.findViewById(R.id.tv_time)).setText(((TextView) clickedRecyclerView.getChildAt(i).findViewById(R.id.tv_time)).getText());
                dragLayout.addView(view);

                if (i == 0) {
                    dragScrollView.setScrollY(-clickedRecyclerView.getChildAt(i).getTop());
                }
            }

            dragView.setPivotY(0);
            dragView.setPivotX(clickedView.getMeasuredWidth() / 2);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            super.onStartDragAnimation(dragView);
            dragView.animate().rotation(6).scaleX(0.7f).scaleY(0.7f).setDuration(ANIMATION_DURATION).start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            super.onEndDragAnimation(dragView);
            dragView.animate().rotation(0).scaleX(1).scaleY(1).setDuration(ANIMATION_DURATION).start();
        }
    }

    private class MyDragItem extends DragItem {

        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            dragView.findViewById(R.id.task_layout).setVisibility(clickedView.findViewById(R.id.task_layout).getVisibility());
            dragView.findViewById(R.id.approve_layout).setVisibility(clickedView.findViewById(R.id.approve_layout).getVisibility());
            dragView.findViewById(R.id.memo_layout).setVisibility(clickedView.findViewById(R.id.memo_layout).getVisibility());
            dragView.findViewById(R.id.custom_layout).setVisibility(clickedView.findViewById(R.id.custom_layout).getVisibility());
            TextUtil.setText(dragView.findViewById(R.id.tv_task_name), ((TextView) clickedView.findViewById(R.id.tv_task_name)).getText());
            TextUtil.setText(dragView.findViewById(R.id.tv_memo_title), ((TextView) clickedView.findViewById(R.id.tv_memo_title)).getText());
            TextUtil.setText(dragView.findViewById(R.id.tv_custom_title), ((TextView) clickedView.findViewById(R.id.tv_custom_title)).getText());
            TextUtil.setText(dragView.findViewById(R.id.tv_approve_title), ((TextView) clickedView.findViewById(R.id.tv_approve_title)).getText());

            ((ImageView)dragView.findViewById(R.id.iv_head)).setImageDrawable(((ImageView)clickedView.findViewById(R.id.iv_head)).getDrawable());
            ((ImageView)dragView.findViewById(R.id.iv_memo_head)).setImageDrawable(((ImageView)clickedView.findViewById(R.id.iv_memo_head)).getDrawable());
            ((ImageView)dragView.findViewById(R.id.iv_custom_head)).setImageDrawable(((ImageView)clickedView.findViewById(R.id.iv_custom_head)).getDrawable());
            ((ImageView)dragView.findViewById(R.id.iv_approve_head)).setImageDrawable(((ImageView)clickedView.findViewById(R.id.iv_approve_head)).getDrawable());

            CardView dragCard = dragView.findViewById(R.id.card_view);
            CardView clickedCard = clickedView.findViewById(R.id.card_view);

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
//            dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.project_card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card_view);
            CardView clickedCard = clickedView.findViewById(R.id.card_view);
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card_view);
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            ObjectAnimator rotation = ObjectAnimator.ofFloat(dragCard, "rotation", 6);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setDuration(ANIMATION_DURATION);
            rotation.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card_view);
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 2);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();

            ObjectAnimator rotation = ObjectAnimator.ofFloat(dragCard, "rotation", 0);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setDuration(ANIMATION_DURATION);
            rotation.start();
        }
    }
}
