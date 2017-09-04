package com.dreamgear.gm;

public class GMLoginData {
	GMUserData data;
	String token;
	long time;
	
	
	public GMUserData getData() {
		return data;
	}
	public void setData(GMUserData data) {
		this.data = data;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
