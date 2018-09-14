package com.hjhq.teamface.project.ui.add;

import android.widget.EditText;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2018/4/10.
 * 项目文件夹(一级)
 */

public class ProjectAddFolderActivityDelegate extends AppDelegate {
    private EditText mEtName;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_folder_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_add_folder_title);
        setRightMenuTexts(R.color.app_blue, "保存");
        mEtName = get(R.id.et_name);
    }


    public String getName() {
        return mEtName.getText().toString();
    }

    public void getFolderName(String folderName) {
        mEtName.setText(folderName);
    }
}
