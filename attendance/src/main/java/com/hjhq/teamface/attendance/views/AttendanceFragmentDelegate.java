package com.hjhq.teamface.attendance.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;


/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class AttendanceFragmentDelegate extends AppDelegate {

    Button mButton;
    RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_fragment_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mButton = get(R.id.action_btn2);
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemClick(RecyclerView.OnItemTouchListener l) {
        mRecyclerView.addOnItemTouchListener(l);
    }


    public void setTimeAndAction(int i, String s) {
        mButton.setText(s);
        switch (i) {
            case 1:
                mButton.setBackground(getActivity().getResources().getDrawable(R.drawable.attendance_blue_bg));
                break;
            case 2:
                mButton.setBackground(getActivity().getResources().getDrawable(R.drawable.attendance_green_bg));

                break;
            case 3:
                mButton.setBackground(getActivity().getResources().getDrawable(R.drawable.attendance_orange_bg));

                break;
            case 4:
                mButton.setBackground(getActivity().getResources().getDrawable(R.drawable.attendance_gray_bg));

                break;
        }
    }
}
