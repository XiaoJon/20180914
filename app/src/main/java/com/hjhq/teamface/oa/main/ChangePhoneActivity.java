package com.hjhq.teamface.oa.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import butterknife.Bind;

/**
 * 变更手机号
 *
 * @author lx
 * @date 2017/6/15
 */

public class ChangePhoneActivity extends BaseTitleActivity {

    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.iv_clean)
    ImageView ivClean;

    @Override
    protected int getChildView() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void setListener() {
        setOnClicks(btnNext, ivClean);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnNext.setEnabled(checkPhone());
            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClean.setVisibility(TextUtil.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void initData() {
        setActivityTitle("变更手机号码");
        tvHint.setText(SPHelper.getUserAccount());
    }

    @Override
    public void onClick(View view) {
        if (view == btnNext) {
            next();
        } else if (view == ivClean) {
            TextUtil.setText(etPhone, "");
            ivClean.setVisibility(View.GONE);
        }
    }

    /**
     * 下一步
     */
    private void next() {
        if (!checkPhone()) {
            showToast("手机号码格式错误");
            return;
        }
        String phone = etPhone.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, phone);
        bundle.putInt(Constants.DATA_TAG2, Constants.VERIFY_CODE_PHONE);
        UIRouter.getInstance().openUri(this, "DDComp://login/verify", bundle);
    }

    /**
     * 格式校验
     *
     * @return
     */
    private boolean checkPhone() {
        String phone = etPhone.getText().toString();
        return FormValidationUtils.isMobile(phone);
    }
}
