package com.hjhq.teamface.filelib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hjhq.teamface.filelib.R;


/**
 * Created by Administrator on 2017/3/29.
 */

public class ColorItemAdapter extends BaseAdapter {
    private Context mContext;
    private int[] arr;
    private int currPostion;

    public ColorItemAdapter(Context context, int[] arr, int currentPosition) {
        this.mContext = context;
        this.arr = arr;
        this.currPostion = currentPosition;
    }

    @Override
    public int getCount() {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }

    @Override
    public Integer getItem(int position) {
        return arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.filelib_color_choose_item, null);
            viewHolder.mcolor = (RelativeLayout) convertView.findViewById(R.id.color_choose_rl_bg);
            viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.color_choose_ivIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mcolor.setBackgroundResource(arr[position]);
        if (position == currPostion) {
            viewHolder.mIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mIcon.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void setCurrentPostion(int currentPostion) {
        this.currPostion = currentPostion;
        notifyDataSetChanged();
    }

    class ViewHolder {
        RelativeLayout mcolor;
        ImageView mIcon;
    }

}
