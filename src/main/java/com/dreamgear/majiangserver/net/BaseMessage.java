package com.dreamgear.majiangserver.net;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;

public class BaseMessage{
	private UserInfo userInfo = null;
	public int protocol = GameProtocol.DEFAULT;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
    public void SetProtocol(){
    	if(protocol == GameProtocol.DEFAULT){
    		protocol = GetProtocol();
    	}
    }

    public void SetProtocol(int protocol){
    	this.protocol = protocol;
    }
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.DEFAULT;
	}
	
	public String GetString(){
		return JSON.toJSONString(this);
	}
	
	public void Send(){
		ServerManager.GetInstance().SendMessage(this);
	}
}
