package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 *@author xiaobo.cui 2014年11月26日 上午10:40:15
 *
 */
public class SortToken implements Serializable{
	private static final long serialVersionUID = -5042555548155872108L;
	/** 简拼 */
	public String simpleSpell = "";
	/** 全拼 */
	public String wholeSpell = "";
	/** 中文全名 */
	public String chName = "";
	@Override
	public String toString() {
		return "[simpleSpell=" + simpleSpell + ", wholeSpell=" + wholeSpell + ", chName=" + chName + "]";
	}
}
