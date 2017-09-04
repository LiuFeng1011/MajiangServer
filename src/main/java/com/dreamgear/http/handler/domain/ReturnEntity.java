package com.dreamgear.http.handler.domain;

import java.util.Map;

public class ReturnEntity {
	public static final int SUCC = 1;
	public static final int FAIL = 0;
	int status;// 0失败 1.成功
	String msg = "";// 失败原因
	Map<String,Object> data;// 返回数据

	public static ReturnEntity createSucc(Map<String,Object> data ){
		ReturnEntity entity = new ReturnEntity();
		entity.setStatus(SUCC);
		entity.setMsg("success");
		entity.setData(data);
		return entity;
	}
	
	public static ReturnEntity createFail(String msg){
		ReturnEntity entity = new ReturnEntity();
		entity.setStatus(FAIL);
		entity.setMsg(msg);
		return entity;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
