package com.hjhq.teamface.customcomponent.widget2.input;

import android.view.View;
import android.widget.ImageView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

import java.text.DecimalFormat;

/**
 * 公式组件
 *
 * @author lx
 * @date 2017/8/23
 */

public class FormulaView extends InputCommonView {
    private String numberType;
    /**
     * 百分比 小数位
     */
    private int decimalLen;

    public FormulaView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            decimalLen = TextUtil.parseInt(field.getDecimalLen(), 0);
            numberType = field.getNumberType();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void setVisibility(int visibility) {
        //在新增和编辑状态中，不能改变其状态
        if (CustomConstants.ADD_STATE == state || CustomConstants.EDIT_STATE == state) {
            if (visibility == View.VISIBLE) {
                return;
            }
        }
        super.setVisibility(visibility);
    }

    @Override
    public void initOption() {
        ImageView ivLeft = mView.findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        if (CustomConstants.SENIOR_FORMULA.equals(type)) {
            setLeftImage(R.drawable.custom_icon_formula);
        } else {
            setLeftImage(R.drawable.custom_icon_function);
        }
        etInput.setClickable(false);
        etInput.setFocusable(false);
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    @Override
    public void setContent(String content) {
        if (content == null) {
            content = "";
        }

        if (!TextUtil.isEmpty(content) && decimalLen > 0) {
            if ("0".equals(numberType)) {
                String formatStr = "0.";
                for (int i = 0; i < decimalLen; i++) {
                    formatStr = formatStr + "0";
                }
                content = new DecimalFormat(formatStr).format(TextUtil.parseDouble(content));
            } else if ("2".equals(numberType)) {
                //百分比
                content += "%";
            }
        }
        //整形去除小数点
        if ("1".equals(numberType)) {
            content = TextUtil.doubleParseInt(content);
        }
        TextUtil.setText(etInput, content);
    }
}
