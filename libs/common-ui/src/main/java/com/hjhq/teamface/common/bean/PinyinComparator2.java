package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.zygote.SingleInstance;

import java.util.Comparator;

public class PinyinComparator2 extends SingleInstance implements
		Comparator<SortModel> {

	private PinyinComparator2() {
	}

	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static PinyinComparator2 getInstance() {
		return (PinyinComparator2) SingleInstance
				.getInstance(PinyinComparator2.class.getName());
	}


	@Override
	public int compare(SortModel o1, SortModel o2) {
		if (o1.sortKey.equals("@") || o2.sortKey.equals("#")) {
			return -1;
		} else if (o1.sortKey.equals("#")
				|| o2.sortKey.equals("@")) {
			return 1;
		} else {
			return o1.sortKey.compareTo(o2.sortKey);
		}
	}
}