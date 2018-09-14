package com.hjhq.teamface.basis.view.member;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加人员控件
 * Created by lx on 2017/4/6.
 */

public class AddMemberView extends FrameLayout {

    AddMemberViewAdapter adapter;
    RecyclerView mRecyclerView;
    TextView countTv;
    RelativeLayout llCount;
    View mView;
    ImageView addMemberIv;
    RelativeLayout mRlAdd;
    List<Member> mMembers = new ArrayList<>();
    View footer;
    private int footerWidth;
    private boolean hasReLayout = false;
    private OnAddMemberClickedListener onAddMemberClickedListener;
    private int addRlVisibility = View.VISIBLE;

    private int maxItemNum = 5;


    public AddMemberView(@NonNull Context context) {
        super(context);
        initView();
    }

    public AddMemberView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddMemberView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(21)
    public AddMemberView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        reLayout();
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        reLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reLayout();
    }

    public void setMaxItemNum(int maxItemNum) {
        this.maxItemNum = maxItemNum;
    }

    public void setMembers(List<Member> memberList) {
        if (memberList == null) {
            memberList = new ArrayList<>();
        }
        this.mMembers = memberList;
        adapter.setNewData(mMembers);
//        adapter.getData().clear();
//        adapter.getData().addAll(mMembers);

        adapter.notifyDataSetChanged();

        if (!CollectionUtils.isEmpty(mMembers)) {
            countTv.setText("等" + mMembers.size() + "555" + "个");

        }
//        llCount.setVisibility(View.VISIBLE);
//        mRlAdd.setVisibility(GONE);
        hasReLayout = false;
        reLayout();
    }

    public List<Member> getMembers() {
        return this.mMembers;
    }

    public void setAddMemberIvVisibility(int visibility) {
        addRlVisibility = visibility;
        if (View.VISIBLE == visibility) {
            mRlAdd.setVisibility(View.VISIBLE);
        } else if (View.GONE == visibility) {
            mRlAdd.setVisibility(View.GONE);
        } else if (View.INVISIBLE == visibility) {
            mRlAdd.setVisibility(View.GONE);
        }
        requestLayout();
        invalidate();
    }

    private void reLayout() {
        //按照人员的个数重新布局
        if (hasReLayout) {
            return;
        }
        int rvChildViewWidth = 0;
        View rvChildView = mRecyclerView.getLayoutManager().getChildAt(0);
        if (rvChildView != null) {
            rvChildViewWidth = rvChildView.getWidth();//获取recyclerview子view布局
        }
        countTv.setText("等" + mMembers.size() + "人");

        //获取后面添加成员图标的布局
        footerWidth = mRlAdd.getWidth() > llCount.getWidth() ? mRlAdd.getWidth() : llCount.getWidth();

        int width = getWidth() - footerWidth;
        if (rvChildViewWidth == 0 || width < 0) {
            return;
        }
        int maxVisibleCount = width / rvChildViewWidth;//最多能容纳的成员数量
        if (maxVisibleCount > maxItemNum) {
            maxVisibleCount = maxItemNum;
        }


        llCount.requestLayout();
        llCount.invalidate();


        if (!CollectionUtils.isEmpty(mMembers) && maxVisibleCount < mMembers.size()) {
            List<Member> subMembers = this.mMembers.subList(0, maxVisibleCount);
            adapter = new AddMemberViewAdapter(subMembers);
            mRecyclerView.setAdapter(adapter);
            mRlAdd.setVisibility(GONE);
            llCount.setVisibility(View.VISIBLE);
            requestLayout();
            invalidate();
        } else {
            //可以全部显示，隐藏个数tv
            llCount.setVisibility(View.GONE);
            if (addRlVisibility == View.GONE || addRlVisibility == View.INVISIBLE) {
                mRlAdd.setVisibility(GONE);
            } else {
                mRlAdd.setVisibility(VISIBLE);
            }


        }
        /*if (mMembers.size() <= maxVisibleCount) {
            llCount.setVisibility(View.GONE);
            mRlAdd.setVisibility(VISIBLE);
        } else {
            llCount.setVisibility(View.VISIBLE);
            mRlAdd.setVisibility(GONE);
        }*/
        hasReLayout = true;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mView = inflater.inflate(R.layout.view_add_members_view, null);
        llCount = (RelativeLayout) mView.findViewById(R.id.member_sum_ll);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.person_in_charge_rv);
        mRlAdd = (RelativeLayout) mView.findViewById(R.id.rl_add);
        countTv = (TextView) mView.findViewById(R.id.person_in_charge_count_tv);


        addView(mView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AddMemberViewAdapter(null);
        mRecyclerView.setAdapter(adapter);
        requestLayout();
        mRlAdd.setOnClickListener(view -> {
            if (onAddMemberClickedListener != null) {
                onAddMemberClickedListener.onAddMemberClicked();
            }
        });
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onAddMemberClickedListener != null) {
                    onAddMemberClickedListener.onAddMemberClicked();
                }
            }
        });
        countTv.setOnClickListener(view -> {
            if (onAddMemberClickedListener != null) {
                onAddMemberClickedListener.onAddMemberClicked();
            }
        });
    }

    public void setOnAddMemberClickedListener(OnAddMemberClickedListener onAddMemberClickedListener) {
        this.onAddMemberClickedListener = onAddMemberClickedListener;
    }

    public interface OnAddMemberClickedListener {
        void onAddMemberClicked();
    }

    /**
     * 设置尾部是否可见
     *
     * @param visibility
     */
    public void setFooterVisibility(int visibility) {
        footer.setVisibility(visibility);
    }


}
