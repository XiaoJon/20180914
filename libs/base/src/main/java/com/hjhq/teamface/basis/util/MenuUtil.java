package com.hjhq.teamface.basis.util;

import android.app.Activity;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Menu 工具类
 * Created by lx on 2017/8/31.
 */

public class MenuUtil {
    /**
     * 设置文本Menu的字体颜色
     *
     * @param activity
     * @param mColor
     */
    public static void setActionBarText(final Activity activity, int mColor) {

        try {
            final LayoutInflater inflater = activity.getLayoutInflater();
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);
            LayoutInflaterCompat.setFactory(inflater, (parent, name, context, attrs) -> {
                //因为我使用的是supportv7包
                if (name.equalsIgnoreCase("android.support.v7.view.menu.IconMenuItemView")
                        || name.equalsIgnoreCase("android.support.v7.view.menu.ActionMenuItemView")) {
                    final View view;
                    try {
                        view = inflater.createView(name, null, attrs);
                        if (view instanceof TextView)
                            ((TextView) view).setTextColor(mColor);
                        view.setOnLongClickListener(v -> false);
                        return view;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InflateException ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
