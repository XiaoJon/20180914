package com.hjhq.teamface.customcomponent.widget2.base;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;


/**
 * @author lx
 * @date 2017/6/20
 * 选择类型通用控件
 */

public abstract class SelectCommonView extends BaseView {

    protected TextView tvTitle;
    protected LinearLayout llRoot;
    protected TextView tvContent;
    protected ImageView ivRight;
    private View llSelect;

    public SelectCommonView(CustomBean bean) {
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

        llSelect = mView.findViewById(R.id.ll_select);
        tvTitle = mView.findViewById(R.id.tv_title);
        llRoot = mView.findViewById(R.id.ll_content);
        tvContent = mView.findViewById(R.id.tv_content);
        ivRight = mView.findViewById(R.id.iv_right);

        initView();
        initOption();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        if (isDetailState()) {
            pointOut ="";
            ivRight.setVisibility(View.GONE);
            llRoot.setClickable(false);
            llSelect.setBackgroundResource(R.drawable.custom_widget_detail_border_bg);
        } else {
            if (state == CustomConstants.ADD_STATE) {
                setDefaultValue();
            }
            if (!CustomConstants.FIELD_READ.equals(fieldControl)) {
                llRoot.setOnClickListener(view -> onClick());
            } else {
                llRoot.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
            }
        }
        TextUtil.setHint(tvContent, pointOut);
        setTitle(tvTitle, title);

        Object value = bean.getValue();
        if (value != null && !"".equals(value)) {
            setData(value);
        }
    }

    /**
     * 初始化操作
     */
    public abstract void initOption();

    /**
     * 点击选择
     */
    public void onClick() {
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
    }

    /**
     * 获取提交的值
     *
     * @return
     */
    protected abstract Object getValue();


    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, getValue());
    }

    /**
     * 检测是否为空
     *
     * @return false数值异常  true正常
     */
    @Override
    public boolean checkNull() {
        return "".equals(getValue());
    }

    @Override
    protected abstract void setData(Object value);


    /**
     * 设置默认值
     */
    public void setDefaultValue() {
        TextUtil.setText(tvContent, defaultValue);
    }


}
