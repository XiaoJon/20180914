package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.im.R;

import java.util.List;


/**
 * 
 * @Description TODO 嵌套进ListView中的GridView具体实现
 * 
 */
public class GridViewItemAdapter extends BaseAdapter {

	// 图片路劲
	private List<UploadFileBean> imageList = null;

	// 动态布局加载器
	private LayoutInflater layoutInflater = null;

	public GridViewItemAdapter(Context context, List<UploadFileBean> imageList) {
		this.imageList = imageList;
		// 得到一个布局填充服务
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return imageList.size();
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

		// 得到所点击的图片item
		UploadFileBean imageitem = (UploadFileBean) getItem(position);

		if (convertView == null) {

			// 通过动态布局加载器得到视图转换器对象
			convertView = layoutInflater.inflate(R.layout.gridview_item, null);

			// 设置控件集到convertView
			convertView.setTag(convertView);
		} else {
			convertView = (View) convertView.getTag();
		}

		final ImageView imageview = (ImageView) convertView
				.findViewById(R.id.image);
		ImageLoader.loadImage(convertView.getContext(),imageitem.getFileUrl(),imageview);
		return convertView;
	}
}
