package com.hjhq.teamface.basis.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 图片基本类
 * 
 * @author kin
 * 
 */
public class Picture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8610467571924666209L;

	/**
	 * 图片id
	 */
	private int imageID;

	/**
	 * 绝对路径
	 */
	private String absolutePath = "";

	/**
	 * 是否被选中
	 */
	private boolean choose = false;

	/**
	 * 图片url路径
	 */
	protected String url = "";

	/**
	 * 默认的图片（可以设置为drawable中的资源ID）
	 */
	private int defaultImage = 0;

	public int getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(int defaultImage) {
		this.defaultImage = defaultImage;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public boolean isChoose() {
		return choose;
	}

	public void setChoose(boolean choose) {
		this.choose = choose;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 返回一个可以供ImageLoader使用的路径 如果URL和绝对路径都为空，返回""； 优先使用本地资源.
	 *
	 * @return
	 */
	public Object getShowPath() {

		if (!TextUtils.isEmpty(absolutePath)) {
			return absolutePath;
		}

		if (!TextUtils.isEmpty(url)) {
			return url;
		}

		return defaultImage;
	}

	@Override
	public String toString() {
		return "Picture [imageID=" + imageID + ", absolutePath=" + absolutePath
				+ ", url=" + this.url + "]";
	}

}
