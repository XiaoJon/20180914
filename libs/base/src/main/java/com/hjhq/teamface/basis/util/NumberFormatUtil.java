package com.hjhq.teamface.basis.util;

import android.text.TextUtils;

/**
 * 将阿拉伯数字文字中文数字
 */
public class NumberFormatUtil {
    //单位
    private static final String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    private static final char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    /**
     * 将整数转换成汉字数字
     *
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero && i >= 1) {
                if ('0' == val[i - 1]) {
                    return numArray[0] + "";
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        if (num == 10) {
            return units[1];
        }
        if (num > 10 && num <= 90) {
            int i = num / 10;
            if (num % 10 == 0) {
                return numArray[i] + units[1];
            }
        }
        if (num >= 10 && num < 20) {
            return sb.toString().substring(1, sb.toString().length());
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            if (sb.toString().endsWith("零")) {
                return sb.toString().substring(0, sb.toString().length() - 1);
            }
        }
        return sb.toString();
    }

    /**
     * 将小数转换成汉字数字
     *
     * @param decimal 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }

    /**
     * 格式化小数部分的数字
     *
     * @param decimal 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }

    public static String integerToString(int num) {
        if (0 == num) {
            return numArray[0] + "";
        }
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    //当前val[i]的下一个值val[i-1]为0则不输出零
                    continue;
                } else {
                    //只有当当前val[i]的下一个值val[i-1]不为0才输出零
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        String value = sb.toString();
        if (value.endsWith("零")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }


} 