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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 人员控件
 *
 * @author lx
 * @date 2017/4/6
 */

public class MembersView extends FrameLayout {

    HorizontalAddMemberAdapter adapter;
    RecyclerView mRecyclerView;
    TextView countTv;
    List<Member> mMembers = new ArrayList<>();
    View memberSum;
    private int footerWidth;
    onAddMemberClickedListener onAddMemberClickedListener;
    onMemberSumClickedListener onMemberSumClickedListener;
    private View addMemberLl;
    private boolean hasReLayout;
    private int addmemberVis = View.VISIBLE;
    private View flRecycler;

    public MembersView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MembersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MembersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(21)
    public MembersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        reLayout();
    }

    public void setMember(Member member) {
        List list = new ArrayList();
        list.add(member);
        setMembers(list);
    }

    public void setMembers(List<Member> memberList) {
        if (memberList == null) {
            memberList = new ArrayList<>();
        }
        int oldSize = adapter.getItemCount();
        int newSize = memberList.size();

        if (newSize >= oldSize) {
            adapter.setNewData(memberList);
        } else {
            for (int j = oldSize - 1; j >= newSize; j--) {
                adapter.remove(j);
            }
            for (int i = 0; i < newSize; i++) {
                adapter.setData(i, memberList.get(i));
            }
        }
        this.mMembers = memberList;
        hasReLayout = false;
    }

    private void reLayout() {
        if (hasReLayout) {
            return;
        }
        int rvChildViewWidth = 0;
        View rvChildView = mRecyclerView.getLayoutManager().getChildAt(0);
        if (rvChildView != null) {
            rvChildViewWidth = rvChildView.getWidth();//获取recyclerview子view布局
        }
        if (addmemberVis == View.VISIBLE) {
            //获取后面添加成员图标的布局
            footerWidth = rvChildViewWidth;
        }

        int width = getWidth() - footerWidth;
        if (rvChildViewWidth == 0 || width < 0) {
            //可以全部显示，隐藏个数tv
            memberSum.setVisibility(View.GONE);
            addMemberLl.setVisibility(addmemberVis);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flRecycler.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            flRecycler.setLayoutParams(layoutParams);
            hasReLayout = true;
            return;
        }
        int maxVisibleCount = width / rvChildViewWidth;//最多能容纳的成员数量
        if (!CollectionUtils.isEmpty(mMembers) && maxVisibleCount < mMembers.size()) {
            //尾部文字长度
            footerWidth += DeviceUtils.dpToPixel(getContext(), 60);
            width = getWidth() - footerWidth;
            //重新计算最多能容纳的成员数量
            maxVisibleCount = width / rvChildViewWidth;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flRecycler.getLayoutParams();
            layoutParams.width = maxVisibleCount * rvChildViewWidth;
            flRecycler.setLayoutParams(layoutParams);
//            List<Member> subMembers = mMembers.subList(0, maxVisibleCount);\
//            adapter = new HorizontalAddMemberAdapter(subMembers);
//            mRecyclerView.setAdapter(adapter);

            memberSum.setVisibility(View.VISIBLE);
            addMemberLl.setVisibility(addmemberVis);
            countTv.setText("等" + mMembers.size() + "人");
            requestLayout();
            invalidate();
        } else {
            //可以全部显示，隐藏个数tv
            memberSum.setVisibility(View.GONE);
            addMemberLl.setVisibility(addmemberVis);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flRecycler.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            flRecycler.setLayoutParams(layoutParams);
        }
        hasReLayout = true;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mView = inflater.inflate(R.layout.view_members_view, null);

        mRecyclerView = mView.findViewById(R.id.person_in_charge_rv);
        memberSum = mView.findViewById(R.id.member_sum_ll);
        countTv = memberSum.findViewById(R.id.person_in_charge_count_tv);
        addMemberLl = mView.findViewById(R.id.add_member_ll);
        flRecycler = mView.findViewById(R.id.fl_recycler);
        addView(mView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HorizontalAddMemberAdapter(null);
        mRecyclerView.setAdapter(adapter);

        addMemberLl.setOnClickListener(view -> {
            if (onAddMemberClickedListener != null) {
                onAddMemberClickedListener.onAddMemberClicked();
            }
        });
//        mRecyclerView.setOnClickListener(v -> {
//            if (OnAddMemberClickedListener != null) {
//                OnAddMemberClickedListener.onAddMemberClicked();
//            }
//        });
        memberSum.setOnClickListener(v -> {
            if (onMemberSumClickedListener != null) {
                onMemberSumClickedListener.onMemberSumClicked();
            }
        });
    }

    public void setOnAddMemberClickedListener(MembersView.onAddMemberClickedListener onAddMemberClickedListener) {
        this.onAddMemberClickedListener = onAddMemberClickedListener;
    }

    public interface onAddMemberClickedListener {
        void onAddMemberClicked();
    }

    public interface onMemberSumClickedListener {
        void onMemberSumClicked();
    }

    public void setOnMemberSumClickedListener(MembersView.onMemberSumClickedListener onMemberSumClickedListener) {
        this.onMemberSumClickedListener = onMemberSumClickedListener;
    }

    public List<Member> getMembers() {
        return this.mMembers;
    }

    public void setAddMemberIvVisibility(int visibility) {
        this.addmemberVis = visibility;
        addMemberLl.setVisibility(visibility);
    }

    /**
     * 设置尾部是否可见
     *
     * @param visibility
     */
    public void setFooterVisibility(int visibility) {
        memberSum.setVisibility(visibility);
    }

}
