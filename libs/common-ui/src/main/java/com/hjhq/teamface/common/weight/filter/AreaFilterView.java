package com.hjhq.teamface.common.weight.filter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.weight.BaseFilterView;

import kankan.wheel.widget.regionselect.ProvinceSelectActivity;
import kankan.wheel.widget.regionselect.model.Result;


/**
 * 筛选条件-省市区
 */

public class AreaFilterView extends BaseFilterView implements ActivityPresenter.OnActivityResult {
    private LinearLayout radioGroup;
    private CheckBox radioBtnHave;
    private CheckBox radioBtnNull;
    private TextView mAreaView;
    private RelativeLayout actionRl;
    private LinearLayout actionLl;
    ImageView ivArraw;
    private TextView tvTitle;
    private boolean flag = false;
    private int itemId = 0;
    //初始化组件用到的数据
    private String itemTitle;
    private String districtId;

    public AreaFilterView(FilterFieldResultBean.DataBean bean) {
        super(bean);
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {

        mView = View.inflate(activity, R.layout.crm_item_goods_filter_by_area, null);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);

        radioGroup = (LinearLayout) mView.findViewById(R.id.radio_group);
        radioBtnHave = (CheckBox) mView.findViewById(R.id.check_input);
        radioBtnNull = (CheckBox) mView.findViewById(R.id.check_null);
        mAreaView = (TextView) mView.findViewById(R.id.tv_area);
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


        mAreaView.setOnClickListener(v -> {
            ((ActivityPresenter) getContext()).setOnActivityResult(index, this);
            CommonUtil.startActivtiyForResult(getContext(), ProvinceSelectActivity.class, index);
        });


        radioBtnNull.setOnClickListener(v -> {
            radioBtnHave.setChecked(false);
            districtId = "";
            TextUtil.setText(mAreaView, districtId);
        });
        radioBtnHave.setOnClickListener(v -> {
            radioBtnNull.setChecked(false);
            districtId = "";
            TextUtil.setText(mAreaView, districtId);
        });
    }

    public boolean formatCheck() {
        return true;
    }

    @Override
    public boolean put(JSONObject json) throws Exception {
        if (radioBtnHave.isChecked()) {
            json.put(keyName, "ISNOTNULL");
        } else if (radioBtnNull.isChecked()) {
            json.put(keyName, "ISNULL");
        } else if (!TextUtil.isEmpty(districtId)) {
            json.put(keyName, districtId.toString());
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == index && resultCode == ProvinceSelectActivity.SUCESS) {
            radioBtnNull.setChecked(false);
            radioBtnHave.setChecked(false);
            districtId = "";
            Result result = (Result) data.getSerializableExtra(ProvinceSelectActivity.RESULT_KEY);
            String provinceName = result.getProvinceName();
            String cityName = result.getCityName();
            String districtName = result.getDistrictName();
            StringBuilder builder = new StringBuilder();

            if (!TextUtils.isEmpty(provinceName) && !"null".equals(provinceName)) {
                builder.append(provinceName);
                districtId += result.getProvinceID();
            }
            if (!TextUtils.isEmpty(cityName) && !"null".equals(cityName)) {
                builder.append(cityName);
                districtId += "," + result.getCityID();
            }
            if (!TextUtils.isEmpty(districtName) && !"null".equals(districtName)) {
                builder.append(districtName);
                districtId += "," + result.getDistrictID();
            }
            TextUtil.setText(mAreaView, builder.toString());
        }
    }
}
