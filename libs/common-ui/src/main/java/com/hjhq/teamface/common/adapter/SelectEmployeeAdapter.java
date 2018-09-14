package com.hjhq.teamface.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.List;
import java.util.Locale;

/**
 * 选择员工适配器
 *
 * @author lx
 * @date 2017/4/10
 */
public class SelectEmployeeAdapter extends BaseQuickAdapter<SortModel, BaseViewHolder> implements SectionIndexer {
    private boolean showCheck = true;

    public SelectEmployeeAdapter(List<SortModel> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SortModel item) {
        helper.setVisible(R.id.iv_select, showCheck);
        TextView tvTitle = helper.getView(R.id.title);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvSubTitle = helper.getView(R.id.tv_sub_title);
        TextView tvLetter = helper.getView(R.id.catalog);
        ImageView avatar = helper.getView(R.id.avatar_in_contacts_iv);

        int selectState = item.member.getSelectState();
        if (MemberUtils.checkState(selectState, C.CAN_NOT_SELECT)) {
            if (item.member.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_prohibit_select);
            } else {
                ivSelect.setImageResource(R.drawable.icon_prohibit_unselect);
            }
        } else if (MemberUtils.checkState(selectState, C.FREE_TO_SELECT)) {
            if (item.member.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_selected);
            } else {
                ivSelect.setImageResource(R.drawable.icon_unselect);
            }
        }

        //根据position获取分类的首字母的Char ascii值
        int position = getData().indexOf(item);
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            tvLetter.setVisibility(View.VISIBLE);
            TextUtil.setText(tvLetter, item.sortLetters);
        } else {
            tvLetter.setVisibility(View.GONE);
        }

        TextUtil.setText(tvTitle, item.name);
        TextUtil.setText(tvSubTitle, item.role);
        ImageLoader.loadCircleImage(mContext, item.photo, avatar, item.name);
    }

    public boolean isShowCheck() {
        return showCheck;
    }

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getData().get(i).sortLetters;
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        List<SortModel> data = getData();
        if (CollectionUtils.isEmpty(data)) {
            return 0;
        } else {
            return data.get(position).sortLetters.charAt(0);
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
