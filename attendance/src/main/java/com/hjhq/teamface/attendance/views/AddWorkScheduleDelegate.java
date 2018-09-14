package com.hjhq.teamface.attendance.views;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddWorkScheduleDelegate extends AppDelegate {


    RelativeLayout mRoot;
    EditText etName;

    TextView tvTime11;
    TextView tvTime12;
    TextView tvTime13;
    TextView tvTime14;
    TextView tvTime21;
    TextView tvTime22;
    TextView tvTime23;
    TextView tvTime24;
    TextView tvTime31;
    TextView tvTime32;
    TextView tvTime33;
    TextView tvTime34;
    TextView tvTime35;
    TextView tvTime36;

    TextView mTotalTime;

    String[] timeValue = new String[14];
    boolean hasRestTime = false;

    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_work_schedule_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_add_work_schedule_title);
        // TODO: 2018/6/8 蓝色带返回字样切图 
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        setRightMenuTexts(R.color.app_blue, "保存");
        timeValue = getActivity().getResources().getStringArray(R.array.attendance_work_time_menu);
        mRoot = get(R.id.rl_root);


        etName = get(R.id.et_name);
        tvTime11 = get(R.id.tv_time11);
        tvTime12 = get(R.id.tv_time12);
        tvTime13 = get(R.id.tv_time13);
        tvTime14 = get(R.id.tv_time14);
        tvTime21 = get(R.id.tv_time21);
        tvTime22 = get(R.id.tv_time22);
        tvTime23 = get(R.id.tv_time23);
        tvTime24 = get(R.id.tv_time24);
        tvTime31 = get(R.id.tv_time31);
        tvTime32 = get(R.id.tv_time32);
        tvTime33 = get(R.id.tv_time33);
        tvTime34 = get(R.id.tv_time34);
        tvTime35 = get(R.id.tv_time35);
        tvTime36 = get(R.id.tv_time36);
        mTotalTime = get(R.id.tv_total);
        TextUtil.setText(tvTime11, timeValue[0]);
        TextUtil.setText(tvTime12, timeValue[1]);
        TextUtil.setText(tvTime13, timeValue[2]);
        TextUtil.setText(tvTime14, timeValue[3]);
        TextUtil.setText(tvTime21, timeValue[4]);
        TextUtil.setText(tvTime22, timeValue[5]);
        TextUtil.setText(tvTime23, timeValue[6]);
        TextUtil.setText(tvTime24, timeValue[7]);
        TextUtil.setText(tvTime31, timeValue[8]);
        TextUtil.setText(tvTime32, timeValue[9]);
        TextUtil.setText(tvTime33, timeValue[10]);
        TextUtil.setText(tvTime34, timeValue[11]);
        TextUtil.setText(tvTime35, timeValue[12]);
        TextUtil.setText(tvTime36, timeValue[13]);
        getTotal(1);
    }


    public void switchType(int i) {
        getTotal(i);
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

    public void setTime(int i, String time) {
        switch (i) {
            case 11:
                TextUtil.setText(tvTime11, time);
                timeValue[0] = time;
                getTotal(1);
                break;
            case 12:
                TextUtil.setText(tvTime12, time);
                timeValue[1] = time;
                getTotal(1);
                break;
            case 13:
                TextUtil.setText(tvTime13, time);
                timeValue[2] = time;
                getTotal(1);
                break;
            case 14:
                TextUtil.setText(tvTime14, time);
                timeValue[3] = time;
                getTotal(1);
                break;
            case 21:
                TextUtil.setText(tvTime21, time);
                timeValue[4] = time;
                getTotal(2);
                break;
            case 22:
                TextUtil.setText(tvTime22, time);
                timeValue[5] = time;
                getTotal(2);
                break;
            case 23:
                TextUtil.setText(tvTime23, time);
                timeValue[6] = time;
                getTotal(2);
                break;
            case 24:
                TextUtil.setText(tvTime24, time);
                timeValue[7] = time;
                getTotal(2);
                break;
            case 31:
                TextUtil.setText(tvTime31, time);
                timeValue[8] = time;
                getTotal(3);
                break;
            case 32:
                TextUtil.setText(tvTime32, time);
                timeValue[9] = time;
                getTotal(3);
                break;
            case 33:
                TextUtil.setText(tvTime33, time);
                timeValue[10] = time;
                getTotal(3);
                break;
            case 34:
                TextUtil.setText(tvTime34, time);
                timeValue[11] = time;
                getTotal(3);
                break;
            case 35:
                TextUtil.setText(tvTime35, time);
                timeValue[12] = time;
                getTotal(3);
                break;
            case 36:
                TextUtil.setText(tvTime36, time);
                timeValue[13] = time;
                getTotal(3);
                break;
        }
    }

    public void setHasRestTime(boolean hasRestTime) {
        this.hasRestTime = hasRestTime;
        getTotal(1);
    }

    /**
     * 获取输入的班次名字
     *
     * @return
     */
    public String getName() {
        String name = "";
        if (!TextUtils.isEmpty(etName.getText().toString())) {
            name = etName.getText().toString();
        }
        return name;
    }

    public String getTime(int i) {
        if (i > 13 || i < 0) {
            return "";
        }
        return timeValue[i];
    }

    public String getTotalTimeString(String time1, String time2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();


        String totalTime = "合计工作时长0小时0分钟";
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) {
            return totalTime;
        }
        String[] split1 = time1.split(":");
        String[] split2 = time2.split(":");
        int hour1 = TextUtil.parseInt(split1[0]);
        int hour2 = TextUtil.parseInt(split2[0]);

        int minute1 = TextUtil.parseInt(split1[1]);
        int minute2 = TextUtil.parseInt(split2[1]);
        calendar1.set(Calendar.HOUR, hour1);
        calendar2.set(Calendar.HOUR, hour2);
        calendar1.set(Calendar.MINUTE, minute1);
        calendar2.set(Calendar.MINUTE, minute2);
        long t1 = calendar1.getTimeInMillis();
        long t2 = calendar2.getTimeInMillis();
        long total = t2 - t1;
        if (total < 0) {
            total = total + 24 * 60 * 60 * 1000;
        }

        long totalHour = total / (60 * 60 * 1000);
        long residueMinuteLong = total - totalHour * (60 * 60 * 1000);
        long totalMinute = residueMinuteLong / (60 * 1000);

        totalTime = totalHour + "小时" + totalMinute + "分钟";

        return totalTime;
    }

    public int[] getTotalTimeInt(String time1, String time2) {
        int[] hourAndMinute = new int[]{0, 0};
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) {
            return hourAndMinute;
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        String[] split1 = time1.split(":");
        String[] split2 = time2.split(":");
        int hour1 = TextUtil.parseInt(split1[0]);
        int hour2 = TextUtil.parseInt(split2[0]);

        int minute1 = TextUtil.parseInt(split1[1]);
        int minute2 = TextUtil.parseInt(split2[1]);
        calendar1.set(Calendar.HOUR, hour1);
        calendar2.set(Calendar.HOUR, hour2);
        calendar1.set(Calendar.MINUTE, minute1);
        calendar2.set(Calendar.MINUTE, minute2);

        long t1 = calendar1.getTimeInMillis();
        long t2 = calendar2.getTimeInMillis();
        long total = t2 - t1;
        if (total < 0) {
            total = total + 24 * 60 * 60 * 1000;
        }

        long totalHour = total / (60 * 60 * 1000);
        long residueMinuteLong = total - totalHour * (60 * 60 * 1000);
        long totalMinute = residueMinuteLong / (60 * 1000);


        hourAndMinute[0] = ((int) totalHour);
        hourAndMinute[1] = ((int) totalMinute);

        return hourAndMinute;
    }

    public void getTotal(int type) {
        switch (type) {
            case 1:
                int[] totalTimeInt1 = getTotalTimeInt(timeValue[0], timeValue[1]);
                int[] totalTimeInt2 = getTotalTimeInt(timeValue[2], timeValue[3]);

                if (hasRestTime) {
                    int h = totalTimeInt1[0] - totalTimeInt2[0];
                    int m = totalTimeInt1[1] - totalTimeInt2[1];
                    if (m < 0) {
                        m = m + 60;
                        h = h - 1;
                    }
                    TextUtil.setText(mTotalTime, "合计工作时长" + h + "小时" + m + "分钟");
                } else {
                    TextUtil.setText(mTotalTime, "合计工作时长" + totalTimeInt1[0] + "小时" + totalTimeInt1[1] + "分钟");

                }

                break;
            case 2:
                int[] totalTimeInt3 = getTotalTimeInt(timeValue[4], timeValue[5]);
                int[] totalTimeInt4 = getTotalTimeInt(timeValue[6], timeValue[7]);
                TextUtil.setText(mTotalTime, "合计工作时长" + (totalTimeInt3[0] + totalTimeInt4[0]) + "小时" + (totalTimeInt3[1] + totalTimeInt4[1]) + "分钟");
                break;
            case 3:
                int[] totalTimeInt5 = getTotalTimeInt(timeValue[8], timeValue[9]);
                int[] totalTimeInt6 = getTotalTimeInt(timeValue[10], timeValue[11]);
                int[] totalTimeInt7 = getTotalTimeInt(timeValue[12], timeValue[13]);
                TextUtil.setText(mTotalTime, "合计工作时长" + (totalTimeInt5[0] + totalTimeInt6[0] + totalTimeInt7[0]) + "小时" + (totalTimeInt5[1] + totalTimeInt6[1] + totalTimeInt7[1]) + "分钟");
                break;
        }
    }

    public void setName(String name) {
        etName.setText(name);
    }

    public String getTotalTimeString() {
        return mTotalTime.getText().toString();


    }
}
