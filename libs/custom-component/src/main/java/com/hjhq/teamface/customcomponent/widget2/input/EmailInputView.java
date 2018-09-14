package com.hjhq.teamface.customcomponent.widget2.input;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;


/**
 * 邮箱组件
 *
 * @author lx
 * @date 2017/8/23
 */

public class EmailInputView extends InputCommonView {
    public EmailInputView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        if (isDetailState()) {
            setRightImage(R.drawable.custom_icon_email2);
        } else {
            setLeftImage(R.drawable.custom_icon_email);
        }
        setRepeatCheckIcon();
        etInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        if (isDetailState() && isEmailValid(getContent())) {
            ivRight.setOnClickListener(v -> DialogUtils.getInstance().sureOrCancel(getContext(), getContent(), null, "发邮件", "取消", etInput, () -> {
                ArrayList<ReceiverBean> list = new ArrayList<>();
                EmailBean bean = new EmailBean();
                ReceiverBean rb = new ReceiverBean();
                rb.setMail_account(getContent());
                list.add(rb);

                bean.setTo_recipients(list);
                bean.setAttachments_name(new ArrayList<>());
                bean.setBcc_recipients(new ArrayList<>());
                bean.setCc_recipients(new ArrayList<>());
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.DATA_TAG3, EmailConstant.EDIT_AGAIN);
                bundle.putSerializable(Constants.DATA_TAG5, bean);
                UIRouter.getInstance().openUri(getContext(), "DDComp://email/new_email", bundle);
            }));
        }
    }

    @Override
    public boolean formatCheck() {
        if (!isEmailValid(getContent())) {
            ToastUtils.showError(getContext(), title + "格式错误");
            return false;
        }
        return true;
    }


    public boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
