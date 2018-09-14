package com.hjhq.teamface.filelib.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.FileDownloadLogBean;

import java.util.List;


public class FileDownloadLogAdapter extends BaseQuickAdapter<FileDownloadLogBean.DataBean, BaseViewHolder> {

    public FileDownloadLogAdapter(List<FileDownloadLogBean.DataBean> data) {
        super(R.layout.filelib_download_log_list_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FileDownloadLogBean.DataBean item) {
        helper.setText(R.id.tv_folder_name, item.getEmployee_name() + "");
        helper.setText(R.id.download_times, item.getNumber() + "次");
        helper.setText(R.id.time, DateTimeUtil.longToStr(Long.parseLong(item.getLately_time()), "yyyy-MM-dd HH:mm") + "");
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(),
                item.getPicture(), helper.getView(R.id.iv_conversation_avatar), item.getEmployee_name());


    }

}