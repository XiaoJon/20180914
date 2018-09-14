package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.bean.SortToken;
import com.hjhq.teamface.basis.database.Member;

public class SortModel extends Contact {

	/** 显示数据拼音的首字母 */
	public String sortLetters;
	public String role;//角色

	public Member member;

	public SortToken sortToken = new SortToken();
	
	

	public SortModel(String name, String number, String sortKey) {
		super(name, number, sortKey);
	}

	@Override
	public String toString() {
		return "SortModel [sortLetters=" + sortLetters + ", sortToken=" + sortToken.toString() + "]";
	}

}
