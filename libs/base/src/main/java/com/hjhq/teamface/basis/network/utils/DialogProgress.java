package com.hjhq.teamface.basis.network.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.TextUtil;

/**
 * MIUI适配Progress
 * Created by Administrator on 2018/1/13.
 */

public class DialogProgress extends Dialog {
    public DialogProgress(Context context) {
        super(context);
    }

    public DialogProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.tipTextView).setVisibility(View.VISIBLE);
            TextView txt = findViewById(R.id.tipTextView);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static DialogProgress show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return null;
        }
        DialogProgress dialog = new DialogProgress(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_loading_two);
        if (TextUtil.isEmpty(message)) {
            message = "加载中...";
        }

        TextView txt = dialog.findViewById(R.id.tipTextView);
        txt.setText(message);
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        // 监听返回键处理
        dialog.setOnCancelListener(dialog1 -> {
            dialog1.dismiss();
            if (cancelListener != null) {
                cancelListener.onCancel(dialog1);
            }
        });
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }
}
