package com.hjhq.teamface.project.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.customcomponent.CustomComponentModel;
import com.hjhq.teamface.customcomponent.adapter.PictureItemAdapter;

import java.util.List;

/**
 * 任务图片
 *
 * @author lx
 * @date 2017/5/15
 */

public class TaskPictureItemAdapter extends PictureItemAdapter {
    public TaskPictureItemAdapter(ActivityPresenter activity, List data, int state, String moduleBean, String fieldControl) {
        super(activity, data, state, moduleBean, fieldControl);
    }


    @Override
    protected void uploadFile(BaseViewHolder helper, UploadFileBean item, String fileUrl) {
        new CustomComponentModel().uploadFile(mActivity, fileUrl, moduleBean, new ProgressSubscriber<UpLoadFileResponseBean>(mActivity) {
            @Override
            public void onNext(UpLoadFileResponseBean upLoadFileResponseBean) {
                super.onNext(upLoadFileResponseBean);
                List<UploadFileBean> data = upLoadFileResponseBean.getData();
                //处理上传后的数据
                handleUploadData(data, item);
                //显示文件信息
                showFileInfo(helper, item);
            }
        });
    }
}
