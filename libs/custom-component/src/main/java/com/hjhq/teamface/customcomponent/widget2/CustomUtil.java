package com.hjhq.teamface.customcomponent.widget2;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.HidenFieldBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.RegionUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.widget2.barcode.BarcodeView;
import com.hjhq.teamface.customcomponent.widget2.file.AttachmentView;
import com.hjhq.teamface.customcomponent.widget2.file.PictureView;
import com.hjhq.teamface.customcomponent.widget2.input.AutoNumberView;
import com.hjhq.teamface.customcomponent.widget2.input.EmailInputView;
import com.hjhq.teamface.customcomponent.widget2.input.FormulaView;
import com.hjhq.teamface.customcomponent.widget2.input.LocationInputView;
import com.hjhq.teamface.customcomponent.widget2.input.MultiTextInputView;
import com.hjhq.teamface.customcomponent.widget2.input.NumberInputView;
import com.hjhq.teamface.customcomponent.widget2.input.PhoneInputView;
import com.hjhq.teamface.customcomponent.widget2.input.TextInputView;
import com.hjhq.teamface.customcomponent.widget2.input.UrlInputView;
import com.hjhq.teamface.customcomponent.widget2.reference.ReferenceView;
import com.hjhq.teamface.customcomponent.widget2.select.DepartmentView;
import com.hjhq.teamface.customcomponent.widget2.select.LocationSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.MemberView;
import com.hjhq.teamface.customcomponent.widget2.select.MultiPickListSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.MultiSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.PickListView;
import com.hjhq.teamface.customcomponent.widget2.select.TimeSelectView;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;
import com.hjhq.teamface.customcomponent.widget2.subforms.CommonSubFormsView;
import com.hjhq.teamface.customcomponent.widget2.web.RichTextWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 布局帮助类
 *
 * @author lx
 * @date 2017/9/30
 */

public class CustomUtil {
    /**
     * 绘制布局
     *
     * @param llContent 父类控件
     * @param bean      数据
     * @param state     类型  0：新增 1：详情  2：编辑
     * @return 返回组件
     */
    public static BaseView drawLayout(LinearLayout llContent, CustomBean bean, int state) {
        if (bean == null) {
            return null;
        }
        CustomBean.FieldBean field = bean.getField();
        if (field != null && !"1".equals(field.getTerminalApp())) {
            //该组件app端不可用
            return null;
        }

        BaseView view = null;
        switch (bean.getType()) {
            case CustomConstants.IDENTIFIER:
                //自动编号
                view = new AutoNumberView(bean);
                break;
            case CustomConstants.TEXT:
                //单行文本
                view = new TextInputView(bean);
                break;
            case CustomConstants.PICKLIST:
                //下拉选项
                view = new PickListView(bean);
                break;
            case CustomConstants.MULTI:
                //复选框
                view = new MultiSelectView(bean);
                break;
            case CustomConstants.MUTLI_PICKLIST:
                //多级下拉
                view = new MultiPickListSelectView(bean);
                break;
            case CustomConstants.LOCATION:
                //定位
                view = new LocationInputView(bean);
                break;
            case CustomConstants.AREA:
                view = new LocationSelectView(bean);
                break;
            case CustomConstants.TEXTAREA:
                //多行文本
                view = new MultiTextInputView(bean);
                break;
            case CustomConstants.DATETIME:
                view = new TimeSelectView(bean);
                break;
            case CustomConstants.NUMBER:
                //数字
                view = new NumberInputView(bean);
                break;
            case CustomConstants.PHONE:
                //电话
                view = new PhoneInputView(bean);
                break;
            case CustomConstants.EMAIL:
                //邮箱
                view = new EmailInputView(bean);
                break;
            case CustomConstants.REFERENCE:
                //引用
                view = new ReferenceView(bean);
                Context context = llContent.getContext();
                if (llContent.getContext() instanceof ReferenceViewInterface) {
                    ((ReferenceView) view).setReferenceViewInterface((ReferenceViewInterface) context);
                }
                break;
            case CustomConstants.URL:
                //超链接
                view = new UrlInputView(bean);
                break;
            case CustomConstants.ATTACHMENT:
                //附件
                view = new AttachmentView(bean);
                break;
            case CustomConstants.PICTURE:
                //图片
                view = new PictureView(bean);
                break;
            case CustomConstants.FORMULA:
            case CustomConstants.SENIOR_FORMULA:
            case CustomConstants.FUNCTION_FORMULA:
                //公式
                view = new FormulaView(bean);
                break;
            case CustomConstants.PERSONNEL:
                //人员
                view = new MemberView(bean);
                break;
            case CustomConstants.SUBFORM:
                //子表单
                view = new CommonSubFormsView(bean);
                break;
            case CustomConstants.RICH_TEXT:
                view = new RichTextWebView(bean);
                //富文本
                break;
            case CustomConstants.DEPARTMENT:
                //部门
                view = new DepartmentView(bean);
                break;
            case CustomConstants.BARCODE:
                //部门
                view = new BarcodeView(bean);
                break;
            default:
                break;
        }

        if (view != null) {
            view.setState(state);
            view.addView(llContent, (Activity) llContent.getContext());
            view.setStateVisible();
        }
        return view;
    }


    /**
     * 获取布局组件的值
     *
     * @param mViewList
     * @param activity
     * @param json
     */
    public static boolean put(List<SubfieldView> mViewList, Activity activity, JSONObject json) {
        for (SubfieldView view : mViewList) {
            ArrayList viewList = view.getViewList();
            boolean put = put(activity, viewList, json);
            if (!put) {
                return put;
            }
        }
        return true;
    }

    /**
     * 获取布局组件的值
     *
     * @param activity
     * @param mViewList
     * @param json
     */
    public static boolean put(Activity activity, List<BaseView> mViewList, JSONObject json) {
        for (BaseView view : mViewList) {
            if (!view.getVisibility() && view.isHidenField()) {
                //被下拉组件隐藏的需要返还空字符串
                json.put(view.getKeyName(), "");
                continue;
            }
            boolean must = CustomConstants.FIELD_MUST.equals(view.getFieldControl());

            //新增时显示，不能是只读，需要必填 时为空 检测才不通过
            if (view.checkNull() && must) {
                ToastUtils.showError(activity, view.getBean().getLabel() + "不能为空");
                return false;
            } else if (view.formatCheck()) {
                view.put(json);
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取布局组件的值,不做校验
     *
     * @param mViewList
     * @param json
     */
    public static boolean putNoCheck(List<BaseView> mViewList, JSONObject json) {
        for (BaseView view : mViewList) {
            if (!view.getVisibility()) {
                if (view.isHidenField()) {
                    //被下拉组件隐藏的需要返还空字符串
                    json.put(view.getKeyName(), "");
                }
                continue;
            }
            view.put(json);
        }
        return true;
    }

    /**
     * 获取布局关联组件的值
     *
     * @param mViewList
     * @param json
     */
    public static boolean putReference(List<SubfieldView> mViewList, JSONObject json) {
        for (SubfieldView subfieldView : mViewList) {
            for (BaseView view : subfieldView.getViewList()) {
                if (view instanceof ReferenceView && view.getVisibility()) {
                    view.put(json);
                }
            }
        }
        return true;
    }

    /**
     * 设置显示关联列表的值
     *
     * @param textView 文本控件
     * @param rowBean  内容对象
     */
    public static void setReferenceTempValue(TextView textView, RowBean rowBean) {
        setTempValue(textView, rowBean, false);
    }

    /**
     * 设置显示详情头部信息
     *
     * @param textView 文本控件
     * @param rowBean  内容对象
     */
    public static void setDetailHeaderValue(TextView textView, RowBean rowBean) {
        try {
            setTempValue(textView, rowBean, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置显示列表的值
     *
     * @param textView 文本控件
     * @param rowBean  内容对象
     * @param isColor  是否显示解析颜色
     */
    public static void setTempValue(TextView textView, RowBean rowBean, boolean isColor) {
        if (rowBean == null) {
            return;
        }

        String name = rowBean.getName();
        String value = rowBean.getValue();
        if (TextUtil.isEmpty(value)) {
            textView.setText("");
            return;
        }

        //附件、子表、图片 不显示
        boolean isSubform = name.startsWith(CustomConstants.SUBFORM);
        boolean isPicture = name.startsWith(CustomConstants.PICTURE);
        boolean isAttachment = name.startsWith(CustomConstants.ATTACHMENT);
        if (isSubform || isPicture || isAttachment) {
            return;
        }

        StringBuilder text = new StringBuilder();
        String color = null;
        if (name.startsWith(CustomConstants.PERSONNEL) || name.startsWith(CustomConstants.REFERENCE) || name.startsWith(CustomConstants.DEPARTMENT)) {
            //人员、关联
            text.append(getJsonArrayValue(value, "name"));
        } else if (name.startsWith(CustomConstants.PICKLIST) || name.startsWith(CustomConstants.MULTI) || name.startsWith(CustomConstants.MUTLI_PICKLIST)) {
            //下拉
            text.append(getJsonArrayValue(value, "label"));
            if (isColor) {
                String[] split = getJsonArrayValue(value, "color").split(",");
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

        TextUtil.setText(textView, text.toString());
        boolean checkColor = ColorUtils.checkColor(color);
        //是否要显示颜色
        if (isColor) {
            //是否有颜色 且 不能是白色
            if (checkColor && !color.toUpperCase().startsWith("#FFFFFF")) {
                textView.setBackgroundColor(ColorUtils.hexToColor(color));
                textView.setTextColor(ColorUtils.hexToColor("#FFFFFF"));
            } else {
                textView.setBackgroundColor(ColorUtils.hexToColor("#FFFFFF"));
                textView.setTextColor(ColorUtils.hexToColor("#4a4a4a"));
            }
        }
    }

    /**
     * 解析 JSONArray的json字符串
     *
     * @param json  json字符串
     * @param field 解析字段
     * @return 字段的值
     */
    private static String getJsonArrayValue(String json, String field) {
        try {
            StringBuilder text = new StringBuilder();
            if (TextUtil.isEmpty(json) || TextUtil.isEmpty(field)) {
                return text.toString();
            }
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
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解析 JSONObject
     *
     * @param json  json字符串
     * @param field 解析字段
     * @return 字段的值
     */
    private static String getJsonObjectValue(String json, String field) {
        String text = "";
        try {
            if (TextUtil.isEmpty(json) || TextUtil.isEmpty(field)) {
                return text;
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject != null) {
                text = jsonObject.getString(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 接收下拉组件控制的隐藏组件通知
     *
     * @param tag
     * @param mViewList
     */
    public static void handleHidenFields(int hashCode, Object tag, List<SubfieldView> mViewList) {
        //隐藏组件时无需清除下拉控制组件的值
        RxManager.$(hashCode).on(tag, hides -> {
            List<HidenFieldBean> hidenFields = (List<HidenFieldBean>) hides;

            HidenFieldBean controlField = hidenFields.get(0);
            hidenFields.remove(0);
            //是否在下拉选择组件后面
            boolean after = false;
            for (SubfieldView subfieldView : mViewList) {
                for (BaseView baseView : subfieldView.getViewList()) {
                    //布局文件隐藏的不受影响
                    if (!baseView.getVisibility() && !baseView.isHidenField()) {
                        continue;
                    }
                    String keyName = baseView.getKeyName();
                    if (after) {
                        boolean isShow = true;
                        for (HidenFieldBean hidenField : hidenFields) {
                            if (keyName.equals(hidenField.getName())) {
                                isShow = false;
                                break;
                            }
                        }
                        baseView.setHidenFieldVisible(isShow);
                    }
                    //控制当前下拉组件以下的 组件
                    if (keyName.equals(controlField.getName())) {
                        if (!baseView.getVisibility() && baseView.isHidenField()) {
                            //已被下拉隐藏的组件不能在 隐藏其他组件
                            return;
                        }
                        after = true;
                    }
                }
            }
        });
        //隐藏组件时需要清除下拉控制组件的值
        RxManager.$(hashCode).on(CustomConstants.CLEAR_FIELD_VALUE_TAG + tag, hides -> {
            List<HidenFieldBean> hidenFields = (List<HidenFieldBean>) hides;

            HidenFieldBean controlField = hidenFields.get(0);
            hidenFields.remove(0);
            //是否在下拉选择组件后面
            boolean after = false;
            for (SubfieldView subfieldView : mViewList) {
                for (BaseView baseView : subfieldView.getViewList()) {
                    //布局文件隐藏的不受影响
                    if (!baseView.getVisibility() && !baseView.isHidenField()) {
                        continue;
                    }
                    String keyName = baseView.getKeyName();
                    if (after) {
                        boolean isShow = true;
                        for (HidenFieldBean hidenField : hidenFields) {
                            if (keyName.equals(hidenField.getName())) {
                                isShow = false;
                                break;
                            }
                        }
                        baseView.setHidenFieldVisible(isShow);
                        //清除下拉控制组件的值
                        if (baseView instanceof PickListView) {
                            PickListView pickListView = (PickListView) baseView;
                            //单选并且有值
                            if (!pickListView.isMulti() && !CollectionUtils.isEmpty(pickListView.getCheckEntrys())) {
                                //有下拉控制的需要清除值
                                if (!CollectionUtils.isEmpty(pickListView.getCheckEntrys().get(0).getHidenFields())) {
                                    pickListView.clear();
                                }
                            }
                        }
                    }
                    //控制当前下拉组件以下的 组件
                    if (keyName.equals(controlField.getName())) {
                        if (!baseView.getVisibility() && baseView.isHidenField()) {
                            //已被下拉隐藏的组件不能在 隐藏其他组件
                            return;
                        }
                        after = true;
                    }
                }
            }
        });

        //清除下拉组件隐藏字段控制
        RxManager.$(hashCode).on(CustomConstants.CLEAR_FIELD_HIDE_TAG + tag, hides -> {
            List<HidenFieldBean> hidenFields = (List<HidenFieldBean>) hides;

            List<BaseView> baseViewList = new ArrayList<>();
            for (SubfieldView subfieldView : mViewList) {
                baseViewList.addAll(subfieldView.getViewList());
            }
            for (HidenFieldBean hidenField : hidenFields) {
                for (BaseView baseView : baseViewList) {
                    //布局文件隐藏的不受影响
                    if (!baseView.getVisibility() && !baseView.isHidenField()) {
                        continue;
                    }
                    if (baseView.getKeyName().equals(hidenField.getName())) {
                        baseView.setHidenFieldVisible(true);
                        break;
                    }
                }
            }

        });
    }

    /**
     * 得到所有email地址
     *
     * @param detailMap
     * @return
     */
    public static String getEmail(Map<String, Object> detailMap) {
        StringBuilder sb = new StringBuilder();
        for (String next : detailMap.keySet()) {
            if (next.startsWith(CustomConstants.EMAIL)) {
                String email = detailMap.get(next) + "";
                sb.append(email).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } else {
            return null;
        }
    }


}
