package com.hjhq.teamface.memo.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.bean.MemoBean;

import java.util.List;


public class MemoMemberAdapter extends BaseQuickAdapter<MemoBean.MemberBean, BaseViewHolder> {
    private int type;
    private boolean isContentChanged = false;

    public MemoMemberAdapter(int type, List<MemoBean.MemberBean> data) {
        super(R.layout.memo_content_member, data);
        this.type = type;

    }


    @Override
    protected void convert(BaseViewHolder helper, MemoBean.MemberBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setVisible(R.id.tv_name, false);
        ImageView avatar = helper.getView(R.id.iv_avatar);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getAvatar(), avatar, item.getName());

        if (type == 1) {
            helper.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_delete).setVisibility(View.GONE);
        }
        helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(helper.getAdapterPosition());
                //notifyItemRemoved(helper.getAdapterPosition());
                notifyDataSetChanged();
                isContentChanged = true;
                if (mListener != null) {
                    mListener.onRemove();
                }
            }
        });
    }

    public boolean isContentChanged() {
        return isContentChanged;
    }

    public interface OnMemberRemoveListener {
        void onRemove();
    }

    private OnMemberRemoveListener mListener;

    public void setOnMemberRemoveListener(OnMemberRemoveListener listener) {
        this.mListener = listener;
    }


}