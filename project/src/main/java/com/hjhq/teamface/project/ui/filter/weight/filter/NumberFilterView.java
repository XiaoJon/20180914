package com.hjhq.teamface.project.ui.filter.weight.filter;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ConditionBean;
import com.hjhq.teamface.project.ui.filter.weight.BaseFilterView;


/**
 * 筛选条件-数字
 */

public class NumberFilterView extends BaseFilterView {
    private LinearLayout radioGroup;
    private CheckBox radioBtnHave;
    private CheckBox radioBtnNull;
    private EditText minEt;
    private EditText maxEt;
    private RelativeLayout actionRl;
    private LinearLayout actionLl;
    private TextView tvTitle;
    //初始化组件用到的数据
    private String itemTitle;
    //选中的CheckBox的id
    private int itemId = 0;


    private boolean flag = false;
    private float minValue;
    private float maxValue;
    private View ivArraw;

    public NumberFilterView(ConditionBean bean) {
        super(bean);
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {

        mView = View.inflate(activity, R.layout.crm_item_goods_filter_by_number, null);
        radioGroup = (LinearLayout) mView.findViewById(R.id.radio_group);
        radioBtnHave = (CheckBox) mView.findViewById(R.id.check_input);
        radioBtnNull = (CheckBox) mView.findViewById(R.id.check_null);
        minEt = (EditText) mView.findViewById(R.id.et_min);
        maxEt = (EditText) mView.findViewById(R.id.et_max);

        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        actionRl = (RelativeLayout) mView.findViewById(R.id.rl_title_number);
        actionLl = (LinearLayout) mView.findViewById(R.id.ll_action_number);
        ivArraw = mView.findViewById(R.id.iv);
        if (!flag) {
            actionLl.setVisibility(View.GONE);
        }
        setClickListener();
        initData();
        initView();

        parent.addView(mView, index);


    }

    private void initView() {
        TextUtil.setText(tvTitle, title);
    }

    private void initData() {

    }

    private void setClickListener() {
        actionRl.setOnClickListener(v -> {
            SoftKeyboardUtils.hide(v);
            if (!flag) {
                actionLl.setVisibility(View.VISIBLE);
                flag = !flag;
                rotateCButton(mActivity, ivArraw, 0f, 180f, 500,
                        R.drawable.icon_sort_down);
            } else {
                actionLl.setVisibility(View.GONE);
                flag = !flag;
                rotateCButton(mActivity, ivArraw, 0f, -180f, 500,
                        R.drawable.icon_sort_down);
            }
        });
        minEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                radioBtnNull.setChecked(false);
                radioBtnHave.setChecked(false);
            } else {
                SoftKeyboardUtils.hide(radioGroup);
            }
        });
        maxEt.setOnFocusChangeListener((v, b) -> {
            if (b) {
                radioBtnNull.setChecked(false);
                radioBtnHave.setChecked(false);
            } else {
                SoftKeyboardUtils.hide(radioGroup);
            }
        });
        radioBtnNull.setOnClickListener(v -> {
            LogUtil.e("radioBtnNull" + radioBtnNull.isChecked() + "");

            radioBtnHave.setChecked(false);
            minEt.setText("");
            minEt.clearFocus();
            maxEt.setText("");
            maxEt.clearFocus();
            SoftKeyboardUtils.hide(radioBtnHave);
        });
        radioBtnHave.setOnClickListener(v -> {
            LogUtil.e("radioBtnHave" + radioBtnHave.isChecked() + "");

            radioBtnNull.setChecked(false);
            minEt.setText("");
            minEt.clearFocus();
            maxEt.setText("");
            maxEt.clearFocus();

            SoftKeyboardUtils.hide(radioBtnHave);
        });

    }


    public boolean formatCheck() {
        return true;
    }

    @Override
    public boolean put(JSONObject json) throws Exception {
        JSONObject data = new JSONObject();

        if (radioBtnHave.isChecked()) {
            data.put("component", 1);
        } else if (radioBtnNull.isChecked()) {
            data.put("component", 0);
        } else {
            if (!TextUtil.isEmpty(maxEt.getText().toString())) {
                maxValue = Float.parseFloat(maxEt.getText().toString());
                data.put("maxValue", maxValue);
                json.put(keyName, data);

            }
            if (!TextUtil.isEmpty(minEt.getText().toString())) {
                minValue = Float.parseFloat(minEt.getText().toString());
                data.put("minValue", minValue);
                json.put(keyName, data);
            }
        }
        return true;
    }

    @Override
    public void reset() {
        minEt.setText("");
        minEt.clearFocus();
        maxEt.setText("");
        maxEt.clearFocus();
        radioBtnNull.setChecked(false);
        radioBtnHave.setChecked(false);
    }

}
