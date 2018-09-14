package com.hjhq.teamface.customcomponent.widget2.input;

import android.view.View;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

/**
 * 自动编号
 *
 * @author lx
 * @date 2017/8/23
 */

public class AutoNumberView extends InputCommonView {
    public AutoNumberView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        etInput.setClickable(false);
        etInput.setFocusable(false);
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
    public boolean formatCheck() {
        return true;
    }
}
