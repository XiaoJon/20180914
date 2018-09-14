package com.hjhq.teamface.basis.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 颜色工具类
 * Created by lx on 2017/5/22.
 */

public class ColorUtils {
    private static final String REGULAR = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$";

    /**
     * 资源 转换成 Color
     *
     * @param colorRes 资源
     */
    public static int resToColor(Context context, int colorRes) {

        int ret = 0xffffff;
        try {
            ret = ResourcesCompat.getColor(context.getResources(), colorRes, null);
        } catch (Exception e) {
        }

        return ret;
    }

    /**
     * 资源 转换成 十六进制
     *
     * @param colorRes 资源
     */
    public static String resToHex(Context context, int colorRes) {
        int resourcesColor = resToColor(context, colorRes);
        String color = Integer.toHexString(resourcesColor);
        if (color.length() == 8) {
            return "#" + color.substring(2);
        }
        return "#" + color;
    }

    /**
     * 将十六进制 颜色代码 转换为 Color
     *
     * @param color 十六进制颜色
     * @return Color
     */
    public static int hexToColor(String color) {
        return hexToColor(color, "#FFFFFF");
    }

    /**
     * 将十六进制 颜色代码 转换为 Color
     *
     * @param color   十六进制颜色
     * @param defaule 默认颜色
     * @return Color
     */
    public static int hexToColor(String color, String defaule) {
        if (!checkColor(color)) {
            color = defaule;
        }
        return Color.parseColor(color);
    }

    /**
     * 检测十六进制的颜色
     *
     * @param color 颜色
     */
    public static boolean checkColor(String color) {
        if (TextUtils.isEmpty(color) || !Pattern.matches(REGULAR, color)) {
            return false;
        }
        return true;
    }
}
