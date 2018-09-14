package com.hjhq.teamface.customcomponent.widget2.input;

import android.text.InputType;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;


/**
 * 电话组件
 *
 * @author lx
 * @date 2017/8/23
 */

public class PhoneInputView extends InputCommonView {
    private static final int PHONE_NUM_LEN = 11;
    /**
     * 位数：0:不限,1:11位
     */
    private int phoneLenth = 0;
    /**
     * 类型：0:电话,1:手机号码
     */
    private int phoneType = 1;

    public PhoneInputView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            phoneLenth = TextUtil.parseInt(field.getPhoneLenth());
            phoneType = TextUtil.parseInt(field.getPhoneType());
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        if (isDetailState()) {
            setRightImage(R.drawable.custom_icon_phone2);
        } else {
            setLeftImage(R.drawable.custom_icon_phone);
        }
        setRepeatCheckIcon();
        if (phoneType == 1) {
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            etInput.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_VARIATION_PHONETIC);
        }
        if (isDetailState() && !TextUtil.isEmpty(getContent())) {
            ivRight.setOnClickListener(v ->
                    SystemFuncUtils.callPhone(getContext(), getContent()));
        }
    }

    @Override
    public boolean formatCheck() {
        if (phoneLenth == 1) {
            int length = getContent().length();
            if (length != 0 && length != PHONE_NUM_LEN) {
                ToastUtils.showError(getContext(), title + "只能输入11位");
                return false;
            }
        }
        return true;
    }
}
