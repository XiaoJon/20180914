package com.hjhq.teamface.customcomponent.widget2.input;

import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数值组件
 *
 * @author lx
 * @date 2017/8/23
 */
public class NumberInputView extends InputCommonView {
    private int numberType;//数字类型：0数字、1整数、2百分比
    private int betweenMin;//范围最小值
    private int betweenMax;//范围最大值
    private int numberLenth;//小数位  位数(1 2 3 4)
    private Pattern mPattern;

    public NumberInputView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            numberType = TextUtil.parseInt(field.getNumberType());
            betweenMin = TextUtil.parseInt(field.getBetweenMin(), Integer.MIN_VALUE);
            betweenMax = TextUtil.parseInt(field.getBetweenMax(), Integer.MAX_VALUE);
            numberLenth = TextUtil.parseInt(field.getNumberLenth());
            if (numberLenth < 1 || numberLenth > 4) {
                numberLenth = 2;
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        setLeftImage(R.drawable.custom_icon_number);
        if (1 == numberType) {//整数
            mPattern = Pattern.compile("([0-9])*");
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(1);
            if (numberLenth != 1) {
                sb.append("," + numberLenth);
            }
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mPattern = Pattern.compile("(([0-9]*)|([0-9]*\\.)|([0-9]*\\.\\d{" + sb.toString() + "}))");
            if (2 == numberType) {
                ImageView ivRight = mView.findViewById(R.id.iv_right);
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setImageResource(R.drawable.custom_icon_percent);
            }
        }
        InputFilter[] filter = {(source, start, end, dest, dstart, dend) -> {
            String sourceText = source.toString();
            //验证删除等按键
            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }
            String text = dest.subSequence(0, dstart) + sourceText + dest.subSequence(dend, dest.length());
            Matcher matcher = mPattern.matcher(text);
            if (!matcher.matches()) {
                return "";
            }

            return source;
        }};
        etInput.setFilters(filter);
    }

    @Override
    public Object getValue() {
        String content = etInput.getText().toString().trim();
        int length = content.length();
        if (length == 0) {
            return "";
        }
        if (1 != numberType) {
            //小数或百分比
            int pointIndex = content.indexOf(".");
            int size;
            if (pointIndex == -1) {
                content += ".";
                size = numberLenth;
            } else {
                size = numberLenth - (length - pointIndex - 1);
            }
            for (int j = 0; j < size; j++) {
                content += "0";
            }
        }
        return content;
    }

    @Override
    public boolean formatCheck() {
        String content = getContent();
        if (TextUtil.isEmpty(content)) {
            return true;
        }
        double number = TextUtil.parseDouble(content);
        if ((betweenMin != Integer.MIN_VALUE && betweenMax != Integer.MAX_VALUE) && (number > betweenMax || number < betweenMin)) {
            ToastUtils.showError(getContext(), title + "输入范围" + betweenMin + "-" + betweenMax);
            return false;
        }
        boolean matches = mPattern.matcher(content).matches();
        if (!matches) {
            ToastUtils.showError(getContext(), title + "格式错误");
        }
        return matches;
    }
}
