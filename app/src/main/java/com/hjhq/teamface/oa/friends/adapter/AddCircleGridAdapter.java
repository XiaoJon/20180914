package com.hjhq.teamface.oa.friends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.bean.Photo;

import java.util.ArrayList;

/**
 * @Description
 */
public class AddCircleGridAdapter extends BaseAdapter {

    // 图片路劲
    private ArrayList<Photo> imageList = new ArrayList<>();


    // 动态布局加载器
    private LayoutInflater layoutInflater = null;

    // 最大显示数量
    private int maxDisplayCount = -1;

    public AddCircleGridAdapter(Context context) {
        // 得到一个布局填充服务
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (imageList == null) {
            return 1;
        }
        int count = imageList.size();
        if (maxDisplayCount != -1) {
            count = count <= maxDisplayCount ? count : maxDisplayCount;
        }
        return count + 1;
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageHolder imageHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_item, null);
            imageHolder = new ImageHolder();
            imageHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            convertView.setTag(imageHolder);
        } else {
            imageHolder = (ImageHolder) convertView.getTag();
        }


        imageHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position == getCount() - 1) {
            ImageLoader.loadImage(convertView.getContext(), R.drawable.add_picture_press, imageHolder.imageView);
        } else {
            Photo imageitem = (Photo) getItem(position);
            ImageLoader.loadImage(convertView.getContext(), imageitem.getUrl(), imageHolder.imageView);
        }
        return convertView;
    }

    class ImageHolder {
        ImageView imageView;
    }

    public int getMaxDisplayCount() {
        return maxDisplayCount;
    }

    public void setMaxDisplayCount(int maxDisplayCount) {
        this.maxDisplayCount = maxDisplayCount;
    }


    public void add(Photo photo) {
        if (photo == null) {
            return;
        }
        this.imageList.add(photo);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Photo> imageList) {
        if (CollectionUtils.isEmpty(imageList)) {
            return;
        }
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }

    public ArrayList<Photo> getImageList() {
        return imageList;
    }

    public void remove(int position) {
        if (position > -1 && position < imageList.size()) {
            imageList.remove(position);
            notifyDataSetChanged();
        }
    }
}
