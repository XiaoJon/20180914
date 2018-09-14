package com.hjhq.teamface.attendance.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.LeavingLateAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：其他设置视图
 */

public class AdditionalSettingsDelegate extends AppDelegate {
    ImageView ivArrow2;
    ImageView ivArrow3;
    ImageView ivArrow4;
    ImageView ivArrow5;
    ImageView ivArrow6;

    TextView remindBefore;
    TextView remindAfter;

    TextView lateTime;
    TextView lateMinutes;


    TextView absentMinutes1;
    TextView absentMinutes2;
    //榜单设置相关
    TextView rangeStryle;
    TextView earlyArrival;
    TextView diligentRules;
    TextView lateRules;
    TextView lateRangeRules;


    RecyclerView mRecyclerView;


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_additional_settings_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ivArrow2 = get(R.id.arrow2);
        ivArrow3 = get(R.id.arrow3);
        ivArrow4 = get(R.id.arrow4);
        ivArrow5 = get(R.id.arrow5);
        ivArrow6 = get(R.id.arrow6);
        remindBefore = get(R.id.text222);
        remindAfter = get(R.id.text232);
        lateTime = get(R.id.text522);
        lateMinutes = get(R.id.text532);
        absentMinutes1 = get(R.id.text622);
        absentMinutes2 = get(R.id.text632);

        rangeStryle = get(R.id.text323);
        earlyArrival = get(R.id.text333);
        diligentRules = get(R.id.text343);
        lateRules = get(R.id.text353);
        lateRangeRules = get(R.id.text363);

        mRecyclerView = get(R.id.rv1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // TODO: 2018/6/8 蓝色带返回字样切图
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        get(R.id.rl21).setOnClickListener(v -> {
            if (get(R.id.rl22).getVisibility() == View.VISIBLE) {
                get(R.id.rl22).setVisibility(View.GONE);
                get(R.id.rl23).setVisibility(View.GONE);
                ivArrow2.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                get(R.id.rl22).setVisibility(View.VISIBLE);
                get(R.id.rl23).setVisibility(View.VISIBLE);
                ivArrow2.setImageResource(R.drawable.attendance_open_icon);
            }


        });
        get(R.id.rl31).setOnClickListener(v -> {

            if (get(R.id.rl32).getVisibility() == View.VISIBLE) {
                get(R.id.rl32).setVisibility(View.GONE);
                get(R.id.rl33).setVisibility(View.GONE);
                get(R.id.rl34).setVisibility(View.GONE);
                get(R.id.rl35).setVisibility(View.GONE);
                get(R.id.rl36).setVisibility(View.GONE);
                ivArrow3.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                get(R.id.rl32).setVisibility(View.VISIBLE);
                get(R.id.rl33).setVisibility(View.VISIBLE);
                get(R.id.rl34).setVisibility(View.VISIBLE);
                get(R.id.rl35).setVisibility(View.VISIBLE);
                get(R.id.rl36).setVisibility(View.VISIBLE);
                ivArrow3.setImageResource(R.drawable.attendance_open_icon);
            }


        });
        get(R.id.rl41).setOnClickListener(v -> {
            if (get(R.id.rl42).getVisibility() == View.VISIBLE) {
                get(R.id.rl42).setVisibility(View.GONE);
                get(R.id.rl_rv1).setVisibility(View.GONE);
                ivArrow4.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                get(R.id.rl42).setVisibility(View.VISIBLE);
                get(R.id.rl_rv1).setVisibility(View.VISIBLE);
                ivArrow4.setImageResource(R.drawable.attendance_open_icon);
            }


        });
        get(R.id.rl51).setOnClickListener(v -> {
            if (get(R.id.rl52).getVisibility() == View.VISIBLE) {
                get(R.id.rl52).setVisibility(View.GONE);
                get(R.id.rl53).setVisibility(View.GONE);
                ivArrow5.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                get(R.id.rl52).setVisibility(View.VISIBLE);
                get(R.id.rl53).setVisibility(View.VISIBLE);
                ivArrow5.setImageResource(R.drawable.attendance_open_icon);
            }


        });
        get(R.id.rl61).setOnClickListener(v -> {
            if (get(R.id.rl62).getVisibility() == View.VISIBLE) {
                get(R.id.rl62).setVisibility(View.GONE);
                get(R.id.rl63).setVisibility(View.GONE);
                ivArrow6.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                get(R.id.rl62).setVisibility(View.VISIBLE);
                get(R.id.rl63).setVisibility(View.VISIBLE);
                ivArrow6.setImageResource(R.drawable.attendance_open_icon);
            }


        });

    }

    /**
     * 提醒员工打上班卡
     *
     * @param s
     */
    public void setRemindTimeBefore(String s) {
        TextUtil.setText(remindBefore, s);
    }

    /**
     * 榜单统计方式
     *
     * @param s
     */
    public void setRangeRules(String s) {
        TextUtil.setText(rangeStryle, s);
    }

    /**
     * 早到榜设置
     *
     * @param s
     */
    public void setEarlyArrival(String s) {
        TextUtil.setText(earlyArrival, s);

    }

    /**
     * 勤勉榜设置
     *
     * @param s
     */
    public void setDiligentRules(String s) {
        TextUtil.setText(diligentRules, s);
    }

    /**
     * 迟到榜设置
     *
     * @param s
     */
    public void setLateArrival(String s) {
        TextUtil.setText(lateRules, s);
    }

    /**
     * 迟到排序规则
     *
     * @param s
     */
    public void setLateArrivalRules(String s) {
        TextUtil.setText(lateRangeRules, s);
    }

    /**
     * 提醒员工打下班卡
     *
     * @param s
     */
    public void setRemindTimeAfter(String s) {
        remindAfter.setText(s);

    }

    public void setText(int type, String value1) {
        switch (type) {
            case 1:
                TextUtil.setText(lateTime, value1 + "次");
                break;
            case 2:
                TextUtil.setText(lateMinutes, value1 + "分钟");
                break;
            case 3:
                TextUtil.setText(absentMinutes1, value1 + "分钟");
                break;
            case 4:
                TextUtil.setText(absentMinutes2, value1 + "分钟");
                break;
            default:

                break;


        }


    }

    public void setAdapter(LeavingLateAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemClick(RecyclerView.OnItemTouchListener l) {
        mRecyclerView.addOnItemTouchListener(l);
    }
}
