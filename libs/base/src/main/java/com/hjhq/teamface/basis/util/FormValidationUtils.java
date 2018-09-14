package com.hjhq.teamface.basis.util;

/**
 * Created by lx on 2017/3/23.
 */

import java.util.regex.Pattern;

/**
 * 表单校验工具类
 *
 * @author sunger
 */
public class FormValidationUtils {

    public static final int MIN_PWD_LENGTH = 6;
    public static final int MAX_PWD_LENGTH = 16;

    /**
     * 是否包含数字
     *
     * @param str 文本
     * @return 验证通过返回true
     */
    public static boolean isDight(String str) {
        boolean isDigit = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            }
        }
        return isDigit;
    }

    /**
     * 是否包含小写字母
     *
     * @param str 文本
     * @return 验证通过返回true
     */
    public static boolean isLowerCase(String str) {
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        return isUpperCase;
    }

    /**
     * 是否包含大写字母
     *
     * @param str 文本
     * @return 验证通过返回true
     */
    public static boolean isUpperCase(String str) {
        boolean isLowerCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                isLowerCase = true;
            }
        }
        return isLowerCase;
    }

    /**
     * 是否包含特殊字符
     *
     * @param str 文本
     * @return 验证通过返回true
     */
    public static boolean isSpecialChar(String str) {
        String t = "~`@#$%^&*-_=+|\\?/()<>[]{}\",.;'!";
        boolean ist = false;
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < t.length(); j++) {
                if (str.charAt(i) == t.charAt(j)) {
                    ist = true;
                }
            }
        }
        return ist;
    }

    /**
     * 手机号校验 注：支持+86校验
     *
     * @param phoneNum 手机号码
     * @return 验证通过返回true
     */
    public static boolean isMobile(String phoneNum) {
        if (phoneNum == null) {
            return false;
        }
        // 如果手机中有+86则会自动替换掉  
        return phoneNum.replace("+86", "").length() == 11;
    }


    /**
     * 默认密码校验
     * 6到16位数字字母下划线
     *
     * @param pwd
     * @return
     */
    public static boolean isPassword(String pwd) {
        boolean isLenght = pwd.length() >= MIN_PWD_LENGTH && pwd.length() <= MAX_PWD_LENGTH;
        boolean upperCase = isUpperCase(pwd) || isLowerCase(pwd) || isDight(pwd);
        boolean validation = validation("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$", pwd);
        return upperCase && validation && isLenght;
    }


    //
    public static boolean isVerificationCode(String verificationCode) {
        return validation(
                "^\\d{6}$",
                verificationCode);
    }

    /**
     * 邮箱校验
     *
     * @param mail 邮箱字符串
     * @return 如果是邮箱则返回true，如果不是则返回false
     */
    public static boolean isEmail(String mail) {
        return validation(
                "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",
                mail);
    }


    /**
     * 正则校验
     *
     * @param str 需要校验的字符串
     * @return 验证通过返回true
     */
    public static boolean validation(String pattern, CharSequence str) {
        if (str == null)
            return false;
        return Pattern.compile(pattern).matcher(str).matches();
    }

}  