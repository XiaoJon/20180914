package com.hjhq.teamface.filelib.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.RootFolderResBean;

import java.util.List;


public class RootFolderAdapter extends BaseQuickAdapter<RootFolderResBean.DataBean, BaseViewHolder> {

    public RootFolderAdapter(List<RootFolderResBean.DataBean> data) {
        super(R.layout.filelib_root_folder_item, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, RootFolderResBean.DataBean item) {
        helper.setText(R.id.tv_nav, item.getName());
        helper.setImageResource(R.id.iv_nav, getRes(item.getId()));


    }

    /**
     * 获取文件夹图标
     *
     * @param id
     * @return
     */
    private int getRes(String id) {
        switch (id) {
            case "1":
                return R.drawable.filelib_personal_file_icon;
            case "2":
                return R.drawable.icon_app_file;
            case "3":
                return R.drawable.filelib_company_file_icon;
            case "4":
                return R.drawable.filelib_my_share_file_icon;
            case "5":
                return R.drawable.filelib_share_to_me_file_icon;
            case "6":
                return R.drawable.filelib_project_file_icon;
            default:
                break;
        }
        return 0;
    }


}