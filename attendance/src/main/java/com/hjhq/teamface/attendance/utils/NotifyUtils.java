package com.hjhq.teamface.attendance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.CustomPopWindow;

import java.lang.reflect.Field;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by Administrator on 2018/6/6.
 * Describe：
 */

public class NotifyUtils {
    private static NotifyUtils instance;

    private NotifyUtils() {
    }

    public synchronized static NotifyUtils getInstance() {
        if (instance == null) {
            instance = new NotifyUtils();
        }
        return instance;
    }

    public NotifyUtils showOperationNotify(final Activity activity, int type, String content1, String content2, View root) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View rootView = LayoutInflater.from(activity).inflate(
                R.layout.attendance_notify_dialog, null);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        TextView tvNotify = rootView.findViewById(R.id.tv_notify);
        TextView tvTime1 = rootView.findViewById(R.id.tv_time1);
        TextView tvTime2 = rootView.findViewById(R.id.tv_time2);
        Button confirmBtn = rootView.findViewById(R.id.btn_confirm);
        RelativeLayout rl1 = rootView.findViewById(R.id.rl1);
        RelativeLayout rl2 = rootView.findViewById(R.id.rl2);
        RelativeLayout rl3 = rootView.findViewById(R.id.rl3);
        switch (type) {
            case AttendanceConstants.TYPE_WORK_NORMAL:
                //正常上班
                rl2.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                tvTime1.setText(content1);
                tvTime2.setText(content2);
                tvTime1.setTextColor(activity.getResources().getColor(R.color.attendance_666666));
                tvTime2.setTextColor(activity.getResources().getColor(R.color.attendance_state_1));
                break;
            case AttendanceConstants.TYPE_WORK_UNNORMAL:
                //上班迟到
                rl1.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                tvNotify.setText(content1);
                tvTime2.setText(content2);
                tvNotify.setTextColor(activity.getResources().getColor(R.color.attendance_state_2));
                tvTime2.setTextColor(activity.getResources().getColor(R.color.attendance_state_2));
                break;
            case AttendanceConstants.TYPE_OFF_NORMAL:
                //正常下班
                rl2.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                tvTime1.setText(content1);
                tvTime2.setText(content2);
                tvTime1.setTextColor(activity.getResources().getColor(R.color.attendance_666666));
                tvTime2.setTextColor(activity.getResources().getColor(R.color.attendance_state_1));
                break;
            case AttendanceConstants.TYPE_OFF_UNNORMAL:
                //早退
                rl1.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                tvNotify.setText(content1);
                tvTime2.setText(content2);
                tvNotify.setTextColor(activity.getResources().getColor(R.color.attendance_state_3));
                tvTime2.setTextColor(activity.getResources().getColor(R.color.attendance_state_3));
                break;
            case AttendanceConstants.TYPE_NO_RECORD:
                //旷工
                rl1.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                tvNotify.setText(content1);
                tvTime2.setText(content2);
                tvNotify.setTextColor(activity.getResources().getColor(R.color.attendance_state_4));
                tvTime2.setTextColor(activity.getResources().getColor(R.color.attendance_state_4));
                break;
        }

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, rootView);


        /**
         * 点击了确认
         */
        confirmBtn.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return instance;
    }

    public NotifyUtils showEditMenu1(final Activity activity, String title1, String hint1, View root, OnSingleValueListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View rootView = LayoutInflater.from(activity).inflate(
                R.layout.attendance_edit_late_times_dialog, null);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        TextView tvTitle1 = rootView.findViewById(R.id.title);
        EditText etContent1 = rootView.findViewById(R.id.et);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        Button btnSure = rootView.findViewById(R.id.btn_sure);
        TextUtil.setText(tvTitle1, title1);
        if (TextUtils.isEmpty(hint1)) {
            etContent1.setHint(hint1);
        }
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, rootView);

        /**
         * 点击了确认
         */
        btnSure.setOnClickListener(view -> {
            String s1 = etContent1.getText().toString();

            if (TextUtils.isEmpty(s1)) {
                return;
            } else {
                if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                    if (listener != null) {
                        listener.clickSure(s1);
                    }
                    mResendMailPopup.dismiss();
                }
            }

        });
        btnCancel.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return instance;
    }

    public NotifyUtils showEditMenu2(final Activity activity, String title1, String title2, String title3, String hint1, String hint2, View root, OnClickSureListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View rootView = LayoutInflater.from(activity).inflate(
                R.layout.attendance_edit_dialog, null);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        TextView tvTitle1 = rootView.findViewById(R.id.title1);
        TextView tvTitle2 = rootView.findViewById(R.id.title2);
        TextView tvTitle3 = rootView.findViewById(R.id.title3);
        EditText etContent1 = rootView.findViewById(R.id.et1);
        EditText etContent2 = rootView.findViewById(R.id.et2);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        Button btnSure = rootView.findViewById(R.id.btn_sure);
        TextUtil.setText(tvTitle1, title1);
        TextUtil.setText(tvTitle2, title2);
        TextUtil.setText(tvTitle3, title3);
        if (TextUtils.isEmpty(hint1)) {
            etContent1.setHint(hint1);
        }
        if (TextUtils.isEmpty(hint2)) {
            etContent2.setHint(hint2);
        }
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, rootView);

        /**
         * 点击了确认
         */
        btnSure.setOnClickListener(view -> {
            String s1 = etContent1.getText().toString();
            String s2 = etContent2.getText().toString();
            if (TextUtils.isEmpty(s1) || TextUtil.isEmpty(s2)) {
                return;
            } else {
                if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                    if (listener != null) {
                        listener.clickSure(s1, s2);
                    }
                    mResendMailPopup.dismiss();
                }
            }

        });
        btnCancel.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return instance;
    }

    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView) {
        return initDisPlay(activity, dm, mResendMailPopupView, true);
    }

    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView, boolean bl) {
        PopupWindow mResendMailPopup = new PopupWindow(mResendMailPopupView,
                dm.widthPixels, dm.heightPixels,
                true);
        //解决华为闪屏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //没有设置宽高显示不全的问题
        mResendMailPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mResendMailPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mResendMailPopup.setTouchable(true);
        mResendMailPopup.setOutsideTouchable(true);
        mResendMailPopup.setBackgroundDrawable(new ColorDrawable());
        ScreenUtils.letScreenGray(activity);
        if (bl) {
            mResendMailPopup.setAnimationStyle(R.style.AnimTools);
        }
        return mResendMailPopup;
    }

    /**
     * 确定和取消的接口
     */


    public interface OnClickSureListener {
        void clickSure(String value1, String value2);
    }

    public interface OnSingleValueListener {
        void clickSure(String value1);
    }

    public interface OnTimeSelectedListener {

        void selected(String time);

    }

    public interface OnDateSelectedListener {

        void selected(long time);

    }

    /**
     * 选择时间
     *
     * @param activity
     * @param parent
     * @param listener
     * @return
     */
    public static CustomPopWindow showTimeSelectMenu(Activity activity, View parent, String time,
                                                     final OnTimeSelectedListener listener) {

        int visibleItems = 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (!TextUtils.isEmpty(time)) {
            String[] split = time.split(":");
            int hour = TextUtil.parseInt(split[0]);
            int minute = TextUtil.parseInt(split[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }
        Context mContext = parent.getContext();
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_email_select_time, null);
        SoftKeyboardUtils.hide(parent);
        Button sureButton = (Button) view.findViewById(R.id.sure);
        WheelView mHourWheelView = (WheelView) view.findViewById(R.id.hour);
        TextView tvTime = ((TextView) view.findViewById(R.id.tv_time));
        tvTime.setText(time + "");

        WheelView mMinuteWheelView = (WheelView) view.findViewById(R.id.minute);
        NumericWheelAdapter mHourAdapter = new NumericWheelAdapter(activity, 0, 23, "%02d");
        NumericWheelAdapter mMinuteAdapter = new NumericWheelAdapter(activity, 0, 59, "%02d");
        mHourAdapter.setItemResource(R.layout.item_wheel_text);
        mHourAdapter.setItemTextResource(R.id.tempValue);
        mHourAdapter.setLabel("");
        mHourWheelView.setVisibleItems(visibleItems);
        mHourWheelView.setViewAdapter(mHourAdapter);
        mHourWheelView.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        mHourWheelView.setCyclic(true);//是否可循环滑动


        mHourWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                String hour = "";
                String minute = "";
                if (newValue == 0 || newValue < 10) {
                    hour = "0" + newValue;
                } else {
                    hour = newValue + "";
                }
                if (mMinuteWheelView.getCurrentItem() == 0 || mMinuteWheelView.getCurrentItem() < 10) {
                    minute = "0" + mMinuteWheelView.getCurrentItem();
                } else {
                    minute = "" + mMinuteWheelView.getCurrentItem();
                }
                tvTime.setText(hour + ":" + minute);
            }
        });
        mMinuteWheelView.setVisibility(View.VISIBLE);
        mMinuteAdapter.setItemResource(R.layout.item_wheel_text);
        mMinuteAdapter.setItemTextResource(R.id.tempValue);
        mMinuteAdapter.setLabel("");

        mMinuteWheelView.setVisibleItems(visibleItems);
        mMinuteWheelView.setViewAdapter(mMinuteAdapter);
        mMinuteWheelView.setCurrentItem(calendar.get(Calendar.MINUTE));
        mMinuteWheelView.setCyclic(true);//是否可循环滑动
        mMinuteWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            String hour = "";
            String minute = "";
            if (newValue == 0 || newValue < 10) {
                minute = "0" + newValue;
            } else {
                minute = "" + newValue;
            }
            if (mHourWheelView.getCurrentItem() == 0 || mHourWheelView.getCurrentItem() < 10) {
                hour = "0" + mHourWheelView.getCurrentItem();
            } else {
                hour = "" + mHourWheelView.getCurrentItem();
            }
            tvTime.setText(hour + ":" + minute);
        });

        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)//显示的布局，还可以通过设置一个View
//                     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setWidth((int) ScreenUtils.getScreenWidth(mContext))
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .setAnimationStyle(R.style.pop_bottom_anim)
                .create()//创建PopupWindowpopWindow
                .showAtLocation(parent, Gravity.BOTTOM, 0, 0);//显示PopupWindow
        sureButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.selected(tvTime.getText().toString());
            }
            popWindow.dissmiss();
        });

        return popWindow;
    }

    public static CustomPopWindow showDateSelectMenu(Activity activity, View parent, String time,
                                                     final OnDateSelectedListener listener) {

        int visibleItems = 5;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar2.set(Calendar.YEAR, startYear);
        calendar2.set(Calendar.MONTH, startMonth - 1);
        calendar2.set(Calendar.DAY_OF_MONTH, startDay);
        if (!TextUtils.isEmpty(time)) {
            String[] split = time.split(":");
            int hour = TextUtil.parseInt(split[0]);
            int minute = TextUtil.parseInt(split[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }
        int maxDayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Context mContext = parent.getContext();
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_email_select_date, null);
        SoftKeyboardUtils.hide(parent);
        Button sureButton = (Button) view.findViewById(R.id.sure);
        WheelView yearWv = (WheelView) view.findViewById(R.id.year);
        WheelView monthWv = (WheelView) view.findViewById(R.id.month);
        WheelView dayWv = (WheelView) view.findViewById(R.id.day);
        TextView tvTime = ((TextView) view.findViewById(R.id.tv_time));
        TextView tvCancel = ((TextView) view.findViewById(R.id.tv_cancel));
        tvTime.setText(time + "");

        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(activity, startYear, 2050, "%02d");
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(activity, 1, 12, "%02d");
        NumericWheelAdapter dayAdapter = new NumericWheelAdapter(activity, 1, maxDayNum, "%02d");

        //年
        yearAdapter.setItemResource(R.layout.item_wheel_text);
        yearAdapter.setItemTextResource(R.id.tempValue);
        yearAdapter.setLabel("");
        yearWv.setVisibleItems(visibleItems);
        yearWv.setViewAdapter(yearAdapter);
        yearWv.setCurrentItem(0);
        yearWv.setCyclic(true);//是否可循环滑动
        //月
        monthWv.setVisibility(View.VISIBLE);
        monthAdapter.setItemResource(R.layout.item_wheel_text);
        monthAdapter.setItemTextResource(R.id.tempValue);
        monthAdapter.setLabel("");
        monthWv.setVisibleItems(visibleItems);
        monthWv.setViewAdapter(monthAdapter);
        monthWv.setCurrentItem(calendar.get(Calendar.MONTH));
        monthWv.setCyclic(true);//是否可循环滑动
        //日
        dayAdapter.setItemResource(R.layout.item_wheel_text);
        dayAdapter.setItemTextResource(R.id.tempValue);
        dayAdapter.setLabel("");
        dayWv.setVisibleItems(visibleItems);
        dayWv.setViewAdapter(dayAdapter);
        dayWv.setCurrentItem(calendar.get(Calendar.DATE) - 1);
        dayWv.setCyclic(true);//是否可循环滑动


        yearWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                CharSequence itemText = yearAdapter.getItemText(newValue);
                int year = TextUtil.parseInt(itemText.toString());
                calendar2.set(Calendar.YEAR, year);
                int maximum = calendar2.getActualMaximum(Calendar.DATE);
                NumericWheelAdapter adapter = new NumericWheelAdapter(activity, 1, maximum, "%02d");
                adapter.setItemResource(R.layout.item_wheel_text);
                adapter.setItemTextResource(R.id.tempValue);
                adapter.setLabel("");
                dayWv.setViewAdapter(adapter);
                dayWv.setVisibleItems(visibleItems);
                dayWv.setCyclic(true);//是否可循环滑动
            }
        });
        monthWv.addChangingListener((wheel, oldValue, newValue) -> {
            CharSequence itemText = monthAdapter.getItemText(newValue);
            int month = TextUtil.parseInt(itemText.toString());
            int day = dayWv.getCurrentItem() + 1;
            calendar2.set(Calendar.MONTH, month - 1);
            int maximum = calendar2.getActualMaximum(Calendar.DATE);
            NumericWheelAdapter adapter = new NumericWheelAdapter(activity, 1, maximum, "%02d");
            adapter.setItemResource(R.layout.item_wheel_text);
            adapter.setItemTextResource(R.id.tempValue);
            adapter.setLabel("");
            dayWv.setViewAdapter(adapter);
            dayWv.setVisibleItems(visibleItems);
            dayWv.setCyclic(true);//是否可循环滑动
        });
        dayWv.addChangingListener((wheel, oldValue, newValue) -> {
            int maximum = calendar2.getActualMaximum(Calendar.DATE);
            int day = newValue + 1;
            if (day > maximum) {
                dayWv.setCurrentItem(maximum - 1);
                calendar2.set(Calendar.DAY_OF_MONTH, maximum);
            } else {
                calendar2.set(Calendar.DAY_OF_MONTH, day);
            }
        });
        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)//显示的布局，还可以通过设置一个View
//                     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setWidth((int) ScreenUtils.getScreenWidth(mContext))
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .setAnimationStyle(R.style.pop_bottom_anim)
                .create()//创建PopupWindowpopWindow
                .showAtLocation(parent, Gravity.BOTTOM, 0, 0);//显示PopupWindow
        sureButton.setOnClickListener(v -> {
            if (listener != null) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, startYear + yearWv.getCurrentItem());
                c.set(Calendar.MONTH, monthWv.getCurrentItem());
                c.set(Calendar.DAY_OF_MONTH, dayWv.getCurrentItem() + 1);
                listener.selected(c.getTimeInMillis());
            }
            popWindow.dissmiss();
        });
        tvCancel.setOnClickListener(v -> {
            popWindow.dissmiss();
        });

        return popWindow;
    }

    public static CustomPopWindow showDateSelectMenu2(Activity activity, View parent, String time,
                                                      final OnDateSelectedListener listener) {

        Context mContext = parent.getContext();
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_pick_date_layout, null);
        SoftKeyboardUtils.hide(parent);
        Button sureButton = (Button) view.findViewById(R.id.sure);
        DatePicker datePiker = view.findViewById(R.id.date_piker);
        set_date_picker_text_colour(mContext, datePiker, 55, true);


        TextView tvTime = ((TextView) view.findViewById(R.id.tv_time));
        TextView tvCancel = ((TextView) view.findViewById(R.id.tv_cancel));
        tvTime.setText(time + "");


        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)//显示的布局，还可以通过设置一个View
//                     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setWidth((int) ScreenUtils.getScreenWidth(mContext))
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .setAnimationStyle(R.style.pop_bottom_anim)
                .create()//创建PopupWindowpopWindow
                .showAtLocation(parent, Gravity.BOTTOM, 0, 0);//显示PopupWindow
        sureButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.selected(System.currentTimeMillis());
            }
            popWindow.dissmiss();
        });
        tvCancel.setOnClickListener(v -> {
            popWindow.dissmiss();
        });

        return popWindow;
    }

    private static void set_date_picker_text_colour(Context context, DatePicker time_picker, int dpVal, boolean isDate) {
        Resources system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("year", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("month", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("day", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) time_picker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) time_picker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) time_picker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(context, hour_numberpicker, dpVal, isDate);
        set_numberpicker_text_colour(context, minute_numberpicker, dpVal, isDate);
        set_numberpicker_text_colour(context, ampm_numberpicker, dpVal, isDate);
    }

    private static void set_numberpicker_text_colour(Context context, NumberPicker number_picker, int dpVal, boolean isDate) {
        final int count = number_picker.getChildCount();
        //这里就是要设置的颜色，修改一下作为参数传入会更好
        final int color = context.getResources().getColor(android.R.color.black);

        for (int i = 0; i < count; i++) {
            View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                Field divider = number_picker.getClass().getDeclaredField("mSelectionDivider");

                wheelpaint_field.setAccessible(true);
                divider.setAccessible(true);
                divider.set(number_picker, new ColorDrawable(Color.parseColor("#999999")));//设置分割线颜色

                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                if (child instanceof EditText) {
                    ((EditText) child).setTextColor(color);
                }
                number_picker.invalidate();
                if (isDate) {
                    setPickerSize(context, number_picker, dpVal, 0, 0, 30, 0);
                } else {
                    setPickerSize(context, number_picker, dpVal, 0, 0, 0, 0);
                }
            } catch (NoSuchFieldException e) {
                Log.w("setColor", e);
            } catch (IllegalAccessException e) {
                Log.w("setColor", e);
            } catch (IllegalArgumentException e) {
                Log.w("setColor", e);
            }
        }
    }

    private static void setPickerSize(Context context, NumberPicker np, int widthDpValue, int left, int top, int right, int bottom) {
        int widthPxValue = dp2px(context, widthDpValue);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPxValue, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);//这儿参数可根据需要进行更改
        np.setLayoutParams(params);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
