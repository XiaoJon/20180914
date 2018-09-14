package com.hjhq.teamface.customcomponent.widget2.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.InputFocusInterface;


/**
 * @author xj
 * @date 2017/6/20
 * 输入类型通用控件
 */

public abstract class InputCommonView extends BaseView implements InputFocusInterface {
    protected TextView tvTitle;
    protected LinearLayout llRoot;
    protected EditText etInput;
    private View llInput;
    protected ImageView ivLeft;
    protected ImageView ivRight;

    public InputCommonView(CustomBean bean) {
        super(bean);
    }

    /**
     * 获取布局
     */
    public abstract int getLayout();

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        mView = View.inflate(activity, getLayout(), null);
        parent.addView(mView);

        tvTitle = mView.findViewById(R.id.tv_title);
        llRoot = mView.findViewById(R.id.ll_content);

        llInput = mView.findViewById(R.id.ll_input);
        etInput = mView.findViewById(R.id.et_input);
        ivLeft = mView.findViewById(R.id.iv_left);
        ivRight = mView.findViewById(R.id.iv_right);

        initView();
        initOption();
        RxManager.$(aHashCode).on(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, o -> {
            clearFocus();
        });
    }

    /**
     * 初始化控件数据
     */
    private void initView() {
        if (isDetailState()) {
            pointOut ="";
            etInput.setFocusable(false);
            llInput.setBackgroundResource(R.drawable.custom_widget_detail_border_bg);
        } else {
            if (state == CustomConstants.ADD_STATE) {
                setContent(defaultValue);
            }
            if (CustomConstants.FIELD_READ.equals(fieldControl)) {
                etInput.setFocusable(false);
                etInput.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
            } else {
                etInput.setOnFocusChangeListener((v, hasFocus) -> {
                    LayerDrawable layerDrawable = (LayerDrawable) llInput.getBackground();
                    Drawable drawable = layerDrawable.getDrawable(0);
                    if (drawable instanceof GradientDrawable) {
                        ((GradientDrawable) drawable).setColor(ColorUtils.hexToColor(hasFocus ? "#008FE5" : "#D5D5D5"));
                    }
                    if (!hasFocus && !checkNull() && isLinkage) {
                        linkageData();
                    }
                });
                etInput.setOnClickListener(v -> {
                    etInput.setFocusable(true);//设置输入框可聚集
                    etInput.setFocusableInTouchMode(true);//设置触摸聚焦
                    etInput.requestFocus();//请求焦点
                    etInput.findFocus();//获取焦点
                });
            }
        }
        TextUtil.setHint(etInput, pointOut);
        setTitle(tvTitle, title);


        Object value = bean.getValue();
        setData(value);
    }


    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        TextUtil.setText(etInput, content);
    }

    @Override
    protected void setData(Object value) {
        if (value == null) {
            return;
        }
        String content = value.toString();
        setContent(content);
    }


    /**
     * 初始化操作
     */
    public abstract void initOption();

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, getValue());
    }


    /**
     * 检测是否为空
     *
     * @return 必填时是否为空
     */
    @Override
    public boolean checkNull() {
        return "".equals(getValue());
    }

    public Object getValue() {
        return getContent();
    }

    public String getContent() {
        if (etInput == null) {
            return "";
        }
        return etInput.getText().toString().trim();
    }

    /**
     * 设置查重图标
     */
    protected void setRepeatCheckIcon() {
        ImageView ivRight = mView.findViewById(R.id.iv_right);
        if (bean.getField() != null) {
            String repeatCheck = bean.getField().getRepeatCheck();
            boolean hasIcon = "1".equals(repeatCheck) || "2".equals(repeatCheck);
            if (hasIcon && !isDetailState()) {
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setOnClickListener(view -> repeatCheck());
            }
        }
    }

    /**
     * 查重
     */
    private void repeatCheck() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, bean.getModuleBean());
        bundle.putString(Constants.DATA_TAG1, getContent());
        bundle.putString(Constants.DATA_TAG2, keyName);
        bundle.putString(Constants.DATA_TAG3, title);
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_REPEAT_CHECK_CODE, bundle);
    }

    protected void setLeftImage(int res) {
        if (ivLeft == null) {
            return;
        }
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(res);
    }

    protected void setRightImage(int res) {
        if (ivRight == null) {
            return;
        }
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(res);
    }

    public void clearFocus() {
        if (etInput != null) {
            etInput.setFocusable(false);
        }
    }
}
