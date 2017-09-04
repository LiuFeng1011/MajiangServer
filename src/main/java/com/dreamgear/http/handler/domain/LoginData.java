package com.dreamgear.http.handler.domain;

public class LoginData {
	long id;
	String openid;
	long createTime;
	long lastLoginTime;
	
	public String nickname = "";//	用户昵称
	public String sex = "";//	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String province = "";//	用户个人资料填写的省份
	public String city = "";//	普通用户个人资料填写的城市
	public String country = "";//	国家，如中国为CN
	public String head = "";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	
}
