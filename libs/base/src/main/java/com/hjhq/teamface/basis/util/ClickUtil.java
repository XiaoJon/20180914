package com.hjhq.teamface.basis.util;

/**
 * Created by Administrator on 2018/8/6.
 * Describe：
 */

public class ClickUtil {
    private static long time = 0L;

    public static void click(ClickListener listener) {
        if (System.currentTimeMillis() - time > 1500) {
            if (listener != null) {
                time = System.currentTimeMillis();
                listener.click();
            }
        }

    }

    public interface ClickListener {
        void click();
    }
}
