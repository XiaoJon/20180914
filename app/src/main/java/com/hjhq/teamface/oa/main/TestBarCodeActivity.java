package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.EncodingUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.common.activity.CaptureActivity;
import com.hjhq.teamface.common.utils.CommonUtil;

/**
 * 二维码名片
 *
 * @author Administrator
 */
public class TestBarCodeActivity extends BaseTitleActivity {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView ivScan;
    ImageView ivShow;
    EditText mEditText;
    EditText mEditText2;
    String barcodeValue = "6923644283575";


    @Override
    protected int getChildView() {
        return R.layout.acitivity_test_barcode;
    }

    @Override
    protected void setListener() {
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.startActivtiyForResult(mContext, CaptureActivity.class, Constants.REQUEST_CODE1);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    showBarcode(s.toString());
                } else {
                    mImageView2.setImageBitmap(null);
                }
            }
        });
        ivScan.setOnClickListener(v -> {
            CommonUtil.startActivtiyForResult(mContext, CaptureActivity.class, Constants.REQUEST_CODE1);
        });
        ivShow.setOnClickListener(v -> {
            showBarcodePop(barcodeValue);
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("条形码");
        mImageView1 = findViewById(R.id.iv1);
        mImageView2 = findViewById(R.id.iv2);
        mImageView3 = findViewById(R.id.iv3);
        mEditText = findViewById(R.id.et);
        mEditText2 = findViewById(R.id.et2);
        ivScan = findViewById(R.id.iv_scan);
        ivShow = findViewById(R.id.iv_show);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && requestCode == Constants.REQUEST_CODE1) {
            if (data != null) {
                String content = data.getStringExtra(Constants.DATA_TAG1);
                if (!TextUtils.isEmpty(content)) {
                    mImageView2.post(new Runnable() {
                        @Override
                        public void run() {
                            mEditText.setText(content);
                            mEditText2.setText(content);
                            barcodeValue = content;
                            showBarcode(content);
                        }
                    });
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showBarcode(String content) {
        try {
            mImageView2.setImageBitmap(EncodingUtils.createBarcode(mContext, content, mImageView2.getWidth(), mImageView2.getHeight(), false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBarcodePop(String content) {
        DialogUtils.getInstance().showBarcode(mContext, "", content, ivShow.getRootView(), new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {

            }
        });
    }
}
