package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.im.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 添加群组成员适配器
 */
public class AddGroupMemberSortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> mList;
    private List<SortModel> mSelectedList;
    private Context mContext;

    public AddGroupMemberSortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        mSelectedList = new ArrayList<>();
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final SortModel mSortModel = mList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_add_group_member, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.number);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.ivSelect = (ImageView) view.findViewById(R.id.iv_select);
            viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_in_contacts_iv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mSortModel.sortLetters);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(mSortModel.name);
        viewHolder.tvNumber.setText(mSortModel.number);
        viewHolder.ivSelect.setVisibility(mSortModel.isAdded?View.VISIBLE:View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleChecked(position);
                notifyDataSetChanged();
            }
        });

        return view;

    }

    public static class ViewHolder {
        public TextView tvLetter;
        public TextView tvTitle;
        public TextView tvNumber;
        public ImageView ivSelect;
        public ImageView avatar;

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (mList == null || mList.size() == 0) {
            return 0;
        } else {
            return mList.get(position).sortLetters.charAt(0);
        }

    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    private boolean isSelected(SortModel model) {
        return mSelectedList.contains(model);
        //return true;
    }

    public void toggleChecked(int position) {
        SortModel sortModel = mList.get(position);
        if (isSelected(sortModel)) {
            removeSelected(sortModel);
        } else {
            setSelected(sortModel);
        }

    }

    public void setSelected(SortModel sortModel){
        if (!isSelected(sortModel)){
            sortModel.isAdded = true;
            mSelectedList.add(sortModel);
        }
    }


    private void removeSelected(SortModel sortModel) {
        if (isSelected(sortModel)) {
            sortModel.isAdded = false;
            mSelectedList.remove(sortModel);
        }
    }

    public List<SortModel> getSelectedList() {
        return mSelectedList;
    }
}