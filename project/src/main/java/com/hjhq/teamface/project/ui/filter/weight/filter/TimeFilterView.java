package com.hjhq.teamface.project.ui.filter.weight.filter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ConditionBean;
import com.hjhq.teamface.project.ui.filter.weight.BaseFilterView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 筛选条件中单选或多选
 *
 * @author Administrator
 */
public class TimeFilterView extends BaseFilterView implements View.OnClickListener {

    private RelativeLayout rlAction;
    private LinearLayout rlContent;
    private TextView tvTitle;
    private TextView tvStart;
    private TextView tvStop;
    private LinearLayout radioGroup;
    private CheckBox today;
    private CheckBox yesterday;
    private CheckBox lastSevenDay;
    private CheckBox lastThirtyDay;
    private CheckBox thisMonth;
    private CheckBox lastMonth;
    private CheckBox thisSeason;
    private CheckBox lastSeason;
    private final int START_TIME = 1;
    private final int END_TIME = 2;
    private List<CheckBox> cbList = new ArrayList<>();
    //初始化组件用到的数据
    private String itemTitle;
    private String emptyTime = "年-月-日";
    private long startTime = -1L;
    private long endTime = -1L;
    private long selectTime = -1L;
    private boolean flag = false;
    private boolean clearCheck = false;
    private View ivArraw;


    public TimeFilterView(ConditionBean bean) {
        super(bean);
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {

        mView = View.inflate(activity, R.layout.crm_item_goods_filter_by_time, null);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tvStart = (TextView) mView.findViewById(R.id.tv_start_time_content);
        tvStop = (TextView) mView.findViewById(R.id.tv_end_time_content);
        radioGroup = (LinearLayout) mView.findViewById(R.id.radio_group);
        //
        today = (CheckBox) mView.findViewById(R.id.today);
        yesterday = (CheckBox) mView.findViewById(R.id.yesterday);
        lastSevenDay = (CheckBox) mView.findViewById(R.id.last_seven_day);
        lastThirtyDay = (CheckBox) mView.findViewById(R.id.last_thirty_day);
        thisMonth = (CheckBox) mView.findViewById(R.id.this_month);
        lastMonth = (CheckBox) mView.findViewById(R.id.last_month);
        thisSeason = (CheckBox) mView.findViewById(R.id.this_season);
        lastSeason = (CheckBox) mView.findViewById(R.id.last_season);
        ivArraw = mView.findViewById(R.id.iv);
        cbList.add(today);
        cbList.add(yesterday);
        cbList.add(lastSevenDay);
        cbList.add(lastThirtyDay);
        cbList.add(thisMonth);
        cbList.add(lastMonth);
        cbList.add(thisSeason);
        cbList.add(lastSeason);
        today.setOnClickListener(this);
        yesterday.setOnClickListener(this);
        lastSevenDay.setOnClickListener(this);
        lastThirtyDay.setOnClickListener(this);
        thisMonth.setOnClickListener(this);
        lastMonth.setOnClickListener(this);
        thisSeason.setOnClickListener(this);
        lastSeason.setOnClickListener(this);


        rlAction = (RelativeLayout) mView.findViewById(R.id.rl_title_time);
        rlContent = (LinearLayout) mView.findViewById(R.id.ll_action_time);
        if (!flag) {
            rlContent.setVisibility(View.GONE);
        }

        initData();
        initView();
        setClickListener();
        parent.addView(mView, index);

    }

    private void initView() {
        TextUtil.setText(tvTitle, title);

    }

    private void initData() {

    }

    private void setClickListener() {
        rlAction.setOnClickListener(v -> {
            SoftKeyboardUtils.hide(v);
            if (!flag) {
                rlContent.setVisibility(View.VISIBLE);
                flag = !flag;
                rotateCButton(mActivity, ivArraw, 0f, 180f, 500,
                        R.drawable.icon_sort_down);
            } else {
                rlContent.setVisibility(View.GONE);
                flag = !flag;
                rotateCButton(mActivity, ivArraw, 0f, -180f, 500,
                        R.drawable.icon_sort_down);
            }
        });
        tvStart.setOnClickListener(v -> {
            showDialogPick(tvStart, START_TIME);
            clearCheck();
        });
        tvStop.setOnClickListener(v -> {
            showDialogPick(tvStop, END_TIME);
            clearCheck();
        });


    }

    private void clearCheck() {
        for (int i = 0; i < cbList.size(); i++) {
            cbList.get(i).setChecked(false);
        }
    }

    public boolean formatCheck() {
        return true;
    }

    @Override
    public boolean put(JSONObject json) throws Exception {
        boolean flag = false;
        Map<String, Long> data = new HashMap<>();

        if (startTime != -1L && endTime != -1L) {
            data.put("startTime", startTime);
            flag = true;
        }
        if (endTime != -1L) {
            data.put("endTime", endTime);
            flag = true;
        }

        for (int i = 0; i < cbList.size(); i++) {
            if (cbList.get(i).isChecked()) {
                selectTime = i;
                flag = true;
                data = CustomTimeUtil.getTimeRange(i);
                break;
            }
        }

        if (flag) {
            json.put(keyName, data);
        }

        return true;
    }

    @Override
    public void reset() {
        for (int i = 0; i < cbList.size(); i++) {
            cbList.get(i).setChecked(false);
            tvStart.setText(emptyTime);
            tvStop.setText(emptyTime);
            startTime = -1;
            endTime = -1;
        }
    }

    /**
     * 将两个选择时间的dialog放在该函数中
     *
     * @param timeText 文本控件
     * @param witch    类型
     */
    private void showDialogPick(final TextView timeText, int witch) {
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(timeText.getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    calendar.set(year1, monthOfYear + 1, dayOfMonth);
                    String time = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    Long dateLong = DateTimeUtil.strToLong(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                    clearCheck = true;
                    if (witch == START_TIME) {
                        if (endTime != -1L && dateLong >= endTime) {
                            ToastUtils.showError(getContext(), "开始日期必须早于结束日期。");
                            return;
                        } else {
                            startTime = dateLong;
                            timeText.setText(time);
                            selectTime = -1;
                        }

                    }
                    if (witch == END_TIME) {
                        if (startTime != -1L && dateLong <= startTime) {
                            ToastUtils.showError(getContext(), "结束日期必须晚于结束日期。");
                            return;
                        } else {

                            endTime = dateLong + 24 * 60 * 60 * 1000 - 1;
                            timeText.setText(time);
                            selectTime = -1;
                        }
                    }
                }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();

    }


    @Override
    public void onClick(View v) {

        for (int i = 0; i < cbList.size(); i++) {
            if (cbList.get(i).getId() == v.getId()) {
                tvStart.setText(emptyTime);
                tvStop.setText(emptyTime);
                startTime = -1;
                endTime = -1;
            } else {
                cbList.get(i).setChecked(false);
            }

        }
    }

}
