package com.hjhq.teamface.oa.friends;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.hjhq.teamface.common.bean.FriendsListBean;

/**
 * 朋友圈媒体布局接口
 * 
 * @author kin
 * 
 */
public interface FriendMedia {

	/**
	 * 加载数据
	 * 
	 * @param categoryItem
	 * @param activity
	 */
	public void setData(FriendsListBean.DataBean.ListBean categoryItem, Activity activity);

	/**
	 * 加载布局
	 * 
	 * @param layoutInflater
	 */
	public void inflate(LayoutInflater layoutInflater);

	/**
	 * 初始化
	 */
	public void createMedia();

	/**
	 * 获取View
	 * 
	 * @return
	 */
	public View getView();

	public void share();

}
