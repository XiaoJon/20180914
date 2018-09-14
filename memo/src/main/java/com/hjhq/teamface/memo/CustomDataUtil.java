package com.hjhq.teamface.memo;

import android.text.Html;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.RowDataBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.RegionUtil;
import com.hjhq.teamface.basis.util.TextUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 * Describe：
 */

public class CustomDataUtil {
    private static String getJsonArrayValue(String rawValue, String json, String field) {
        StringBuilder text = new StringBuilder();
        if (TextUtil.isEmpty(json) || TextUtil.isEmpty(field)) {
            return text.toString();
        }
        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            if (!CollectionUtils.isEmpty(jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    text.append(jsonObject.getString(field) + ",");
                }
                if (!TextUtil.isEmpty(text)) {
                    text.deleteCharAt(text.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return rawValue;
        }
        return text.toString();
    }

    private static String getJsonObjectValue(String json, String field) {
        String text = "";
        if (TextUtil.isEmpty(json) || TextUtil.isEmpty(field)) {
            return text;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject != null) {
            text = jsonObject.getString(field);
        }
        return text;
    }

    /**
     * 获取数据
     *
     * @param rowBean
     * @return
     */
    public static String getDataValue(RowDataBean rowBean) {
        boolean isColor = false;
        if (rowBean == null) {
            return "";
        }
        String name = rowBean.getName();
        String value = JSONObject.toJSONString(rowBean.getValue());
        String rawValue = rowBean.getValue().toString();

        if (TextUtil.isEmpty(value)) {

            return "";
        }

        //附件、子表、图片 不显示
        boolean isSubform = name.startsWith(CustomConstants.SUBFORM);
        boolean isPicture = name.startsWith(CustomConstants.PICTURE);
        boolean isAttachment = name.startsWith(CustomConstants.ATTACHMENT);
        if (isSubform || isPicture || isAttachment) {
            return "";
        }

        StringBuilder text = new StringBuilder();
        String color = null;
        if (name.startsWith(CustomConstants.PERSONNEL) || name.startsWith(CustomConstants.REFERENCE) || name.startsWith(CustomConstants.DEPARTMENT)) {
            //人员、关联
            text.append(getJsonArrayValue(rawValue, value, "name"));
        } else if (name.startsWith(CustomConstants.PICKLIST) || name.startsWith(CustomConstants.MULTI) || name.startsWith(CustomConstants.MUTLI_PICKLIST)) {
            //下拉
            text.append(getJsonArrayValue(rawValue, value, "label"));
            if (isColor) {
                String[] split = getJsonArrayValue(rawValue, value, "color").split(",");
                if (split.length > 0) {
                    color = split[0];
                }
            }
        } else if (name.startsWith(CustomConstants.DATETIME)) {
            //时间
            String format = rowBean.getOther();
            if (TextUtil.isEmpty(format)) {
                format = "yyyy-MM-dd";
            }
            long time = TextUtil.parseLong(value);
            String s = DateTimeUtil.longToStr(time, format);
            text.append(s);
        } else if (name.startsWith(CustomConstants.AREA)) {
            //省市区
            text.append(RegionUtil.code2String(value));
        } else if (name.startsWith(CustomConstants.LOCATION)) {
            //定位
            text.append(getJsonObjectValue(value, "value"));
        } else {
            text.append(value);
        }
        return text.toString();
    }

    public static List<Member> getMmeberValue(RowDataBean rowBean) {
        List<Member> list = new ArrayList<>();
        Object value = rowBean.getValue();
        String jsonString = JSONObject.toJSONString(value);
        if (rowBean.getValue() instanceof List) {
            try {
                List<Member> memberList = (List<Member>) rowBean.getValue();
                if (memberList != null && memberList.size() > 0) {
                    list.addAll(memberList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public static String getTextValue(RowDataBean rowBean) {
        String name = rowBean.getName();
        StringBuilder sb = new StringBuilder();
        String label = rowBean.getLabel();
        String jsonString = JSONObject.toJSONString(rowBean.getValue());
        org.json.JSONObject jo = null;
        org.json.JSONArray ja = null;
        if (!TextUtils.isEmpty(name)) {
            name = name.substring(0, name.indexOf("_"));
            switch (name) {
                case CustomConstants.IDENTIFIER:
                case CustomConstants.TEXT:
                case CustomConstants.TEXTAREA:
                case CustomConstants.AREA:
                case CustomConstants.NUMBER:
                case CustomConstants.PHONE:
                case CustomConstants.EMAIL:
                case CustomConstants.MAIL_BOX_SCOPE:
                case CustomConstants.URL:
                case CustomConstants.DEPARTMENT:
                    sb.append(label + ":" + rowBean.getValue());
                    break;
                case CustomConstants.PERSONNEL:
                    sb.append(label + ":");
                    try {
                        ja = new org.json.JSONArray(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ja != null) {
                        for (int i = 0; i < ja.length(); i++) {
                            sb.append(ja.optJSONObject(i).optString("name") + ",");
                        }
                        if (sb.toString().endsWith(",")) {
                            sb = new StringBuilder(sb.toString().substring(0, sb.length() - 1));
                        }
                    }
                    break;
                case MsgConstant.TYPE_PICKLIST:
                    sb.append("[下拉列表]");
                    break;
                case MsgConstant.TYPE_DATETIME:
                    sb.append(label + ":" + DateTimeUtil.longToStr(TextUtil.parseLong(rowBean.getValue() + ""), "yyyy-MM-dd H:mm"));
                    break;
                case CustomConstants.RICH_TEXT:
                    //富文本
                    sb.append(label + ":" + Html.fromHtml(rowBean.getValue() + ""));
                    break;
                case CustomConstants.LOCATION:
                    // TODO: 2018/6/22 位置信息解析
                    sb.append("[位置]");
                    break;
                case CustomConstants.REFERENCE:
                    sb.append("[引用]");
                    break;
                case CustomConstants.PICTURE:
                    sb.append("[图片]");
                    break;
                case CustomConstants.FORMULA:
                    sb.append("[公式]");
                    break;
                case CustomConstants.FUNCTION_FORMULA:
                    sb.append("[函数公式]");
                    break;
                case CustomConstants.SENIOR_FORMULA:
                    sb.append("[高级公式]");
                    break;
                case CustomConstants.SUBFORM:
                    sb.append("[子表单]");
                    break;
                default:
                    sb.append(label + ":" + jo.optString("value"));
                    break;
            }
            return sb.toString();
        } else {
            return sb.toString();
        }
    }

    /**
     * 获取人员类型数据
     *
     * @param rowBean
     * @return
     */
    public static List<Member> getMemberValue(RowDataBean rowBean) {
        String name = rowBean.getName();
        StringBuilder sb = new StringBuilder();
        String label = rowBean.getLabel();
        String jsonString = JSONObject.toJSONString(rowBean.getValue());
        org.json.JSONArray ja = null;
        List<Member> list = new ArrayList<>();

        if (!TextUtils.isEmpty(name) && name.startsWith(CustomConstants.PERSONNEL)) {
            try {
                ja = new org.json.JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ja != null) {
                for (int i = 0; i < ja.length(); i++) {
                    try {
                        final org.json.JSONObject jsonObject = ja.getJSONObject(i);
                        Member m = new Member();
                        m.setName(jsonObject.optString("name"));
                        m.setPost_name(jsonObject.optString("post_name"));
                        m.setId(jsonObject.optLong("id"));
                        m.setPicture(jsonObject.optString("picture"));
                        list.add(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }
}
