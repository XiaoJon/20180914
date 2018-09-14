package com.hjhq.teamface.project.widget.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.RegionUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.ReferenceViewInterface;
import com.hjhq.teamface.customcomponent.widget2.input.AutoNumberView;
import com.hjhq.teamface.customcomponent.widget2.input.EmailInputView;
import com.hjhq.teamface.customcomponent.widget2.input.FormulaView;
import com.hjhq.teamface.customcomponent.widget2.input.LocationInputView;
import com.hjhq.teamface.customcomponent.widget2.input.NumberInputView;
import com.hjhq.teamface.customcomponent.widget2.input.PhoneInputView;
import com.hjhq.teamface.customcomponent.widget2.input.TextInputView;
import com.hjhq.teamface.customcomponent.widget2.input.UrlInputView;
import com.hjhq.teamface.customcomponent.widget2.reference.ReferenceView;
import com.hjhq.teamface.customcomponent.widget2.select.LocationSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.MemberView;
import com.hjhq.teamface.customcomponent.widget2.select.MultiPickListSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.MultiSelectView;
import com.hjhq.teamface.customcomponent.widget2.select.TimeSelectView;
import com.hjhq.teamface.customcomponent.widget2.subforms.CommonSubFormsView;
import com.hjhq.teamface.customcomponent.widget2.web.RichTextWebView;
import com.hjhq.teamface.project.widget.file.TaskAttachmentView;
import com.hjhq.teamface.project.widget.file.TaskPictureView;
import com.hjhq.teamface.project.widget.reference.PersonalTaskReferenceView;
import com.hjhq.teamface.project.widget.select.PickListTagView;

import java.util.List;

/**
 * 项目自定义
 * Created by Administrator on 2018/7/9.
 */

public class ProjectCustomUtil {

    /**
     * 绘制布局
     *
     * @param llContent 父类控件
     * @param bean      数据
     * @param state     类型  0：新增 1：详情  2：编辑
     * @return 返回组件
     */
    public static BaseView drawLayout(LinearLayout llContent, CustomBean bean, int state, boolean personalTask) {
        if (bean == null) {
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
                view = new PickListTagView(bean);
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
                view = new TextInputView(bean);
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
                if (personalTask && ProjectConstants.PROJECT_TASK_RELATION.equals(bean.getName())) {
                    view = new PersonalTaskReferenceView(bean);
                } else {
                    view = new ReferenceView(bean);
                }
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
                view = new TaskAttachmentView(bean);
                break;
            case CustomConstants.PICTURE:
                //图片
                view = new TaskPictureView(bean);
                break;
            case CustomConstants.FORMULA:
            case CustomConstants.FUNCTION_FORMULA:
            case CustomConstants.SENIOR_FORMULA:
                //公式
                view = new FormulaView(bean);
                break;
            case CustomConstants.PERSONNEL:
                //人员
                view = new MemberView(bean);
                break;
            case CustomConstants.DEPARTMENT:
                //部门
                view = new MemberView(bean);
                break;
            case CustomConstants.RICH_TEXT:
                //富文本
                view = new RichTextWebView(bean);
                break;
            case CustomConstants.SUBFORM:
                //子表单
                view = new CommonSubFormsView(bean);
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
     * @param activity
     * @param mViewList
     * @param json
     */
    public static boolean put(Activity activity, List<BaseView> mViewList, JSONObject json) {
        for (BaseView view : mViewList) {
            if (!view.getVisibility()) {
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
     * 设置显示关联列表的值
     *
     * @param textView 文本控件
     * @param rowBean  内容对象
     */
    public static void setReferenceTempValue(TextView textView, RowBean rowBean) {
        setTempValue(textView, rowBean, false);
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
        }
        return text.toString();
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
        if (TextUtil.isEmpty(json) || TextUtil.isEmpty(field)) {
            return text;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject != null) {
            text = jsonObject.getString(field);
        }
        return text;
    }
}
