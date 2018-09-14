package com.hjhq.teamface.project.widget.file;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.customcomponent.widget2.file.AttachmentView;
import com.hjhq.teamface.project.adapter.TaskAttachmentItemAdapter;

/**
 * Created by Administrator on 2018/8/30.
 */

public class TaskAttachmentView extends AttachmentView {
    public TaskAttachmentView(CustomBean bean) {
        super(bean);
    }

    @Override
    protected void initAdapter() {
        fileAdapter = new TaskAttachmentItemAdapter((ActivityPresenter) mActivity, uploadFileBeanList, state, bean.getModuleBean(), fieldControl);
        mRecyclerView.setAdapter(fileAdapter);
    }
}
