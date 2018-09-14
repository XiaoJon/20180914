package com.hjhq.teamface.common.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.common.R;

import java.util.List;


public class EmailAttachmentAdapter extends BaseQuickAdapter<AttachmentBean, BaseViewHolder> {
    public static final int NO_ACTION_TAG = 0;
    public static final int ADD_EMAIL_TAG = 1;
    public static final int EMAIL_DETAIL_TAG = 2;
    private int currentTag = NO_ACTION_TAG;


    public EmailAttachmentAdapter(int tag, List<AttachmentBean> data) {
        super(R.layout.email_file_list_item, data);
        this.currentTag = tag;
    }


    @Override
    protected void convert(BaseViewHolder helper, AttachmentBean item) {
        switch (currentTag) {
            case NO_ACTION_TAG:
                helper.setVisible(R.id.menu, false);
                break;
            case ADD_EMAIL_TAG:
                helper.setVisible(R.id.menu, true);
                helper.setImageResource(R.id.menu, R.drawable.email_delete_attachment_icon);
                break;
            case EMAIL_DETAIL_TAG:
                helper.setVisible(R.id.menu, true);
                helper.setImageResource(R.id.menu, R.drawable.icon_file_download);
                break;
            default:
                helper.setVisible(R.id.menu, false);
                break;
        }

        helper.setVisible(R.id.tv_file_name, true);
        helper.setVisible(R.id.tv_file_size, true);
        helper.setVisible(R.id.rl_menu, true);
        helper.addOnClickListener(R.id.rl_menu);
        ImageView icon = helper.getView(R.id.iv_conversation_avatar);
        helper.setImageResource(R.id.iv_conversation_avatar, R.drawable.icon_unknow);
        if (currentTag == EMAIL_DETAIL_TAG) {
            item.setFromWhere(EmailConstant.FROM_UPLOAD);
        }
        helper.setText(R.id.tv_file_name, item.getFileName());
        helper.setText(R.id.tv_file_size, FormatFileSizeUtil.formatFileSize(TextUtil.parseLong(item.getFileSize())));
        switch (item.getFromWhere()) {
            case EmailConstant.FROM_LOCAL_FILE:
                if (FileTypeUtils.isImage(item.getFileType())) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), icon, item.getFileUrl(), R.drawable.icon_jpg);
                } else {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(FileTypeUtils.getExtensionName(item.getFileName())), icon);
                }
                break;
            case EmailConstant.FROM_FILE_LIB:
            case EmailConstant.FROM_UPLOAD:
                String url = item.getFileUrl();
                //如果是文件库添加的附件,转换url
                if (url.contains(FileConstants.FILE_BASE_URL)) {
                    url = url.replace(FileConstants.FILE_BASE_URL, FileConstants.FILE_THUMB_BASE_URL);
                }
                if (FileTypeUtils.isImage(item.getFileType()) || FileTypeUtils.isImage(FileTypeUtils.getExtensionName(item.getFileName()))) {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), icon, url + "&width=64", R.drawable.icon_jpg);
                } else {
                    ImageLoader.loadImage(helper.getConvertView().getContext(), FileTypeUtils.getFileTypeIcon(item.getFileType()), icon);
                }
                break;
            default:
                break;


        }


    }

}