package com.hjhq.teamface.filelib.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.SwipeMenuLayout;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.activity.FolderAuthManageActivity;
import com.hjhq.teamface.filelib.bean.FolderAuthDetailBean;

import java.util.List;


public class FolderAuthMemberAdapter extends BaseQuickAdapter<FolderAuthDetailBean.SettingBean, BaseViewHolder> {
    public static final int ADMIN_TAG = 1;
    public static final int MEMBER_TAG = 2;
    public static int FOLDER_TYPE = -1;
    int tag = -1;
    FolderAuthManageActivity mActivity;


    public FolderAuthMemberAdapter(ActivityPresenter activity, int folderType,
                                   int tag, List<FolderAuthDetailBean.SettingBean> data) {
        super(R.layout.filelib_folder_auth_member_item, data);
        this.tag = tag;
        this.FOLDER_TYPE = folderType;
        this.mActivity = (FolderAuthManageActivity) activity;

    }


    @Override
    protected void convert(BaseViewHolder helper, FolderAuthDetailBean.SettingBean item) {
        helper.setIsRecyclable(false);
        helper.setText(R.id.tv_conversation_title, item.getEmployee_name());
        SwipeMenuLayout layout = helper.getView(R.id.sml);
        if ((!TextUtils.isEmpty(item.getEmployee_id()) && item.getEmployee_id().equals(SPHelper.getEmployeeId()))
                || getItemCount() == 1) {
            layout.setSwipeEnable(false);
        }else{
            layout.setSwipeEnable(true);
        }

        if (tag == ADMIN_TAG) {
            //点击事件,移除管理员
            if ("1".equals(item.getSign_type())) {

                layout.setSwipeEnable(false);
            } else if ("0".equals(item.getSign_type())) {

                helper.getView(R.id.menu_ll).setOnClickListener(v -> {
                    mActivity.delMamager(helper.getAdapterPosition());
                    remove(helper.getAdapterPosition());
                    notifyDataSetChanged();
                });
            } else {

                layout.setSwipeEnable(false);
            }
            //管理员图标
            helper.setVisible(R.id.icon_admin, true);
        } else if (tag == MEMBER_TAG) {

            if (FOLDER_TYPE == FileConstants.PUBLIC_FOLDER) {

                layout.setSwipeEnable(false);
            }
            //点击事件,移除成员
            helper.getView(R.id.menu_ll).setOnClickListener(v -> {
                mActivity.delMember(helper.getAdapterPosition());
                remove(helper.getAdapterPosition());
                notifyDataSetChanged();
            });
            StringBuilder sb = new StringBuilder();
            helper.setVisible(R.id.icon_admin, false);
            if ("1".equals(item.getUpload())) {
                sb.append("上传  ");
            }
            if ("1".equals(item.getDownload())) {
                sb.append("下载  ");
            }
            sb.append("预览");
            helper.setText(R.id.tv_last_msg_time, sb.toString());
        }

        ImageView image = helper.getView(R.id.iv_conversation_avatar);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), image, item.getEmployee_name());


    }

}