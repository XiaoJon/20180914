package com.hjhq.teamface.project.widget.file;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.customcomponent.widget2.file.PictureView;
import com.hjhq.teamface.project.adapter.TaskPictureItemAdapter;

/**
 * Created by Administrator on 2018/8/30.
 */

public class TaskPictureView extends PictureView {
    public TaskPictureView(CustomBean bean) {
        super(bean);
    }

    @Override
    protected void initAdapter() {
        fileAdapter = new TaskPictureItemAdapter((ActivityPresenter) mActivity, uploadFileBeanList, state, bean.getModuleBean(), fieldControl);
        mRecyclerView.setAdapter(fileAdapter);
    }
}
