package com.hjhq.teamface.project.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

/**
 * 工作台
 *
 * @author Administrator
 * @date 2018/5/14
 */

public class WorkbenchDelegate extends AppDelegate {
    public BoardView mBoardView;


    @Override
    public int getRootLayoutId() {
        return R.layout.project_fragment_workbench;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        mBoardView = get(R.id.board_view);

        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.project_task_list_item));
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);

    }



    private static class MyDragItem extends DragItem {

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

//            dragCard.setMaxCardElevation(40);
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
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card_view);
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

    }
}
