package com.hjhq.teamface.project.ui.add;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 新增项目/项目设置
 */

public class AddProjectFolderDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_folder_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        get(R.id.iv_back).setOnClickListener(v -> {
            getActivity().finish();
        });

    }


}
