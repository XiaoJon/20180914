package com.hjhq.teamface.attendance.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AddDateAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddRulesDelegate extends AppDelegate {
    RecyclerView mRecyclerView1;
    RecyclerView mRecyclerView2;
    TextView tvStartTime;


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_rules_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_add_rules_title);
        // TODO: 2018/6/8 蓝色带返回字样切图 
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        setRightMenuTexts(R.color.app_blue, "保存");
        mRecyclerView1 = get(R.id.rv_11);
        mRecyclerView2 = get(R.id.rv_12);
        tvStartTime = get(R.id.tv32);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


    }

    public void setAdapter1(AddDateAdapter adapter) {
        mRecyclerView1.setAdapter(adapter);

    }

    public void setAdapter2(AddDateAdapter adapter) {
        mRecyclerView2.setAdapter(adapter);

    }

    public void setItemClick1(RecyclerView.OnItemTouchListener l) {
        mRecyclerView1.addOnItemTouchListener(l);
    }

    public void setItemClick2(RecyclerView.OnItemTouchListener l) {
        mRecyclerView2.addOnItemTouchListener(l);
    }


    public void switchType(int i) {
        switch (i) {
            case 1:
                get(R.id.type1).setVisibility(View.VISIBLE);
                get(R.id.type2).setVisibility(View.GONE);
                get(R.id.type3).setVisibility(View.GONE);
                break;
            case 2:
                get(R.id.type1).setVisibility(View.GONE);
                get(R.id.type2).setVisibility(View.VISIBLE);
                get(R.id.type3).setVisibility(View.GONE);
                break;
            case 3:
                get(R.id.type1).setVisibility(View.GONE);
                get(R.id.type2).setVisibility(View.GONE);
                get(R.id.type3).setVisibility(View.VISIBLE);
                break;
        }


    }

    public void setStartTime(int i, String time) {
        if (TextUtils.isEmpty(time)) {
            TextUtil.setText(tvStartTime, "00:00");
        } else {
            TextUtil.setText(tvStartTime, time);
        }
    }

    public String getStartTime() {
        String startTime = "00:00";
        String s = tvStartTime.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            startTime = tvStartTime.getText().toString();
        }
        return startTime;


    }

}
