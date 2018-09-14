package com.hjhq.teamface.customcomponent.widget2.input;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

/**
 * 多行文本
 *
 * @author lx
 * @date 2017/8/23
 */

public class MultiTextInputView extends InputCommonView {

    public MultiTextInputView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_multi_widget_layout;
    }

    @Override
    public void initOption() {

    }

    @Override
    public boolean formatCheck() {
        return true;
    }
}
