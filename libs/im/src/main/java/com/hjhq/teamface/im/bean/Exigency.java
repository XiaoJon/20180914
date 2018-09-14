package com.hjhq.teamface.im.bean;

import java.io.Serializable;

public class Exigency implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3908717161280710236L;

	/**
	 * 电话号码
	 */
	private String telphone;

	/**
	 * 真实姓名
	 */
	private String employeeName;
	

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Override
	public String toString() {
		return "Exigency [telphone=" + telphone + ", employeeName="
				+ employeeName + "]";
	}

}
