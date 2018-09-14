package com.hjhq.teamface.project.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2018/6/25.
 */

public class TaskUtil {
    /**
     * 输入的接口
     */
    public interface OnInputClickListner {
        boolean inputClickSure(PopupWindow popup,String content);

        boolean inputClickCancel(PopupWindow popup,String content);
    }

    /**
     * @param activity 活动界面
     * @param root     布局控件
     * @param listener 监听
     */
    public static PopupWindow inputDialog(Activity activity, View root, OnInputClickListner listener) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.project_task_check_dialog_input, null);

        EditText dialogInput = mResendMailPopupView.findViewById(R.id.et_dialog_input);

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);

        //点击了确定
        mResendMailPopupView.findViewById(R.id.dialog_btnSure).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                if (listener != null) {
                    boolean bl = listener.inputClickSure(mResendMailPopup,dialogInput.getText().toString());
                    if (bl) {
                        mResendMailPopup.dismiss();
                        ScreenUtils.letScreenLight(activity);
                    }
                } else {
                    ScreenUtils.letScreenLight(activity);
                    mResendMailPopup.dismiss();
                }
            }
        });

        //点击了取消
        mResendMailPopupView.findViewById(R.id.dialog_btnCancel).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                if (listener != null) {
                    boolean bl = listener.inputClickCancel(mResendMailPopup,dialogInput.getText().toString());
                    if (bl) {
                        mResendMailPopup.dismiss();
                        ScreenUtils.letScreenLight(activity);
                    }
                } else {
                    ScreenUtils.letScreenLight(activity);
                    mResendMailPopup.dismiss();
                }
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return mResendMailPopup;
    }

    private static PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView) {
        return initDisPlay(activity, dm, mResendMailPopupView, true);
    }

    private static PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView, boolean bl) {
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
}
