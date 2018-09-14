package com.hjhq.teamface.basis.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.listener.LinkClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文本工具类
 *
 * @author lx
 * @date 2017/6/30
 */

public class TextUtil {
    /**
     * 判断文本内容是否为null或空字符串
     *
     * @param str 文本
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 为文本控件填充内容
     *
     * @param view 文本控件
     * @param str  文本
     */
    public static void setText(TextView view, CharSequence str) {
        if (str != null && view != null) {
            view.setText(str);
        }
    }

    /**
     * 为文本控件填充非空内容
     *
     * @param view 文本控件
     * @param str  文本
     */
    public static void setNonEmptyText(TextView view, String str) {
        if (!isEmpty(str) && view != null) {
            view.setText(str);
        }
    }

    /**
     * 为编辑控件填充内容
     *
     * @param view 编辑控件
     * @param str  文本
     */
    public static void setText(EditText view, String str) {
        if (str != null && view != null) {
            view.setText(str);
        }
    }

    /**
     * 设置内容，没有就隐藏TextView
     *
     * @param view 文本控件
     * @param str  文本
     */
    public static void setTextorVisibility(TextView view, CharSequence str) {
        if (view == null) {
            return;
        }
        if (isEmpty(str)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            setText(view, str);
        }
    }

    /**
     * 为文本控件设置提示
     *
     * @param view 文本控件
     * @param hint 提示
     */
    public static void setHint(TextView view, String hint) {
        if (hint != null && view != null) {
            view.setHint(hint);
        }
    }

    /**
     * 返回非空的值
     *
     * @param val1 值1
     * @param val2 值2
     */
    public static String getNonNull(String val1, String val2) {
        if (val1 != null) {
            return val1;
        }
        if (val2 != null) {
            return val2;
        }
        return "";
    }

    /**
     * 比较两个值是否一样，都为null返回false
     *
     * @param str String类型
     * @param i   Integer类型
     */
    public static boolean equals(String str, Integer i) {
        if (i == null || str == null) {
            return false;
        } else if (str.equals(i + "")) {
            return true;
        }
        return false;
    }

    /**
     * 比较两个值是否一样，都为null返回false
     *
     * @param str  String类型
     * @param str2 String类型
     */
    public static boolean equals(String str, String str2) {
        if (str2 == null || str == null) {
            return false;
        } else if (str.equals(str2)) {
            return true;
        }
        return false;
    }

    /**
     * 将文本解析成int类型数字，默认为0
     *
     * @param str 文本
     * @return 数字
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 将文本解析成int类型数字
     *
     * @param str 文本
     * @param def 默认数字
     * @return 数字
     */
    public static int parseInt(String str, int def) {
        try {
            if (!TextUtil.isEmpty(str)) {
                def = Integer.parseInt(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 将文本解析成long类型数字，默认为0
     *
     * @param str 文本
     * @return 数字
     */
    public static long parseLong(String str) {
        return parseLong(str, 0);
    }

    /**
     * 将文本解析成long类型数字
     *
     * @param str 文本
     * @param def 默认数字
     * @return 数字
     */
    public static long parseLong(String str, long def) {
        if (isEmpty(str)) {
            return def;
        }
        try {
            def = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 将文本解析成double类型数字，默认为0
     *
     * @param str 文本
     * @return 数字
     */
    public static double parseDouble(String str) {
        return parseDouble(str, 0);
    }

    /**
     * 将文本解析成double类型数字
     *
     * @param str 文本
     * @param def 默认数字
     * @return 数字
     */
    public static double parseDouble(String str, double def) {
        try {
            def = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 根据关键字匹配内容
     *
     * @param keyword 关键字
     * @param content 内容
     * @return
     */
    public static SpannableString getSpannableString(String keyword, String content) {
        SpannableString ss = new SpannableString(content);
        if (!TextUtils.isEmpty(keyword)) {
            Pattern p = Pattern.compile(keyword);
            Matcher matcher = p.matcher(content);
            while (matcher.find()) {
                String str = matcher.group();
                int matcherStart = matcher.start();
                int matcherEnd = matcher.end();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#3689E9"));
                ss.setSpan(foregroundColorSpan, matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    public static SpannableString getClickSpannableString(Context context, String keyword, String content) {
        SpannableString ss = new SpannableString(content);
        if (!TextUtils.isEmpty(keyword)) {
            Pattern p = Pattern.compile(keyword);
            Matcher matcher = p.matcher(content);
            while (matcher.find()) {
                String str = matcher.group();
                int matcherStart = matcher.start();
                int matcherEnd = matcher.end();
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#3689E9"));
//                ss.setSpan(foregroundColorSpan, matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ClickableSpan clickableSpan = new LinkClickableSpan(context);
                ss.setSpan(clickableSpan, matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    /**
     * 根据关键字匹配内容
     *
     * @param keyword 关键字
     * @param content 内容
     * @param color   颜色
     * @return
     */
    public static SpannableString getSpannableString(String keyword, String content, int color) {
        SpannableString ss = new SpannableString(content);
        if (!TextUtils.isEmpty(keyword)) {
            Pattern p = Pattern.compile(keyword);
            Matcher matcher = p.matcher(content);
            while (matcher.find()) {
                String str = matcher.group();
                int matcherStart = matcher.start();
                int matcherEnd = matcher.end();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                ss.setSpan(foregroundColorSpan, matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    /**
     * 将double类型字符串转换成int类型字符串
     *
     * @param text
     * @return
     */
    public static String doubleParseInt(String text) {
        double v = parseDouble(text, -99899);
        if (v != -99899) {
            text = ((int) v) + "";
        }
        return text;
    }

}
