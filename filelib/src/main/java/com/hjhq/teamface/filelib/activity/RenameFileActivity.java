package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.view.RenameFileDelegate;

/**
 * 文件重命名
 */

public class RenameFileActivity extends ActivityPresenter<RenameFileDelegate, FilelibModel> {

    String fileName = "";
    EditText etSign;

    @Override
    public void init() {
        initView();
        initData();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DATA_TAG1, fileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            fileName = savedInstanceState.getString(Constants.DATA_TAG1);
        }
    }

    protected void initView() {
        etSign = viewDelegate.get(R.id.et_sign);
        viewDelegate.setTitle(getString(R.string.rename));
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.save));
    }

    protected void initData() {
        etSign.setText(fileName);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        renameFile();
        return super.onOptionsItemSelected(item);
    }

    private void renameFile() {
        final String sign = etSign.getText().toString();
        if (TextUtils.isEmpty(sign) || sign.length() > 20) {
            ToastUtils.showToast(mContext, "文件名在1-20字之间!");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, sign);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
