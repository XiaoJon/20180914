package com.hjhq.teamface.oa.login.enums;

/**
 * @author lanzl
 *	2016年12月26日下午2:45:50
 *	package-info
 */

public enum EnumLoginType {

	other(0,"其他"),
	pwd(1,"账号密码登录"),
	qrCode(2,"扫码登录"),
	inviteCode(3,"邀请码登录"),
	weChat(4,"qq登录"),
	qq(4,"微信登录");

	private int value;
	private String desc;

	private EnumLoginType(int value, String desc) {
		this.value = value;
		this.desc=desc;
	}
	
	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static EnumLoginType setValue(int str) {
		for(EnumLoginType e: EnumLoginType.values()){
			if(e.getValue()==str){
				return e;
			}
		}
		return other;
	}
}