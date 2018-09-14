package com.hjhq.teamface.custom.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.custom.R;

/**
 * 新增共享
 *
 * @author xj
 * @date 2017/9/4
 */

public class AddShareActivity extends ActivityPresenter<AddShareDelegate, AddCustomModel> implements View.OnClickListener {
    private String moduleBean;
    private String objectId;
    private static final String[] PERMISSIONS = {"只读", "读/写", "读/写/删"};

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            objectId = intent.getStringExtra(Constants.DATA_ID);
        }
    }

    @Override
    public void init() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveCustomData();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存
     */
    private void saveCustomData() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.ll_permission){
                selectPermission();
        }
    }

    /**
     * 选择权限
     */
    private void selectPermission() {
        PopUtils.showBottomMenu(this, viewDelegate.getRootView(), "选择权限", PERMISSIONS, position -> {

            return true;
        });
    }
}
