package com.hjhq.teamface.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

/**
 * @author lx
 * @date 2017/3/31
 */

public class EditActivity extends ActivityPresenter<EditDelegate, CommonModel> {

    public static String KEY_TITLE = "title";
    public static String KEY_HINT = "hint";
    public static String KEY_MAX_LENGTH = "max_length";
    public static String KEY_ORIGINAL_TEXT = "original_text";
    public static String KEY_TAG = "tag";
    public static String KEY_MUST = "must";


    private EditText mEditText;
    private TextView actProjectDescTvName;
    private String title;
    private String hint;
    private String originalText;
    private String tag;
    private int maxLength;
    private int id;
    private boolean must;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (getIntent() != null) {
            //取出保存的值
            Intent intent = getIntent();
            title = intent.getStringExtra(KEY_TITLE);
            hint = intent.getStringExtra(KEY_HINT);
            originalText = intent.getStringExtra(KEY_ORIGINAL_TEXT);
            tag = intent.getStringExtra(KEY_TAG);
            maxLength = intent.getIntExtra(KEY_MAX_LENGTH, 0);
            id = intent.getIntExtra(Constants.DATA_ID, 0);
            must = intent.getBooleanExtra(KEY_MUST, false);


        }
    }

    @Override
    public void init() {
        initView();
    }

    protected void initView() {
        mEditText = (EditText) findViewById(R.id.et);
        mEditText.setMaxEms(maxLength);
        actProjectDescTvName = (TextView) findViewById(R.id.act_project_desc_tvName);
        SoftKeyboardUtils.hide(mEditText);
        viewDelegate.setTitle(title);

        TextUtil.setTextorVisibility(actProjectDescTvName, tag);
        if (!TextUtils.isEmpty(originalText) && !originalText.equals("null")) {
            mEditText.setText(originalText);
        }

        mEditText.setHint(hint);
        mEditText.selectAll();
        if (maxLength != 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            mEditText.setFilters(filters);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SoftKeyboardUtils.hide(mEditText);
        String s = mEditText.getText().toString().trim();
        if (maxLength <= 25) {
            s = s.replace(" ", "").replace("\r", "").replace("\r\n", "").replace("\t", "");
        }
        if (must && (TextUtils.isEmpty(s) || s.length() > maxLength)) {
            ToastUtils.showError(mContext, tag + "必填且不超过" + maxLength + "字");
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.FILE_DESCRIPTION, s);
        intent.putExtra(Constants.DATA_ID, id);
        setResult(Activity.RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);

    }

}
