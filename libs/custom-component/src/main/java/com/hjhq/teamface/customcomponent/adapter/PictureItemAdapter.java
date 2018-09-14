package com.hjhq.teamface.customcomponent.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.customcomponent.CustomComponentModel;
import com.hjhq.teamface.customcomponent.R;

import java.util.List;

/**
 * @author lx
 * @date 2017/5/15
 */

public class PictureItemAdapter extends BaseQuickAdapter<UploadFileBean, BaseViewHolder> {
    protected String fieldControl;
    protected String moduleBean;
    protected int state;
    protected ActivityPresenter mActivity;

    public PictureItemAdapter(ActivityPresenter activity, List data, int state, String moduleBean, String fieldControl) {
        super(R.layout.custom_item_picture_v2, data);
        this.mActivity = activity;
        this.state = state;
        this.moduleBean = moduleBean;
        this.fieldControl = fieldControl;
    }

    @Override
    protected void convert(BaseViewHolder helper, UploadFileBean item) {

        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ImageView ivDelete = helper.getView(R.id.iv_del);
        helper.addOnClickListener(R.id.iv_del);
        helper.addOnClickListener(R.id.iv_icon);
        String fileUrl = item.getFileUrl();
        if (CustomConstants.URL.equals(fileUrl)) {
            ImageLoader.loadImage(ivIcon.getContext(), R.drawable.icon_add_picture_no_bg, ivIcon);
            helper.addOnClickListener(R.id.iv_icon);
            ivDelete.setVisibility(View.GONE);
            return;
        }

        if (FileTypeUtils.isImage(item.getFileType())) {
            ImageLoader.loadRoundImage(mContext, fileUrl, ivIcon, 2, R.drawable.icon_default);
        } else if (FileTypeUtils.isAudio(item.getFileType())) {
            ivIcon.setImageResource(R.drawable.icon_mp3);
        }

        if (state == CustomConstants.DETAIL_STATE || CustomConstants.FIELD_READ.equals(fieldControl)) {
            //详情不显示
            helper.setVisible(R.id.iv_del, false);
            showFileInfo(helper, item);
        } else {
            helper.setVisible(R.id.iv_del, true);

            //是否已上传
            if (item.getSerialNumber() != null) {
                //显示进度
                showFileInfo(helper, item);
            } else {
                uploadFile(helper, item, fileUrl);
            }
        }
    }

    protected void uploadFile(final BaseViewHolder helper, final UploadFileBean item, String fileUrl) {
        new CustomComponentModel().uploadApplyFile(mActivity, fileUrl, moduleBean, new ProgressSubscriber<UpLoadFileResponseBean>(mActivity) {
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

    /**
     * 显示文件信息
     *
     * @param helper
     * @param item
     */
    protected void showFileInfo(BaseViewHolder helper, UploadFileBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvSize = helper.getView(R.id.tv_size);

        long uploadTime = TextUtil.parseLong(item.getUpload_time());
        if (uploadTime != 0) {
            String time = DateTimeUtil.longToStr(uploadTime, "yyyy-MM-dd");
            TextUtil.setText(tvTime, time);
        }

        long size = TextUtil.parseLong(item.getFileSize());
        String fileSize = FormatFileSizeUtil.formatFileSize(size);

        TextUtil.setText(tvName, item.getUpload_by());
        TextUtil.setText(tvSize, fileSize);
        if (FileTypeUtils.isImage(item.getFileType())) {
            ImageLoader.loadImage(mContext, item.getFileUrl(), ivIcon, R.drawable.icon_default, R.drawable.icon_default);
        }
    }

    /**
     * 处理上传后的数据
     *
     * @param data
     * @param item
     */
    protected void handleUploadData(List<UploadFileBean> data, UploadFileBean item) {
        if (data != null && data.size() > 0) {
            UploadFileBean uploadFileBean = data.get(0);
            item.setUpload_time(uploadFileBean.getUpload_time());
            item.setUpload_by(uploadFileBean.getUpload_by());
            item.setFileName(uploadFileBean.getFileName());
            item.setFileSize(uploadFileBean.getFileSize());
            item.setFileType(uploadFileBean.getFileType());
            item.setFileUrl(uploadFileBean.getFileUrl());
            item.setSerialNumber(uploadFileBean.getSerialNumber());
            item.setOriginalFileName(uploadFileBean.getOriginalFileName());
        }
    }
}
