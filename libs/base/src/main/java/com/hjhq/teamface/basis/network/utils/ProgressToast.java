package com.hjhq.teamface.basis.network.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.view.WindowProgress;


/**
 * WindowManager 全局Loading
 *
 * @author Administrator
 * @date 2018/1/22
 */

public class ProgressToast {
    private WindowProgress root;
    private Context mContext;

    private static class SingletonHolder {
        private static final ProgressToast INSTANCE = new ProgressToast();
    }

    public static ProgressToast getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ProgressToast() {
    }


    /**
     * 创建基于Window的提示窗体
     *
     * @param msg
     */
    public synchronized void createWindowView(Context context, String msg) {
        this.mContext = context;
        if (root != null) {
            return;
        }

        final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = -2;
        params.width = -2;
        params.format = 1;
        params.gravity = Gravity.CENTER;

        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        LayoutInflater inflater = LayoutInflater.from(context);
        // 得到加载view
        root = (WindowProgress) inflater.inflate(R.layout.dialog_loading_two, null);
        // 提示文字
        TextView tipTextView = root.findViewById(R.id.tipTextView);
        // 设置加载信息
        tipTextView.setText(msg);

        try {
            wm.addView(root, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //监听返回键
        root.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    /**
     * 关闭window
     */
    public synchronized void closeWindowView() {
        if (root == null) {
            return;
        }
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        try {
            wm.removeView(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        root = null;
        mContext = null;
    }

    /**
     * 返回鍵监听
     */
    private WindowProgress.DispatchKeyEventListener mDispatchKeyEventListener = event -> {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME || event.getKeyCode() == KeyEvent.KEYCODE_CALL) {
            closeWindowView();
            return true;
        }
        return false;
    };

}
