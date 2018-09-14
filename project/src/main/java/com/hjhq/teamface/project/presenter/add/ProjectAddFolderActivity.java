package com.hjhq.teamface.project.presenter.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.ui.add.ProjectAddFolderActivityDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 新增/编辑项目文件夹
 */
@RouteNode(path = "/project_add_folder", desc = "项目新建二级文件夹")
public class ProjectAddFolderActivity extends ActivityPresenter<ProjectAddFolderActivityDelegate, ProjectModel2> {
    private long mProjectId = 0L;
    private String mFolderId = "";
    private String mDataId = "";
    private String mFolderName;
    private int type;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(ProjectConstants.EDIT_FOLDER_TYPE);
            mProjectId = bundle.getLong(Constants.DATA_TAG1, 0L);
            mFolderId = bundle.getString(Constants.DATA_TAG2);
            mFolderName = bundle.getString(Constants.DATA_TAG3);
            mDataId = bundle.getString(Constants.DATA_TAG4);
            if (!TextUtils.isEmpty(mFolderName)) {
                viewDelegate.getFolderName(mFolderName);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String folderName = viewDelegate.getName();
        if (TextUtils.isEmpty(folderName)) {
            ToastUtils.showToast(mContext, "文件夹名字不能为空");
            return true;
        }
        switch (type) {
            case ProjectConstants.ADD_FOLDER:
            case ProjectConstants.ADD_SUB_FOLDER:
                saveFolder(folderName);
                break;
            case ProjectConstants.EDIT_FOLDER:
                editFolder(folderName);
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存修改
     */
    private void editFolder(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mDataId);
        map.put("name", name);
        map.put("project_id", mProjectId + "");

        model.editProjectFolder(mContext, map, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    /**
     * 新建文件夹
     */
    private void saveFolder(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        //0创建文件夹,1创建子文件夹
        if (type == ProjectConstants.ADD_FOLDER) {
            map.put("type", "0");
            map.put("parent_id", mProjectId + "");
        } else if (type == ProjectConstants.ADD_SUB_FOLDER) {
            map.put("parent_id", mFolderId);
            map.put("type", "1");
        }
        map.put("project_id", mProjectId + "");
        model.addProjectFolder(mContext, map, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
