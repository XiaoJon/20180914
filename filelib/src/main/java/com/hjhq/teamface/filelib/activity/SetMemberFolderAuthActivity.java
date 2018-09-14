package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.view.SetMemberAuthDelegate;

public class SetMemberFolderAuthActivity extends ActivityPresenter<SetMemberAuthDelegate, FilelibModel> implements View.OnClickListener {

    RelativeLayout rlUpload;
    RelativeLayout rlDownload;
    ImageView ivDownload;
    ImageView ivUpload;

    Bundle mBundle;
    String folderId;
    String employeeId;
    String authUpload;
    String authDownload;

    @Override
    public void init() {
        initView();
    }


    protected void initView() {
        viewDelegate.setTitle(getString(R.string.filelib_set_folder_auth_title));
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.confirm));
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            employeeId = mBundle.getString(Constants.DATA_TAG1);
            folderId = mBundle.getString(FileConstants.FOLDER_ID);
            authDownload = mBundle.getString(FileConstants.AUTH_DOWNLOAD);
            authUpload = mBundle.getString(FileConstants.AUTH_UPLOAD);
        }

        rlUpload = (RelativeLayout) findViewById(R.id.act_coop_rl_two);
        rlDownload = (RelativeLayout) findViewById(R.id.act_coop_rl_three);
        ivDownload = (ImageView) findViewById(R.id.act_coop_iv_three);
        ivUpload = (ImageView) findViewById(R.id.act_coop_iv_two);

        ivDownload.setVisibility("1".equals(authDownload) ? View.VISIBLE : View.GONE);
        ivUpload.setVisibility("1".equals(authUpload) ? View.VISIBLE : View.GONE);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        rlUpload.setOnClickListener(this);
        rlDownload.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        String upload = ivUpload.getVisibility() == View.VISIBLE ? "1" : "0";
        String download = ivDownload.getVisibility() == View.VISIBLE ? "1" : "0";


        model.updateSetting(SetMemberFolderAuthActivity.this, folderId, employeeId, upload,
                download, new ProgressSubscriber<BaseBean>(SetMemberFolderAuthActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                       /* intent.putExtra(Constants.DATA_TAG1, upload);
                        intent.putExtra(Constants.DATA_TAG2, download);*/

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        return super.onOptionsItemSelected(item);
    }


    int menuPosition = 0;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.act_coop_rl_two) {
            ivUpload.setVisibility(ivUpload.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        } else if (view.getId() == R.id.act_coop_rl_three) {
            ivDownload.setVisibility(ivDownload.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
