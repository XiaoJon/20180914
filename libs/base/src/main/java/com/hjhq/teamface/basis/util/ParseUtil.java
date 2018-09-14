package com.hjhq.teamface.basis.util;


import android.text.TextUtils;

import com.hjhq.teamface.basis.bean.FieldInfoBean;
import com.hjhq.teamface.basis.bean.SortToken;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2017/11/23
 */

public class ParseUtil {
    /**
     * 中文字符串匹配
     */
    public static final String chReg = "[\\u4E00-\\u9FA5]+";

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public static SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }

    /**
     * 解析sort_key,封装简拼,全拼。
     * Android 5.0 以上使用
     *
     * @param sortKey
     * @return
     */
    public static SortToken parseSortKeyLollipop(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            boolean isChinese = sortKey.matches(chReg);
            // 分割条件：中文不分割，英文以大写和空格分割
            String regularExpression = isChinese ? "" : "(?=[A-Z])|\\s";

            String[] enStrs = sortKey.split(regularExpression);

            for (int i = 0, length = enStrs.length; i < length; i++)
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += getSortLetter(String.valueOf(enStrs[i].charAt(0)));
                    token.wholeSpell += CharacterParser.getInstance().getSelling(enStrs[i]);
                }
        }
        return token;
    }


    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    public static String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        //汉字转换成拼音
        String pinyin = CharacterParser.getInstance().getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    public static String getSortLetterBySortKey(String sortKey) {
        String letter = "#";
        if (sortKey == null || "".equals(sortKey.trim())) {
            return letter;
        }
        //汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        } else
//			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// 5.0以上需要判断汉字
            if (sortString.matches("^[\u4E00-\u9FFF]+$"))// 正则表达式，判断是否为汉字
                letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
//		}
        return letter;
    }

    /**
     * 从自定义的数据获取格式化的字符串
     *
     * @param list
     * @return
     */
    public static String getStringValue(List<FieldInfoBean> list) {
        if (list == null || list.size() <= 0) {
            return "";
        }
        String field_label = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            FieldInfoBean item = list.get(i);
            switch (item.getType()) {
                case CustomConstants.IDENTIFIER:
                case CustomConstants.TEXT:
                case CustomConstants.TEXTAREA:
                case CustomConstants.AREA:
                case CustomConstants.NUMBER:
                case CustomConstants.PHONE:
                case CustomConstants.EMAIL:
                case CustomConstants.MAIL_BOX_SCOPE:
                case CustomConstants.URL:
                case CustomConstants.PERSONNEL:
                case CustomConstants.DEPARTMENT:
                    if (list.size() == 1 || i == list.size() - 1) {
                        sb.append(item.getField_label() + ":" + item.getField_value());
                    } else {
                        sb.append(item.getField_label() + ":" + item.getField_value());
                        sb.append("\n");
                    }
                    break;
                case MsgConstant.TYPE_PICKLIST:
                    String field_value = item.getField_value();
                    String filedValue = "";
                    try {
                        JSONArray ja = new JSONArray(field_value);
                        org.json.JSONObject jb = ((org.json.JSONObject) ja.get(0));
                        filedValue = jb.optString("label");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (list.size() == 1 || i == list.size() - 1) {
                        sb.append(item.getField_label() + ":" + filedValue);
                    } else {
                        sb.append(item.getField_label() + ":" + filedValue);
                        sb.append("\n");
                    }
                    break;
                case MsgConstant.TYPE_DATETIME:
                    String value = item.getField_value();
                    long time = TextUtil.parseLong(value);
                    if (time > 0) {
                        String datetime = DateTimeUtil.longToStr(time, "yyyy-MM-dd HH:mm");
                        if (list.size() == 1 || i == list.size() - 1) {
                            sb.append(item.getField_label() + ":" + datetime);
                        } else {
                            sb.append(item.getField_label() + ":" + datetime);
                            sb.append("\n");
                        }
                    }
                    break;
                case CustomConstants.RICH_TEXT:
                    //富文本
                    sb.append("内容:[富文本]");
                    break;
                case CustomConstants.LOCATION:
                    try {
                        String locationValue = item.getField_value();
                        JSONObject jb = new JSONObject(locationValue);
                        if (list.size() == 1 || i == list.size() - 1) {
                            sb.append(item.getField_label() + ":" + jb.opt("value"));
                        } else {
                            sb.append(item.getField_label() + ":" + jb.opt("value"));
                            sb.append("\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case CustomConstants.REFERENCE:
                    String field_value2 = item.getField_value();
                    StringBuilder filedValue2 = new StringBuilder();
                    try {
                        JSONArray ja = new JSONArray(field_value2);
                        for (int k = 0; k < ja.length(); k++) {
                            org.json.JSONObject jb = ((org.json.JSONObject) ja.get(k));
                            if (!TextUtils.isEmpty(filedValue2)) {
                                filedValue2.append("," + jb.optString("name"));
                            } else {
                                filedValue2.append(jb.optString("name"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (list.size() == 1 || i == list.size() - 1) {
                        sb.append(item.getField_label() + ":" + filedValue2.toString());
                    } else {
                        sb.append(item.getField_label() + ":" + filedValue2.toString());
                        sb.append("\n");
                    }
                    break;
                case CustomConstants.PICTURE:
                    sb.append("内容:[图片]");
                    break;
                case CustomConstants.FORMULA:
                    sb.append("内容:[公式]");
                    break;
                case CustomConstants.FUNCTION_FORMULA:
                    sb.append("内容:[函数公式]");
                    break;
                case CustomConstants.SENIOR_FORMULA:
                    sb.append("内容:[高级公式]");
                    break;
                case CustomConstants.SUBFORM:
                    sb.append("内容:[子表单]");
                    break;
                default:
                    if (list.size() == 1 || i == list.size() - 1) {
                        sb.append(item.getField_label() + ":" + item.getField_value());
                    } else {
                        sb.append(item.getField_label() + ":" + item.getField_value());
                        sb.append("\n");
                    }
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 将字节数组转为long
     *
     * @param b
     * @return
     */
    public static long bytes2Long(byte[] b) {
        long s = 0;
        try {
            for (int i = 0; i < 7; ++i) {
                if (b[7 - i] >= 0) {
                    s = s + b[7 - i];
                } else {
                    s = s + 256 + b[7 - i];
                }

                s = s * 256;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }
}
