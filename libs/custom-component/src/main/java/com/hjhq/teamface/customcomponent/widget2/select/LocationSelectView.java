package com.hjhq.teamface.customcomponent.widget2.select;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.RegionUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.SelectCommonView;

import kankan.wheel.widget.regionselect.ProvinceSelectActivity;
import kankan.wheel.widget.regionselect.model.Result;


/**
 * 省市区选择
 *
 * @author lx
 * @date 2017/8/23
 */

public class LocationSelectView extends SelectCommonView implements ActivityPresenter.OnActivityResult {
    /**
     * 编号和名称组合
     */
    String district = "";

    public LocationSelectView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_select_single_widget_layout;
    }

    @Override
    public void initOption() {
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            tvContent.addTextChangedListener(new TextWatcherUtil.MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (TextUtil.isEmpty(s.toString())) {
                        ivRight.setImageResource(R.drawable.icon_to_next);
                    } else {
                        ivRight.setImageResource(R.drawable.clear_button);
                    }
                }
            });
            ivRight.setOnClickListener(v -> {
                String content = tvContent.getText().toString();
                if (!TextUtil.isEmpty(content)) {
                    tvContent.setText("");
                    district = "";
                }
            });
        }
    }

    @Override
    protected void setData(Object value) {
        if (value == null) {
            return;
        }
        String s = RegionUtil.code2String(district = value + "");
        TextUtil.setNonEmptyText(tvContent, s);
    }

    @Override
    public void onClick() {
        super.onClick();
        ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
        Bundle bundle = new Bundle();
        bundle.putString(ProvinceSelectActivity.DEFAULT_SELECT_KEY, RegionUtil.getArea(district));
        CommonUtil.startActivtiyForResult(getContext(), ProvinceSelectActivity.class, code, bundle);
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code && resultCode == ProvinceSelectActivity.SUCESS) {
            Result result = (Result) data.getSerializableExtra(ProvinceSelectActivity.RESULT_KEY);
            String provinceName = result.getProvinceName();
            String cityName = result.getCityName();
            String districtName = result.getDistrictName();
            StringBuilder builder = new StringBuilder();

            district = "";
            if (!TextUtils.isEmpty(provinceName)) {
                builder.append(provinceName);
                district += result.getProvinceID() + ":" + provinceName;
            }
            if (!TextUtils.isEmpty(cityName)) {
                builder.append(cityName);
                district += "," + result.getCityID() + ":" + cityName;
            }
            if (!TextUtils.isEmpty(districtName)) {
                builder.append(districtName);
                district += "," + result.getDistrictID() + ":" + districtName;
            }
            TextUtil.setNonEmptyText(tvContent, builder.toString());
            if (!checkNull() && isLinkage) {
                linkageData();
            }
        }
    }

    @Override
    public Object getValue() {
        return district;
    }
}
