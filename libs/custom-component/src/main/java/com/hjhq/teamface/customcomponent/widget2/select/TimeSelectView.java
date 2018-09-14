package com.hjhq.teamface.customcomponent.widget2.select;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.SelectCommonView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间组件
 *
 * @author lx
 * @date 2017/8/23
 */

public class TimeSelectView extends SelectCommonView implements ActivityPresenter.OnActivityResult {
    private Calendar calendar = Calendar.getInstance();

    private String datetimeType;
    /**
     * 0 没有默认值 1 当前时间  2 指定时间
     */
    private String defaultValueId;

    public TimeSelectView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            defaultValueId = TextUtil.getNonNull(field.getDefaultValueId(), "0");
            datetimeType = TextUtil.getNonNull(field.getFormatType(), "yyyy-MM-dd");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_select_single_widget_layout;
    }

    @Override
    public void initView() {
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            tvContent.addTextChangedListener(new TextWatcherUtil.MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (TextUtil.isEmpty(s.toString())) {
                        ivRight.setImageResource(R.drawable.custom_icon_date);
                    } else {
                        ivRight.setImageResource(R.drawable.clear_button);
                    }
                }
            });
            ivRight.setOnClickListener(v -> {
                String content = tvContent.getText().toString();
                if (!TextUtil.isEmpty(content)) {
                    calendar.setTime(new Date());
                    TextUtil.setText(tvContent, "");
                }
            });
        }
        super.initView();
    }

    @Override
    public void initOption() {
        ivRight.setImageResource(R.drawable.custom_icon_date);
    }

    @Override
    protected void setData(Object value) {
        if (value == null) {
            return;
        }
        if ("".equals(value)) {
            value = "0";
        }
        try {
            long l = new BigDecimal(value + "").longValue();
//        long l = TextUtil.parseLong(value + "");
            if (l != 0) {
                calendar.setTimeInMillis(l);
                setDateTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick() {
        super.onClick();
        showDialogPick();
    }

    @Override
    public boolean formatCheck() {
        return true;
    }


    /**
     * 将两个选择时间的dialog放在该函数中
     */
    private void showDialogPick() {
        ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, calendar);
        bundle.putString(DateTimeSelectPresenter.FORMAT, datetimeType);
        CommonUtil.startActivtiyForResult(getContext(), DateTimeSelectPresenter.class, code, bundle);
    }

    /**
     * 设置时间
     */
    private void setDateTime() {
        String date = DateTimeUtil.longToStr(calendar.getTime().getTime(), datetimeType);
        TextUtil.setText(tvContent, date);
    }

    @Override
    public void setDefaultValue() {
        if ("1".equals(defaultValueId)) {
            calendar.setTime(new Date());
            setDateTime();
        } else if ("2".equals(defaultValueId)) {
            long l = TextUtil.parseLong(defaultValue);
            if (l != 0) {
                calendar.setTime(new Date(l));
                setDateTime();
            }
        }
    }

    @Override
    public Object getValue() {
        if (TextUtil.isEmpty(tvContent.getText().toString())) {
            return "";
        }
        return calendar.getTime().getTime();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                this.calendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                setDateTime();
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                calendar.setTime(new Date());
                TextUtil.setText(tvContent, "");
            }
            if (!checkNull() && isLinkage) {
                linkageData();
            }
        }
    }
}
