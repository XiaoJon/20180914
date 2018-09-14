package com.hjhq.teamface.basis.bean;


import java.util.ArrayList;

public class Photo extends Picture {

	public Photo(){}
	public Photo(String url){this.url = url;}

	/**
	 *
	 */
	private static final long serialVersionUID = 6629379543077100898L;

	/**
	 * 图片名称，默认为空
	 */
	private String name = "";

	/**
	 * 图片描述
	 */
	private String info = "";

	/**
	 * 上传日期
	 */
	private long date;


	/**
	 * 是否可以删除
	 */
	private boolean canDelete = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}


	public static ArrayList<Photo> toPhotoList(String url){
		ArrayList<Photo> list = new ArrayList<>();
		Photo photo = new Photo(url);
		list.add(photo);
		return list;
	}

	@Override
	public String toString() {
		return "Photo{" +
				"name='" + name + '\'' +
				", info='" + info + '\'' +
				", date=" + date +
				", url='" + url + '\'' +
				", canDelete=" + canDelete +
				'}';
	}
}
