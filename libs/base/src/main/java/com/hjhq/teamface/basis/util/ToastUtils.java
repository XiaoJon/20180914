
package com.hjhq.teamface.basis.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjhq.teamface.basis.R;

/**
 * Toast工具类，不与业务关联
 *
 * @author mou
 */
public class ToastUtils {
    //执行成功
    public static final String SUCCESS = "success";
    //执行失败
    public static final String FAILED = "failed";
    //警告
    public static final String WARNING = "warning";
    //拒绝
    public static final String REFUSED = "refused";
    private static Toast toast;

    /**
     * 显示成功
     *
     * @param res 文本资源
     */
    public static void showSuccess(Context mContext, int res) {
        showSuccess(mContext, mContext.getString(res));
    }

    /**
     * 显示成功
     *
     * @param text 显示内容
     */
    public static void showSuccess(Context mContext, String text) {
        showToast(mContext, text, SUCCESS);
    }

    /**
     * 显示失败
     *
     * @param res 文本资源
     */
    public static void showError(Context mContext, int res) {
        showError(mContext, mContext.getString(res));
    }

    /**
     * 显示失败
     *
     * @param text 显示内容
     */
    public static void showError(Context mContext, String text) {
        showToast(mContext, text, FAILED);
    }

    /**
     * 显示成功或失败或警告等
     *
     * @param text
     * @param type
     */
    public static void showToast(Context mContext, @NonNull String text, @NonNull String type) {
        if (mContext == null || TextUtils.isEmpty(text)) {
            return;
        }
        View view = View.inflate(mContext, R.layout.center_toast_layout, null);
        ImageView icon = view.findViewById(R.id.iv_icon);
        TextView toastText = view.findViewById(R.id.tv_name);
        Toast imageToast = new Toast(mContext);
        imageToast.setGravity(Gravity.CENTER, 0, 0);
        imageToast.setView(view);
        imageToast.setDuration(Toast.LENGTH_SHORT);
        toastText.setText(text);
        switch (type) {
            //成功
            case SUCCESS:
                icon.setImageResource(R.drawable.action_ok);
                break;
            //失败
            case FAILED:
                icon.setImageResource(R.drawable.action_failed);
                break;
            //拒绝
            case REFUSED:
                icon.setImageResource(R.drawable.action_failed);
                break;
            //警告
            case WARNING:
                icon.setImageResource(R.drawable.action_failed);
                break;
            default:
                break;
        }
        imageToast.show();
    }

    /**
     * toast提示
     *
     * @param mContext 上下文
     * @param res      资源id
     */
    public static void showToast(Context mContext, int res) {
        showToast(mContext, mContext.getString(res));
    }

    /**
     * toast提示
     *
     * @param mContext 上下文
     * @param text     提示内容
     */
    public static void showToast(Context mContext, String text) {
        if (text == null || "".equals(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
