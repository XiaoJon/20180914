package com.hjhq.teamface.oa.friends.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.im.adapter.GridViewItemAdapter;
import com.hjhq.teamface.oa.friends.FriendMedia;
import com.hjhq.teamface.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片媒体
 * 
 * @author kin
 * 
 */
public class FriendMediaPicture implements FriendMedia {

	/**
	 * 当前布局
	 */
	private View view;

	/**
	 * 媒体文件
	 */
	private FriendsListBean.DataBean.ListBean categoryItem;

	/**
	 * 当前Activity
	 */
	private Activity activity;

	@Override
	public void inflate(LayoutInflater layoutInflater) {
		// TODO Auto-generated method stub

		List<UploadFileBean> images = categoryItem.getImages();

		if (images.size() > 1) {
			view = layoutInflater.inflate(R.layout.friend_picture_item, null);
		} else {
			view = layoutInflater.inflate(R.layout.single_friend_picture_item,
					null);
		}

	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public void share() {
		// TODO Auto-generated method stub

		List<UploadFileBean> images = categoryItem.getImages();

		if (null == images || images.isEmpty()) {

			CommonUtil.showToast("没有图片，不能分享。");

			return;

		}

	}

	@Override
	public void setData(FriendsListBean.DataBean.ListBean  categoryItem, Activity activity) {
		// TODO Auto-generated method stub
		this.categoryItem = categoryItem;

		this.activity = activity;

		// LogUtils.e(categoryItem.getContent().getType()+"22222");
	}

	@Override
	public void createMedia() {
		// TODO Auto-generated method stub
		final List<UploadFileBean> images = categoryItem.getImages();

		if (images.size() > 1) {
			GridViewItemAdapter adapter = new GridViewItemAdapter(activity,
					images);

			((GridView) view).setAdapter(adapter);

			// 为GridView添加每一项的点击事件
			((GridView) view).setOnItemClickListener((parent, view1, position, id) -> {
                // 点击列表项转入ViewPager显示界面
                showBigImage(position);
            });
		} else if (images.isEmpty()) {
			view.setVisibility(View.GONE);
		}else{
			final ImageView imageView = (ImageView) view
					.findViewById(R.id.single_imageView);
			ImageLoader.loadImage(activity,images.get(0).getFileUrl(),imageView);

			imageView.setOnClickListener(v -> showBigImage(0));
			view.setOnClickListener(v -> {});
		}

	}


	private void showBigImage(int position) {
		List<UploadFileBean> images = categoryItem.getImages();
		ArrayList<Photo> list = new ArrayList<>();
		for (UploadFileBean bean : images) {
			Photo photo = new Photo();
			photo.setName(bean.getFileName());
			photo.setUrl(bean.getFileUrl());
			list.add(photo);
		}
		// 点击列表项转入ViewPager显示界面
		Intent intent = new Intent(activity, ImagePagerActivity.class);

		intent.putExtra(ImagePagerActivity.PICTURE_LIST, list);

		intent.putExtra(ImagePagerActivity.SELECT_INDEX, position);

		activity.startActivity(intent);

	}
}
