package com.hjhq.teamface.oa.friends.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.MyApplication;
import com.hjhq.teamface.bean.TextTag;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.basis.bean.Photo;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    /**
     * 前后台交互日期格式
     */
    public static final String DEFAULT_DATE = "yyyy-MM-dd_HH:mm:ss";

    public static final String DEFAULT_DECIMAL = "###.##";

    /**
     * 投票日期格式
     **/
    public static final String VOTE_DATE_FORMAT = "MM/dd HH:mm";

    /**
     * 默认整数长度
     **/
    public static final int DEFAULT_INTEGER_LENGTH = 9;

    // 转向另一个页面
    public static void gotoActivity(Activity poFrom, Class<?> poTo,
                                    boolean pbFinish, Map<String, String> pmExtra) {
        Intent loIntent = new Intent(poFrom, poTo);

        loIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (pmExtra != null && !pmExtra.isEmpty()) {
            Iterator<String> loKeyIt = pmExtra.keySet().iterator();
            while (loKeyIt.hasNext()) {
                String lsKey = loKeyIt.next();
                loIntent.putExtra(lsKey, pmExtra.get(lsKey));
            }
        }
        if (pbFinish) {
            poFrom.finish();
        }
        poFrom.startActivity(loIntent);
    }

    /**
     * 设定制定字符高亮显示
     *
     * @param src
     * @param filter
     * @param defaultColor
     * @param spannaColor
     * @return
     */
    public static SpannableString getSpannable(String src, String filter,
                                               int defaultColor, int spannaColor) {

        if (Utils.isStrEmpty(src)) {
            return new SpannableString("");
        }

        if (Utils.isStrEmpty(filter)) {
            return new SpannableString(src);
        }

        SpannableString spanString = new SpannableString(src);
        int startIndex = 0;
        int length = 0;
        int color = defaultColor;
        int index = src.indexOf(filter);
        if (index != -1) {
            color = spannaColor;
            startIndex = index;
            length = filter.length();
        }

        ForegroundColorSpan span = new ForegroundColorSpan(MyApplication
                .getInstance().getResources().getColor(color));
        spanString.setSpan(span, startIndex, startIndex + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;

    }

    // 转向另一个页面
    public static void gotoActivity(Activity poFrom, String poTo,
                                    boolean pbFinish, Map<String, String> pmExtra) {
        Intent loIntent = new Intent();
        loIntent.setAction(poTo);

        if (pmExtra != null && !pmExtra.isEmpty()) {
            Iterator<String> loKeyIt = pmExtra.keySet().iterator();
            while (loKeyIt.hasNext()) {
                String lsKey = loKeyIt.next();
                loIntent.putExtra(lsKey, pmExtra.get(lsKey));
            }
        }

        if (pbFinish) {

            poFrom.finish();
        }

        poFrom.startActivity(loIntent);
    }

    // 转向另一个页面
    public static void gotoActivity(Activity poFrom, Class<?> poTo,
                                    boolean pbFinish, int statAmin, int endAmin) {
        Intent loIntent = new Intent(poFrom, poTo);

        if (pbFinish) {

            poFrom.finish();
        }

        poFrom.startActivity(loIntent);

        poFrom.overridePendingTransition(statAmin, endAmin);

    }

    // 转向另一个页面
    public static void gotoActivity(Activity poFrom, Class<?> poTo,
                                    boolean pbFinish, int statAmin, int endAmin, Bundle mBundle) {

        Intent loIntent = new Intent(poFrom, poTo);

        // Bundle mBundle = new Bundle();
        // mBundle.putSerializable(DialogActivity.POSITON, extra);

        loIntent.putExtras(mBundle);

        if (pbFinish) {

            poFrom.finish();
        }

        poFrom.startActivity(loIntent);

        poFrom.overridePendingTransition(statAmin, endAmin);

    }

    /**
     * 跳转至图片显示页面
     *
     * @param activity
     * @param annexs
     * @param selectIndex 默认设置显示图片位置
     * @param isCanDelete 是否允许删除操作
     */
    public static void gotoImagePagerActivity(Activity activity,
                                              ArrayList<Photo> annexs, int selectIndex, boolean isCanDelete) {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.PICTURE_LIST, annexs);
        intent.putExtra(ImagePagerActivity.SELECT_INDEX, selectIndex);
        intent.putExtra(ImagePagerActivity.IS_CAN_DELETE, isCanDelete);
        activity.startActivity(intent);
    }

    // 字符串是否为空（全是不可见字符的字符串认为是空）
    public static boolean isStrEmpty(Editable poStr) {
        if (poStr == null) {
            return true;
        } else {
            String lsStr = poStr.toString();
            return isStrEmpty(lsStr);
        }
    }

    // 字符串是否为空（全是不可见字符的字符串认为是空）
    public static boolean isStrEmpty(String psStr) {
        return psStr == null || psStr.trim().length() == 0;
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        if (context == null) {
            return true;
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (pxValue / dm.scaledDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

    /**
     * 文本时间跳转
     *
     * @param textView    需改变的文本框对象
     * @param handler     hander启动对象
     * @param formatStr   时间格式
     * @param delayMillis 刷新时间
     * @return
     */
    public static Runnable textTimeAutoTrun(final TextView[] textView,
                                            final Handler handler, final String formatStr, final int delayMillis) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String str = (String) new SimpleDateFormat(formatStr)
                        .format(calendar.getTimeInMillis());
                for (TextView temp : textView) {
                    temp.setText(str);
                }

                handler.postDelayed(this, delayMillis);
            }
        };
        return runnable;
    }

    /**
     * int型转为中文数字
     *
     * @param toZH 需要转换的数字
     * @return
     */
    public static String intToZHDigital(int toZH) {
        String[] zh = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

        String str = "";
        StringBuffer sb = new StringBuffer(String.valueOf(toZH));
        sb = sb.reverse();
        int r = 0;
        int l = 0;
        for (int j = 0; j < sb.length(); j++) {
            /**
             * 当前数字
             */
            r = Integer.valueOf(sb.substring(j, j + 1));

            if (j != 0)
            /**
             * 上一个数字
             */
                l = Integer.valueOf(sb.substring(j - 1, j));

            if (j == 0) {
                if (r != 0 || sb.length() == 1)
                    str = zh[r];
                continue;
            }

            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7
                    || j == 9) {
                if (r != 0)
                    str = zh[r] + unit[j] + str;
                else if (l != 0)
                    str = zh[r] + str;
                continue;
            }

            if (j == 4 || j == 8) {
                str = unit[j] + str;
                if ((l != 0 && r == 0) || r != 0)
                    str = zh[r] + str;
                continue;
            }
        }
        if (String.valueOf(toZH).length() == 2
                && str.substring(0, 1).equals("一")) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * @param str
     * @return
     */
    public static String getDate(String str) {

        String result = "";

        try {
            result = str.split("_")[0];
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    /**
     * @param str
     * @return
     */
    public static String getTime(String str) {

        String result = "";

        try {
            result = str.split("_")[1];
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    /**
     * @param str
     * @param format 转换格式 null值默认"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date strToDate(String str, String format) {
        if (format == null) {
            format = "yyyy-MM-dd_HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param str
     * @return
     */
    public static Date strToDefaultDate(String str) {

        return strToDate(str, DEFAULT_DATE);
    }

    /**
     * @param millis
     * @return
     */
    public static String dateToDefaultStr(long millis) {

        String str = (String) new SimpleDateFormat(DEFAULT_DATE, Locale.CHINA)
                .format(millis);
        return str;
    }

    /**
     * @return
     */
    public static String getDefaultDateStr() {

        String str = (String) new SimpleDateFormat(DEFAULT_DATE, Locale.CHINA)
                .format(System.currentTimeMillis());
        return str;
    }

    /**
     * @param millis
     * @param format 转换格式 null值默认"yyyy-MM-dd_HH:mm:ss"
     * @return
     */
    public static String dateToStr(long millis, String format) {
        if (format == null) {
            format = "yyyy-MM-dd_HH:mm:ss";
        }
        String str = (String) new SimpleDateFormat(format, Locale.CHINA)
                .format(millis);
        return str;
    }

    /**
     * 时间类型的str 转为另一种格式的str
     *
     * @param convertedStr    被转换的str
     * @param convertedFormat 被转换的str格式
     * @param convertFormat   需转换成的格式
     * @return 转换后的str
     */
    public static String dateTypeStrToStr(String convertedStr,
                                          String convertedFormat, String convertFormat) {
        if (convertedStr == null || TextUtils.isEmpty(convertedStr)) {
            return "";
        }
        return dateToStr(strToDate(convertedStr, convertedFormat).getTime(),
                convertFormat);
    }

    /**
     * @param editText
     * @return
     */
    public static String getEditString(EditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    /**
     * @param textView
     * @return
     */
    public static String getTextString(TextView textView) {
        if (textView != null && textView.getText() != null) {
            return textView.getText().toString();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Activity activity) {

        String version = "Version  ";

        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    activity.getPackageName(), 0);
            version = version + info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }

    /**
     * 标示android 设备的序列号
     *
     * @return
     */
    public static String getSerialNumber() {

        final TelephonyManager tm = (TelephonyManager) MyApplication
                .getInstance().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = tm.getDeviceId();
        // tmSerial = "" + tm.getSimSerialNumber();
        // androidId = ""
        // + android.provider.Settings.Secure.getString(MyApplication
        // .getContextObject().getContentResolver(),
        // android.provider.Settings.Secure.ANDROID_ID);
        //
        // UUID deviceUuid = new UUID(androidId.hashCode(),
        // ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //
        // String uniqueId = deviceUuid.toString();

        return tmDevice;
    }

    /**
     * 获取手机号
     */
    public static String getDevicePhone() {
        TelephonyManager tm = (TelephonyManager) MyApplication
                .getInstance().getSystemService(Context.TELEPHONY_SERVICE);

        //获取本机号码
        return tm.getLine1Number();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName() {

        String version = "";

        try {
            PackageManager manager = MyApplication.getInstance()
                    .getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication
                    .getInstance().getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }

    /**
     * 正则 验证
     *
     * @param pattern
     * @param input
     * @return
     */
    public static boolean patternMatcher(String pattern, CharSequence input) {
        if (pattern == null) {
            pattern = "^$";
        }
        if (input == null) {
            input = "";
        }
        return Pattern.compile(pattern).matcher(input).matches();
    }

    /**
     * 获取wifi名称
     *
     * @param context
     * @return
     */
    public static String getWifiName(Context context) {
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

    /**
     * 隐藏输入框
     *
     * @param activity
     */
    public static void hideInputMethod(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 默认保留两位小数 数字
     */
    public static String getDefaultDecimal(double dfNumber) {
        DecimalFormat df = new DecimalFormat(DEFAULT_DECIMAL);
        return df.format(dfNumber);
    }

    /**
     * 只允许输入两位小数
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText) {
        setPricePoint(editText, DEFAULT_INTEGER_LENGTH);
    }

    public static void setPricePoint(final EditText editText,
                                     final int integerLen) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                if (s.toString().contains("-")) {
                    int index = s.toString().trim().lastIndexOf("-");
                    if (index != 0) {
                        s = s.subSequence(0, s.length() - 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }

                if (s.toString().trim().contains(".")) {
                    int index = s.toString().trim().lastIndexOf(".");
                    if (index != 0) {
                        String temp = s.toString().trim().substring(0, index);
                        if (temp.length() > integerLen) {
                            String temp1 = s.toString().trim()
                                    .substring(0, start).toString();
                            String temp2 = s.toString().trim()
                                    .substring(start + count);
                            temp = temp1 + temp2;
                            editText.setText(temp);
                            editText.setSelection(temp.length());
                        }
                    }
                } else if (s.toString().trim().length() > integerLen) {
                    String temp = s.toString().trim();
                    String temp1 = temp.substring(0, start).toString();
                    String temp2 = temp.substring(start + count);
                    temp = temp1 + temp2;
                    editText.setText(temp);
                    editText.setSelection(temp.length());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    /**
     * 验证是否有某权限
     *
     * @param permission
     * @return
     */
    public static boolean checkPermission(String permission) {

//        ResponseLogin login = ContactDataBase.getInstance().getCurrenUserInfo();
//
//        ArrayList<String> authorityList = login.getAuthorityList();
//
//        // LogUtils.e("~~~~~~~~~2222", authorityList.toString());
//
//        if (null == authorityList || null == permission) {
//            return false;
//        }
//
//        // 管理员权限
//        if (authorityList.contains(Authority.ADMIN)) {
//            return true;
//        }
//
//        return authorityList.contains(permission);

        return true;
    }

    /**
     * 保留小数点后两位
     *
     * @return
     */
    public static String formatDecimalFloatOne(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
    }

    /**
     * 保留位数格式化
     *
     * @return
     */
    public static String formatDecimal(double d, String format) {
        try {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 调用系统拨号界面
     *
     * @param telNumber
     * @param activity
     */
    public static void callPerson(String telNumber, Activity activity) {
        Intent intent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + telNumber));
        activity.startActivity(intent);
    }

    /**
     * 调用系统发送短信界面
     *
     * @param telNumber
     * @param smsBody
     * @param activity
     */
    public static void sendMessage(String telNumber, String smsBody,
                                   Activity activity) {

        Uri smsToUri = Uri.parse("smsto:" + telNumber);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", smsBody);

        activity.startActivity(intent);

    }


    /**
     * parameter 2 is contain in parameter 1.
     *
     * @param sourceFlag
     * @param compareFlag
     * @return
     */
    public static boolean isFlagContain(int sourceFlag, int compareFlag) {
        return (sourceFlag & compareFlag) == compareFlag;
    }

    public static boolean isNull(Object o) {
        return o == null ? true : false;
    }

    public static boolean isNull(List<?> list) {
        return list == null || list.size() == 0 ? true : false;
    }

    public static boolean isNull(String str) {
        return TextUtils.isEmpty(str) ? true : false;
    }

    /**
     * 跨年显示：年/月/日,否则显示：月/日
     *
     * @param time
     * @param showHour 是否显示时分
     * @return
     */
    public static String returnYearMonthDay(long time, boolean showHour) {
        String formatYear = "yyyy/MM/dd";
        String formatMonth = "MM/dd";
        if (showHour) {
            formatYear = "yyyy/MM/dd HH:mm";
            formatMonth = "MM/dd HH:mm";
        }
        String timeText = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int inputYear = calendar.get(Calendar.YEAR);
        int inputMonth = calendar.get(Calendar.MONTH) + 1;
        int inputDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);

        if (inputYear != nowYear) {
            timeText = Utils.dateToStr(time, formatYear);
        } else {
            timeText = Utils.dateToStr(time, formatMonth);
        }

        return timeText;
    }

    /**
     * 文本后追加图标
     *
     * @param context
     * @param tv      文本框
     * @param desc    文本内容
     * @param maxLine 文本显示最大行数
     * @param dd      图标
     */
    public static void ellipsizeWithDrawable(final Context context,
                                             final TextView tv, TextTag tag, final String desc,
                                             final Drawable dd, final int maxLine) {
        ellipsizeWithDrawable(context, tv, tag, desc, dd, maxLine, 0, 0, 0);
    }

    public static void ellipsizeWithDrawable(final Context context,
                                             final TextView tv, final TextTag tag, final String desc,
                                             final Drawable dd, final int maxLine, final int localColor,
                                             final int startLocalIndex, final int localLength) {
        if (tag == null) {
            return;
        }
        if (desc == null) {
            return;
        }
        if (tag.width <= 0) {
            tv.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            // TODO Auto-generated method stub
                            if (Build.VERSION.SDK_INT >= 16) {

                                tv.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                tv.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                            ellipsizeText(context, tv, tag, desc, dd, maxLine,
                                    localColor, startLocalIndex, localLength);
                        }
                    });
        } else {

            ellipsizeText(context, tv, tag, desc, dd, maxLine, localColor,
                    startLocalIndex, localLength);

        }
    }

    private static void ellipsizeText(final Context context, final TextView tv,
                                      TextTag tag, final String desc, final Drawable dd,
                                      final int maxLine, final int localColor, final int startLocalIndex,
                                      final int localLength) {
        if (tag == null) {

            return;
        }

        if (tag.getWidth() <= 0) {

            int paddingLeft = tv.getPaddingLeft();
            int paddingRight = tv.getPaddingRight();
            TextPaint paint = tv.getPaint();
            float moreText = tv.getTextSize() * 3;

            tag.setMoreText(moreText);
            tag.setPaddingLeft(paddingLeft);
            tag.setPaddingRight(paddingRight);
            tag.setPaint(paint);
            tag.setWidth(tv.getWidth());
            tag.setTextSize(tv.getTextSize());
        }

        // Drawable dd = context.getResources().getDrawable(
        // R.drawable.confirm_open_down);
        VerticalImageSpan is = null;
        if (dd != null) {
            float textSize = tag.getTextSize();
            float scale = textSize / dd.getIntrinsicWidth();
            dd.setBounds(0, 0, (int) (dd.getIntrinsicWidth() * scale),
                    (int) (dd.getIntrinsicHeight() * scale));

            is = new VerticalImageSpan(dd);
        }

        float availableTextWidth = (tag.width - tag.paddingLeft - tag.paddingRight)
                * maxLine - tag.moreText;

        CharSequence ellipsizeStr = TextUtils.ellipsize(desc, tag.paint,
                availableTextWidth, TextUtils.TruncateAt.END);

        CharSequence temp = "";
        if (ellipsizeStr.length() < desc.length()) {
            temp = ellipsizeStr;
        } else {
            temp = desc;
        }
        if (dd != null) {
            temp = temp + ".";
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
        if (localColor != 0) {
            ForegroundColorSpan span = new ForegroundColorSpan(localColor);
            ssb.setSpan(span, startLocalIndex, startLocalIndex + localLength,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (is != null) {
            ssb.setSpan(is, temp.length() - 1, temp.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tv.setText(ssb);
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    public static String getRefreshTime(long lastRefreshTime) {
        String showTime = "刚刚";
        long nowTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastRefreshTime);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        calendar.setTimeInMillis(nowTime);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
        int nowHour = calendar.get(calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(calendar.MINUTE);

//		LogUtils.e("-----------111---" + month + "-" + day + "-" + hour + "-"
//				+ minute);
//		LogUtils.e("-----------222---" + nowMonth + "-" + nowDay + "-"
//				+ nowHour + "-" + nowMinute);

        if (month != nowMonth) {
            return (nowTime - month) + "个月以前";
        } else if (day != nowDay) {
            return (nowDay - day) + "天以前";
        } else if (nowHour != hour) {
            return (nowHour - hour) + "小时以前";
        } else if (minute != nowMinute) {
            return (nowMinute - minute) + "分钟以前";
        }
        return showTime;
    }


}