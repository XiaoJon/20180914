package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.im.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeInfoAdapter extends BaseAdapter {

    private Context context;

    private List<EmployeeDetailBean.DataBean.Photo> photos = new ArrayList<>();

    public EmployeeInfoAdapter(Context context, List<EmployeeDetailBean.DataBean.Photo> photos) {
        super();
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos == null ? 0 : photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_employee_info, null);
            holder.iv_employee_head = (ImageView) convertView.findViewById(R.id.iv_employee_head);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        EmployeeDetailBean.DataBean.Photo photo = photos.get(position);

        ImageLoader.loadImage(context, photo.getFileUrl(), holder.iv_employee_head);

        return convertView;
    }

    class Holder {
        ImageView iv_employee_head;
    }
}
