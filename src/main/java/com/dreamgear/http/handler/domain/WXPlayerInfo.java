package com.dreamgear.http.handler.domain;

public class WXPlayerInfo {
	String code;
	
	long uid = -1;
	
	public String access_token;//	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	public String expires_in;//	access_token接口调用凭证超时时间，单位（秒）
	public String refresh_token;//	用户刷新access_token
	public String scope;//	用户授权的作用域，使用逗号（,）分隔
	public String openid = "";//	用户的唯一标识
	
	public String nickname = "";//	用户昵称
	public String sex = "";//	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String province = "";//	用户个人资料填写的省份
	public String city = "";//	普通用户个人资料填写的城市
	public String country = "";//	国家，如中国为CN
	public String headimgurl = "";//	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	public String privilege = "";//	用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	public String unionid = "";//	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	
	public int errcode = -1;
	public String errmsg;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	
	
}
