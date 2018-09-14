package com.hjhq.teamface.project.ui.filter.weight.filter;

import android.app.Activity;
import android.text.InputType;
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
 * 筛选条件-关键字
 */

public class KeywordFilterView extends BaseFilterView {
    private LinearLayout radioGroup;
    private CheckBox radioBtnHave;
    private CheckBox radioBtnNull;
    private EditText mEtKeyword;
    private RelativeLayout actionRl;
    private LinearLayout actionLl;
    private TextView tvTitle;
    private boolean flag = false;
    private String keyword;
    private int itemId = 0;
    //初始化组件用到的数据
    private String itemTitle;
    private String itemType;
    private View ivArraw;

    public KeywordFilterView(ConditionBean bean) {
        super(bean);
        itemType = bean.getType();
    }


    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {

        mView = View.inflate(activity, R.layout.crm_item_goods_filter_by_text, null);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);

        radioGroup = (LinearLayout) mView.findViewById(R.id.radio_group);
        radioBtnHave = (CheckBox) mView.findViewById(R.id.check_input);
        radioBtnNull = (CheckBox) mView.findViewById(R.id.check_null);
        mEtKeyword = (EditText) mView.findViewById(R.id.et_input);
        actionRl = (RelativeLayout) mView.findViewById(R.id.rl_title_text);
        actionLl = (LinearLayout) mView.findViewById(R.id.ll_action_text);
        ivArraw = mView.findViewById(R.id.iv);
        if (!flag) {
            actionLl.setVisibility(View.GONE);
        }
        initView();
        setClickListener();
        parent.addView(mView, index);

    }

    private void initView() {
        TextUtil.setText(tvTitle, title);
        switch (itemType) {
            case "phone":
                mEtKeyword.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "number":
                mEtKeyword.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "url":
                mEtKeyword.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "email":
                mEtKeyword.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT
                        | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                break;
            default:
                break;
        }
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


        mEtKeyword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                radioBtnNull.setChecked(false);
                radioBtnHave.setChecked(false);
            } else {
                SoftKeyboardUtils.hide(mEtKeyword);
            }
        });


        radioBtnNull.setOnClickListener(v -> {
            LogUtil.e("radioBtnNull" + radioBtnNull.isChecked() + "");

            radioBtnHave.setChecked(false);
            mEtKeyword.setText("");
            mEtKeyword.clearFocus();

            SoftKeyboardUtils.hide(mEtKeyword);
        });
        radioBtnHave.setOnClickListener(v -> {
            LogUtil.e("radioBtnHave" + radioBtnHave.isChecked() + "");

            radioBtnNull.setChecked(false);
            mEtKeyword.setText("");
            mEtKeyword.clearFocus();

            SoftKeyboardUtils.hide(mEtKeyword);
        });
    }

    public boolean formatCheck() {
        return true;
    }

    @Override
    public boolean put(JSONObject json) throws Exception {
        keyword = mEtKeyword.getText().toString().trim();
        if (radioBtnHave.isChecked()) {
            json.put(keyName, "ISNOTNULL");
        } else if (radioBtnNull.isChecked()) {
            json.put(keyName, "ISNULL");
        } else if (!TextUtil.isEmpty(keyword)) {
            json.put(keyName, keyword);
        }
        return true;
    }

    @Override
    public void reset() {
        mEtKeyword.setText("");
        mEtKeyword.clearFocus();
        radioBtnHave.setChecked(false);
        radioBtnNull.setChecked(false);
    }

}
